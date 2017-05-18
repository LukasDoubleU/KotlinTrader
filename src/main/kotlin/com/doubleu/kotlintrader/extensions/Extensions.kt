package com.doubleu.kotlintrader.extensions

import com.doubleu.kotlintrader.database.Entity
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import tornadofx.*
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

fun <R> KProperty<R>.isBoolean() = this.clazz().isSubclassOf(Boolean::class)

fun <R> KProperty<R>.isEntity() = this.clazz().isSubclassOf(Entity::class)

fun <R> KProperty<R>.clazz() = this.returnType.jvmErasure

fun <R> KProperty<R>.get() = this.getter.call()

fun <R, V> KMutableProperty<R>.set(value: V) = this.setter.call(value)

fun Any?.valueOf() = this?.toString() ?: ""

fun VBox.center() {
    alignment = Pos.CENTER
}

fun HBox.center() {
    alignment = Pos.CENTER
}

fun Region.fillHorizontally() {
    useMaxWidth = true
    hgrow = Priority.ALWAYS
}

fun Number.pow(number: Number) = Math.pow(this.toDouble(), number.toDouble())

fun Number.sqrt() = Math.sqrt(this.toDouble())

fun <T> ObservableValue<T>.onChangeWithOld(op: (T, T) -> Unit) = apply { addListener { _, oldValue, newValue -> op(oldValue, newValue) } }

private val nf = NumberFormat.getInstance(Locale.ENGLISH).apply { isGroupingUsed = false }

fun Number.limitDecimals(places: Number) = nf.apply {
    minimumFractionDigits = places.toInt()
    maximumFractionDigits = places.toInt()
}.format(this.toDouble()).toDouble()

fun random(from: Number, to: Number) = ThreadLocalRandom.current().nextDouble(from.toDouble(), to.toDouble()).limitDecimals(2)