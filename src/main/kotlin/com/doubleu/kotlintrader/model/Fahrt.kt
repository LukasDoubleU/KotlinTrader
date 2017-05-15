package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

@Deprecated("Not usable as of now, requires inserts")
class Fahrt(von_id: Long, nach_id: Long) : RefEntity<Fahrt>(von_id, nach_id) {

    val von: Ort by reference(this::von, this::id)
    val vonProperty = property(this::von)

    val nach: Ort by reference(this::nach, this::id2)
    val nachProperty = property(this::nach)

    var strecke: Int by delegate(this::strecke)
    val streckeProperty = property(this::strecke)

    override fun model(property: ObjectProperty<Fahrt?>) = Model(property)

    class Model(property: ObjectProperty<Fahrt?> = SimpleObjectProperty<Fahrt>())
        : ItemViewModel<Fahrt?>(itemProperty = property) {

        val von_id = bind { item?.idProperty }
        val nach_id = bind { item?.id2Property }
        val von = bind { item?.vonProperty }
        val nach = bind { item?.nachProperty }
        val strecke = bind { item?.streckeProperty }
    }

}