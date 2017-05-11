package com.doubleu.kotlintrader.delegates

import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.Property
import kotlin.reflect.KProperty

abstract class DatabaseDelegate<X> {
    abstract val property: Property<*>
    abstract operator fun getValue(ignore: Entity, ignore2: KProperty<*>): X
    abstract operator fun setValue(ignore: Entity, ignore2: KProperty<*>, value: X)
}