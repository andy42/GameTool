package com.jaehl.gametools.ui.page.itemDetailsPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.AppColor
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.IngredientList
import com.jaehl.gametools.ui.component.ItemIcon
import com.jaehl.gametools.ui.viewModel.ItemRecipeViewModel
import com.jaehl.gametools.util.Log


@Composable
fun ItemDetailsPage(
    item : Item,
    viewModel : ItemDetailsViewModel,
    onGoBackClicked: () -> Unit,
    onEditClicked: (item : Item?) -> Unit
) {
    var showBaseCrafting = remember { mutableStateOf(false) }
    var collapseList = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        AppBar(
            item = item,
            onBackClick = onGoBackClicked,
            onEditClicked = {
                onEditClicked(viewModel.item.value)
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                ItemQuickInfo(
                    viewModel.item.value,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .background(R.Color.primary)

            ) {
                Text(
                    text = "Crafting",
                    modifier = Modifier.padding(15.dp)
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                ) {
                    IconButton(
                        modifier = Modifier,
                        content = {
                            Icon(Icons.Outlined.ArrowDropDown, "Edit", tint = if(collapseList.value) Color.White else Color.Black)
                        },
                        onClick = {
                            collapseList.value = !collapseList.value
                        }
                    )
                    IconButton(
                        modifier = Modifier,
                        content = {
                            Icon(Icons.Outlined.List, "Edit", tint = if(showBaseCrafting.value) Color.White else Color.Black)
                        },
                        onClick = {
                            showBaseCrafting.value = !showBaseCrafting.value
                        }
                    )
                }

            }
            IngredientList(
                Modifier.padding(top = 10.dp, bottom = 10.dp),
                collapseList.value,
                if(showBaseCrafting.value) viewModel.baseRecipe.value else viewModel.recipe.value
            )
        }
    }
}

@Composable
fun AppBar(
    item : Item,
    onBackClick : () -> Unit,
    onEditClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(item.name)
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

@Composable
fun ItemQuickInfo(item : Item, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(250.dp)
            .background(R.Color.primary)
    ) {
        ItemQuickInfoheading(item.name)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 1.dp, end = 1.dp)
                .background(R.Color.pageBackground)
        ){
            ItemIcon(
                item.iconPath,
                modifier = Modifier
                    .align(Alignment.Center),
                size = 100.dp)
        }
        ItemQuickInfoheading("General")
        ItemQuickInfoTitleValue("Category", item.category.value)
        ItemQuickInfoTitleValue("Tech Tier", item.techTier.toString())
        if(item.craftedAt != null) {
            ItemQuickInfoTitleValue("Crafted at", item.craftedAt)
        }
    }
}

@Composable
fun ItemQuickInfoheading(text : String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            color = R.Color.textDark,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun ItemQuickInfoTitleValue(title : String, value : String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 1.dp, end = 1.dp, bottom = 1.dp)
            .background(R.Color.pageBackground)
    ) {
        Text(
            text = title,
            color = R.Color.textDark,
            modifier = Modifier
                .width(100.dp)
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
        )
        Text(
            text = value,
            color = R.Color.textDark,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
        )
    }
}