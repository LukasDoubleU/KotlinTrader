package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Ware(override val id: Long) : Entity(id) {

    var name: String by delegate(this::name)
    val nameProperty = property(this::name)

    var preis: Double by delegate(this::preis)
    val preisDouble = property(this::preis)

    class Model(property: ObjectProperty<Ware?> = SimpleObjectProperty<Ware>())
        : ItemViewModel<Ware?>(itemProperty = property) {

        val id = bind { item?.idProperty }
        val name = bind { item?.nameProperty }
        val preisDouble = bind { item?.preisDouble }
    }

}