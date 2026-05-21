package com.chase.providers

import com.chase.models.Model

interface Provider<T : Model> {

    fun get(id: Int): T?

    fun get(ids: List<Int>): List<T>

    fun get(vararg ids: Int): List<T>

    fun add(item: T)

    fun remove(id: Int)

    fun remove(item: T)
}