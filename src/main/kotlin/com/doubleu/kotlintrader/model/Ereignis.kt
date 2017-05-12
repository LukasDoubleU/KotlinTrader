package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.Entity

class Ereignis(override val id: Int) : Entity() {

    var beschreibung: String? by delegate(this::beschreibung)
    val beschreibungProperty = property(this::beschreibung)

}