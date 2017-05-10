package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Ware(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    val nameProperty = property(this::name)

    var preis: Double? by delegate(this::preis)
    val preisDouble = property(this::preis)

}