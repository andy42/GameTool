package com.jaehl.gametools.ui.page.gameListPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavGameListener
import javax.inject.Inject

class GameListPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val navBackListener : NavBackListener,
    private val navGameListener: NavGameListener
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel : GameListViewModel

    init {
        appComponent.inject(this)
        viewModel.navGameListener = navGameListener
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        GameListPage(
            viewModel = viewModel,
            navBackListener = navBackListener,
            //onSelectGameClick = onSelectGameClick,
            navGameListener = navGameListener
        )
    }
}