package com.jaehl.gametools.ui.page.craftingListDetailsPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.FlowRow
import com.jaehl.gametools.ui.component.IngredientList
import com.jaehl.gametools.ui.component.ItemIcon

@Composable
fun CraftingListDetailsPage(
    craftingListId : String,
    viewModel : CraftingListDetailsViewModel,
    onGoBackClicked: () -> Unit,
    onCraftingListEditClick : (String?) -> Unit,
    onItemClick : (Item) -> Unit
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        AppBar(
            title = viewModel.title.value,
            onBackClick = {
                onGoBackClicked()
            },
            onEditClicked = {
                onCraftingListEditClick(craftingListId)
            }
        )

        Column(modifier = Modifier.padding(20.dp)) {
            viewModel.sections.forEach { section ->
                Section(viewModel, section, onItemClick)
            }
        }
    }
}

@Composable
fun Item(itemViewModel : CraftingListDetailsViewModel.ItemViewModel, onItemClick : (item : Item) -> Unit){
    Box {
        Row(
            modifier = Modifier
                .padding(end = 10.dp, top = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .clickable {
                    onItemClick(itemViewModel.item)
                }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemIcon(
                iconPath = itemViewModel.item.iconPath,
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp),
                size = 70.dp
            )
            Text(
                itemViewModel.amount.toString(),
                fontSize = 30.sp
            )
        }
    }
}

@Composable
fun Section(
    viewModel : CraftingListDetailsViewModel,
    section : CraftingListDetailsViewModel.SectionViewModel,
    onItemClick : (item : Item) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(R.Color.primary)
        ) {
            Text(
                text = section.name,
                modifier = Modifier.padding(15.dp)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
            ) {
                IconButton(
                    modifier = Modifier,
                    content = {
                        Icon(Icons.Outlined.ArrowDropDown, "Edit", tint = if(section.collapseIngredientList) Color.White else Color.Black)
                    },
                    onClick = {
                        viewModel.collapseIngredientList(section.id)
                    }
                )
                IconButton(
                    modifier = Modifier,
                    content = {
                        Icon(Icons.Outlined.List, "Edit", tint = if(section.showBaseCrafting) Color.White else Color.Black)
                    },
                    onClick = {
                        viewModel.showBaseCrafting(section.id)
                    }
                )
            }

        }
        FlowRow {
            section.itemList.forEach{ item ->
                Item(item, onItemClick)
            }
        }
        IngredientList(
            Modifier.padding(top = 10.dp, bottom = 10.dp),
            section.collapseIngredientList,
            if (section.showBaseCrafting) section.baseIngredient else section.ingredient
        )
    }
}

@Composable
fun AppBar(
    title : String,
    onBackClick : () -> Unit,
    onEditClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(content = {
                Icon(Icons.Outlined.ArrowBack, "back", tint = Color.White)
            }, onClick = {
                onBackClick()
            })
        },
        actions = {
            IconButton(content = {
                Icon(Icons.Outlined.Edit, "Edit", tint = Color.White)
            }, onClick = {
                onEditClicked()
            })
        }
    )
}