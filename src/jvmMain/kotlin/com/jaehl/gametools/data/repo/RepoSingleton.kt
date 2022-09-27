package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.CraftingListsFileImp
import com.jaehl.gametools.data.local.GameListFileImp
import com.jaehl.gametools.data.local.ItemListFileImp

object RepoSingleton {
    val itemRepo : ItemRepo
    val craftingListRepo : CraftingListRepo
    val gameRepo : GamesRepo

    init {
        itemRepo = ItemRepo(ItemListFileImp())
        craftingListRepo = CraftingListRepo(CraftingListsFileImp())
        gameRepo = GamesRepo(GameListFileImp())
    }
}