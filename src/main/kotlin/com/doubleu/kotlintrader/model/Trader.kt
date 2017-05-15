package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.valueOf
import com.doubleu.kotlintrader.util.MD5
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Trader(override val id: Long) : Entity(id) {

    var name: String by delegate(this::name)
    val nameProperty = property(this::name)

    var pass: String by delegate(this::pass)
    val passProperty = property(this::pass)

    var geld: Double by delegate(this::geld)
    val geldProperty = property(this::geld)

    var master: Boolean by delegate(this::master)
    val masterProperty = property(this::master)

    fun checkPw(s: String) = MD5.encrypt(s) == pass

    override fun toString() = name.valueOf()

    class Model(traderProperty: ObjectProperty<Trader?> = SimpleObjectProperty<Trader>())
        : ItemViewModel<Trader?>(itemProperty = traderProperty) {

        val id = bind { item?.idProperty }
        val name = bind { item?.nameProperty }
        val pass = bind { item?.passProperty }
        val geld = bind { item?.geldProperty }
        val master = bind { item?.masterProperty }
    }

}