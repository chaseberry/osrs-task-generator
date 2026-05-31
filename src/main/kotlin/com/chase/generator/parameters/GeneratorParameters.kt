package com.chase.generator.parameters

import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import kotlinx.serialization.Serializable

@Serializable
class GeneratorParameters(
    val numberOfGenerations: Int,
    val taskBreakdownPerGeneration: List<TaskTier>,
    val defaultCompletionsPerHourModifier: Double,
    val sourceCompletionsPerHourModifier: Map<Int, Double>? = null,
    val uniqueTasksPerGeneration: Boolean = true,
    val allUniqueTasks: Boolean = false,
    val generateTaskFilter: Filter<TaskType>? = null,
    val factorTaskTimeForSlayerBosses: Boolean,

    // definitions
    val easyTaskHours: Int, // <=
    val mediumTaskHours: Int, // <=
    val hardTaskHours: Int, // <=
    val eliteTaskHours: Int? = null,

    // filtering options
    val itemFilters: ItemFilter? = null,
    val itemSourceFilter: ItemSourceFilter? = null,
    val customTaskFilters: TaskFilter? = null,

    ) {
}