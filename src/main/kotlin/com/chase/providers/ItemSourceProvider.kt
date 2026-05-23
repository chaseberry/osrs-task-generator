package com.chase.providers

import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import kotlinx.coroutines.flow.Flow

interface ItemSourceProvider : Provider<ItemSource> {

    suspend fun search(query: String): Flow<ItemSource>

    suspend fun query(
        only: List<Int>?,
        except: List<Int>?,
        types: List<ItemSourceType>,
        tags: List<ItemSourceTag>,
    ): Flow<ItemSource>
}