package com.doubleu.trader

import com.doubleu.trader.database.Database
import kotlin.reflect.KProperty

/**
 * Delegates the model properties to the database
 */
class DatabaseDelegate<T> {

	operator fun getValue(entity: Entity, property: KProperty<*>) = Database.getProperty<T>(entity, property.name)

	operator fun setValue(entity: Entity, property: KProperty<*>, value: T?) = Database.setProperty<T>(entity, property.name, value)

}