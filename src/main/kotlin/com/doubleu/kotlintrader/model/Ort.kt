package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.valueOf

class Ort(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    val nameProperty = property(this::name)

    var kapazitaet: Double? by delegate(this::kapazitaet)
    val kapazitaetProperty = property(this::kapazitaet)

    /**
     * Overridden for meaningful representation inside the combobox of the TradeView
     */
    override fun toString() = name.valueOf()

}