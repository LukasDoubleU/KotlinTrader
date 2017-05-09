package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.Entity

class Ort(override val id: Int) : Entity() {

	var name by DatabaseDelegate<String>()
	var kapazitaet by DatabaseDelegate<Double>()

}