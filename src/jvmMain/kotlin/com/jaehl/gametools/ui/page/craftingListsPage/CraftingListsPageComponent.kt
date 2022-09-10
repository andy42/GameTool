package com.jaehl.gametools.ui.page.craftingListsPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component

class CraftingListsPageComponent (
    private val componentContext: ComponentContext,
    private val onGoBackClicked: () -> Unit,
    private val onCraftingListDetailsClick: (String) -> Unit,
    private val onCraftingListEditClick: (String?) -> Unit
) : Component, ComponentContext by componentContext {

    private val viewModel = CraftingListsViewModel(RepoSingleton.craftingListRepo)

    init {
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        CraftingListsPage(
            viewModel = viewModel,
            onGoBackClicked = onGoBackClicked,
            onCraftingListDetailsClick = onCraftingListDetailsClick,
            onCraftingListEditClick = onCraftingListEditClick
        )
    }
}