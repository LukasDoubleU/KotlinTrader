package com.doubleu.kotlintrader.extensions

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import tornadofx.*
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

fun <R> KProperty<R>.isBoolean() = this.clazz().isSubclassOf(Boolean::class)

fun <R> KProperty<R>.clazz() = this.returnType.jvmErasure

fun <R> KProperty<R>.get() = this.getter.call()

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

fun Region.fill() {
    useMaxWidth = true
    hgrow = Priority.ALWAYS
    vgrow = Priority.ALWAYS
}

fun Number.pow(number: Number) = Math.pow(this.toDouble(), number.toDouble())

fun Number.sqrt() = Math.sqrt(this.toDouble())

fun <T> ObservableValue<T>.onChangeWithOld(op: (T, T) -> Unit) = apply { addListener { _, oldValue, newValue -> op(oldValue, newValue) } }

fun <T : Any?> Property<T>.mutateOnChange(mutator: (T?) -> T?) = onChange {
    val changed = mutator(value)
    if (changed != value) value = changed
}

fun <T> Property<T>.mutateOnChangeWithOld(mutator: (T, T) -> T) = onChangeWithOld<T> { old, new ->
    if (old == new) return@onChangeWithOld
    val changed = mutator(old, new)
    value = changed
}