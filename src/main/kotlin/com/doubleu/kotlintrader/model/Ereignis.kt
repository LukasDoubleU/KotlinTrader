package com.doubleu.trader.model

import com.doubleu.trader.DatabaseDelegate
import com.doubleu.trader.Entity

class Ereignis(override val id: Int) : Entity() {

	var beschreibung by DatabaseDelegate<String>()

}