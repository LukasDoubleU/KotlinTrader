package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.util.MD5

class Trader(override val id: Int) : Entity() {

    var name: String? by delegate(this::name)
    fun nameProperty() = property(this::name)

    var pass: String? by delegate(this::pass)
    fun passProperty() = property(this::pass)

    var geld: Double? by delegate(this::geld)
    fun geldProperty() = property(this::geld)

    var master: Boolean? by delegate(this::master)
    fun masterProperty() = property(this::master)

    fun checkPw(s: String) = MD5.encrypt(s) == pass

}