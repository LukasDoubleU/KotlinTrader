package com.doubleu.kotlintrader.extensions

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
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

fun Bindings.never() = SimpleBooleanProperty(false)

fun Bindings.always() = SimpleBooleanProperty(true)

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

fun Region.fillVertically() {
    useMaxWidth = true
    vgrow = Priority.ALWAYS
}

fun Region.fill() {
    fillHorizontally()
    fillVertically()
}