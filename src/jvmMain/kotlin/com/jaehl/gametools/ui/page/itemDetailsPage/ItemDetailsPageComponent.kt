package com.jaehl.gametools.ui.page.itemDetailsPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavItemListener
import javax.inject.Inject

class ItemDetailsPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val item : Item,
    private val navBackListener : NavBackListener,
    private val navItemListener : NavItemListener,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel : ItemDetailsViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope, item)
        }

        ItemDetailsPage(
            item = item,
            viewModel = viewModel,
            navBackListener = navBackListener,
            navItemListener = navItemListener)
    }
}