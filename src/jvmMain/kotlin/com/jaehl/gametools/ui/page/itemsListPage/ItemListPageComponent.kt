package com.jaehl.gametools.ui.page.itemsListPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.*
import javax.inject.Inject

class ItemListPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val navBackListener : NavBackListener,
    private val navItemListener : NavItemListener,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel : ItemListViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        ItemListPage(
            viewModel = viewModel,
            navBackListener = navBackListener,
            navItemListener = navItemListener
        )
    }
}