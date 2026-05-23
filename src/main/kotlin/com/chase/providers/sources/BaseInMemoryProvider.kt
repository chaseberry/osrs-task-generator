package com.chase.providers.sources

import com.chase.models.Model
import com.chase.providers.Provider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

abstract class BaseInMemoryProvider<T : Model>(
    protected val items: MutableList<T>,
) : Provider<T> {

    override suspend fun get(id: Int): T? = items.find { it.id == id }

    override suspend fun get(ids: List<Int>): List<T> = items.filter { it.id in ids }

    override suspend fun get(vararg ids: Int): List<T> = get(ids.toList())

    override suspend fun add(item: T) {
        if (get(item.id) != null) {
            throw IllegalArgumentException("Item with id '${item.id}' already exists.")
        }

        items.add(item)
    }

    override suspend fun remove(id: Int) {
        items.removeIf { it.id == id }
    }

    override suspend fun remove(item: T) {
        items.remove(item)
    }

    override suspend fun stream(): Flow<T> = items.asFlow()

}