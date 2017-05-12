package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.delegates.DatabaseDelegate
import com.doubleu.kotlintrader.delegates.MutableReferenceDelegate
import com.doubleu.kotlintrader.delegates.PropertyDelegate
import com.doubleu.kotlintrader.delegates.ReferenceDelegate
import javafx.beans.property.Property
import kotlin.collections.set
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * A simple Database Entity. ID Column is required
 */
abstract class Entity {

    init {
        registerId(this.id)
    }

    abstract val id: Long

    val delegateMap = mutableMapOf<KProperty<*>, DatabaseDelegate<*>>()

    fun <V> delegate(property: KProperty<V>): PropertyDelegate<V> {
        val delegate = PropertyDelegate(this, property)
        delegateMap[property] = delegate
        return delegate
    }

    inline fun <reified T : Entity> reference(
            key: KProperty<T>,
            property: KProperty<Long>)
            : ReferenceDelegate<T> {
        val delegate = ReferenceDelegate(T::class, property)
        delegateMap[key] = delegate
        return delegate
    }

    inline fun <reified T : Entity> mutableReference(
            key: KMutableProperty0<T?>,
            property: KMutableProperty0<Long?>)
            : MutableReferenceDelegate<T> {
        val delegate = MutableReferenceDelegate(T::class, property)
        delegateMap[key] = delegate
        return delegate
    }

    @Suppress("UNCHECKED_CAST")
    fun <V> property(property: KProperty<V>): Property<V> = (delegateMap[property]?.property
            ?: throw RuntimeException("${property.name} wasn't yet delegated!")) as Property<V>

    protected fun registerId(id: Long) {
        // TODO: Use INSERT if not present, think of default values
    }
}