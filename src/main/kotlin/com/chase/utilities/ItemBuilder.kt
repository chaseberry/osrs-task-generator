package com.chase.utilities

import com.chase.models.items.Item
import com.chase.models.items.ItemTag

class ItemBuilder : Builder<Item>("Item") {

    override fun newItem(): Item = Item(
        id = param("id") { toIntOrNull() },
        name = param("name"),
        tags = listEnum("tags", ItemTag::class)
    )

}