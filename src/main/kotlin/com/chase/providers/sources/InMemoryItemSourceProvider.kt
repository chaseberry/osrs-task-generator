package com.chase.providers.sources

import com.chase.models.sources.ItemSource
import com.chase.providers.ItemSourceProvider

class InMemoryItemSourceProvider(
    itemSources: List<ItemSource>,
) : ItemSourceProvider, BaseInMemoryProvider<ItemSource>(itemSources.toMutableList()) {
}