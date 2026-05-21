package com.chase.utilities

import com.chase.models.items.ItemTag
import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType

class ItemSourceBuilder : Builder<ItemSourceTag> {


    override fun build(): List<ItemSourceTag> {
        TODO("Not yet implemented")
    }



    private fun itemSource() = ItemSource(
        id = param("id") { toIntOrNull() },
        name = param("name"),
        type = enumParam("type", ItemSourceType::class),
        drops = TODO(),
        rollsPerHour = param("rolls per hour") { toIntOrNull() },
        tags = listEnum("tags", ItemSourceTag::class)
    )

}