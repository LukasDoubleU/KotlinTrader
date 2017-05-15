package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.database.Entity
import kotlin.reflect.KProperty

/**
 * Delegates simple properties to the database
 */
class PropertyDelegate<V>(val entity: Entity, val field: KProperty<V>) : DatabaseDelegate<V>() {

    override fun retrieve() = Database.getProperty(entity, field)

    override fun process(value: V) = Database.setProperty(entity, field, value)
}