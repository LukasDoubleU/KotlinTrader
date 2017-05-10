package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.extensions.isBoolean
import com.doubleu.kotlintrader.extensions.toInt
import com.doubleu.kotlintrader.extensions.toSQLString
import com.doubleu.kotlintrader.extensions.valueOf
import com.doubleu.kotlintrader.model.*
import java.lang.RuntimeException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Helper methods for database interaction
 */
object DBHelper {

    fun getIdColumnName(refEntity: RefEntity): Array<String> {
        return when (refEntity) {
            is Fahrt -> arrayOf("id_von", "id_nach")
            is Ort_has_Ware -> arrayOf("ware_id", "ort_id")
            is Schiff_has_Ware -> arrayOf("ware_id", "schiff_id")
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

    fun <V> parseValue(property: KProperty<V>, value: V?): String {
        return if (property.isBoolean()) {
            when (property) {
                Schiff::blocked -> (value as Boolean).toInt().toString()
                Trader::master -> (value as Boolean).toSQLString()
                else -> value.valueOf()
            }
        } else value.valueOf()
    }

}