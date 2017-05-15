package com.doubleu.kotlintrader.data

import com.doubleu.kotlintrader.database.DBHelper
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.model.*
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.concurrent.Task
import tornadofx.*

sealed class Storage<T : Entity<T>>(
        val supplier: () -> List<T>,
        vararg loadOnChange: ObservableValue<*>) {

    companion object {
        private val tasks = mutableListOf<Storage<*>.StorageTask>().observable()
    }

    private val items: ObservableList<T> = mutableListOf<T>().observable()
    private val onLoadFinish = mutableListOf<(List<T>) -> Unit>()

    init {
        for (observer in loadOnChange) {
            observer.onChange { if (it != null) load() else clear() }
        }
        // Load / Clear when we Connect / Disconnect from the Database
        DBHelper.onConnect { load() }
        DBHelper.onDisconnect { clear() }
    }

    /**
     * Indicates whether the [items] are currently being [loaded][load]
     */
    val loading = SimpleBooleanProperty(false)

    /**
     * Indicates whether ANY [StorageTask] is running.
     * (Not just the one associated with the receiver object!)
     */
    val anyLoading = Bindings.isNotEmpty(tasks)!!

    /**
     * Retrieves the stored [items]
     */
    fun get() = items

    /**
     * Reloads the contained [items]
     */
    fun load() = Thread(StorageTask(supplier)).start()

    /**
     * Executes the given [op] when [load] finishes
     */
    fun onLoadFinish(op: (List<T>) -> Unit) {
        onLoadFinish += op
    }

    /**
     * Clears all [items]
     */
    fun clear() = items.clear()

    /**
     * Performs the given [action] on each element.
     */
    fun forEach(action: (T) -> Unit) = items.forEach(action)

    /**
     * Returns the first element matching the given [predicate], or `null` if element was not found.
     */
    fun find(predicate: (T) -> Boolean) = items.firstOrNull(predicate)

    /**
     * Returns a list containing only elements matching the given [predicate].
     */
    fun filter(predicate: (T) -> Boolean) = items.filter(predicate)

    internal inner class StorageTask(val func: () -> List<T>) : Task<Unit>() {
        private var _items = mutableListOf<T>()

        override fun call() {
            loading.set(true)
            tasks += this
            _items.addAll(func.invoke())
        }

        override fun done() {
            Platform.runLater {
                items.clear()
                items += _items
                onLoadFinish.forEach { it.invoke(items) }
                tasks.remove(this)
                loading.set(false)
            }
        }
    }
}

object Users : Storage<Trader>({ Database.findAll<Trader>() })

object Orte : Storage<Ort>({ Database.findAll<Ort>() })

object Angebote : Storage<Ort_has_Ware>({ Database.findAll<Ort_has_Ware>() }) {

    fun sorted() = get().sortedBy { it.ortName }.sortedBy { it.wareName }.sortedByDescending { it.preis }.observable()

}

object OrtWaren : Storage<Ort_has_Ware>({ Database.findAllBy(Ort_has_Ware::ort_id, Data.ort?.id) }, Data.Ort)

object SchiffWaren : Storage<Schiff_has_Ware>({ Database.findAllBy(Schiff_has_Ware::schiff_id, Data.schiff?.id) }, Data.Schiff)

object Schiffe : Storage<Schiff>({ Database.findAll<Schiff>() })