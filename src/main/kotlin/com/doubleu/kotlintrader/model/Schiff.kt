package com.doubleu.trader.model

import com.doubleu.trader.Entity
import com.doubleu.trader.DatabaseDelegate

class Schiff(override val id: Int) : Entity() {

	var ort_id by DatabaseDelegate<Int>()
	var trader_id by DatabaseDelegate<Int>()
	var name by DatabaseDelegate<String>()
	var tonnage by DatabaseDelegate<Int>()
	var wert by DatabaseDelegate<Double>()
	var fahrtkosten by DatabaseDelegate<Float>()
	var blocked by DatabaseDelegate<Boolean>()

}