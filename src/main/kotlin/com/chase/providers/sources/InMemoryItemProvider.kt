package com.chase.providers.sources

import com.chase.models.items.Item
import com.chase.providers.ItemProvider

class InMemoryItemProvider(
    items: List<Item>,
) : ItemProvider, BaseInMemoryProvider<Item>(items) {



}