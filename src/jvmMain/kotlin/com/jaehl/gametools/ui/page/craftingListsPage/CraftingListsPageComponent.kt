package com.jaehl.gametools.ui.page.craftingListsPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavCraftingListListener
import javax.inject.Inject

class CraftingListsPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val navBackListener : NavBackListener,
    private val navCraftingListListener : NavCraftingListListener,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var  viewModel : CraftingListsViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        CraftingListsPage(
            viewModel = viewModel,
            navBackListener = navBackListener,
            navCraftingListListener = navCraftingListListener
        )
    }
}