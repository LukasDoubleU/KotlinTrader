package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.RefEntity
import tornadofx.*

class Ort_has_Ware(override val id: Int, override val id2: Int) : RefEntity() {

	val ware_id by property(id)
	val ort_id by property(id2)

	var menge by DatabaseDelegate<Double>()
	var kapazitaet by DatabaseDelegate<Int>()
	var preis by DatabaseDelegate<Double>()
	var produktion by DatabaseDelegate<Double>()
	var verbrauch by DatabaseDelegate<Double>()

}