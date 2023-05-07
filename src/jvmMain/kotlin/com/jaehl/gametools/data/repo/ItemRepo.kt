package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.ItemListFile
import com.jaehl.gametools.data.local.LocalFiles
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*

class ItemRepo(val itemListFile : ItemListFile) {
    private val itemMap = LinkedHashMap<String, Item>()
    private var loaded = false

    private var items = MutableSharedFlow<List<Item>>(replay = 1)

    private var selectedGame : Game? = null
    @Synchronized
    suspend fun setGame(game : Game) {
            selectedGame = game
            loadLocal(true)
            items.tryEmit(itemMap.values.toList())
    }

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
            itemListFile.save(getFileName(), itemMap.values.toList())
            items.tryEmit(itemMap.values.toList())
        }
    }

    private fun getGameDir() : String {
        return selectedGame?.getDirectory() ?: ""
    }

    fun createItemsFromImages() = GlobalScope.async {
        val imagesDir = LocalFiles.getFile("${getGameDir()}/images")
        imagesDir.walkTopDown().forEach { file ->
            if (file.name.split(".").last() != "png") return@forEach

            val key = file.name.split(".").first()
            if (itemMap.contains(key)) return@forEach

            itemMap[key] = Item(
                id = key,
                name = key.replace("_", " "),
                category = ItemCategory.None,
                iconPath = "${getGameDir()}/images/${key}.png"
            )
            itemListFile.save(getFileName(), itemMap.values.toList())
            items.tryEmit(itemMap.values.toList())
        }
    }

    @Synchronized
    private fun loadLocal(forceReload : Boolean = false){

        if(loaded && !forceReload) return
        if(selectedGame == null) return

        try {
            itemMap.clear()
            itemListFile.load(getFileName()).forEach {
                it.iconPath = "${getGameDir()}/images/${it.id}.png"
                itemMap[it.id] = it
            }
        } catch (t : Throwable){
            System.out.println(t.message)
        }
    }

    private fun getFileName() : String{
        return "${selectedGame?.id}/items.json"
    }
}