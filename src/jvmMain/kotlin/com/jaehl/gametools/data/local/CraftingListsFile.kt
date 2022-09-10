package com.jaehl.gametools.data.local

import com.jaehl.gametools.data.model.CraftingList

interface CraftingListsFile {
    fun load(fileName : String)  : List<CraftingList>
    fun save(fileName : String, craftingLists : List<CraftingList>) : Boolean
}