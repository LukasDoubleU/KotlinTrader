package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Ort(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    fun nameProperty() = property(this::name)

    var kapazitaet: Double? by delegate(this::kapazitaet)
    fun kapazitaetProperty() = property(this::kapazitaet)

    /**
     * Overriden for meaningful representation inside the combobox of the TradeView
     * TODO consider using a converter in the box later
     */
    override fun toString() = name ?: "null"

}