package com.chase.providers

import com.chase.models.items.Item
import kotlinx.coroutines.flow.Flow

interface ItemProvider: Provider<Item> {

    suspend fun search(name: String): Flow<Item>

}