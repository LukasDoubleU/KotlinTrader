package com.doubleu.trader.model

import com.doubleu.trader.DatabaseDelegate
import com.doubleu.trader.database.RefEntity
import tornadofx.*

class Fahrt(override val id: Int, override val id2: Int) : RefEntity() {

	val id_von by property(id)
	val id_nach by property(id2)

	val strecke by DatabaseDelegate<Int>()

}