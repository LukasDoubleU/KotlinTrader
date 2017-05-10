package com.doubleu.kotlintrader.extensions

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.database.RefEntity
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

fun <R> KProperty<R>.isBoolean() = this.clazz().isSubclassOf(Boolean::class)

fun <R> KProperty<R>.isEntity() = this.clazz().isSubclassOf(Entity::class)

fun <R> KProperty<R>.isRefEntity() = this.clazz().isSubclassOf(RefEntity::class)

fun <R> KProperty<R>.clazz() = this.returnType.jvmErasure

fun Boolean?.toInt() = if (this.value()) 1 else 0

fun Boolean?.value() = this ?: false

fun Boolean?.toSQLString() = (if (this.value()) "'1'" else "'0'")

fun Any?.valueOf() = this?.toString() ?: ""