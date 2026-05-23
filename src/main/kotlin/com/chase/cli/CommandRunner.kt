package com.chase.cli

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

    fun get(index: Int, message: String? = null): Any {
        return input.args.getOrNull(index) ?: error(message ?: "Missing argument @ position $index")
    }

    inline fun <reified T> arg(index: Int, message: String? = null): T {
        return get(0, message) as? T ?: error(
            message ?: "Cannot convert argument @ position $index to ${T::class.simpleName}"
        )
    }

    fun error(message: String): Nothing = throw CommandExecutionException(input.raw, message)
}