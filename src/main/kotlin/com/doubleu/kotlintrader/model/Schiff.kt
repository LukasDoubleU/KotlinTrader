package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.random
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Schiff(override val id: Long) : Entity<Schiff>(id) {

    var ort_id: Long by delegate(this::ort_id, -1)
    val ort_idProperty = property(this::ort_id)

    var ort: Ort by mutableReference(this::ort, this::ort_id)
    val ortProperty = property(this::ort)

    var trader_id: Long by delegate(this::trader_id, -1)
    val trader_idProperty = property(this::trader_id)

    var trader: Trader by mutableReference(this::trader, this::trader_id)
    val traderProperty = property(this::trader)

    var name: String by delegate(this::name, "-1")
    val nameProperty = property(this::name)

    var tonnage: Int by delegate(this::tonnage, 1000)
    val tonnageProperty = property(this::tonnage)

    var wert: Double by delegate(this::wert, random(100, 10000).toDouble())
    val wertProperty = property(this::wert)

    var fahrkosten: Float by delegate(this::fahrkosten, random(1, 10).toFloat())
    val fahrkostenProperty = property(this::fahrkosten)

    var blocked: Boolean by delegate(this::blocked, false)
    val blockedProperty = property(this::blocked)

    override fun model(property: ObjectProperty<Schiff?>) = Model(property)

    class Model(property: ObjectProperty<Schiff?> = SimpleObjectProperty<Schiff>())
        : ItemViewModel<Schiff?>(itemProperty = property) {

        val id = bind { item?.idProperty }
        val ort_id = bind { item?.ort_idProperty }
        val ort = bind { item?.ortProperty }
        val trader_id = bind { item?.trader_idProperty }
        val trader = bind { item?.traderProperty }
        val name = bind { item?.nameProperty }
        val tonnage = bind { item?.tonnageProperty }
        val wert = bind { item?.wertProperty }
        val fahrkosten = bind { item?.fahrkostenProperty }
        val blocked = bind { item?.blockedProperty }
    }

}
