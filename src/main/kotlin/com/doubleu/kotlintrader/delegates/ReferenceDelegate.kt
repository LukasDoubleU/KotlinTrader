package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.get
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.primaryConstructor

/**
 * Delegates an immutable Entity-Reference to the database
 */
class ReferenceDelegate<T : Entity<T>>(val referencedClazz: KClass<T>, val field: KProperty<Long>) : DatabaseDelegate<T>() {

    override fun retrieve() = referencedClazz.primaryConstructor!!.call(field.get())

    override fun process(value: T) = throw UnsupportedOperationException("${this@ReferenceDelegate::class.simpleName} is immutable!")

}