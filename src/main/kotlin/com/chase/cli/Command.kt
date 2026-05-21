package com.chase.cli

class Command(
    private val command: String,
    private val onInvoke: CommandRunner.() -> Unit,
) {

    fun tryInvoke(input: CommandInput): Boolean {
        if (input.command != command) {
            return false
        }

        val runner = CommandRunner(input)
        onInvoke(runner)

        return true
    }

}