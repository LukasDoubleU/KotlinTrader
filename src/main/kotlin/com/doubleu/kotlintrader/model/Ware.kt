package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.Entity

class Ware(override val id: Int) : Entity() {

	var name by DatabaseDelegate<String>()
	var preis by DatabaseDelegate<Double>()

}