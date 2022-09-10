package com.jaehl.gametools.ui.page.craftingListDetailsPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component

class CraftingListDetailsPageComponent (
    private val componentContext: ComponentContext,
    private val craftingListId : String,
    private val onGoBackClicked: () -> Unit,
    private val onCraftingListEditClick : (String?) -> Unit,
    private val onItemClick : (Item) -> Unit
) : Component, ComponentContext by componentContext {

    private val viewModel = CraftingListDetailsViewModel(
        craftingListId,
        RepoSingleton.itemRepo,
        RepoSingleton.craftingListRepo
    )
    init {
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        CraftingListDetailsPage(
            craftingListId = craftingListId,
            viewModel = viewModel,
            onGoBackClicked = onGoBackClicked,
            onCraftingListEditClick = onCraftingListEditClick,
            onItemClick = onItemClick
        )
    }
}