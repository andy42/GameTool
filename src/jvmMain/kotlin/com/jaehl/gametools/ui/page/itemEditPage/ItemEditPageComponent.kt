package com.jaehl.gametools.ui.page.itemEditPage

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import javax.inject.Inject

class ItemEditPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val item : Item?,
    private val navBackListener : NavBackListener,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel : ItemEditViewModel

    init {
        appComponent.inject(this)
        viewModel.setup(item)

    }

    @Composable
    override fun render() {
        ItemEditPage(item = item, viewModel = viewModel, navBackListener = navBackListener)
    }
}