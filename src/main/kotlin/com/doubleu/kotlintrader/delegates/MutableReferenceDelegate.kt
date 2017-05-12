package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.full.primaryConstructor

class MutableReferenceDelegate<T : Entity>(
        val referencedClazz: KClass<T>,
        val field: KMutableProperty0<Long?>)
    : DatabaseDelegate<T?>() {

    override val property: Property<T> = MyProperty()

    fun getValue() = referencedClazz.primaryConstructor!!.call(field.get())

    fun setValue(value: T?, setProperty: Boolean) {
        field.set(value?.id?.toLong())
        // Don't set the property explicitly
        // (false when the property delegated us here)
        if (setProperty) property.value = value
    }

    inner class MyProperty : SimpleObjectProperty<T>() {
        override fun get() = this@MutableReferenceDelegate.getValue()
        override fun set(v: T?) = this@MutableReferenceDelegate.setValue(v, false)
    }

    override operator fun getValue(ignore: Entity, ignore2: KProperty<*>) = getValue()

    override operator fun setValue(ignore: Entity, ignore2: KProperty<*>, value: T?) = setValue(value, false)
}
