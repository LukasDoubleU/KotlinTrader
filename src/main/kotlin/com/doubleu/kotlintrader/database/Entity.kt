package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.delegates.DatabaseDelegate
import com.doubleu.kotlintrader.delegates.MutableReferenceDelegate
import com.doubleu.kotlintrader.delegates.PropertyDelegate
import com.doubleu.kotlintrader.delegates.ReferenceDelegate
import com.doubleu.kotlintrader.extensions.get
import javafx.beans.property.Property
import kotlin.collections.set
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * A simple Database Entity. ID Column is required
 */
abstract class Entity {

    abstract val id: Long

    val delegateMap = mutableMapOf<KProperty<*>, DatabaseDelegate<*>>()

    /**
     * Reloads the Entity from the database
     *
     * [lazy]: if true the values get updated the next time they are being called
     */
    fun retrieve(lazy: Boolean) {
        for ((property, delegate) in delegateMap) {
            delegate.retrieveFromDb = true
            if (!lazy) {
                property.get()
            }
        }
    }

    /**
     * Returns a [Delegate][PropertyDelegate] to the given [property].
     */
    protected fun <V> delegate(property: KProperty<V>): PropertyDelegate<V> {
        val delegate = PropertyDelegate(this, property)
        delegateMap[property] = delegate
        return delegate
    }

    /**
     * Returns a [Delegate][ReferenceDelegate] to the given [property].
     */
    protected inline fun <reified T : Entity> reference(
            key: KProperty<T>,
            property: KProperty<Long>)
            : ReferenceDelegate<T> {
        val delegate = ReferenceDelegate(T::class, property)
        delegateMap[key] = delegate
        return delegate
    }

    /**
     * Returns a [Delegate][ReferenceDelegate] to the given [property].
     */
    protected inline fun <reified T : Entity> mutableReference(
            key: KMutableProperty0<T?>,
            property: KMutableProperty0<Long?>)
            : MutableReferenceDelegate<T> {
        val delegate = MutableReferenceDelegate(T::class, property)
        delegateMap[key] = delegate
        return delegate
    }

    /**
     * Returns the [FXProperty][Property] associated with the passed [Entity-Property][property]
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <V> property(property: KProperty<V>): Property<V> = (delegateMap[property]?.valueProperty
            ?: throw RuntimeException("${property.name} wasn't yet delegated!")) as Property<V>

}