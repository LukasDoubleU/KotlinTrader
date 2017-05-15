package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.pow
import com.doubleu.kotlintrader.extensions.sqrt
import com.doubleu.kotlintrader.extensions.valueOf
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Ort(override val id: Long) : Entity<Ort>(id) {

    var name: String by delegate(this::name)
    val nameProperty = property(this::name)

    var kapazitaet: Double by delegate(this::kapazitaet)
    val kapazitaetProperty = property(this::kapazitaet)

    var x: Int by delegate(this::x)
    val xProperty = property(this::x)

    var y: Int by delegate(this::y)
    val yProperty = property(this::y)

    /**
     * Overridden for meaningful representation inside the combobox of the TradeView
     */
    override fun toString() = name.valueOf()

    /**
     * Calculates the distance between this and the [other] [Ort]
     */
    fun distanceTo(other: Ort) = ((other.x - x).pow(2) - (other.y - y).pow(2)).sqrt()

    override fun model(property: ObjectProperty<Ort?>) = Model(property)

    class Model(property: ObjectProperty<Ort?> = SimpleObjectProperty<Ort>())
        : ItemViewModel<Ort?>(itemProperty = property) {

        val id = bind { item?.idProperty }
        val name = bind { item?.nameProperty }
        val kapazitaet = bind { item?.kapazitaetProperty }
        val x = bind { item?.xProperty }
        val y = bind { item?.yProperty }
    }

}