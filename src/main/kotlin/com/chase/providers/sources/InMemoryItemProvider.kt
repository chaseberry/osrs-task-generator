package com.chase.providers.sources

import com.chase.models.items.Item
import com.chase.providers.ItemProvider
import com.chase.utilities.regexify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class InMemoryItemProvider(
    items: List<Item>,
) : ItemProvider, BaseInMemoryProvider<Item>(items.toMutableList()) {

    override suspend fun search(name: String): Flow<Item> = with(name.regexify()) {
        items.filter { containsMatchIn(it.name) }.asFlow()
    }

}