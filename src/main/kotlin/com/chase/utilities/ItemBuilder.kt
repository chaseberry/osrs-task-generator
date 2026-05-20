package com.chase.utilities

import com.chase.models.items.Item

class ItemBuilder : Runnable {

    override fun run() {

    }

    private fun item() = Item(
        id = param("id") { toIntOrNull() },
        name = param("name"),
        tags =
    )

}