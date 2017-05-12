package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Ware(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    fun nameProperty() = property(this::name)

    var preis: Double? by delegate(this::preis)
    fun preisDouble() = property(this::preis)

}