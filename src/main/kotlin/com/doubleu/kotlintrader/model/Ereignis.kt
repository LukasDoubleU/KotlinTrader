package com.doubleu.kotlintrader.model

import com.doubleu.kotlintrader.database.DatabaseDelegate
import com.doubleu.kotlintrader.database.Entity

class Ereignis(override val id: Int) : Entity() {

	var beschreibung by DatabaseDelegate<String>()

}