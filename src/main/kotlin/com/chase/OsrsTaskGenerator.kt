package com.chase

import com.chase.cli.Cli
import com.chase.generator.Generator
import com.chase.generator.parameters.GeneratorParameters
import com.chase.points.PointAssigner
import com.chase.points.parameters.PointAssignmentParameters
import com.chase.providers.sources.InMemoryCustomTaskProvider
import com.chase.providers.sources.InMemoryItemProvider
import com.chase.providers.sources.InMemoryItemSourceProvider
import com.chase.utilities.TaskRenderer
import com.chase.utilities.readFile
import kotlinx.serialization.json.Json

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
            is RunConfiguration.RunMode.GenerateTasks -> runGenerateTasks(runConfig, runConfig.runMode.parametersFile)
            is RunConfiguration.RunMode.GeneratePoints -> runGeneratePoints(runConfig, runConfig.runMode.parametersFile)
        }
    }

    private suspend fun runCli(runConfiguration: RunConfiguration) = with(runConfiguration.dataSource) {
        Cli(
            itemSourceProvider(),
            itemProvider(),
            customTaskProvider(),
            runConfiguration,
        ).run()
    }

    private suspend fun runGenerateTasks(config: RunConfiguration, path: String) = with(config.dataSource) {
        val params = Json.decodeFromString<GeneratorParameters>(readFile(path))

        val items = itemProvider()
        val sources = itemSourceProvider()
        val r = TaskRenderer(items, sources)

        Generator(
            params,
            items,
            sources,
            customTaskProvider()
        ).generateTasks().forEach {
            println()
            println(
                it.map {
                    r.renderTaskAsString(it)
                }.joinToString("\n")
            )
        }
    }

    private suspend fun runGeneratePoints(config: RunConfiguration, path: String) = with(config.dataSource) {
        val params = Json.decodeFromString<PointAssignmentParameters>(readFile(path))
        val items = itemProvider()
        val sources = itemSourceProvider()

        PointAssigner(
            params,
            sources,
            items,
        ).calculatePoints()
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

    private suspend fun RunConfiguration.DataSource.itemSourceProvider() = when (this) {
        is RunConfiguration.DataSource.InMemory -> InMemoryItemSourceProvider(preseed(itemSourcePreseedFile))
    }

    private suspend fun RunConfiguration.DataSource.itemProvider() = when (this) {
        is RunConfiguration.DataSource.InMemory -> InMemoryItemProvider(preseed(itemPreseedFile))
    }

    private suspend fun RunConfiguration.DataSource.customTaskProvider() = when (this) {
        is RunConfiguration.DataSource.InMemory -> InMemoryCustomTaskProvider(preseed(customTaskPreseedFile))
    }

    suspend inline fun <reified T> preseed(path: String): List<T> = try {
        Json.decodeFromString(readFile(path))
    } catch (e: Exception) {
        println("Failed to load preseed file $path")
        e.printStackTrace()
        throw e
    }

}