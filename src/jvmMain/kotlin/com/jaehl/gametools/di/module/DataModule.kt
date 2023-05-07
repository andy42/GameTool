package com.jaehl.gametools.di.module

import com.jaehl.gametools.data.repo.CraftingListRepo
import com.jaehl.gametools.data.repo.GamesRepo
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.data.repo.RepoSingleton
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun itemRepo() : ItemRepo {
        return RepoSingleton.itemRepo
    }

    @Provides
    fun craftingListRepo() : CraftingListRepo {
        return RepoSingleton.craftingListRepo
    }

    @Provides
    fun gamesRepo() : GamesRepo {
        return RepoSingleton.gameRepo
    }
}