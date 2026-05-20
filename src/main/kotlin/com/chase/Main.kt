package com.chase

import com.chase.models.sources.ItemDrop
import com.chase.models.sources.ItemSource
import com.chase.models.sources.ItemSourceCategory

fun main() {
    val testDataset = listOf(
        ItemSource(
            "KBD",
            ItemSourceCategory.Boss,
            listOf(
                ItemDrop("D Pick", 1000),
                ItemDrop("KBD Heads", 128),
                ItemDrop("Elite Clue", 450),
                ItemDrop("Visage", 5000)
            ),
            60
        ),
        ItemSource(
            "KQ",
            ItemSourceCategory.Boss,
            listOf(
                ItemDrop("D Chain", 128),
                ItemDrop("D2h", 256),
                ItemDrop("D Pick", 400),
                ItemDrop("Jar of Sand", 2000)
            ),
            60
        )
    )
}