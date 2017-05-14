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

    val valueProperty = object : SimpleObjectProperty<X>() {
        override fun get(): X {
            if (retrieveFromDb) {
                super.set(retrieve())
                retrieveFromDb = false
            }
            return super.get()
        }

        override fun set(newValue: X) {
            super.set(newValue)
            process(value)
        }
    }
    protected var _value by valueProperty

    var retrieveFromDb = true

    protected abstract fun retrieve(): X
    protected abstract fun process(value: X)

    operator fun getValue(ignore: Entity, ignore2: KProperty<*>): X = _value
    operator fun setValue(ignore: Entity, ignore2: KProperty<*>, newValue: X) {
        _value = newValue
    }

}