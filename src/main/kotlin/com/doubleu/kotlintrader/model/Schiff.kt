package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Schiff(override val id: Int) : Entity() {

    var ort_id: Int? by delegate(this::ort_id)
    val ort_idProperty = property(this::ort_id)

    var trader_id: Int? by delegate(this::trader_id)
    val trader_idProperty = property(this::trader_id)

    var name: String? by delegate(this::name)
    val nameProperty = property(this::name)

    var tonnage: Int? by delegate(this::tonnage)
    val tonnageProperty = property(this::tonnage)

    var wert: Double? by delegate(this::wert)
    val wertProperty = property(this::wert)

    var fahrtkosten: Float? by delegate(this::fahrtkosten)
    val fahrtkostenProperty = property(this::fahrtkosten)

    var blocked: Boolean? by delegate(this::blocked)
    val blockedProperty = property(this::blocked)

}