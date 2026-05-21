package com.chase.cli

import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.utilities.param
import com.chase.utilities.runs

class Cli(
    val itemSourceProvider: ItemSourceProvider,
    val itemProvider: ItemProvider,
) : Runnable {

    private val commands = listOf(
        "search" runs {
            subCommand {
                "items" runs {

                }
                "itemsources" runs {

                }
            }
        }
    )

    override fun run() {
        while (loop());

    }

    private fun loop(): Boolean {
        val input = CommandInput.parse(param("command"))
        if (input.command == "quit") {
            return false
        }

        commands.firstOrNull() {
            it.tryInvoke(input)
        } ?: println("unknown command")

        return true
    }

}