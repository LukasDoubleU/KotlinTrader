package com.doubleu.trader.model

import com.doubleu.trader.Entity
import com.doubleu.trader.DatabaseDelegate

class Ort(override val id: Int) : Entity() {

	var name by DatabaseDelegate<String>()
	var kapazitaet by DatabaseDelegate<Double>()

}