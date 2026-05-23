package com.chase.generator

import com.chase.models.tasks.TaskTier
import kotlinx.serialization.Serializable

@Serializable
class GeneratorParameters(
    val numberOfGenerations: Int,
    val taskBreakdownPerGeneration: List<TaskTier>,
    val completionsPerHourModifier: Double,


    // filtering options
    val itemFilter: GenerationFilter<Int>? = null,
    // contains tag, doesnt contain tag
    val itemSourceFilter: GenerationFilter<Int>? = null,


    ) {
}