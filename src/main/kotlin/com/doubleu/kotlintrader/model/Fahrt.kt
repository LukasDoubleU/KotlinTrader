package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity

class Fahrt(val von_id: Long, val nach_id: Long) : RefEntity(von_id, nach_id) {

    val von: Ort by reference(this::von, this::von_id)
    val vonProperty = property(this::von)

    val nach: Ort by reference(this::nach, this::nach_id)
    val nachProperty = property(this::nach)

    var strecke: Int? by delegate(this::strecke)
    val streckeProperty = property(this::strecke)

}