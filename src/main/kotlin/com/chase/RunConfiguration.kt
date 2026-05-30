package com.chase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RunConfiguration(
    val dataSource: DataSource,
    val runMode: RunMode,
) {

    @Serializable
    sealed class RunMode() {

        @SerialName("Cli")
        @Serializable
        object Cli : RunMode()

        @SerialName("Generate")
        @Serializable
        class Generate(
            val parametersFile: String
        ): RunMode()
    }

    @Serializable
    sealed class DataSource {

        @Serializable
        @SerialName("InMemory")
        class InMemory(
            val itemPreseedFile: String,
            val itemSourcePreseedFile: String,
            val customTaskPreseedFile: String,
        ) : DataSource()
    }

}