package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.valueOf
import com.doubleu.kotlintrader.util.MD5
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class Trader(override val id: Long) : Entity<Trader>(id) {

    var name: String by delegate(this::name, "-1")
    val nameProperty = property(this::name)

    var pass: String by delegate(this::pass, "-1")
    val passProperty = property(this::pass)

    var geld: Double by delegate(this::geld, 10_000.0)
    val geldProperty = property(this::geld)

    var master: Boolean by delegate(this::master, false)
    val masterProperty = property(this::master)

    fun checkPw(s: String) = MD5.encrypt(s) == pass

    override fun toString() = name.valueOf()

    override fun model(property: ObjectProperty<Trader?>) = Model(property)

    class Model(property: ObjectProperty<Trader?> = SimpleObjectProperty<Trader>())
        : ItemViewModel<Trader?>(itemProperty = property) {

        val id = bind { item?.idProperty }
        val name = bind { item?.nameProperty }
        val pass = bind { item?.passProperty }
        val geld = bind { item?.geldProperty }
        val geldAsString = DoubleBinding.doubleExpression(geld).asString()!!
        val master = bind { item?.masterProperty }
    }

}