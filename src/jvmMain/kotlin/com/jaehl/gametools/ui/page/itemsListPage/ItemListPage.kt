package com.jaehl.gametools.ui.page.itemsListPage

import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.ui.component.Picker
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import com.jaehl.gametools.ui.AppColor
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.ItemCategoryPickDialog
import com.jaehl.gametools.ui.component.ItemIcon
import java.io.File

@Composable
fun ItemRow(
    index : Int,
    item : Item,
    onItemClick: (Item) -> Unit,
    onItemEditClick: (Item?) -> Unit
){
    Row (
        modifier = Modifier
            .clickable {  onItemClick(item) }
            .background(if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd),
        verticalAlignment = Alignment.CenterVertically
    ){
        ItemIcon(item.iconPath)
        Text(
            item.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        )
        IconButton(content = {
            Icon(Icons.Outlined.Edit, "back", tint = Color.Black)
        }, onClick = {
            onItemEditClick(item)
        })
    }
}

@Composable
fun ItemList(
    itemList : List<Item>,
    onItemClick: (Item) -> Unit,
    onItemEditClick: (Item?) -> Unit
){
    LazyColumn {
        itemsIndexed(itemList) { index, item ->
            ItemRow(index, item, onItemClick, onItemEditClick)
        }
    }
}

@Composable
fun ItemListPage(
    viewModel : ItemListViewModel,
    onGoBackClicked: () -> Unit,
    onItemClick: (Item) -> Unit,
    onItemEditClick: (Item?) -> Unit
) {
    var isItemCategoryPickOpen by remember { mutableStateOf(false) }

    Box {
        Column(modifier = Modifier) {
            com.jaehl.gametools.ui.component.AppBar(
                title = "Items",
                returnButton = true,
                onBackClick = {
                    onGoBackClicked()
                }
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Button(onClick = {
                    onItemEditClick(null)
                }) {
                    Text("Create New")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.searchText.value,
                        onValueChange = {
                            viewModel.onSearchTextChange(it)
                        },
                        label = { Text("Search") },
                        modifier = Modifier
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                    ) {
                        OutlinedTextField(
                            value = viewModel.filterCategory.value.value,
                            onValueChange = { },
                            label = { Text("Filter Category") }
                        )
                        Box(modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                isItemCategoryPickOpen = true
                            })
                    }
                }

                ItemList(viewModel.items, onItemClick, onItemEditClick)
            }
        }
        if(isItemCategoryPickOpen){
            ItemCategoryPickDialog(
                title = "Category",
                categoryList = ItemCategory.values().toList(),
                onCategoryClick = {
                    isItemCategoryPickOpen = false
                    viewModel.onItemCategoryClick(it)
                },
                onClose = {
                    isItemCategoryPickOpen = false
                }
            )
        }
    }
}