package com.jaehl.gametools.ui.page.itemDetailsPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.IngredientList
import com.jaehl.gametools.ui.component.ItemIcon


@Composable
fun ItemDetailsPage(
    item : Item,
    viewModel : ItemDetailsViewModel,
    onGoBackClicked: () -> Unit,
    onEditClicked: (item : Item?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(R.Color.pageBackground)
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
            viewModel.recipes.forEachIndexed { index, recipeViewModel ->
                Recipe(
                    viewModel = viewModel,
                    recipeIndex = index,
                    recipe = recipeViewModel
                )
            }
        }
    }
}

@Composable
fun Recipe(
    viewModel : ItemDetailsViewModel,
    recipeIndex : Int,
    recipe : ItemDetailsViewModel.RecipeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(R.Color.cardBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(R.Color.primary)

        ) {
            Text(
                text = "Recipe ${recipeIndex + 1}",
                modifier = Modifier.padding(15.dp)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
            ) {
                IconButton(
                    modifier = Modifier,
                    content = {
                        Icon(Icons.Outlined.ArrowDropDown, "Edit", tint = if(recipe.collapseList) Color.White else Color.Black)
                    },
                    onClick = {
                        viewModel.onCollapseListToggle(recipeIndex)
                        //collapseList.value = !collapseList.value
                    }
                )
                IconButton(
                    modifier = Modifier,
                    content = {
                        Icon(Icons.Outlined.List, "Edit", tint = if(recipe.showBaseCrafting) Color.White else Color.Black)
                    },
                    onClick = {
                        viewModel.onShowBaseCraftingToggle(recipeIndex)
                    }
                )
            }

        }
        IngredientList(
            Modifier.padding(top = 10.dp, bottom = 10.dp),
            recipe.collapseList,
            if(recipe.showBaseCrafting) recipe.baseIngredients else recipe.ingredients
        )
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
                .background(R.Color.cardBackground)
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
            .background(R.Color.cardBackground)
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