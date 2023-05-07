package com.jaehl.gametools.ui.page.craftingListDetailsPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavCraftingListListener
import com.jaehl.gametools.ui.navigation.NavItemListener
import javax.inject.Inject

class CraftingListDetailsPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val craftingListId : String,
    private val navBackListener : NavBackListener,
    private val navCraftingListListener : NavCraftingListListener,
    private val navItemListener : NavItemListener

    ) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel : CraftingListDetailsViewModel
    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope, craftingListId)
        }

        CraftingListDetailsPage(
            craftingListId = craftingListId,
            viewModel = viewModel,
            navBackListener = navBackListener,
            navCraftingListListener = navCraftingListListener,
            navItemListener = navItemListener
        )
    }
}