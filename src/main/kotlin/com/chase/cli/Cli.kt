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
import com.chase.utilities.toFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.serialization.json.Json

class Cli(
    val itemSourceProvider: ItemSourceProvider,
    val itemProvider: ItemProvider,
    val taskProvider: TaskProvider,
    val runConfiguration: RunConfiguration,
) {

    private val commands = listOf(
        "get" runs {
            subCommand {
                "item" runs {
                    println(itemProvider.get(arg<Int>(0)) ?: "Item does not exist")
                }
                "itemsource" runs {
                    val item = itemSourceProvider.get(arg<Int>(0))
                    println(item?.copy(drops = emptyList()) ?: "ItemSource does not exist")
                    item?.drops?.forEach {
                        println("${itemProvider.get(it.itemId)?.name ?: "unknown item"} at 1 / ${it.dropRate}")
                    }
                }
                "task" runs {
                    println(taskProvider.get(arg<Int>(0)) ?: "Task does not exist")
                }
            }
        },
        "search" runs {
            subCommand {
                "items" runs {
                    itemProvider.search(arg(0)).onItemOrEmpty("No items match") {
                        println("${it.id}: ${it.name}")
                    }
                }
                "itemsources" runs {
                    itemSourceProvider.search(arg(0)).onItemOrEmpty("No itemSources match") {
                        println("${it.id}: ${it.name}")
                    }
                }
                "tasks" runs {
                    TODO("")
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
                    ItemSourceBuilder(itemProvider).getItems().forEach {
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
        "remove" runs {
            subCommand {
                "item" runs {
                    println(itemProvider.remove(arg<Int>(0)) ?: "Item does not exist")
                }
                "itemsource" runs {
                    println(itemSourceProvider.remove(arg<Int>(0)) ?: "ItemSource does not exist")
                }
                "task" runs {
                    println(taskProvider.remove(arg<Int>(0)) ?: "Task does not exist")
                }
            }
        },
        "export" runs {
            if (runConfiguration.dataSource !is RunConfiguration.DataSource.InMemory) {
                error("Cannot export data unless the data source is in memory.")
            }

            println("Exporting Items...")
            itemProvider.stream().toFile(runConfiguration.dataSource.itemPreseedFile) {
                Json.encodeToString(it)
            }

            println("Exporting Item Sources...")
            itemSourceProvider.stream().toFile(runConfiguration.dataSource.itemSourcePreseedFile) {
                Json.encodeToString(it)
            }

            println("Exporting Tasks...")
            taskProvider.stream().toFile(runConfiguration.dataSource.customTaskPreseedFile) {
                Json.encodeToString(it)
            }
        },
        "generate" runs {

        }
    )

    suspend fun run() {
        while (loop());
    }

    private suspend fun loop(): Boolean {
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

    private suspend fun <T> Flow<T>.onItemOrEmpty(emptyMessage: String, onItem: (T) -> Unit) = onEmpty {
        println(emptyMessage)
    }.collect { onItem(it) }

}