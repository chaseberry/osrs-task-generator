package com.chase.utilities

import com.chase.models.items.Item
import com.chase.models.items.ItemTag

class ItemBuilder : Builder<Item> {

    override fun build(): List<Item> {
        val items = ArrayList<Item>()

        do {
            print("Add Item Y/N: ")
            if (!readln().first().equals('y', true)) {
                break
            }

            items.add(item())

        } while (true)

        return items
    }

    private fun item() = Item(
        id = param("id") { toIntOrNull() },
        name = param("name"),
        tags = listEnum("tags", ItemTag::class)
    )

}