package com.doubleu.kotlintrader.database

/**
 * A simple Database Entity. ID Column is required
 */
abstract class Entity {

    init {
        registerId(id)
    }

    abstract val id: Int

    protected fun registerId(id: Int) {
        // TODO: Use INSERT if not present
    }

}