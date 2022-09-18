package com.jaehl.gametools.ui.page.craftingListEditPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.*

@Composable
fun CraftingListEditPage(
    viewModel : CraftingListEditViewModel,
    onGoBackClicked: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            AppBar(
                title = viewModel.windowTitle.value,
                returnButton = true,
                onBackClick = {
                    onGoBackClicked()
                }
            )

            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 10.dp),
                    value = viewModel.craftingListTitle.value,
                    onValueChange = { value ->
                        viewModel.craftingListTitle.value = value
                    },
                    label = { Text("Crafting List Title") }
                )
                viewModel.sections.forEach {
                    Section(viewModel, it)
                }
                Button(
                    onClick = {
                        viewModel.addNewSection()
                    },
                    modifier = Modifier
                ) {
                    Text("Add Section")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.save()
                        },
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.Center)
                    ) {
                        Text("Save")
                    }
                }

            }
        }
        if(viewModel.isItemPickOpen.value) {
            ItemPickDialog(
                title = "Item Picker",
                viewModel.pickerItems,
                onItemClick = {item ->
                    viewModel.onItemPickerItemClick(item)
                },
                isClearable = false,
                searchText = viewModel.searchText.value,
                onSearchChange = {
                    viewModel.onItemPickerSearchChange(it)
                },
                onClose = {
                    viewModel.onItemPickerItemClick(null)
                }
            )
        }
    }
}

@Composable
fun Section(
    viewModel : CraftingListEditViewModel,
    section : CraftingListEditViewModel.SectionViewModel
){
    Box(
        modifier = Modifier
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(R.Color.primary)
                    .padding(2.dp)
                    .background(Color.White)
            ) {

                OutlinedTextField(
                    value = section.name,
                    onValueChange = { viewModel.updateSectionName(section.id, it) },
                    label = { Text("section Name") },
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1.0f)
                )
                IconButton(
                    content = {
                        Icon(Icons.Outlined.Delete, "delete", tint = Color.Black)
                    },
                    onClick = {
                        viewModel.removeSection(section.id)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            section.itemList.values.forEachIndexed { index, itemViewModel ->
                ItemRecipeRow(
                    index,
                    section.id,
                    viewModel,
                    itemViewModel
                )
            }
            Button(
                onClick = {
                    viewModel.openItemPicker(section.id)
                }
            ) {
                Text("Add Item")
            }
        }
    }
}

@Composable
fun ItemRecipeRow(
    index : Int,
    sectionId : String,
    viewModel: CraftingListEditViewModel,
    itemViewModel :CraftingListEditViewModel.ItemViewModel
){
    Row (
        modifier = Modifier
            .background(if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            modifier = Modifier.width(200.dp).padding(top = 10.dp),
            onClick = { },
            border = BorderStroke(1.dp, Color.Red),
        ){
            Row(
                //modifier = Modifier()
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemIcon(itemViewModel.item.iconPath)
                Text(
                    itemViewModel.item.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.padding(start = 10.dp),
            value = if(itemViewModel.amount == 0) "" else  itemViewModel.amount.toString(),
            onValueChange = { value ->
                viewModel.updateItemAmount(sectionId, itemViewModel.item.id, value.filter { it.isDigit() })
            },
            label = { Text("amount") }
        )
        IconButton(content = {
            Icon(Icons.Outlined.Delete, "delete", tint = Color.Black)
        }, onClick = {
            viewModel.removeItem(sectionId, itemViewModel.item)
        })
    }
}