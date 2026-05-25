package com.chase.utilities


import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class WeirdGloopData<T : WeirdGloopEntity>(
    private val sourceUrl: String,
    private val serializer: KSerializer<T>,
) {

    private val data: List<T>?

    init {
        data = runBlocking {
            load()
        }
    }

    @Suppress("JSON_FORMAT_REDUNDANT")
    private suspend fun load(): List<T>? = try {
        Json {
            ignoreUnknownKeys = true
        }.decodeFromString<List<T>>(
            ListSerializer(serializer),
            HttpClient(OkHttp) {
                expectSuccess = true
                engine {
                    config {
                        followRedirects(true)
                    }
                }
            }.use {
                it.get(sourceUrl).bodyAsText()
            }.dropWhile { it != '[' },
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    fun lookupId(name: String): Int? {
        data ?: return null
        val regex = name.regexify()
        val validItems = data.filter { regex.containsMatchIn(it.name) || regex.containsMatchIn(it.configName) }
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

}