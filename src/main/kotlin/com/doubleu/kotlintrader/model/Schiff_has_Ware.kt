package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Schiff_has_Ware(val ware_id: Long, val schiff_id: Long) : RefEntity<Schiff_has_Ware>(ware_id, schiff_id) {

    val ware: Ware by reference(this::ware, this::ware_id)
    val wareProperty = property(this::ware)
    val wareName = ware.name
    val wareNameProperty = ware.nameProperty

    val schiff: Schiff by reference(this::schiff, this::schiff_id)
    val schiffProperty = property(this::schiff)

    var menge: Long by delegate(this::menge, 0)
    val mengeProperty = property(this::menge)

    /**
     * Gibt den Preis dieser Ware im aktuellen Ort zurueck
     */
    fun preisVorOrt() = ware.preisVorOrt()

    override fun model(property: ObjectProperty<Schiff_has_Ware?>) = Model(property)

    class Model(property: ObjectProperty<Schiff_has_Ware?> = SimpleObjectProperty<Schiff_has_Ware>())
        : ItemViewModel<Schiff_has_Ware?>(itemProperty = property) {

        val ware = bind { item?.wareProperty }
        val schiff = bind { item?.schiffProperty }
        val menge = bind { item?.mengeProperty }
    }

}