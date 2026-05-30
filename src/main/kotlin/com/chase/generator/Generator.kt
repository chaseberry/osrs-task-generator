package com.chase.generator

import com.chase.generator.parameters.GeneratorParameters
import com.chase.models.sources.ItemDrop
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.models.tasks.Task
import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.providers.TaskProvider
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

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

    suspend fun generateTasks(): List<List<Task>> {
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
        var taskId = 0
        val sources = itemSourceProvider.query(
            only = parameters.itemSourceFilter?.itemSources?.whiteListValues(),
            except = parameters.itemSourceFilter?.itemSources?.blackListValues(),
            types = ItemSourceType.entries.filter {
                parameters.itemSourceFilter?.itemSourceTypes?.valid(it) ?: true
            },
            tags = ItemSourceTag.entries.filter {
                parameters.itemSourceFilter?.itemSourceTags?.valid(it) ?: true
            },
        ).map {
            val itemMap = itemProvider.get(it.drops.map { it.itemId })

            it to it.drops.associateWith { d -> itemMap.find { it.id == d.itemId } }.filter {
                val item = it.value
                item != null && (parameters.itemFilters?.items?.valid(item.id) ?: true)
                        && (parameters.itemFilters?.tags?.valid(item.tags) ?: true)
            }.mapValues { it.value!! }
        }.toList()

        val itemDropTaskOptions = sources.flatMap { (src, drops) ->
            drops.mapNotNull {
                tierForHours(hoursToDropRate(src.rollsPerHour, it.key))?.let { tier ->
                    Task.ObtainItemTask(
                        id = taskId++,
                        tier = tier,
                        itemId = it.value.id,
                        itemSourceId = src.id
                    )
                }

            }
        }

        val customTaskOptions = taskProvider.query(
            only = parameters.taskFilters?.tasks?.whiteListValues(),
            except = parameters.taskFilters?.tasks?.blackListValues(),
            types = TaskType.entries.filter {
                parameters.taskFilters?.types?.valid(it) ?: true
            },
            tiers = TaskTier.entries.filter {
                parameters.taskFilters?.tiers?.valid(it) ?: true
            },
        ).map {
            it.withNewId(taskId++)
        }.toList()

        return (itemDropTaskOptions + customTaskOptions).groupBy {
            it.tier
        }
    }

    private fun hoursToDropRate(rollsPerHour: Int, drop: ItemDrop): Int {
        return drop.dropRate / (rollsPerHour * parameters.completionsPerHourModifier).toInt()
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