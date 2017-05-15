package com.doubleu.kotlintrader.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.reflect.KProperty

object Settings {

    private val properties = Properties()
    private val file: File
    private val userHome = System.getProperty("user.home")
    private val kotlinTraderDir = "KotlinTrader"
    private val propertyFileName = "Settings.properties"
    private val sep = File.separator

    private val delegate = object {

        operator fun getValue(thisRef: Any, property: KProperty<*>) = properties.getProperty(property.name)

        operator fun setValue(thisRef: Any, property: KProperty<*>, value: String) = properties.setProperty(property.name, value)
    }

    init {
        file = File("$userHome$sep$kotlinTraderDir$sep$propertyFileName")
        if (!file.exists()) {
            val dir = File("$userHome$sep$kotlinTraderDir")
            if ((dir.exists() || dir.mkdirs()) && file.createNewFile()) {
                restoreDefaults()
            } else {
                throw RuntimeException("Failed to create Settings file!")
            }
        } else {
            load()
        }
    }

    fun load() {
        val input = FileInputStream(file)
        properties.load(input)
        input.close()
    }

    fun store() {
        val output = FileOutputStream(file)
        properties.store(output, null)
        output.close()
    }

    fun restoreDefaults() {
        properties.load(javaClass.getResourceAsStream("/DefaultSettings.properties"))
        store()
        FxDialogs.showInformation("Default Settings were restored.\n(${file.absolutePath})")
    }

    var host: String by delegate
    var database: String by delegate
    var dbUser: String by delegate
    var dbPassword: String by delegate
    var user: String by delegate
    var password: String by delegate

}