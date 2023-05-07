package com.jaehl.gametools.ui.page.gameDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.ui.component.AppBar
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavCraftingListListener
import com.jaehl.gametools.ui.navigation.NavItemListener

@Composable
fun HomePage(
    game : Game,
    navBackListener : NavBackListener,
    navItemListener : NavItemListener,
    navCraftingListListener : NavCraftingListListener,
) {

    Column(modifier = Modifier) {
        AppBar(
            title = game.name,
            returnButton = true,
            onBackClick = {
                navBackListener.navigateBack()
            }
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Button(onClick = {
                navItemListener.openItemList()
            }) {
                Text("Item List")
            }
            Button(onClick = {
                navCraftingListListener.openCraftingLists()
            }) {
                Text("Crafting List")
            }
        }
    }
}