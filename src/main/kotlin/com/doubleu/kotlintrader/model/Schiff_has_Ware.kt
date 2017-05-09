package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.RefEntity
import tornadofx.*

class Schiff_has_Ware(override val id: Int, override val id2: Int) : RefEntity() {

	var ware_id by property(id)
	var schiff_id by property(id2)

	var menge by DatabaseDelegate<Double>()

}