package com.chase.utilities

abstract class Builder<T>(val name: String) {

    protected abstract suspend fun newItem(): T

    suspend fun getItems(): List<T> {
        val items = ArrayList<T>()

        do {
            try {
                items.add(newItem())

                print("Add Another $name Y/N: ")
                if (readln().firstOrNull()?.equals('n', true) == true) {
                    break
                }
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        } while (true)

        return items
    }
}