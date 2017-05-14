package com.doubleu.kotlintrader.database

/**
 * Database Entity with 2 ID Columns
 */
abstract class RefEntity : Entity() {

    abstract val id2: Long

}