package com.chase.utilities

import kotlinx.serialization.Serializable

@Serializable
class WeirdGloopNpc(
    override val id: Int,
    override val name: String,
    override val configName: String,
) : WeirdGloopEntity