package com.chase.cli

class CommandRunner(
    private val input: CommandInput,
) {
    val commands = mutableListOf<Command>()
    infix fun String.runs(onInvoke: CommandRunner.() -> Unit) {
        commands.add(Command(this, onInvoke))
    }

    fun subCommand(bld: CommandRunner.() -> Unit) {
        val subInput = input.toSubCommandInput() ?: return

        bld(this)
        commands.firstOrNull() { it.tryInvoke(subInput) }
    }
}