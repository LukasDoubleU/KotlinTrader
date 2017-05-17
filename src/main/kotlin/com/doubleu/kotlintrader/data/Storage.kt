package com.doubleu.kotlintrader.data

import com.doubleu.kotlintrader.database.DBHelper
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.model.*
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.concurrent.Task
import tornadofx.*

/**
 * Provides Storage for various [Entity]s
 */
sealed class Storage<T : Entity<T>>(val supplier: () -> List<T>) {

    companion object {
        private val tasks = mutableListOf<Storage<*>.StorageTask>().observable()

        /**
         * Indicates whether any Storage is currently being loaded.
         * (Implicates an open database operation)
         */
        val anyLoading = Bindings.isNotEmpty(tasks)!!
    }

    private val items: ObservableList<T> = mutableListOf<T>().observable()
    private val onLoadFinish = mutableListOf<(Storage<T>) -> Unit>()

    /**
     * Indicates whether the [items] are currently being [loaded][load]
     */
    val loading = SimpleBooleanProperty(false)

    init {
        // Load / Clear when we Connect / Disconnect from the Database
        DBHelper.onConnect { load() }
        DBHelper.onDisconnect { clear() }
    }

    /**
     * Retrieves the stored [items]
     */
    fun get() = items

    /**
     * Reloads the contained [items]
     */
    fun load() = Thread(StorageTask(supplier)).start()

    /**
     * Whether loaded Entities should be retrieved eagerly after loading.
     * Recommended when Entities are being loaded directly from the database
     */
    open protected val retrieveOnLoad = true

    /**
     * Executes the given [op] when [load] finishes
     */
    fun onLoadFinish(op: (Storage<T>) -> Unit) {
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
     * Return all elements matching the given [predicate]
     */
    fun findAll(predicate: (T) -> Boolean) = items.filter(predicate)

    /**
     * Returns a list containing only elements matching the given [predicate].
     */
    fun filter(predicate: (T) -> Boolean) = items.filter(predicate)

    /**
     * Helps running [storage loading][load] asyncly
     */
    private inner class StorageTask(val func: () -> List<T>) : Task<Unit>() {
        private var _items = mutableListOf<T>()

        override fun call() {
            loading.set(true)
            tasks.add(this)
            items.clear()
            _items.addAll(func.invoke())
            // Retrieve the items eagerly
            if (retrieveOnLoad)
                _items.forEach { it.retrieve(false) }
        }

        override fun done() {
            Platform.runLater {
                try {
                    items += _items
                    onLoadFinish.forEach { it.invoke(this@Storage) }
                } finally {
                    loading.set(false)
                    tasks.remove(this)
                }
            }
        }
    }
}

/**
 * Holding all [Trader] objects that were loaded from the database
 */
object Users : Storage<Trader>({ Database.findAll<Trader>() })

/**
 * Holding all [Ort] objects that were loaded from the database
 */
object Orte : Storage<Ort>({ Database.findAll<Ort>() })

/**
 * Holding all [Ort_has_Ware] objects that were loaded from the database
 */
object Angebote : Storage<Ort_has_Ware>({ Database.findAll<Ort_has_Ware>() }) {

    init {
        onLoadFinish { OrtWaren.load() }
    }

    fun sorted() = get().sortedBy { it.ortName }.sortedBy { it.wareName }.sortedByDescending { it.preis }.observable()

}

/**
 * Holding [Ort_has_Ware] objects that were loaded from the database.
 * They always depend on the [current ort][Data.ort]
 */
object OrtWaren : Storage<Ort_has_Ware>({ Angebote.findAll { it.ort_id == Data.ort?.id } }) {
    // Do not retrieve eagerly since we're not loading directly from the Database
    override val retrieveOnLoad = false
}

/**
 * Holding [Schiff_has_Ware] objects that were loaded from the database.
 * They always depend on the [current schiff][Data.schiff]
 */
object SchiffWaren : Storage<Schiff_has_Ware>({ Database.findAllBy(Schiff_has_Ware::schiff_id, Data.schiff?.id) })

/**
 * Holding all [Schiff] objects that were loaded from the database
 */
object Schiffe : Storage<Schiff>({ Database.findAll<Schiff>() })