package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.extensions.isBoolean
import com.doubleu.kotlintrader.util.FxDialogs
import javafx.beans.property.SimpleBooleanProperty
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.reflect.KMutableProperty1
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

    fun disconnect() {
        connection?.let {
            it.close()
            connectedProperty.set(false)
        }
    }

    fun <V> setProperty(entity: Entity, property: KProperty<V>, value: V?) {
        // TODO need this? ${DBHelper.parseValue(property, value)}
        val sql = "UPDATE ${DBHelper.getTableName(entity)} SET ${property.name} = $value ${DBHelper.getWhere(entity)}"
        execute(sql)
    }

    @Suppress("UNCHECKED_CAST")
    fun <V> getProperty(entity: Entity, property: KProperty<V>): V {
        val sql = "SELECT ${property.name} FROM ${DBHelper.getTableName(entity)} ${DBHelper.getWhere(entity)}"
        val rs = query(sql)
        rs.next()
        val value = rs.getObject(property.name)
        // Treat Boolean differently: They may be displayed as numeric or string
        if (property.isBoolean() && (value is Number || value is String)) {
            // Type insurance granted by checking property.returnType
            // (V is Boolean in this case)
            return (value.toString() != "0") as V
        }
        return value as V
    }

    inline fun <reified T : Entity> findAll(): List<T> {
        if (T::class is RefEntity) throw UnsupportedOperationException("Can't yet find RefEntities")
        val list = mutableListOf<T>()
        val sql = "SELECT id FROM ${DBHelper.getTableName<T>()}"
        val rs = query(sql)
        while (rs.next()) {
            list += T::class.primaryConstructor!!.call(rs.getInt("id"))
        }
        return list.toList()
    }

    inline fun <reified T : Entity, V> findAllBy(property: KMutableProperty1<T, V?>, value: V?): List<T> {
        // TODO implement properly ;)
        return findAll<T>().filter { property.call(it) == value }
    }

    inline fun <reified T : Entity, V> findFirstBy(property: KMutableProperty1<T, V?>, value: V?): T? {
        val retval = findAllBy(property, value).firstOrNull()
        if (retval == null) FxDialogs.showError("${T::class.simpleName} with ${property.name} '$value' was not found")
        return retval
    }

    fun query(sql: String): ResultSet {
        return connection?.let {
            val statement = connection!!.createStatement()
            return statement.executeQuery(sql)
        } ?: throw RuntimeException("No database connection")
    }

    fun execute(sql: String): Boolean {
        return connection?.let {
            val statement = connection!!.createStatement()
            return statement.execute(sql)
        } ?: throw RuntimeException("No database connection")
    }

}