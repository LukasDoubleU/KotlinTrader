package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.get
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.primaryConstructor

/**
 * Delegates an immutable Entity-Reference to the database
 */
class ReferenceDelegate<T : Entity>(val referencedClazz: KClass<T>, val field: KProperty<Long>) : DatabaseDelegate<T>() {

    override val property: Property<T> = MyProperty()

    fun getValue() = referencedClazz.primaryConstructor!!.call(field.get())

    inner class MyProperty : SimpleObjectProperty<T>() {

        override fun get() = this@ReferenceDelegate.getValue()

        override fun set(newValue: T) = immutable()
    }

    override operator fun getValue(ignore: Entity, ignore2: KProperty<*>) = getValue()

    override operator fun setValue(ignore: Entity, ignore2: KProperty<*>, value: T) = immutable()

    private fun immutable(): Nothing
            = throw UnsupportedOperationException("${this@ReferenceDelegate::class.simpleName} is immutable!")

}