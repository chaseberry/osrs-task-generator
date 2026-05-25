package com.chase.utilities

import com.chase.models.items.Item
import com.chase.models.items.ItemTag
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ItemBuilder : Builder<Item>("Item") {

    val osrsItemData = WeirdGloopData(ITEM_JSON_URL, WeirdGloopItem.serializer())

    override suspend fun newItem(): Item = with(param("name")) {
        Item(
            name = this,
            id = osrsItemData.lookupId(this) ?: param("id") { toIntOrNull() },
            tags = listEnum("tags", ItemTag::class)
        )
    }

    companion object {
        private const val ITEM_JSON_URL = "https://chisel.weirdgloop.org/moid/data_files/itemsmin.js"
    }
}