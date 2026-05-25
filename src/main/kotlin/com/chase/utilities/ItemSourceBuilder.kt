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
import kotlinx.serialization.json.Json

class ItemSourceBuilder(
    val itemProvider: ItemProvider,
) : Builder<ItemSource>("ItemSource") {

    private val npcLookup: List<WeirdGloopNpc>?

    init {
        npcLookup = runBlocking {
            loadNpcLookup()
        }
    }

    override suspend fun newItem(): ItemSource = with(param("name")) {
        ItemSource(
            id = lookupNpcId(this) ?: param("id") { toIntOrNull() },
            name = this,
            type = enumParam("type", ItemSourceType::class),
            drops = ItemDropBuilder(itemProvider).getItems(),
            rollsPerHour = param("rolls per hour") { toIntOrNull()?.takeIf { it > 0 } },
            tags = listEnum("tags", ItemSourceTag::class)
        )
    }

    private fun lookupNpcId(name: String): Int? {
        npcLookup ?: return null
        val regex = name.regexify()
        val validItems = npcLookup.filter { regex.containsMatchIn(it.name) || regex.containsMatchIn(it.configName) }
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
    private suspend fun loadNpcLookup(): List<WeirdGloopNpc>? = try {
        Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<WeirdGloopNpc>>(
            HttpClient(OkHttp) {
                expectSuccess = true
                engine {
                    config {
                        followRedirects(true)
                    }
                }
            }.use {
                it.get(NPC_ID_URL).bodyAsText()
            }.dropWhile { it != '[' }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    companion object {
        const val NPC_ID_URL = "https://chisel.weirdgloop.org/moid/data_files/npcsmin.js"
    }

}