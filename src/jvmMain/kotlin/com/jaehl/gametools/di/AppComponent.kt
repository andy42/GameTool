package com.jaehl.gametools.di

import com.jaehl.gametools.di.module.DataModule
import com.jaehl.gametools.ui.page.craftingListDetailsPage.CraftingListDetailsPageComponent
import com.jaehl.gametools.ui.page.craftingListEditPage.CraftingListEditPageComponent
import com.jaehl.gametools.ui.page.craftingListsPage.CraftingListsPageComponent
import com.jaehl.gametools.ui.page.gameListPage.GameListPageComponent
import com.jaehl.gametools.ui.page.gameDetails.GameDetailsPageComponent
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsPageComponent
import com.jaehl.gametools.ui.page.itemEditPage.ItemEditPageComponent
import com.jaehl.gametools.ui.page.itemsListPage.ItemListPageComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class
    ]
)
interface AppComponent {
    fun inject(craftingListDetailsPageComponent : CraftingListDetailsPageComponent)
    fun inject(craftingListEditPageComponent : CraftingListEditPageComponent)
    fun inject(craftingListsPageComponent : CraftingListsPageComponent)
    fun inject(gameListPageComponent : GameListPageComponent)
    fun inject(homePageComponent : GameDetailsPageComponent)
    fun inject(itemDetailsPageComponent: ItemDetailsPageComponent)
    fun inject(itemEditPageComponent: ItemEditPageComponent)
    fun inject(itemListPageComponent: ItemListPageComponent)
}