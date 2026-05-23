package com.chase.generator.parameters

import com.chase.models.tasks.TaskTier
import kotlinx.serialization.Serializable

@Serializable
class GeneratorParameters(
    val numberOfGenerations: Int,
    val taskBreakdownPerGeneration: List<TaskTier>,
    val completionsPerHourModifier: Double,


    // filtering options
    val itemFilters: ItemFilter? = null,
    val itemSourceFilter: ItemSourceFilter? = null,
    val taskFilters: TaskFilter? = null,

    ) {
}