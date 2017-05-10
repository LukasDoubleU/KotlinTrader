package com.doubleu.kotlintrader.database

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import kotlin.reflect.KProperty

/**
 * Delegates the model properties to the database
 */
class DatabaseDelegate<V>(val entity: Entity, val field: KProperty<V?>) {

    val property: Property<V> = MyProperty()

    fun getValue() = Database.getProperty(entity, field)

    fun setValue(value: V?, setProperty: Boolean) {
        Database.setProperty(entity, field, value)
        // Don't set the property explicitly
        // (false when the property delegated us here)
        if (setProperty) property.value = value
    }

    inner class MyProperty : SimpleObjectProperty<V>() {

        override fun getValue() = this@DatabaseDelegate.getValue()

        override fun setValue(v: V?) = this@DatabaseDelegate.setValue(v, false)

    }

    operator fun getValue(ignore: Entity, ignore2: KProperty<*>) = getValue()

    operator fun setValue(ignore: Entity, ignore2: KProperty<*>, value: V?) = setValue(value, true)

}