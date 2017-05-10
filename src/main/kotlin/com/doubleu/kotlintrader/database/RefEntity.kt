package com.doubleu.kotlintrader.database

/**
 * Database Entity with 2 ID Columns
 */
abstract class RefEntity : Entity() {

    init {
        registerId(id2)
    }

    abstract val id2: Int

}