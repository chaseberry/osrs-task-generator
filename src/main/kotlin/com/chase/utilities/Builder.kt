package com.chase.utilities

abstract class Builder<T>(val name: String) {

    protected abstract fun newItem(): T

    fun getItems(): List<T> {
        val items = ArrayList<T>()

        do {
            print("Add $name Y/N: ")
            if (!readln().first().equals('y', true)) {
                break
            }

            items.add(newItem())

        } while (true)

        return items
    }

}