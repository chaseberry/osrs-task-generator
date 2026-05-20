package com.chase.utilities

fun param(name: String): String {
    do {
        print("$name: ")
        readln().takeIf { it.isNotBlank() }?.let {
            return it
        }
    } while (true)
}

fun <T> param(name: String, error: String? = null, convert: String.() -> T?): T {
    do {
        convert(param(name))?.let {
            return it
        } ?: println(error ?: "Failed to convert $name")
    } while (true)
}

fun params(vararg names: String): List<String> = names.map { param(it) }

fun listParam(name: String): List<String> {
    val items = ArrayList<String>()
    println("name")
}