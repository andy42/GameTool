package com.jaehl.gametools.ui.page.itemDetailsPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component
import java.nio.file.Path

class ItemDetailsPageComponent (
    private val componentContext: ComponentContext,
    private val game : Game,
    private val item : Item,
    private val onGoBackClicked: () -> Unit,
    private val onEditClicked: (item : Item?) -> Unit
) : Component, ComponentContext by componentContext {

    private val viewModel : ItemDetailsViewModel

    init {
        viewModel = ItemDetailsViewModel(RepoSingleton.itemRepo, game)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope, item)
        }

        ItemDetailsPage(item = item, viewModel = viewModel, onGoBackClicked = onGoBackClicked, onEditClicked = onEditClicked)
    }
}