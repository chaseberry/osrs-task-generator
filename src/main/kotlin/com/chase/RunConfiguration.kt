package com.chase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RunConfiguration(
    val dataSource: DataSource,
    val runMode: RunMode,
) {

    enum class RunMode { Cli }

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