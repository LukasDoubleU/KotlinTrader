package com.doubleu.trader.model

import com.doubleu.trader.Entity
import com.doubleu.trader.DatabaseDelegate

class Ereignis(override val id: Int) : Entity() {

	var beschreibung by DatabaseDelegate<String>()

}