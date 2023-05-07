package com.jaehl.gametools.ui.page.craftingListEditPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import javax.inject.Inject

class CraftingListEditPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val craftingListId : String?,
    private val navBackListener : NavBackListener,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel : CraftingListEditViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope, craftingListId, navBackListener)
        }

        CraftingListEditPage(
            viewModel = viewModel,
            navBackListener = navBackListener
        )
    }
}