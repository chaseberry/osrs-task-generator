package com.chase.cli

class CommandExecutionException(
    val command: String,
    message: String
) : RuntimeException(message) {
}