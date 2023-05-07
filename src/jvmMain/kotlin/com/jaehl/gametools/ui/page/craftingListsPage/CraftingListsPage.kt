package com.jaehl.gametools.ui.page.craftingListsPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.AppBar
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.ui.navigation.NavCraftingListListener

@Composable
fun CraftingListsPage(
    viewModel : CraftingListsViewModel,
    navBackListener : NavBackListener,
    navCraftingListListener : NavCraftingListListener
) {

    var showDeleteButtons = remember { mutableStateOf(false) }

    Column(modifier = Modifier) {
        AppBar(
            title = "Crafting Lists",
            returnButton = true,
            onBackClick = {
                navBackListener.navigateBack()
            }
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navCraftingListListener.openCraftingListEdit(null)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                ) {
                    Text("Create New")
                }
                Button(
                    onClick = {
                        showDeleteButtons.value = !showDeleteButtons.value
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text("Edit")
                }
            }
            LazyColumn {
                itemsIndexed(viewModel.list) { index, item ->
                    CraftingListRow(index, viewModel, item, navCraftingListListener::openCraftingListDetails, showDeleteButtons.value)
                }
            }
        }
    }
}

@Composable
fun CraftingListRow(
    index : Int,
    viewModel : CraftingListsViewModel,
    item : CraftingListsViewModel.CraftingListViewModel,
    onCraftingListClick: (String) -> Unit,
    showDeleteButtons : Boolean
){
    Row (
        modifier = Modifier
            .clickable {  onCraftingListClick(item.id) }
            .background(if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            item.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        )
        if(showDeleteButtons) {
            IconButton(
                content = {
                    Icon(Icons.Outlined.Delete, "delete", tint = Color.Black)
                },
                onClick = {
                    viewModel.removeCraftingList(item.id)
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}
