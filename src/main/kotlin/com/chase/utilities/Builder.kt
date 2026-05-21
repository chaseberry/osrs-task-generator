package com.chase.utilities

abstract class Builder<T> {

    protected abstract fun newItem(): T

    fun getItems(): List<T> {

    }

}