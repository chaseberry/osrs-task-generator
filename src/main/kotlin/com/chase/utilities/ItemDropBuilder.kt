package com.chase.utilities

import com.chase.models.sources.ItemDrop

class ItemDropBuilder : Builder<ItemDrop>("ItemDrop") {

    override fun newItem(): ItemDrop = ItemDrop(
        itemId = param("id") { toIntOrNull() },
        dropRate = param("drop rate (1/x)") { toIntOrNull() },
    )

}