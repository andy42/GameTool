package com.jaehl.gametools.ui.page.craftingListEditPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.page.craftingListDetailsPage.CraftingListDetailsPage

class CraftingListEditPageComponent (
    private val componentContext: ComponentContext,
    private val craftingListId : String?,
    private val onGoBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    val viewModel = CraftingListEditViewModel(
        craftingListId,
        RepoSingleton.itemRepo,
        RepoSingleton.craftingListRepo,
        onGoBackClicked
    )

    init {
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        CraftingListEditPage(
            viewModel = viewModel,
            onGoBackClicked = onGoBackClicked
        )
    }
}