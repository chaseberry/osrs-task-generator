package com.chase.utilities

import com.chase.models.sources.ItemDrop
import com.chase.providers.ItemProvider
import kotlinx.coroutines.flow.firstOrNull

class ItemDropBuilder(
    val itemProvider: ItemProvider,
) : Builder<ItemDrop>("ItemDrop") {

    override suspend fun newItem(): ItemDrop = ItemDrop(
        itemId = findItem() ?: param("id") { toIntOrNull() },
        dropRate = param("drop rate (1/x)") { toIntOrNull() },
    )

    private suspend fun findItem(): Int? = itemProvider.search(param("name")).firstOrNull {
        param("Did you mean ${it.name}") { firstOrNull()?.equals('y', true) }
    }?.id

}