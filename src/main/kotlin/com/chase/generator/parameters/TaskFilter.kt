package com.chase.generator.parameters

import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import kotlinx.serialization.Serializable

@Serializable
class TaskFilter(
    val taskFilter: Filter<Int>? = null,
    val tierFilter: Filter<TaskTier>? = null,
    val typeFilter: Filter<TaskType>? = null,
) {
}