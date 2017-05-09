package com.doubleu.kotlintrader.database

import kotlin.reflect.KProperty

/**
 * Delegates the model properties to the database
 */
class DatabaseDelegate<T> {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(entity: Entity, property: KProperty<*>): T? {
        return Database.getProperty(entity, property as KProperty<T>)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun setValue(entity: Entity, property: KProperty<*>, value: T?) {
        Database.setProperty(entity, property as KProperty<T>, value)
    }

}