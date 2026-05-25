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

    val osrsItemData: List<WeirdGloopItem>?

    init {
        osrsItemData = runBlocking {
            loadItemLookup()
        }
    }

    override suspend fun newItem(): Item = with(param("name")) {
        Item(
            name = this,
            id = lookupItemId(this) ?: param("id") { toIntOrNull() },
            tags = listEnum("tags", ItemTag::class)
        )
    }

    private fun lookupItemId(name: String): Int? {
        osrsItemData ?: return null
        val regex = name.regexify()
        val validItems = osrsItemData.filter { regex.containsMatchIn(it.name) || regex.containsMatchIn(it.configName) }
        return when (validItems.size) {
            0 -> null
            1 -> {
                println(" Found id ${validItems.first().id} from ${validItems.first().name}")
                validItems.first().id
            }

            else -> validItems.find {
                param("${it.name} / ${it.configName}") { firstOrNull()?.equals('y', true) }
            }?.id
        }
    }

    @Suppress("JSON_FORMAT_REDUNDANT")
    private suspend fun loadItemLookup(): List<WeirdGloopItem>? = try {
        Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<WeirdGloopItem>>(
            HttpClient(OkHttp) {
                expectSuccess = true
                engine {
                    config {
                        followRedirects(true)
                    }
                }
            }.use {
                it.get(ITEM_JSON_URL).bodyAsText()
            }.dropWhile { it != '[' }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    companion object {
        private const val ITEM_JSON_URL = "https://chisel.weirdgloop.org/moid/data_files/itemsmin.js"
    }
}