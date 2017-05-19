package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.limitDecimals
import kotlin.reflect.KProperty

/**
 * Delegates simple properties to the database
 */
class PropertyDelegate<T, V>(val entity: Entity<T>, val field: KProperty<V>) : DatabaseDelegate<V>() {

    override fun retrieve(): V {
        val dbValue = Database.getProperty(entity, field)
        if (dbValue == null) {
            val defaultValue = entity.default(field)
            valueProperty.set(defaultValue)
            return defaultValue
        }
        return dbValue
    }

    override fun process(value: V) {
        when (value) {
            is Double -> Database.setProperty(entity, field, value.toInt())
            is Float -> Database.setProperty(entity, field, value.toInt())
            else -> Database.setProperty(entity, field, value)
        }
    }
}