package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Entity
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.primaryConstructor

/**
 * Delegates mutable Entity-References to the database
 */
class MutableReferenceDelegate<T : Entity>(
        val referencedClazz: KClass<T>,
        val field: KMutableProperty0<Long?>)
    : DatabaseDelegate<T?>() {

    override fun retrieve() = referencedClazz.primaryConstructor!!.call(field.get())

    override fun process(value: T?) = field.set(value?.id)
}
