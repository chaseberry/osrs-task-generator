package com.chase.utilities

import com.chase.models.sources.ItemDrop
import com.chase.providers.ItemProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlin.collections.firstOrNull

class ItemDropBuilder(
    val itemProvider: ItemProvider,
) : Builder<ItemDrop>("ItemDrop") {

    override suspend fun newItem(): ItemDrop = ItemDrop(
        itemId = findItem() ?: param("id") { toIntOrNull() },
        dropRate = param("drop rate (1/x)") { toIntOrNull() },
    )

    private suspend fun findItem(): Int? = itemProvider.search(param("name").trim()).toList().let {
        if (it.size == 1) {
            println("Found exact match against ${it.first().name}")
            it.first().id
        } else {
            it.firstOrNull {
                param("Did you mean ${it.name}") { firstOrNull()?.equals('y', true) }
            }?.id
        }
    }

}