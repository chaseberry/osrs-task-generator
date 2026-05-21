package com.chase.providers.sources

import com.chase.models.Model
import com.chase.providers.Provider

abstract class BaseInMemoryProvider<T : Model>(
    protected val items: MutableList<T>,
) : Provider<T> {

    constructor(items: List<T>) : this(items.toMutableList())

    override fun get(id: Int): T? = items.find { it.id == id }

    override fun get(ids: List<Int>): List<T> = items.filter { it.id in ids }

    override fun get(vararg ids: Int): List<T> = get(ids.toList())

    override fun add(item: T) {
        if (get(item.id) != null) {
            throw IllegalArgumentException("Item with id '${item.id}' already exists.")
        }

        items.add(item)
    }

    override fun remove(id: Int) {
        items.removeIf { it.id == id }
    }

    override fun remove(item: T) {
        items.remove(item)
    }

}