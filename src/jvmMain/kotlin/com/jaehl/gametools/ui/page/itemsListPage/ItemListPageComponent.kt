package com.jaehl.gametools.ui.page.itemsListPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component

class ItemListPageComponent (
    private val componentContext: ComponentContext,
    private val onGoBackClicked: () -> Unit,
    private val onItemClick: (Item) -> Unit,
    private val onItemEditClick: (Item?) -> Unit
) : Component, ComponentContext by componentContext {

    private val viewModel = ItemListViewModel(RepoSingleton.itemRepo)

    init {

    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        ItemListPage(
            viewModel = viewModel,
            onGoBackClicked = onGoBackClicked,
            onItemClick = onItemClick,
            onItemEditClick = onItemEditClick)
    }
}