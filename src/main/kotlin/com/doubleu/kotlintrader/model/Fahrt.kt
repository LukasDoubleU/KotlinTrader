package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity

class Fahrt(override val id: Int, override val id2: Int) : RefEntity() {

//	TODO implement references
//	val id_von by property(id)
//	val id_nach by property(id2)

    var strecke: Int? by delegate(this::strecke)
    val streckeProperty = property(this::strecke)

}