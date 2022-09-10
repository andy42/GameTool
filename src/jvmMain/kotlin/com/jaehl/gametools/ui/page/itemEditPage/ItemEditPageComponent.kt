package com.jaehl.gametools.ui.page.itemEditPage

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.RepoSingleton
import com.jaehl.gametools.ui.navigation.Component
import java.nio.file.Path

class ItemEditPageComponent (
    private val componentContext: ComponentContext,
    private val item : Item?,
    private val onGoBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    private val viewModel : ItemEditViewModel

    init {
        viewModel = ItemEditViewModel(RepoSingleton.itemRepo)
        viewModel.setup(item)
    }

    @Composable
    override fun render() {
        ItemEditPage(item = item, viewModel = viewModel, onGoBackClicked = onGoBackClicked)
    }
}