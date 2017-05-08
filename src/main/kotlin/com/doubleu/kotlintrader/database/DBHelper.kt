package com.doubleu.trader

import com.doubleu.trader.database.RefEntity
import kotlin.reflect.KClass
import com.doubleu.trader.model.Fahrt
import java.lang.RuntimeException
import com.doubleu.trader.model.Ort_has_Ware
import com.doubleu.trader.model.Schiff_has_Ware

/**
 * Helper methods for database interaction
 */
object DBHelper {

	fun getIdColumnName(refEntity: RefEntity): Array<String> {
		return when {
			refEntity is Fahrt -> arrayOf("id_von", "id_nach")
			refEntity is Ort_has_Ware -> arrayOf("ware_id", "ort_id")
			refEntity is Schiff_has_Ware -> arrayOf("ware_id", "schiff_id")
			else -> throw RuntimeException("Unknown entity $refEntity")
		}
	}

	fun getTableName(entity: Entity): String {
		return entity.javaClass.simpleName.toLowerCase();
	}

	fun getWhere(entity: Entity): String {
		// Treat RefEntitys differently (2 ids)
		return if (entity is RefEntity) getRefWhere(entity) else "WHERE id = ${entity.id}"
	}

	private fun getRefWhere(refEntity: RefEntity): String {
		val ids = getIdColumnName(refEntity)
		return "WHERE ${ids[0]} = ${refEntity.id} AND ${ids[1]} = ${refEntity.id2}"
	}

}