package com.chase.utilities

import com.chase.models.items.ItemTag
import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType

class ItemSourceBuilder : Builder<ItemSource>("ItemSource") {

    override fun newItem(): ItemSource = ItemSource(
        id = param("id") { toIntOrNull() },
        name = param("name"),
        type = enumParam("type", ItemSourceType::class),
        drops = ItemDropBuilder().getItems(),
        rollsPerHour = param("rolls per hour") { toIntOrNull()?.takeIf { it > 0 } },
        tags = listEnum("tags", ItemSourceTag::class)
    )

}