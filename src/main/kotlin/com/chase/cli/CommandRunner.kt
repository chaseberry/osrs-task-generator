package com.chase.cli

import kotlin.reflect.KClass

class CommandRunner(
    private val input: CommandInput,
) {
    val commands = mutableListOf<Command>()
    infix fun String.runs(onInvoke: suspend CommandRunner.() -> Unit) {
        commands.add(Command(this, onInvoke))
    }

    suspend fun subCommand(bld: CommandRunner.() -> Unit) {
        val subInput = input.toSubCommandInput() ?: return

        bld(this)
        commands.firstOrNull { it.tryInvoke(subInput) }
    }

    fun get(index: Int, message: String? = null): String {
        return input.args.getOrNull(index) ?: error(message ?: "Missing argument @ position $index")
    }

    inline fun <reified T : Any> arg(index: Int, message: String? = null): T {
        return convertType(get(index, message), T::class) ?: error(
            message ?: "Cannot convert argument @ position $index to ${T::class.simpleName}"
        )
    }

    fun error(message: String): Nothing = throw CommandExecutionException(input.raw, message)

    fun <T : Any> convertType(arg: String, clazz: KClass<T>): T? {
        return when (clazz) {
            Int::class -> arg.toIntOrNull()
            Double::class -> arg.toDoubleOrNull()
            String::class -> arg
            else -> null
        } as? T
    }
}