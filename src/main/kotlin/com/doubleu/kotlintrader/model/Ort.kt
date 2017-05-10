package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Ort(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    val nameProperty = property(this::name)

    var kapazitaet: Double? by delegate(this::kapazitaet)
    val kapazitaetProperty = property(this::kapazitaet)

}