package com.chase.providers

import com.chase.models.items.Item
import com.chase.models.items.ItemTag
import kotlinx.coroutines.flow.Flow

interface ItemProvider: Provider<Item> {

    suspend fun search(name: String): Flow<Item>

    suspend fun query(
        only: List<Int>?,
        except: List<Int>?,
        tags: List<ItemTag>,
    ): Flow<Item>
}