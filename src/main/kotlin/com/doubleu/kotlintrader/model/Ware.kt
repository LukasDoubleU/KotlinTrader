package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.data.OrtWaren
import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Ware(override val id: Long) : Entity<Ware>(id) {

    var name: String by delegate(this::name)
    val nameProperty = property(this::name)

    var basispreis: Double by delegate(this::basispreis)
    val basispreisDouble = property(this::basispreis)

    /**
     * Gibt den Preis dieser Ware im aktuellen Ort zurueck
     */
    fun preisVorOrt() = OrtWaren.find { it.ware_id == id }?.preis?.toDouble()

    override fun model(property: ObjectProperty<Ware?>) = Model(property)

    class Model(property: ObjectProperty<Ware?> = SimpleObjectProperty<Ware>())
        : ItemViewModel<Ware?>(itemProperty = property) {

        val id = bind { item?.idProperty }
        val name = bind { item?.nameProperty }
        val basispreisDouble = bind { item?.basispreisDouble }
    }

}