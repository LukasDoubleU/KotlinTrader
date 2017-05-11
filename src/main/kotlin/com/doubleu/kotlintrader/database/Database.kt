package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.controller.DatabaseController
import com.doubleu.kotlintrader.extensions.isBoolean
import com.doubleu.kotlintrader.util.FxDialogs
import com.doubleu.kotlintrader.util.Session
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
        val url = "jdbc:mysql://$host/$database?user=$user&password=$pw" +
                "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
        connection = DriverManager.getConnection(url)
        connectedProperty.set(true)
        DatabaseController.connected()
    }

    fun disconnect() {
        connection?.let {
            it.close()
            connectedProperty.set(false)
            Session.logout()
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
        return if (property.isBoolean() && (value is Number || value is String)) {
            // Type insurance granted by checking property.returnType
            // (V is Boolean in this case)
            (value.toString() != "0") as V
        } else value as V
    }

    inline fun <reified T : Entity> findAll() = select<T>(DBHelper.getIdColumnNames<T>())

    inline fun <reified T : Entity> select(columns: Array<String>): List<T> {
        val list = mutableListOf<T>()
        val sql = "SELECT ${columns.joinToString(", ")} FROM ${DBHelper.getTableName<T>()}"
        val rs = query(sql)
        while (rs.next()) {
            val params = mutableListOf<Int>()
            for (s in columns) params += rs.getInt(s)
            list += T::class.primaryConstructor!!.call(*params.toTypedArray())
        }
        return list.toList()
    }

    inline fun <reified T : Entity, V> findAllBy(property: KMutableProperty1<T, V?>, value: V?): List<T> {
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