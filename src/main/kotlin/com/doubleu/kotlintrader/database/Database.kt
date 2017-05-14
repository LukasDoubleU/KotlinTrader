package com.doubleu.kotlintrader.database

import com.doubleu.kotlintrader.extensions.isBoolean
import com.doubleu.kotlintrader.util.FxDialogs
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.primaryConstructor

/**
 * Handles interaction with the database
 */
object Database {

    val connectionProperty = SimpleObjectProperty<Connection?>()
    var connection by connectionProperty

    val connected = Bindings.isNotNull(connectionProperty)!!

    /**
     * Attempts to asyncly create a database connection
     */
    fun connect(host: String, database: String, user: String, pw: String) {
        val url = "jdbc:mysql://$host/$database?user=$user&password=$pw" +
                "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
        DriverManager.getConnection(url)?.let {
            // Makes sure we're in FXThread
            Platform.runLater {
                connection = it
            }
        }
    }

    fun disconnect() {
        // Attempt to close the connection, if there's any
        connection?.close()
        // Makes sure we're in FXThread
        Platform.runLater {
            connection = null
        }
    }

    /**
     * Retrieves the [property] of the [entity] from the database via SQL-SELECT
     */
    @Suppress("UNCHECKED_CAST")
    fun <V> getProperty(entity: Entity, property: KProperty<V>): V {
        val sql = "SELECT ${property.name} FROM ${DBHelper.getTableName(entity)} ${DBHelper.getWhere(entity)}"
        val rs = query(sql)
        synchronized(rs) {
            rs.next()
            val value = rs.getObject(property.name)
            rs.close()
            // Treat Boolean differently: They may be displayed as numeric or string
            return if (property.isBoolean() && (value is Number || value is String)) {
                // Type insurance granted by checking property.returnType
                // (V is Boolean in this case)
                (value.toString() != "0") as V
            } else value as V
        }
    }

    /**
     * Sets the [property] of the given [entity] in the database via SQL-Update
     */
    fun <V> setProperty(entity: Entity, property: KProperty<V>, value: V?) {
        val sql = "UPDATE ${DBHelper.getTableName(entity)} SET ${property.name} = $value ${DBHelper.getWhere(entity)}"
        execute(sql)
    }

    /**
     * Returns all database entries of the given [Entity][T]
     */
    inline fun <reified T : Entity> findAll() = selectEntities<T>(DBHelper.getIdColumnNames<T>())

    /**
     * Returns all [Entities][T].
     * [columns]: The ID Columns of the Entity
     */
    inline fun <reified T : Entity> selectEntities(columns: Array<String>): List<T> {
        val list = mutableListOf<T>()
        val sql = "SELECT ${columns.joinToString(", ")} FROM ${DBHelper.getTableName<T>()}"
        val rs = query(sql)
        synchronized(rs) {
            while (rs.next()) {
                val params = mutableListOf<Int>()
                for (s in columns) params += rs.getInt(s)
                list += T::class.primaryConstructor!!.call(*params.toTypedArray())
            }
            rs.close()
        }
        return list.toList()
    }

    /**
     * Returns all database entries that match the given [property] [value]
     */
    inline fun <reified T : Entity, V> findAllBy(property: KProperty1<T, V?>, value: V?): List<T> {
        return findAll<T>().filter { property.get(it) == value }
    }

    /**
     * Returns the first database entry that matches the given [property] [value] or null
     */
    inline fun <reified T : Entity, V> findFirstBy(property: KProperty1<T, V?>, value: V?): T? {
        val retval = findAllBy(property, value).firstOrNull()
        if (retval == null) FxDialogs.showError("${T::class.simpleName} with ${property.name} '$value' was not found")
        return retval
    }

    /**
     * Executes the given [sql] as a query on the database
     */
    fun query(sql: String): ResultSet {
        return connection?.let {
            val statement = it.createStatement()
            statement.closeOnCompletion()
            return statement.executeQuery(sql)
        } ?: throw RuntimeException("No database connection")
    }

    /**
     * Executes the given [sql] as a statement on the database
     */
    fun execute(sql: String): Boolean {
        return connection?.let {
            val statement = it.createStatement()
            statement.closeOnCompletion()
            return statement.execute(sql)
        } ?: throw RuntimeException("No database connection")
    }
}