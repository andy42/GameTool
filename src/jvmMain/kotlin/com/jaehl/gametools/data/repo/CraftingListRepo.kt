package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.CraftingListsFile
import com.jaehl.gametools.data.local.CraftingListsFileImp
import com.jaehl.gametools.data.model.CraftingList
import com.jaehl.gametools.data.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow

class CraftingListRepo(
    private val  craftingListsFile : CraftingListsFile = CraftingListsFileImp()
) {

    private val craftingListMap = LinkedHashMap<String, CraftingList>()
    private var loaded = false

    private var craftingLists = MutableSharedFlow<List<CraftingList>>(replay = 1)

    fun getCraftingList() : SharedFlow<List<CraftingList>> = craftingLists

    init {
        GlobalScope.async {
            loadLocal(true)
            craftingLists.tryEmit(craftingListMap.values.toList())
        }
    }

    fun getCraftingListAsync(id : String, forceReload : Boolean = false) = flow<CraftingList?>{
        GlobalScope.async {
            loadLocal(forceReload)
            emit(craftingListMap[id])
        }
    }

    fun updateItem(craftingList : CraftingList){
        GlobalScope.async {
            craftingListMap[craftingList.id] = craftingList
            craftingListsFile.save(fileName, craftingListMap.values.toList())
            craftingLists.tryEmit(craftingListMap.values.toList())
        }
    }

    private fun createNewId() : String {
        var id = 0
        while (true){
            if(!craftingListMap.containsKey(id.toString())){
                return id.toString()
            }
            id++
        }
    }

    fun addItem(craftingList : CraftingList){
        GlobalScope.async {
            craftingList.id = createNewId()
            craftingListMap[craftingList.id] = craftingList
            craftingListsFile.save(fileName, craftingListMap.values.toList())
            craftingLists.tryEmit(craftingListMap.values.toList())
        }
    }

    fun removeItem(id : String){
        if(craftingListMap.containsKey(id)) {
            craftingListMap.remove(id)
            craftingListsFile.save(fileName, craftingListMap.values.toList())
            craftingLists.tryEmit(craftingListMap.values.toList())
        }
    }

    suspend fun getCraftingList(id : String?) : CraftingList? {
        if (id == null) return null
        return craftingListMap[id]
    }

    private fun loadLocal(forceReload : Boolean = false){
        if(loaded && !forceReload) return

        try {
            craftingListsFile.load(fileName).forEach {
                craftingListMap[it.id] = it
            }
        } catch (t : Throwable){
            System.out.println(t.message)
        }
    }

    companion object {
        private const val fileName = "craftingLists.json"
    }

}