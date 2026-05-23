package com.chase.generator.parameters

import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import kotlinx.serialization.Serializable

@Serializable
class TaskFilter(
    val tasks: Filter<Int>? = null,
    val tiers: Filter<TaskTier>? = null,
    val types: Filter<TaskType>? = null,
) {
}