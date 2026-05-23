package com.chase.providers.sources

import com.chase.models.sources.ItemSource
import com.chase.providers.ItemSourceProvider
import com.chase.utilities.regexify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class InMemoryItemSourceProvider(
    itemSources: List<ItemSource>,
) : ItemSourceProvider, BaseInMemoryProvider<ItemSource>(itemSources.toMutableList()) {

    override suspend fun search(query: String): Flow<ItemSource> = with(query.regexify()) {
        items.filter { containsMatchIn(it.name) }.asFlow()
    }

}