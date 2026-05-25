package com.chase.utilities

abstract class Builder<T>(val name: String) {

    protected abstract suspend fun newItem(): T

    suspend fun getItems(): List<T> {
        val items = ArrayList<T>()

        do {
            items.add(newItem())

            print("Add Another $name Y/N: ")
            if (!readln().first().equals('y', true)) {
                break
            }
        } while (true)

        return items
    }

}