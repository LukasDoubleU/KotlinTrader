package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.RefEntity

class Schiff_has_Ware(val ware_id: Int, val schiff_id: Int) : RefEntity() {

    // ID aliases
    override val id = ware_id
    override val id2 = schiff_id

    val ware: Ware by reference(this::ware, this::ware_id)
    val wareProperty = property(this::ware)
    val wareName = ware.name

    val schiff: Schiff by reference(this::schiff, this::schiff_id)
    val schiffProperty = property(this::schiff)

    var menge: Long? by delegate(this::menge)
    val mengeProperty = property(this::menge)

}