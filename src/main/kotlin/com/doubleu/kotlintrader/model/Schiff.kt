package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.Entity

class Schiff(override val id: Int) : Entity() {

	var ort_id by DatabaseDelegate<Int>()
	var trader_id by DatabaseDelegate<Int>()
	var name by DatabaseDelegate<String>()
	var tonnage by DatabaseDelegate<Int>()
	var wert by DatabaseDelegate<Double>()
	var fahrtkosten by DatabaseDelegate<Float>()
	var blocked by DatabaseDelegate<Boolean>()

}