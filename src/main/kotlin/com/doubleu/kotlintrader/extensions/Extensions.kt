package com.doubleu.kotlintrader.extensions

import javafx.beans.binding.BooleanBinding
import javafx.beans.property.Property
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

fun Property<Boolean?>.asExpression() = BooleanBinding.booleanExpression(this)

fun <R> KProperty<R>.isBoolean() = this.returnType.jvmErasure == Boolean::class

fun Boolean?.toInt() = if (this.value()) 1 else 0

fun Boolean?.value() = this ?: false

fun Boolean?.toSQLString() = (if (this.value()) "'1'" else "'0'")

fun Any?.valueOf() = this?.toString() ?: ""