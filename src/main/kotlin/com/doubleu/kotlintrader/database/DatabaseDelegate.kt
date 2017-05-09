package com.doubleu.kotlintrader.database

import kotlin.reflect.KProperty

/**
 * Delegates the model properties to the database
 */
class DatabaseDelegate<V> {

    operator fun getValue(entity: Entity, property: KProperty<*>): V? {
        return Database.getProperty(entity, property as KProperty<V>)
    }

    operator fun setValue(entity: Entity, property: KProperty<*>, value: V?) {
        Database.setProperty(entity, property as KProperty<V>, value)
    }

}