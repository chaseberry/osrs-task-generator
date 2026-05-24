package com.chase.providers.sources

import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.providers.ItemSourceProvider
import com.chase.utilities.regexify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class InMemoryItemSourceProvider(
    itemSources: List<ItemSource>,
) : ItemSourceProvider, BaseInMemoryProvider<ItemSource>(itemSources.toMutableList()) {

    override suspend fun search(query: String): Flow<ItemSource> = with(query.regexify()) {
        items.filter { containsMatchIn(it.name) }.asFlow()
    }

    override suspend fun query(
        only: List<Int>?,
        except: List<Int>?,
        types: List<ItemSourceType>,
        tags: List<ItemSourceTag>
    ): Flow<ItemSource> = flow {
        items.forEach {
            if (
                only?.contains(it.id) != false
                && except?.contains(it.id) != true
                && it.type in types
                && it.tags.any { it in tags }
            ) {
                emit(it)
            }
        }
    }

}