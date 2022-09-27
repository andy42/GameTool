package com.jaehl.gametools.ui.page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.component.AppBar
import com.jaehl.gametools.ui.page.itemsListPage.ItemList
import com.jaehl.gametools.ui.page.itemsListPage.ItemListViewModel

@Composable
fun HomePage(
    game : Game,
    onGoBackClicked: () -> Unit,
    onItemListClick: () -> Unit,
    onCraftingListClick: () -> Unit
) {

    Column(modifier = Modifier) {
        AppBar(
            title = game.name,
            returnButton = true,
            onBackClick = {
                onGoBackClicked()
            }
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Button(onClick = {
                onItemListClick()
            }) {
                Text("Item List")
            }
            Button(onClick = {
                onCraftingListClick()
            }) {
                Text("Crafting List")
            }
        }
    }
}