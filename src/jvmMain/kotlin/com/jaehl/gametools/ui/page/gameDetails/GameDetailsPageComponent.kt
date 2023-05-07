package com.jaehl.gametools.ui.page.gameDetails

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.di.AppComponent
import com.jaehl.gametools.ui.navigation.Component
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavCraftingListListener
import com.jaehl.gametools.ui.navigation.NavItemListener
import javax.inject.Inject

class GameDetailsPageComponent (
    appComponent : AppComponent,
    private val componentContext: ComponentContext,
    private val game : Game,
    private val navBackListener : NavBackListener,
    private val navItemListener : NavItemListener,
    private val navCraftingListListener : NavCraftingListListener,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var gameDetailsViewModel: GameDetailsViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        HomePage(
            game = game,
            navBackListener = navBackListener,
            navItemListener = navItemListener,
            navCraftingListListener = navCraftingListListener
        )
    }
}