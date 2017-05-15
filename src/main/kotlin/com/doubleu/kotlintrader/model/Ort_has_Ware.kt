package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity
import java.math.BigDecimal

class Ort_has_Ware(val ware_id: Long, val ort_id: Long) : RefEntity(ware_id, ort_id) {

    val ware: Ware by reference(this::ware, this::ware_id)
    val wareProperty = property(this::ware)
    val wareName = ware.name

    val ort: Ort by reference(this::ort, this::ort_id)
    val ortProperty = property(this::ort)
    val ortName = ort.name

    var menge: BigDecimal? by delegate(this::menge)
    val mengeProperty = property(this::menge)

    var kapazitaet: Int? by delegate(this::kapazitaet)
    val kapazitaetProperty = property(this::kapazitaet)

    var preis: BigDecimal? by delegate(this::preis)
    val preisProperty = property(this::preis)

    var produktion: Double? by delegate(this::produktion)
    val produktionProperty = property(this::produktion)

    var verbrauch: Double? by delegate(this::verbrauch)
    val verbrauchProperty = property(this::verbrauch)

}