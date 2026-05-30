package com.chase.providers

import com.chase.models.tasks.Task
import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import kotlinx.coroutines.flow.Flow

interface TaskProvider : Provider<Task> {

    suspend fun search(query: String): Flow<Task>

    suspend fun query(
        only: List<Int>?,
        except: List<Int>?,
        tiers: List<TaskTier>,
        types: List<TaskType>,
    ): Flow<Task>

}