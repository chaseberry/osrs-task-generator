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

    fun generateTasks() {
        (0 until parameters.numberOfGenerations).forEach { _ ->

        }
    }

    private fun generateSingleTask(): Task {

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
        return (rollsPerHour * parameters.completionsPerHourModifier).toInt() / drop.dropRate
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