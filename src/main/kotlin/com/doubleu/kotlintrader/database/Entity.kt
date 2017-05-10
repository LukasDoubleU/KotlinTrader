package com.doubleu.kotlintrader.database

import javafx.beans.property.Property
import kotlin.collections.set
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * A simple Database Entity. ID Column is required
 */
abstract class Entity {

    init {
        registerId(id)
    }

    abstract val id: Int

    private val delegateMap = mutableMapOf<KProperty<*>, DatabaseDelegate<*>>()

    fun <V> delegate(property: KProperty<V>): DatabaseDelegate<V> {
        val delegate = DatabaseDelegate(this, property)
        delegateMap[property] = delegate
        return delegate
    }

    fun <T : Entity, V> reference(property: KMutableProperty<V>) = null

    // TODO can type safety be achieved there? (Look a the map)
    fun <V> property(property: KProperty<V>): Property<V?> = (delegateMap[property]?.property
            ?: throw RuntimeException("${property.name} wasn't yet delegated!")) as Property<V?>

    protected fun registerId(id: Int) {
        // TODO: Use INSERT if not present, think of default values
    }

//    class ReferenceProperty<T, V>(val property: KMutableProperty<V>) : SimpleObjectProperty<V>() {
//        override fun getValue() = property.getter.call() as T?
//        override fun setValue(value: T?) = property.setter.call(value)
//    }

}