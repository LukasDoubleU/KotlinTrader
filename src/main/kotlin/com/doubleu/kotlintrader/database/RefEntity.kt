package com.doubleu.kotlintrader.database

import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty

/**
 * Database Entity with 2 ID Columns
 */
abstract class RefEntity<T : RefEntity<T>>(
        override val id: Long,
        val id2: Long,
        val id2Property: LongProperty = SimpleLongProperty(id2)
) : Entity<T>(id)