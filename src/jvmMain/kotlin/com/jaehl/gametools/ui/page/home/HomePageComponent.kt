package com.jaehl.gametools.ui.page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsPage
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsViewModel

class HomePageComponent (
    private val componentContext: ComponentContext,
    private val game : Game,
    private val onGoBackClicked: () -> Unit,
    private val onItemListClick: () -> Unit,
    private val onCraftingListClick: () -> Unit
) : Component, ComponentContext by componentContext {

    init {
    }

    @Composable
    override fun render() {
        HomePage(
            game = game,
            onGoBackClicked = onGoBackClicked,
            onItemListClick = onItemListClick,
            onCraftingListClick = onCraftingListClick
        )
    }
}