package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.RefEntity
import tornadofx.*

class Fahrt(override val id: Int, override val id2: Int) : RefEntity() {

	val id_von by property(id)
	val id_nach by property(id2)

	val strecke by DatabaseDelegate<Int>()

}