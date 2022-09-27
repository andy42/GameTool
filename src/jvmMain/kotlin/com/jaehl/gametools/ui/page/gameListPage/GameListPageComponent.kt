package com.jaehl.gametools.ui.page.gameListPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.page.craftingListsPage.CraftingListsPage
import com.jaehl.gametools.ui.page.craftingListsPage.CraftingListsViewModel

class GameListPageComponent (
    private val componentContext: ComponentContext,
    private val onGoBackClicked: () -> Unit,
    private val onSelectGameClick: (Game) -> Unit,
    private val onEditGameClick: (Game?) -> Unit
) : Component, ComponentContext by componentContext {

    private val viewModel = GameListViewModel(RepoSingleton.gameRepo, RepoSingleton.itemRepo, RepoSingleton.craftingListRepo, onSelectGameClick)

    init {
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        GameListPage(
            viewModel = viewModel,
            onGoBackClicked = onGoBackClicked,
            //onSelectGameClick = onSelectGameClick,
            onEditGameClick = onEditGameClick
        )
    }
}