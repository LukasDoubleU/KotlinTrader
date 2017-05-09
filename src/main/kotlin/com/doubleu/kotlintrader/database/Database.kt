package com.doubleu.trader.database

import com.doubleu.trader.DBHelper
import com.doubleu.trader.Entity
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

    public val connectedProperty = SimpleBooleanProperty(false)

    var connection: Connection? = null

    fun connect(host: String, database: String, user: String = "root", pw: String = "") {
        connection = DriverManager.getConnection("jdbc:mysql://$host/$database?user=$user&password=$pw&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
        connectedProperty.set(true)
    }

    fun <T> setProperty(entity: Entity, property: String, value: T?) {
        val sql = "UPDATE ${DBHelper.getTableName(entity)} SET $property = $value ${DBHelper.getWhere(entity)}"
        execute(sql)
    }

    fun <T> getProperty(entity: Entity, property: String): T {
        val sql = "SELECT $property FROM ${DBHelper.getTableName(entity)} ${DBHelper.getWhere(entity)}"
        val rs = execute(sql)
        rs.next()
        return rs.getObject(property) as T // TODO Error Handling
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
        return findAll(clazz).filter { property.call(it)!!.equals(value) }.firstOrNull()
    }

    private fun execute(sql: String): ResultSet {
        return connection?.let {
            var state = connection!!.createStatement()
            var rs = state.executeQuery(sql)
            return rs
        } ?: throw RuntimeException("No database connection")
    }

}