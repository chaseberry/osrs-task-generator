package com.chase.providers

import com.chase.models.sources.ItemSource
import kotlinx.coroutines.flow.Flow

interface ItemSourceProvider : Provider<ItemSource> {

    suspend fun search(query: String): Flow<ItemSource>

}