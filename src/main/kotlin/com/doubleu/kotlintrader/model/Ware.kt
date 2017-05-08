package com.doubleu.trader.model

import com.doubleu.trader.DatabaseDelegate
import com.doubleu.trader.Entity

class Ware(override val id: Int) : Entity() {

	var name by DatabaseDelegate<String>()
	var preis by DatabaseDelegate<Double>()

}