package com.jaehl.gametools.ui.page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.component.AppBar
import com.jaehl.gametools.ui.page.itemsListPage.ItemList
import com.jaehl.gametools.ui.page.itemsListPage.ItemListViewModel

@Composable
fun HomePage(
    onGoBackClicked: () -> Unit,
    onItemListClick: () -> Unit,
    onCraftingListClick: () -> Unit
) {

    Column(modifier = Modifier) {
        AppBar(
            title = "Home",
            returnButton = false,
            onBackClick = {}
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