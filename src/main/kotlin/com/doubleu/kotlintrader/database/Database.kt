package com.doubleu.trader.database

import com.doubleu.trader.DBHelper
import com.doubleu.trader.Entity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.lang.RuntimeException

/**
 * Handles interaction with the database
 */
object Database {

    var connection: Connection? = null

    fun connect(database: String, user: String, pw: String = "") {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/$database?user=$user&password=$pw&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC")
    }

    fun <T> set(entity: Entity, property: String, value: T?) {
        val sql = "UPDATE ${DBHelper.getTableName(entity)} SET $property = $value ${DBHelper.getWhere(entity)}"
        execute(sql)
    }

    fun <T> get(entity: Entity, property: String): T {
        val sql = "SELECT $property FROM ${DBHelper.getTableName(entity)} ${DBHelper.getWhere(entity)}"
        val rs = execute(sql)
        rs.next()
        return rs.getObject(property) as T // TODO Error Handling
    }

    private fun execute(sql: String): ResultSet {
        return connection?.let {
            var state = connection!!.createStatement()
            var rs = state.executeQuery(sql)
            return rs
        } ?: throw RuntimeException("No database connection")
    }

}