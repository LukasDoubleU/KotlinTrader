package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

@Deprecated("No use-case for dis")
class Ereignis(override val id: Long) : Entity<Ereignis>(id) {

    var beschreibung: String by delegate(this::beschreibung, "")
    val beschreibungProperty = property(this::beschreibung)

    override fun model(property: ObjectProperty<Ereignis?>) = Model(property)

    class Model(property: ObjectProperty<Ereignis?> = SimpleObjectProperty<Ereignis>())
        : ItemViewModel<Ereignis?>(itemProperty = property) {

        val beschreibung = bind { item?.beschreibungProperty }
    }
}