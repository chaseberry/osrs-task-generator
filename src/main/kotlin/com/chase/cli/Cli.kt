package com.chase.cli

import com.chase.RunConfiguration
import com.chase.providers.ItemProvider
import com.chase.providers.ItemSourceProvider
import com.chase.providers.TaskProvider
import com.chase.utilities.ItemBuilder
import com.chase.utilities.ItemSourceBuilder
import com.chase.utilities.TaskBuilder
import com.chase.utilities.param
import com.chase.utilities.runs

class Cli(
    val itemSourceProvider: ItemSourceProvider,
    val itemProvider: ItemProvider,
    val taskProvider: TaskProvider,
    val runConfiguration: RunConfiguration,
) : Runnable {

    private val commands = listOf(
        "search" runs {
            subCommand {
                "items" runs {
                    val search = arg<String>(0)
                }
                "itemsources" runs {

                }
                "tasks" runs {

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
                "tasks" runs {
                    TaskBuilder().getItems().forEach {
                        try {
                            taskProvider.add(it)
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
            if (runConfiguration.dataSource !is RunConfiguration.DataSource.InMemory) {
                error("Cannot export data unless the data source is in memory.")
            }

            itemProvider
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

        try {
            commands.firstOrNull {
                it.tryInvoke(input)
            } ?: println("unknown command")
        } catch (e: CommandExecutionException) {
            println("Error running ${e.command}")
            println(" > ${e.message}")
        }

        return true
    }

}