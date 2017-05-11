package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import kotlin.reflect.KProperty

class PropertyDelegate<V>(val entity: Entity, val field: KProperty<V?>) : DatabaseDelegate<V?>() {

    override val property: Property<V> = MyProperty()

    fun getValue() = Database.getProperty(entity, field)

    fun setValue(value: V?, setProperty: Boolean) {
        Database.setProperty(entity, field, value)
        // Don't set the property explicitly
        // (false when the property delegated us here)
        if (setProperty) property.value = value
    }

    inner class MyProperty : SimpleObjectProperty<V>() {
        override fun get() = this@PropertyDelegate.getValue()
        override fun set(v: V?) = this@PropertyDelegate.setValue(v, false)
    }

    override operator fun getValue(ignore: Entity, ignore2: KProperty<*>) = getValue()

    override operator fun setValue(ignore: Entity, ignore2: KProperty<*>, value: V?) = setValue(value, true)
}