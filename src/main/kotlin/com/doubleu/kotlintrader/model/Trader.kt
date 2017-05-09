package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.util.MD5
import tornadofx.*


class Trader(override val id: Int) : Entity() {

    var name by DatabaseDelegate<String>()
    val nameProperty = name.toProperty()
    var pass by DatabaseDelegate<String>()
    val passProperty = pass.toProperty()
    var geld by DatabaseDelegate<Double>()
    val geldProperty = geld.toProperty()
    var master by DatabaseDelegate<Boolean>()
    val masterProperty = master.toProperty()

    fun checkPw(s: String) = MD5.encrypt(s) == pass

}