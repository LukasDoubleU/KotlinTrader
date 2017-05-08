package com.doubleu.trader.database

import com.doubleu.trader.Entity

/**
 * Database Entity with 2 ID Columns
 */
abstract class RefEntity : Entity() {

	init {
		registerId(id2)
	}

	abstract val id2: Int

}