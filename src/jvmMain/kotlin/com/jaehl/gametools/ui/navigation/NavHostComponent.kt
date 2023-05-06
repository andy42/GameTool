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
import com.jaehl.gametools.ui.page.craftingListDetailsPage.CraftingListDetailsPageComponent
import com.jaehl.gametools.ui.page.craftingListEditPage.CraftingListEditPageComponent
import com.jaehl.gametools.ui.page.craftingListsPage.CraftingListsPageComponent
import com.jaehl.gametools.ui.page.gameListPage.GameListPageComponent
import com.jaehl.gametools.ui.page.home.HomePageComponent
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsPageComponent
import com.jaehl.gametools.ui.page.itemEditPage.ItemEditPageComponent
import com.jaehl.gametools.ui.page.itemsListPage.ItemListPageComponent

class NavHostComponent(
    componentContext: ComponentContext,
) : Component, ComponentContext by componentContext {

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
                componentContext,
                ::onGoBackClicked,
                ::onGameClick,
                ::onGameEditClick
            )
            is ScreenConfig.Home -> HomePageComponent(
                componentContext,
                selectedGame,
                ::onGoBackClicked,
                ::onItemListClick,
                ::onCraftingListsClick
            )
            is ScreenConfig.ItemList -> ItemListPageComponent(
                componentContext,
                selectedGame,
                ::onGoBackClicked,
                ::onItemClick,
                ::onItemEditClick
            )
            is ScreenConfig.ItemEdit -> ItemEditPageComponent(
                componentContext,
                selectedGame,
                screenConfig.item,
                ::onGoBackClicked
            )
            is ScreenConfig.ItemDetails -> ItemDetailsPageComponent(
                componentContext,
                selectedGame,
                screenConfig.item,
                ::onGoBackClicked,
                ::onItemEditClick
            )
            is ScreenConfig.CraftingLists -> CraftingListsPageComponent(
                componentContext,
                selectedGame,
                ::onGoBackClicked,
                ::onCraftingListDetailsClick,
                ::onCraftingListEditClick
            )
            is ScreenConfig.CraftingListDetails -> CraftingListDetailsPageComponent(
                componentContext,
                selectedGame,
                screenConfig.craftingListId,
                ::onGoBackClicked,
                ::onCraftingListEditClick,
                ::onItemClick
            )
            is ScreenConfig.CraftingListEdit -> CraftingListEditPageComponent(
                componentContext,
                selectedGame,
                screenConfig.craftingListId,
                ::onGoBackClicked
            )
        }
    }

    private fun onGameClick(game : Game){
        selectedGame = game
        router.push(ScreenConfig.Home)
    }

    private fun onGameEditClick(game : Game?){

    }

    private fun onItemListClick(){
        router.push(ScreenConfig.ItemList)
    }

    private fun onCraftingListsClick(){
        router.push(ScreenConfig.CraftingLists)
    }

    private fun onCraftingListDetailsClick(craftingListId : String){
        router.push(ScreenConfig.CraftingListDetails(craftingListId))
    }

    private fun onCraftingListEditClick(craftingListId : String?){
        router.push(ScreenConfig.CraftingListEdit(craftingListId))
    }

    private fun onItemClick(item : Item) {
        router.push(ScreenConfig.ItemDetails(item))
    }

    private fun onItemEditClick (item : Item?) {
        router.push(ScreenConfig.ItemEdit(item))
    }

    private fun onGoBackClicked() {
        router.pop()
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
        object Home : ScreenConfig()
        object ItemList : ScreenConfig()
        data class ItemEdit(val item : Item?) : ScreenConfig()
        data class ItemDetails(val item : Item) : ScreenConfig()
        object CraftingLists : ScreenConfig()
        data class CraftingListDetails(val craftingListId : String) : ScreenConfig()
        data class CraftingListEdit(val craftingListId : String?) : ScreenConfig()
    }
}
