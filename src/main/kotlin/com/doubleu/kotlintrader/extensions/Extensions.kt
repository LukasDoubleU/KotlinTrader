package com.doubleu.kotlintrader.extensions

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.database.RefEntity
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

fun <R> KProperty<R>.isBoolean() = this.clazz().isSubclassOf(Boolean::class)

fun <R> KProperty<R>.isEntity() = this.clazz().isSubclassOf(Entity::class)

fun <R> KProperty<R>.isRefEntity() = this.clazz().isSubclassOf(RefEntity::class)

fun <R> KProperty<R>.clazz() = this.returnType.jvmErasure

fun <R> KProperty<R>.get() = this.getter.call()

fun <R> KMutableProperty<R>.set(value: R?) = this.setter.call(value)

//fun <R : Entity> KClass<R>.newInstance(vararg params: Any?): R {
//    println("Spread: ${params.valueOf()}")
//    println("Class: ${this.simpleName}")
//    return this.primaryConstructor!!.call(params)
//}
//
//inline fun <reified R : Entity> KClass<R>.newInstance(vararg params: Any?): R {
//    println("Spread: ${params.valueOf()}")
//    println("Class: ${R::class.simpleName}")
//    return R::class.primaryConstructor!!.call(params)
//}

fun Boolean?.toInt() = if (this.value()) 1 else 0

fun Boolean?.value() = this ?: false

fun Boolean?.toSQLString() = (if (this.value()) "'1'" else "'0'")

fun Any?.valueOf() = this?.toString() ?: ""