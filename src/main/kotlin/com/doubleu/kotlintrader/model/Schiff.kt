package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Schiff(override val id: Int) : Entity() {

    var ort_id: Int? by delegate(this::ort_id)
    fun ort_idProperty() = property(this::ort_id)

    var ort: Ort? by mutableReference(this::ort, this::ort_id)
    fun ortProperty() = property(this::ort)

    var trader_id: Int? by delegate(this::trader_id)
    fun trader_idProperty() = property(this::trader_id)

    var trader: Trader? by mutableReference(this::trader, this::trader_id)
    fun traderProperty() = property(this::trader)

    var name: String? by delegate(this::name)
    fun nameProperty() = property(this::name)

    var tonnage: Int? by delegate(this::tonnage)
    fun tonnageProperty() = property(this::tonnage)

    var wert: Double? by delegate(this::wert)
    fun wertProperty() = property(this::wert)

    var fahrtkosten: Float? by delegate(this::fahrtkosten)
    fun fahrtkostenProperty() = property(this::fahrtkosten)

    var blocked: Boolean? by delegate(this::blocked)
    fun blockedProperty() = property(this::blocked)

}