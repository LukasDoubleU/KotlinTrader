package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity

class Schiff_has_Ware(override val id: Int, override val id2: Int) : RefEntity() {

//	TODO implement references
//	var ware
//	var schiff

    var menge: Double? by delegate(this::menge)
    val mengeProperty = property(this::menge)

}