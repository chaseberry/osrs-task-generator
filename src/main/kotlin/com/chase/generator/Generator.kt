package com.chase.generator

import com.chase.generator.parameters.GeneratorParameters
import com.chase.models.items.ItemTag
import com.chase.models.sources.ItemDrop
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.models.tasks.Task
import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.providers.TaskProvider
import com.chase.utilities.TaskRenderer
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.groupBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

class Generator(
    val parameters: GeneratorParameters,
    val itemProvider: ItemProvider,
    val itemSourceProvider: ItemSourceProvider,
    val taskProvider: TaskProvider,
) {

    // What do we need to do
    // 1. Figure out what options are valid
    // 2. For each generation
    //  2.a for each breakdown element
    //   2.a.1 generate one task

    private val startingItemSources = runBlocking {
        itemSourceProvider.query(
            only = parameters.itemSourceFilter?.itemSources?.whiteListValues(),
            except = parameters.itemSourceFilter?.itemSources?.blackListValues(),
            types = ItemSourceType.entries.filter {
                parameters.itemSourceFilter?.itemSourceTypes?.valid(it) ?: true
            },
            tags = ItemSourceTag.entries.filter {
                parameters.itemSourceFilter?.itemSourceTags?.valid(it) ?: true
            },
        )
    }

    private val startingItems = runBlocking {
        itemProvider.query(
            only = parameters.itemFilters?.items?.whiteListValues(),
            except = parameters.itemFilters?.items?.blackListValues(),
            tags = ItemTag.entries.filter {
                parameters.itemFilters?.tags?.valid(it) ?: true
            },
        )
    }

    private var taskId: Int = 0
        get() = field++

    suspend fun generateTasks(): List<List<Task>> {
        taskId = 0
        val generations = ArrayList<List<Task>>()
        val allOptions = buildTaskOptions()
        val usedTasks = if (parameters.allUniqueTasks) {
            mutableListOf<Int>()
        } else {
            null
        }

        (0 until parameters.numberOfGenerations).forEach { _ ->
            generations.add(generateGeneration(allOptions, usedTasks).also {
                usedTasks?.addAll(it.map { it.id })
            })
        }

        return generations
    }

    private fun generateGeneration(options: Map<TaskTier, List<Task>>, usedTasks: List<Int>?): List<Task> {
        val usedTasksThisGeneration = if (usedTasks != null || parameters.uniqueTasksPerGeneration) {
            (usedTasks ?: emptyList()).toMutableList()
        } else {
            null
        }

        return parameters.taskBreakdownPerGeneration.map {
            generateTask(
                options[it] ?: throw GenerationException(parameters, "No tasks for tier $it"),
                usedTasksThisGeneration
            )?.also {
                usedTasksThisGeneration?.add(it.id)
            } ?: throw GenerationException(parameters, "No valid tasks left for tier $it")
        }
    }

    private fun generateTask(options: List<Task>, usedTasks: List<Int>?): Task? {
        if (usedTasks != null && usedTasks.size >= options.size) {
            return null
        }

        if (usedTasks == null) {
            return options.random()
        }

        return options.filter { it.id !in usedTasks }.random()
    }

    private suspend fun buildTaskOptions(): Map<TaskTier, List<Task>> {
        val taskOptions: List<Pair<TaskType, suspend () -> List<Task>>> = listOf(
            TaskType.ObtainSpecificItemFromSpecificSource to { buildObtainSpecificItemFromSpecificSourceTasks() },
            TaskType.ObtainAnyItemFromSpecificSource to { buildObtainAnyItemFromSpecificSourceTasks() },
            TaskType.ObtainSpecificItem to { buildObtainSpecificItemTasks() },
            TaskType.ObtainItemWithTag to { buildObtainItemWithTagTasks() },
            TaskType.ObtainAnyItemFromSourceType to { buildObtainAnyItemFromSourceTypeTasks() },
            TaskType.ObtainAnyItemFromSourceTag to { buildObtainAnyItemFromSourceTagTasks() },
        )

        return (taskOptions.filter {
            parameters.generateTaskFilter?.valid(it.first) ?: true
        }.flatMap { it.second() } + buildCustomTaskOptions()).groupBy {
            it.tier
        }
    }

    private suspend fun buildObtainSpecificItemFromSpecificSourceTasks(): List<Task> {
        return startingItemSources.map {
            val itemMap = itemProvider.get(it.drops.map { it.itemId })

            it to it.drops.associateWith { d -> itemMap.find { it.id == d.itemId } }.filter {
                val item = it.value
                item != null && (parameters.itemFilters?.items?.valid(item.id) ?: true)
                        && (parameters.itemFilters?.tags?.valid(item.tags) ?: true)
            }.mapValues { it.value!! }
        }.toList().flatMap { (src, drops) ->
            drops.mapNotNull {
                tierForHours(hoursToDropRate(src.rollsPerHour, it.key.dropRate, src.id))?.let { tier ->
                    Task.ObtainSpecificItemFromSpecificSourceTask(
                        id = taskId,
                        tier = tier,
                        itemId = it.value.id,
                        itemSourceId = src.id
                    )
                }

            }
        }
    }

    private suspend fun buildObtainAnyItemFromSpecificSourceTasks(): List<Task> {
        return startingItemSources.mapNotNull { source ->
            tierForHours(
                hoursToDropRate(source.rollsPerHour, combineDropRates(source.drops), source.id)
            )?.let {
                Task.ObtainAnyItemFromSpecificSourceTask(
                    id = taskId,
                    tier = it,
                    itemSourceId = source.id
                )
            }
        }.toList()
    }

    private suspend fun buildObtainSpecificItemTasks(): List<Task> {
        return startingItemSources.flatMapConcat {
            it.drops.map { drop -> drop to it }.asFlow()
        }.groupBy {
            it.first.itemId
        }.filter {
            it.value.size > 1
        }.mapNotNull {
            val selected = it.value.minBy { it.first.dropRate }

            tierForHours(
                hoursToDropRate(
                    selected.second.rollsPerHour,
                    selected.first.dropRate,
                    selected.second.id
                )
            )?.let { tier ->
                Task.ObtainSpecificItemTask(
                    id = taskId,
                    tier = tier,
                    itemId = it.key
                )
            }
        }
    }

    private suspend fun buildObtainItemWithTagTasks(): List<Task> {
        return ItemTag.entries.filter {
            parameters.itemFilters?.tags?.valid(it) ?: true
        }.mapNotNull { tag ->
            val items = startingItems.filter { tag in it.tags }.map { it.id }.toList()
            // todo empty checks

            if(items.isEmpty()) {
                return@mapNotNull null
            }

            tierForHours(
                startingItemSources.filter {
                    it.drops.any { it.itemId in items }
                }.map {
                    it.copy(
                        drops = it.drops.filter { it.itemId in items },
                    )
                }.map {
                    hoursToDropRate(
                        it.rollsPerHour,
                        combineDropRates(it.drops),
                        it.id
                    )
                }.toList().takeIf { it.isNotEmpty() }?.min() ?: return@mapNotNull null

            )?.let { tier ->
                Task.ObtainItemWithTagTask(
                    id = taskId,
                    tier = tier,
                    tag = tag
                )
            }

        }
    }

    private suspend fun buildObtainAnyItemFromSourceTypeTasks(): List<Task> {
        return ItemSourceType.entries.mapNotNull { type ->
            tierForHours(
                startingItemSources.filter {
                    it.type == type
                }.map {
                    hoursToDropRate(it.rollsPerHour, combineDropRates(it.drops), it.id)
                }.toList().takeIf { it.isNotEmpty() }?.min() ?: return@mapNotNull null
            )?.let { tier ->
                Task.ObtainAnyItemFromSourceTypeTask(
                    id = taskId,
                    tier = tier,
                    itemSourceType = type,
                )
            }
        }

    }

    private suspend fun buildObtainAnyItemFromSourceTagTasks(): List<Task> {
        return ItemSourceTag.entries.mapNotNull { tag ->
            tierForHours(
                startingItemSources.filter {
                    it.tags.contains(tag)
                }.map {
                    hoursToDropRate(it.rollsPerHour, combineDropRates(it.drops), it.id)
                }.toList().takeIf { it.isNotEmpty() }?.min() ?: return@mapNotNull null
            )?.let { tier ->
                Task.ObtainAnyItemFromSourceTagTask(
                    id = taskId,
                    tier = tier,
                    itemSourceTag = tag,
                )
            }
        }
    }

    private suspend fun buildCustomTaskOptions(): List<Task> {
        return taskProvider.query(
            only = parameters.customTaskFilters?.tasks?.whiteListValues(),
            except = parameters.customTaskFilters?.tasks?.blackListValues(),
            types = TaskType.entries.filter {
                parameters.customTaskFilters?.types?.valid(it) ?: true
            },
            tiers = TaskTier.entries.filter {
                parameters.customTaskFilters?.tiers?.valid(it) ?: true
            },
        ).map {
            it.withNewId(taskId)
        }.toList()
    }

    private fun combineDropRates(drops: List<ItemDrop>): Int {
        return (1 / (drops.map { 1 / it.dropRate.toDouble() }.reduce { acc, i -> acc + i })).toInt()
    }

    private fun hoursToDropRate(rollsPerHour: Int, dropRate: Int, sourceId: Int?): Int {
        val modifier = sourceId?.let { id ->
            parameters.sourceCompletionsPerHourModifier?.let {
                it[id]
            }
        } ?: parameters.defaultCompletionsPerHourModifier

        return (dropRate / (rollsPerHour * modifier)).toInt()
    }

    private fun tierForHours(hours: Int): TaskTier? = when {
        hours <= parameters.easyTaskHours -> TaskTier.Easy
        hours <= parameters.mediumTaskHours -> TaskTier.Medium
        hours <= parameters.hardTaskHours -> TaskTier.Hard
        parameters.eliteTaskHours == null -> TaskTier.Elite
        hours <= parameters.eliteTaskHours -> TaskTier.Elite
        else -> null
    }

}