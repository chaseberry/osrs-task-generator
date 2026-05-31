package com.chase

import kotlinx.coroutines.runBlocking

fun main(args: Array<String>): Unit = runBlocking {

    val configString = args.firstOrNull()?.trim()
        ?: throw IllegalArgumentException("Configuration is required to run OSRS Task Generator")

    OsrsTaskGenerator(configString).start()
}

/* TODO
    6. update ancient wyverns with granite boots/sword
*/