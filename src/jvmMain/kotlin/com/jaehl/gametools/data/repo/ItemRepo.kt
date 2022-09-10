package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.ItemListFile
import com.jaehl.gametools.data.local.ItemListFileImp
import com.jaehl.gametools.data.mock.ItemsMock
import com.jaehl.gametools.data.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import java.util.SortedMap

class ItemRepo(val itemListFile : ItemListFile) {
    private val itemMap = LinkedHashMap<String, Item>()
    private var loaded = false

    private var items = MutableSharedFlow<List<Item>>(replay = 1)

    fun getItems() : SharedFlow<List<Item>> = items

    init {
        GlobalScope.async {
            loadLocal(true)
            items.tryEmit(itemMap.values.toList())
        }
    }

    fun getItemAsync(id : String, forceReload : Boolean = false) = flow<Item?>{
        GlobalScope.async {
            loadLocal(forceReload)
            emit(itemMap[id])
        }
    }

    fun getItemAll() : List<Item> {
        return itemMap.values.toList()
    }

    suspend fun getItem(id : String?) : Item? {
        if (id == null) return null
        return itemMap[id]
    }

    fun updateItem(item : Item){
        GlobalScope.async {
            itemMap[item.id] = item
            itemListFile.save(fileName, itemMap.values.toList())
            items.tryEmit(itemMap.values.toList())
        }
    }

    private fun loadLocal(forceReload : Boolean = false){

        if(loaded && !forceReload) return

        try {
            itemListFile.load(fileName).forEach {
                itemMap[it.id] = it
            }
        } catch (t : Throwable){
            System.out.println(t.message)
        }
    }

    companion object {
        private const val fileName = "items.json"
    }
}