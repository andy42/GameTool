package com.jaehl.gametools.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.di.DaggerAppComponent
import com.jaehl.gametools.ui.page.craftingListDetailsPage.CraftingListDetailsPageComponent
import com.jaehl.gametools.ui.page.craftingListEditPage.CraftingListEditPageComponent
import com.jaehl.gametools.ui.page.craftingListsPage.CraftingListsPageComponent
import com.jaehl.gametools.ui.page.gameListPage.GameListPageComponent
import com.jaehl.gametools.ui.page.gameDetails.GameDetailsPageComponent
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsPageComponent
import com.jaehl.gametools.ui.page.itemEditPage.ItemEditPageComponent
import com.jaehl.gametools.ui.page.itemsListPage.ItemListPageComponent

interface NavBackListener {
    fun navigateBack()
}

interface NavItemListener {
    fun openItemList()
    fun openItemEdit(item : Item?)
    fun openItemDetails(item : Item)
}

interface NavCraftingListListener {
    fun openCraftingLists()
    fun openCraftingListEdit(craftingListId : String?)
    fun openCraftingListDetails(craftingListId : String)
}

interface NavGameListener {
    fun openGameDetails(game : Game)
    fun openGameEdit(game : Game?)
}

class NavHostComponent(
    componentContext: ComponentContext,
) : Component,
    ComponentContext by componentContext,
    NavBackListener,
    NavItemListener,
    NavCraftingListListener,
    NavGameListener

{
    private val appComponent: AppComponent = DaggerAppComponent
        .create()

    private var selectedGame : Game = Game()

    private val router = router<ScreenConfig, Component>(
        initialConfiguration = ScreenConfig.GameList,
        childFactory = ::createScreenComponent
    )

    private fun createScreenComponent(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ): Component {
        return when (screenConfig) {
            is ScreenConfig.GameList -> GameListPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                navBackListener = this,
                navGameListener = this
            )
            is ScreenConfig.GameDetails -> GameDetailsPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                selectedGame,
                navBackListener = this,
                navItemListener = this,
                navCraftingListListener = this
            )
            is ScreenConfig.ItemList -> ItemListPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                navBackListener = this,
                navItemListener = this
            )
            is ScreenConfig.ItemEdit -> ItemEditPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                item = screenConfig.item,
                navBackListener = this,
            )
            is ScreenConfig.ItemDetails -> ItemDetailsPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                item = screenConfig.item,
                navBackListener = this,
                navItemListener = this
            )
            is ScreenConfig.CraftingLists -> CraftingListsPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                navBackListener = this,
                navCraftingListListener = this
            )
            is ScreenConfig.CraftingListDetails -> CraftingListDetailsPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                craftingListId = screenConfig.craftingListId,
                navBackListener = this,
                navCraftingListListener = this,
                navItemListener = this
            )
            is ScreenConfig.CraftingListEdit -> CraftingListEditPageComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                craftingListId = screenConfig.craftingListId,
                navBackListener = this,
            )
        }
    }

    override fun navigateBack() {
        router.pop()
    }

    override fun openGameEdit(game: Game?) {

    }

    override fun openGameDetails(game: Game) {
        selectedGame = game
        router.push(ScreenConfig.GameDetails)
    }

    override fun openItemList(){
        router.push(ScreenConfig.ItemList)
    }
    override fun openItemDetails(item : Item) {
        router.push(ScreenConfig.ItemDetails(item))
    }
    override fun openItemEdit(item : Item?) {
        router.push(ScreenConfig.ItemEdit(item))
    }

    override fun openCraftingLists() {
        router.push(ScreenConfig.CraftingLists)
    }

    override fun openCraftingListEdit(craftingListId: String?) {
        router.push(ScreenConfig.CraftingListEdit(craftingListId))
    }

    override fun openCraftingListDetails(craftingListId: String) {
        router.push(ScreenConfig.CraftingListDetails(craftingListId))
    }

    @OptIn(ExperimentalDecomposeApi::class)
    @Composable
    override fun render() {
        Children(routerState = router.state) {
            it.instance.render()
        }
    }

    private sealed class ScreenConfig : Parcelable {
        object GameList : ScreenConfig()
        object GameDetails : ScreenConfig()
        object ItemList : ScreenConfig()
        data class ItemEdit(val item : Item?) : ScreenConfig()
        data class ItemDetails(val item : Item) : ScreenConfig()
        object CraftingLists : ScreenConfig()
        data class CraftingListDetails(val craftingListId : String) : ScreenConfig()
        data class CraftingListEdit(val craftingListId : String?) : ScreenConfig()
    }
}
