package com.doubleu.trader.model

import com.doubleu.trader.DatabaseDelegate
import com.doubleu.trader.Entity

class Ort(override val id: Int) : Entity() {

	var name by DatabaseDelegate<String>()
	var kapazitaet by DatabaseDelegate<Double>()

}