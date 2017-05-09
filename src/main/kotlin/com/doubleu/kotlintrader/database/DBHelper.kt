package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.model.Fahrt
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import java.lang.RuntimeException
import kotlin.reflect.KClass

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

    fun <T : Entity> getTableName(entity: T) = getTableName(entity::class)

    inline fun <reified T : Entity> getTableName() = getTableName(T::class)

    fun <T : Entity> getTableName(clazz: KClass<T>) = clazz.simpleName!!.toLowerCase()

    fun getWhere(entity: Entity): String {
        // Treat RefEntities differently (2 ids)
        return if (entity is RefEntity) getRefWhere(entity) else "WHERE id = ${entity.id}"
    }

    private fun getRefWhere(refEntity: RefEntity): String {
        val ids = getIdColumnName(refEntity)
        return "WHERE ${ids[0]} = ${refEntity.id} AND ${ids[1]} = ${refEntity.id2}"
    }

}