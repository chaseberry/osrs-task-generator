package com.chase.cli

import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.utilities.ItemBuilder
import com.chase.utilities.ItemSourceBuilder
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
        },
        "add" runs {
            subCommand {
                "items" runs {
                    ItemBuilder().getItems().forEach {
                        try {
                            itemProvider.add(it)
                            println("Added $it")
                        } catch (e: IllegalArgumentException) {
                            println("Failed to add $it")
                            println(e.message)
                        }
                    }
                }
                "itemsources" runs {
                    ItemSourceBuilder().getItems().forEach {
                        try {
                            itemSourceProvider.add(it)
                            println("Added $it")
                        } catch (e: IllegalArgumentException) {
                            println("Failed to add $it")
                            println(e.message)
                        }
                    }
                }
            }
        },
        "export" runs {
            TODO("export providers to save file")
        },
        "generate" runs {

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