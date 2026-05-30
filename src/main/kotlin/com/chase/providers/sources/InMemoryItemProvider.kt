package com.chase.providers.sources

import com.chase.models.items.Item
import com.chase.models.items.ItemTag
import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.providers.ItemProvider
import com.chase.utilities.regexify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class InMemoryItemProvider(
    items: List<Item>,
) : ItemProvider, BaseInMemoryProvider<Item>(items.toMutableList()) {

    override suspend fun search(name: String): Flow<Item> = with(name.regexify()) {
        items.filter { containsMatchIn(it.name) }.asFlow()
    }

    override suspend fun query(
        only: List<Int>?,
        except: List<Int>?,
        tags: List<ItemTag>
    ): Flow<Item> = flow {
        items.forEach {
            if (
                only?.contains(it.id) != false
                && except?.contains(it.id) != true
                && it.tags.any { it in tags }
            ) {
                emit(it)
            }
        }
    }

}