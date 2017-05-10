package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity

class Ort_has_Ware(override val id: Int, override val id2: Int) : RefEntity() {

// TODO implement references
//	val ware_id by property(id)
//	val ort_id by property(id2)

    var menge: Double? by delegate(this::menge)
    val mengeProperty = property(this::menge)

    var kapazitaet: Int? by delegate(this::kapazitaet)
    val kapazitaetProperty = property(this::kapazitaet)

    var preis: Double? by delegate(this::preis)
    val preisProperty = property(this::preis)

    var produktion: Double? by delegate(this::produktion)
    val produktionProperty = property(this::produktion)

    var verbrauch: Double? by delegate(this::verbrauch)
    val verbrauchProperty = property(this::verbrauch)

}