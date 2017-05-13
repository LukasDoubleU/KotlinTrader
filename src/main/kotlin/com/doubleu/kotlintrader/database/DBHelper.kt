package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.model.Fahrt
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import kotlin.reflect.KClass

/**
 * Helper methods for database interaction
 */
object DBHelper {

    /**
     * Returns the names of the id columns of the given [entity]
     */
    fun getIdColumnNames(entity: Entity) = getIdColumnNames(entity::class)

    /**
     * Returns the names of the id columns of the given [Entity][T]
     */
    inline fun <reified T : Entity> getIdColumnNames() = getIdColumnNames(T::class)

    /**
     * Returns the names of the id columns of the given [Entity][T]
     */
    fun <T : Entity> getIdColumnNames(entityClass: KClass<T>): Array<String> {
        return when (entityClass) {
            Fahrt::class -> arrayOf("id_von", "id_nach")
            Ort_has_Ware::class -> arrayOf("ware_id", "ort_id")
            Schiff_has_Ware::class -> arrayOf("ware_id", "schiff_id")
            else -> arrayOf("id")
        }
    }

    /**
     * Returns the name of the database table associated with the given [entity]
     */
    fun <T : Entity> getTableName(entity: T) = getTableName(entity::class)

    /**
     * Returns the name of the database table associated with the given [Entity][T]
     */
    inline fun <reified T : Entity> getTableName() = getTableName(T::class)

    /**
     * Returns the name of the database table associated with the given [Entity][T]
     */
    fun <T : Entity> getTableName(clazz: KClass<T>) = clazz.simpleName!!.toLowerCase()

    /**
     * Returns a WHERE-clause that leads to the passed [entity]
     */
    fun getWhere(entity: Entity): String {
        // Treat RefEntities differently (2 ids)
        return if (entity is RefEntity) getRefWhere(entity) else "WHERE id = ${entity.id}"
    }

    private fun getRefWhere(refEntity: RefEntity): String {
        val ids = getIdColumnNames(refEntity)
        return "WHERE ${ids[0]} = ${refEntity.id} AND ${ids[1]} = ${refEntity.id2}"
    }

}