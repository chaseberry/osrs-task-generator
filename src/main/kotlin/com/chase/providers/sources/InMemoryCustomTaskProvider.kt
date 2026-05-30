package com.chase.providers.sources

import com.chase.models.tasks.Task
import com.chase.models.tasks.TaskTier
import com.chase.models.tasks.TaskType
import com.chase.providers.TaskProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class InMemoryCustomTaskProvider(
    items: List<Task>,
) : TaskProvider, BaseInMemoryProvider<Task>(items.toMutableList()) {

    override suspend fun search(query: String): Flow<Task> = items.filter {
        TODO("query tasks")
    }.asFlow()

    override suspend fun query(
        only: List<Int>?,
        except: List<Int>?,
        tiers: List<TaskTier>,
        types: List<TaskType>
    ): Flow<Task> = flow {
        items.forEach {
            if (
                only?.contains(it.id) != false
                && except?.contains(it.id) != true
                && it.tier in tiers
                && it.type in types
            ) {
                emit(it)
            }
        }
    }

}