package com.chase.providers

import com.chase.models.Model
import kotlinx.coroutines.flow.Flow

interface Provider<T : Model> {

    suspend fun get(id: Int): T?

    suspend fun get(ids: List<Int>): List<T>

    suspend fun get(vararg ids: Int): List<T>

    suspend fun add(item: T)

    suspend fun remove(id: Int)

    suspend fun remove(item: T)

    suspend fun stream(): Flow<T>
}