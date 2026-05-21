package com.chase

import com.chase.providers.sources.InMemoryItemSourceProvider
import com.chase.utilities.readFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class OsrsTaskGenerator(
    val configString: String,
) {

    suspend fun start() {
        val runConfig = try {
            parseConfiguration()
        } catch (e: Exception) {
            println("Failed to start Osrs Task Generator")
            e.printStackTrace()
            return
        }

        return when (runConfig.runMode) {
            RunConfiguration.RunMode.Cli -> runCli(runConfig)
        }
    }

    private fun runCli(runConfiguration: RunConfiguration) {

    }

    private suspend fun parseConfiguration(): RunConfiguration {
        if (configString.startsWith("{")) {
            println("Treating configuration as Json...")
            return parseConfigurationJson(configString)
        }

        println("Treating configuration as File...")
        return parseConfigurationJson(
            readFile(configString)
        )
    }

    private fun parseConfigurationJson(json: String): RunConfiguration = Json.decodeFromString(json)

    private suspend fun setupProviders(runConfiguration: RunConfiguration) {
        when (val s = runConfiguration.dataSource) {
            is RunConfiguration.DataSource.InMemory -> {
                InMemoryItemSourceProvider(
                    preseed(s.itemSourcePreseedFile)
                )
            }
        }
    }

    suspend inline fun <reified T> preseed(path: String): List<T> = Json.decodeFromString(readFile(path))
}