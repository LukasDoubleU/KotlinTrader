package com.doubleu.kotlintrader.database

/**
 * Database Entity with 2 ID Columns
 */
abstract class RefEntity(
        override val id: Long,
        val id2: Long
) : Entity(id)