package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.delegates.DatabaseDelegate
import com.doubleu.kotlintrader.delegates.MutableReferenceDelegate
import com.doubleu.kotlintrader.delegates.PropertyDelegate
import com.doubleu.kotlintrader.delegates.ReferenceDelegate
import com.doubleu.kotlintrader.extensions.get
import com.doubleu.kotlintrader.extensions.set
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleLongProperty
import tornadofx.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

/**
 * A simple Database Entity. ID Column is required
 */
abstract class Entity<T>(open val id: Long,
                         val idProperty: LongProperty = SimpleLongProperty(id)) {

    companion object {
        private var defaultsMap = mutableMapOf<KProperty<*>, Any>()
    }

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
     * Retrieves the default value for the given property
     */
    @Suppress("UNCHECKED_CAST")
    fun <V> default(property: KProperty<V>) = (defaultsMap[property]
            ?: throw RuntimeException("No default provided for ${property.name}")) as V

    /**
     * Returns a [Delegate][PropertyDelegate] to the given [property].
     */
    protected fun <V : Any> delegate(property: KProperty<V>, default: V): PropertyDelegate<T, V> {
        defaultsMap.put(property, default)
        val delegate = PropertyDelegate(this, property)
        delegateMap[property] = delegate
        return delegate
    }

    /**
     * Returns a [Delegate][ReferenceDelegate] to the given [property].
     */
    protected inline fun <reified E : Entity<E>> reference(
            key: KProperty<E>,
            property: KProperty<Long>)
            : ReferenceDelegate<E> {
        val delegate = ReferenceDelegate(E::class, property)
        delegateMap[key] = delegate
        return delegate
    }

    /**
     * Returns a [Delegate][ReferenceDelegate] to the given [property].
     */
    protected inline fun <reified E : Entity<E>> mutableReference(
            key: KMutableProperty0<E>,
            property: KMutableProperty0<Long>)
            : MutableReferenceDelegate<E> {
        val delegate = MutableReferenceDelegate(E::class, property)
        delegateMap[key] = delegate
        return delegate
    }

    /**
     * Returns the [FXProperty][Property] associated with the passed [Entity-Property][property]
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <V> property(property: KProperty<V>): Property<V> = (delegateMap[property]?.valueProperty
            ?: throw RuntimeException("${property.name} wasn't yet delegated!")) as Property<V>

    /**
     * Returns an [ItemViewModel] containing the given [Entity][T]
     */
    abstract fun model(property: ObjectProperty<T?>): ItemViewModel<T?>

    /**
     * Resets this entity back to it's default values
     */
    fun reset() = delegateMap.keys.asSequence()
            .filter { it is KMutableProperty }
            .map { it as KMutableProperty }
            .forEach { it.set(this.default(it)) }
}