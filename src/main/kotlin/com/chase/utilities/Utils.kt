package com.chase.utilities

import com.chase.cli.Command
import com.chase.cli.CommandInput
import com.chase.cli.CommandRunner
import kotlin.reflect.KClass

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

fun listParam(name: String): List<String> = listParams(name) { this }

fun <T> listParams(name: String, error: String? = null, convert: String.() -> T?): List<T> {
    val items = ArrayList<T>()
    println(name)

    do {
        print("> ")
        val r = readln().takeIf { it.isNotBlank() } ?: break
        convert(r)?.let { items.add(it) } ?: println(error ?: "Failed to convert $r")
    } while (true)

    return items
}

fun <T : Enum<T>> enumParam(name: String, clazz: KClass<T>): T {
    do {
        print("$name: ")
        when (val r = readln().takeIf { it.isNotBlank() }) {
            "?" -> printEnum(clazz)
            null -> continue
            else -> clazz.findEnum(r)?.let { return it }
        }
    } while (true)
}

fun <T : Enum<T>> listEnum(name: String, clazz: KClass<T>): List<T> {
    val items = ArrayList<T>()
    println("$name: ")

    do {
        print("> ")
        when (val r = readln().takeIf { it.isNotBlank() }) {
            "?" -> printEnum(clazz)
            null -> break
            else -> clazz.findEnum(r)?.let { items.add(it) }
        }
    } while (true)

    return items
}

fun <T : Enum<T>> printEnum(clazz: KClass<T>) = println(clazz.java.enumConstants.joinToString(", "))

fun <T : Enum<T>> KClass<T>.findEnum(entry: String): T? {
    return java.enumConstants.find { it.name.equals(entry, true) }
}

infix fun String.runs(onInvoke: CommandRunner.() -> Unit) = Command(this, onInvoke)