package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.CraftingListsFileImp
import com.jaehl.gametools.data.local.ItemListFileImp

object RepoSingleton {
    val itemRepo : ItemRepo
    val craftingListRepo : CraftingListRepo

    init {
        itemRepo = ItemRepo(ItemListFileImp())
        craftingListRepo = CraftingListRepo(CraftingListsFileImp())
    }
}