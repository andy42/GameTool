package com.jaehl.gametools.ui.page.itemEditPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.ui.component.Picker
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.ItemCategoryPickDialog
import com.jaehl.gametools.ui.component.ItemIcon
import com.jaehl.gametools.ui.component.ItemPickDialog
import com.jaehl.gametools.ui.viewModel.ItemRecipeViewModel

@Composable
fun ItemEditPage(
    item : Item?,
    viewModel : ItemEditViewModel,
    onGoBackClicked: () -> Unit
) {

    var isItemPickOpen by remember { mutableStateOf(false) }
    var isItemCategoryPickOpen by remember { mutableStateOf(false) }


    var id by remember { mutableStateOf(item?.id ?: "") }
    var name by remember { mutableStateOf(item?.name ?: "") }
    //var selectedCategory by remember { mutableStateOf(item?.category ?: ItemCategory.Item) }
    var techTier by remember { mutableStateOf( (item?.techTier ?: 1).toString()) }
    var allowsCrafting by remember { mutableStateOf(item?.allowsCrafting ?: false) }
    var recipeCraftAmount by remember { mutableStateOf((item?.recipeCraftAmount ?: "1").toString()) }
    var iconPath by remember { mutableStateOf(item?.iconPath ?: "") }

    //var itemRecipeList = viewModel.itemRecipeList.collectAsState()
    if(viewModel.closePage.value){
        onGoBackClicked()
    }
    Box {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            com.jaehl.gametools.ui.component.AppBar(
                title = viewModel.pageTitle.value,
                returnButton = true,
                onBackClick = onGoBackClicked
            )
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = id,
                    enabled = (item == null),
                    onValueChange = { id = it },
                    label = { Text("Id") }
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Picker(
                    title = "Category",
                    value = viewModel.category.value.value,
                    onClick = {
                        isItemCategoryPickOpen = true
                    }
                )
                OutlinedTextField(
                    value = techTier,
                    onValueChange = { techTier = it },
                    label = { Text("Tech Tier") }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = allowsCrafting,
                        onCheckedChange = { allowsCrafting = it }
                    )
                    Text(
                        text = "AllowsCrafting",
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                Picker(
                    title = "Crafted A",
                    value = viewModel.craftedAt.value,
                    onClick = {
                        viewModel.setItemPickerType(ItemEditViewModel.ItemPickerType.CreatedAt)
                        isItemPickOpen = true
                    }
                )

                OutlinedTextField(
                    value = iconPath,
                    onValueChange = { iconPath = it },
                    label = { Text("Icon Path") }
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Crafting Recipe")
                        IconButton(
                            modifier = Modifier.padding(start = 10.dp),
                            content = {
                                Icon(Icons.Outlined.Add, "Add ", tint = Color.Black)
                            }, onClick = {
                                viewModel.setItemPickerType(ItemEditViewModel.ItemPickerType.AddNewRecipe)
                                isItemPickOpen = true
                            })
                    }
                    if(viewModel.itemRecipeList.size > 0){
                        OutlinedTextField(
                            value = recipeCraftAmount,
                            onValueChange = { value ->
                                recipeCraftAmount = value.filter { it.isDigit() }
                            },
                            label = { Text("Recipe Craft Amount") }
                        )
                    }
                    ItemRecipeColumn(
                        itemRecipeList = viewModel.itemRecipeList,
                        onItemClick = {index ->
                            viewModel.setItemPickerType(ItemEditViewModel.ItemPickerType.UpdateRecipe(index))
                            isItemPickOpen = true
                        },
                        onCountChange = { index, count ->
                            viewModel.onCraftingCountChange(index,count)
                        },
                        onItemDelete = {index ->
                            viewModel.onCraftingItemDelete(index)
                        }
                    )
                }

                Button(onClick = {
                    viewModel.save(
                        id = id,
                        name = name,
                        //category = selectedCategory,
                        techTier = techTier,
                        allowsCrafting = allowsCrafting,
                        recipeCraftAmount = recipeCraftAmount,
                        iconPath = iconPath
                    )

                }) {
                    Text(if (item == null) "Save" else "Update")
                }
            }
        }
        if(isItemPickOpen) {
            ItemPickDialog(
                title = "Search",
                viewModel.pickerItems,
                onItemClick = {item ->
                    viewModel.onItemPickerItemClick(item)
                    //viewModel.setCraftedAt(it)
                    isItemPickOpen = false
                },
                isClearable = viewModel.pickerItemType.value is ItemEditViewModel.ItemPickerType.CreatedAt,
                searchText = viewModel.searchText.value,
                onSearchChange = {
                     viewModel.onSearchTextChange(it)
                },
                onClose = {
                    isItemPickOpen = false
                }
            )
        }
        if(isItemCategoryPickOpen){
            ItemCategoryPickDialog(
                title = "Category",
                categoryList = ItemCategory.values().toList().filter { it != ItemCategory.All },
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

@Composable
fun ItemRecipeColumn(
    itemRecipeList : List<ItemRecipeViewModel>,
    onItemClick : (index : Int) -> Unit,
    onCountChange : (index : Int, count : String) -> Unit,
    onItemDelete : (index : Int) -> Unit
){
    Column {
        itemRecipeList.forEachIndexed{index , itemRecipe ->
            ItemRecipeRow(index, itemRecipe,onItemClick, onCountChange, onItemDelete)
        }
    }
}

@Composable
fun ItemRecipeRow(
    index : Int,
    itemRecipe : ItemRecipeViewModel,
    onItemClick : (index : Int) -> Unit,
    onCountChange : (index : Int, count : String) -> Unit,
    onItemDelete : (index : Int) -> Unit
){
    Row (
        modifier = Modifier
            .background(if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            modifier = Modifier.width(200.dp).padding(top = 10.dp),
            onClick = { onItemClick(index)},
            border = BorderStroke(1.dp, Color.Red),
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemIcon(itemRecipe.item.iconPath)
                Text(
                    itemRecipe.item.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.padding(start = 10.dp),
            value = if(itemRecipe.ingredientAmount == 0) "" else  itemRecipe.ingredientAmount.toString(),
            onValueChange = { value ->
                onCountChange(index, value.filter { it.isDigit() })
            },
            label = { Text("count") }
        )
        IconButton(content = {
            Icon(Icons.Outlined.Delete, "delete", tint = Color.Black)
        }, onClick = {
            onItemDelete(index)
        })
    }
}