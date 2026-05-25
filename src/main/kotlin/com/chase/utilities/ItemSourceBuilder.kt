package com.chase.utilities

import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceTag
import com.chase.models.sources.ItemSourceType
import com.chase.providers.ItemProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class ItemSourceBuilder(
    val itemProvider: ItemProvider,
) : Builder<ItemSource>("ItemSource") {

    private val npcLookup = WeirdGloopData(NPC_ID_URL, WeirdGloopNpc.serializer())

    override suspend fun newItem(): ItemSource = with(param("name")) {
        ItemSource(
            id = npcLookup.lookupId(this) ?: param("id") { toIntOrNull() },
            name = this,
            type = enumParam("type", ItemSourceType::class),
            drops = ItemDropBuilder(itemProvider).getItems(),
            rollsPerHour = param("rolls per hour") { toIntOrNull()?.takeIf { it > 0 } },
            tags = listEnum("tags", ItemSourceTag::class)
        )
    }

    companion object {
        const val NPC_ID_URL = "https://chisel.weirdgloop.org/moid/data_files/npcsmin.js"
    }

}