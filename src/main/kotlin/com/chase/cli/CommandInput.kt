package com.chase.cli

data class CommandInput(
    val raw: String,
    val command: String,
    val args: List<String>,
) {

    fun toSubCommandInput(): CommandInput? = CommandInput(
        raw = args.joinToString(" "),
        command = args.firstOrNull() ?: return null,
        args = args.drop(1),
    )

    companion object {
        fun parse(input: String): CommandInput {
            val parts = input.split(Regex("\\s+"))

            return CommandInput(
                input,
                parts.first().lowercase(),
                parts.drop(1),
            )
        }
    }
}