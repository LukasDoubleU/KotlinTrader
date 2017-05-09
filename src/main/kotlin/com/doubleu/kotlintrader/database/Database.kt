package com.doubleu.kotlintrader.database

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.primaryConstructor

/**
 * Handles interaction with the database
 */
object Database {

    val connectedProperty = SimpleBooleanProperty(false)

    var connection: Connection? = null

    fun connect(host: String, database: String, user: String = "root", pw: String = "") {
        connection = DriverManager.getConnection("jdbc:mysql://$host/$database?user=$user&password=$pw&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
        connectedProperty.set(true)
    }

    fun <T> setProperty(entity: Entity, property: KProperty<T>, value: T?) {
        val sql = "UPDATE ${DBHelper.getTableName(entity)} SET $property = $value ${DBHelper.getWhere(entity)}"
        execute(sql)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getProperty(entity: Entity, property: KProperty<T>): T? {
        val sql = "SELECT ${property.name} FROM ${DBHelper.getTableName(entity)} ${DBHelper.getWhere(entity)}"
        val rs = execute(sql)
        val value = rs.getObject(property.name)
        // Treat Boolean differently: They may be displayed as numeric in DB
//        if(property.returnType == Boolean::class
//                && value is Number){
//            // Type insurance granted by checking property.returnType
//            // (T is Boolean in this case)
//            return (value == 0) as T
//        }
        rs.next()
        return value as T
    }

    fun <T : Entity> findAll(clazz: KClass<T>): ObservableList<T> {
        if (clazz is RefEntity) throw UnsupportedOperationException("Can't yet find RefEntities")
        val list = mutableListOf<T>()
        val sql = "SELECT id FROM ${DBHelper.getTableName(clazz)}"
        val rs = execute(sql)
        while (rs.next()) {
            list += clazz.primaryConstructor!!.call(rs.getInt("id"))
        }
        return FXCollections.observableArrayList(list)
    }

    fun <T : Entity, V> findBy(clazz: KClass<T>, property: KProperty<V>, value: V?): T? {
        // TODO implement properly ;)
        return findAll(clazz).filter { property.call(it)!! == value }.firstOrNull()
    }

    private fun execute(sql: String): ResultSet {
        return connection?.let {
            val statement = connection!!.createStatement()
            return statement.executeQuery(sql)
        } ?: throw RuntimeException("No database connection")
    }

}