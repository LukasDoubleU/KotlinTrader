package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.math.BigDecimal

class Ort_has_Ware(val ware_id: Long, val ort_id: Long) : RefEntity<Ort_has_Ware>(ware_id, ort_id) {

    val ware: Ware by reference(this::ware, this::ware_id)
    val wareProperty = property(this::ware)
    val wareName = ware.name
    val wareNameProperty = ware.nameProperty

    val ort: Ort by reference(this::ort, this::ort_id)
    val ortProperty = property(this::ort)
    val ortName = ort.name

    var menge: BigDecimal by delegate(this::menge)
    val mengeProperty = property(this::menge)

    var kapazitaet: Int by delegate(this::kapazitaet)
    val kapazitaetProperty = property(this::kapazitaet)

    var preis: BigDecimal by delegate(this::preis)
    val preisProperty = property(this::preis)

    var produktion: Double by delegate(this::produktion)
    val produktionProperty = property(this::produktion)

    var verbrauch: Double by delegate(this::verbrauch)
    val verbrauchProperty = property(this::verbrauch)

    override fun model(property: ObjectProperty<Ort_has_Ware?>) = Model(property)

    class Model(property: ObjectProperty<Ort_has_Ware?> = SimpleObjectProperty<Ort_has_Ware>())
        : ItemViewModel<Ort_has_Ware?>(itemProperty = property) {

        val ware_id = bind { item?.idProperty }
        val ort_id = bind { item?.id2Property }
        val ware = bind { item?.wareProperty }
        val ort = bind { item?.ortProperty }
        val menge = bind { item?.mengeProperty }
        val kapazitaet = bind { item?.kapazitaetProperty }
        val preis = bind { item?.preisProperty }
        val produktion = bind { item?.produktionProperty }
        val verbrauch = bind { item?.verbrauchProperty }
    }

}