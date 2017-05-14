package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import kotlin.reflect.KProperty

/**
 * Super class for all Delegates that delegate Entity fields to the database.
 * Implementations of this also must provide a [Property].
 */
abstract class DatabaseDelegate<X> {

    internal val valueProperty = SimpleObjectProperty<X>()
    internal var value by valueProperty

    var retrieveFromDb = true

    internal abstract fun retrieve(): X
    internal abstract fun process(value: X)

    operator fun getValue(ignore: Entity, ignore2: KProperty<*>) = getValue()
    operator fun setValue(ignore: Entity, ignore2: KProperty<*>, value: X) = setValue(value)

    fun getValue(): X {
        if (retrieveFromDb) {
            value = retrieve()
            retrieveFromDb = false
        }
        return value
    }

    fun setValue(value: X) {
        this.value = value
        process(value)
    }

}