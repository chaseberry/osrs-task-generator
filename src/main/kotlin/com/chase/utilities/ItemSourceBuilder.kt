package com.chase.utilities

import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceType

class ItemSourceBuilder : Runnable {

    override fun run() {

    }

    private fun itemSource() = ItemSource(
        id = param("id") { toIntOrNull() },
        name = param("name"),
        type = TODO("Enums"),
        drops = TODO(),
        rollsPerHour = param("rolls per hour") { toIntOrNull() },
        tags = TODO()
    )



}