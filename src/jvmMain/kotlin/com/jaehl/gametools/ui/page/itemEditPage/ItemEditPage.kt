package com.jaehl.gametools.ui.page.itemEditPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.ui.component.Picker
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.ItemCategoryPickDialog
import com.jaehl.gametools.ui.component.ItemIcon
import com.jaehl.gametools.ui.component.ItemPickDialog
import com.jaehl.gametools.ui.navigation.NavBackListener

@Composable
fun ItemEditPage(
    item : Item?,
    viewModel : ItemEditViewModel,
    navBackListener : NavBackListener
) {
    var isItemCategoryPickOpen by remember { mutableStateOf(false) }


    var id by remember { mutableStateOf(item?.id ?: "") }
    var name by remember { mutableStateOf(item?.name ?: "") }
    //var selectedCategory by remember { mutableStateOf(item?.category ?: ItemCategory.Item) }
    var techTier by remember { mutableStateOf( (item?.techTier ?: 1).toString()) }
    var allowsCrafting by remember { mutableStateOf(item?.allowsCrafting ?: false) }
    var recipeCraftAmount by remember { mutableStateOf((item?.getRecipe(0)?.craftAmount ?: "1").toString()) }
    var iconPath by remember { mutableStateOf(item?.iconPath ?: "") }

    //var itemRecipeList = viewModel.itemRecipeList.collectAsState()
    if(viewModel.closePage.value){
        navBackListener.navigateBack()
    }
    Box(
        modifier = Modifier
            .background(R.Color.pageBackground)
            .fillMaxHeight()
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            com.jaehl.gametools.ui.component.AppBar(
                title = viewModel.pageTitle.value,
                returnButton = true,
                onBackClick = navBackListener::navigateBack
            )
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(R.Color.cardBackground)
                        .padding(10.dp)
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
                            viewModel.onOpenItemPicker(ItemEditViewModel.ItemPickerType.CreatedAt)
                            //isItemPickOpen = true
                        }
                    )

                    OutlinedTextField(
                        value = iconPath,
                        onValueChange = { iconPath = it },
                        label = { Text("Icon Path") }
                    )
                }

                Column {
                    viewModel.recipeList.forEachIndexed { index, recipeViewModel ->
                        RecipeColumn(
                            viewModel = viewModel,
                            recipeIndex = index,
                            recipe =  recipeViewModel
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(R.Color.cardBackground)
                        .padding(10.dp)
                ) {
                    Button(onClick = {
                        viewModel.onRecipeAdd()

                    }) {
                        Text("Add Recipe")
                    }
                }

                Button(
                    onClick = {
                        viewModel.save(
                            id = id,
                            name = name,
                            //category = selectedCategory,
                            techTier = techTier,
                            allowsCrafting = allowsCrafting,
                            recipeCraftAmount = recipeCraftAmount,
                            iconPath = iconPath
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(if (item == null) "Save" else "Update")
                }
            }
        }
        if(viewModel.isItemPickOpen.value) {
            ItemPickDialog(
                title = "Search",
                viewModel.pickerItems,
                onItemClick = {item ->
                    viewModel.onItemPickerItemClick(item)
                },
                isClearable = viewModel.pickerItemType.value is ItemEditViewModel.ItemPickerType.CreatedAt,
                searchText = viewModel.searchText.value,
                onSearchChange = {
                     viewModel.onSearchTextChange(it)
                },
                onClose = {
                    viewModel.onCloseItemPicker()
                    //isItemPickOpen = false
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
fun RecipeColumn(
    viewModel: ItemEditViewModel,
    recipeIndex : Int,
    recipe : ItemEditViewModel.RecipeViewModel
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp, top = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(R.Color.cardBackground)
            .padding(10.dp)
    ) {
        Text(text = "Recipe ${recipeIndex + 1}")
        if (viewModel.recipeList.size > 0) {
            OutlinedTextField(
                value = if (recipe.craftAmount == 0) "" else recipe.craftAmount.toString(),
                onValueChange = { value ->
                    viewModel.onCraftAmountChange(
                        recipeIndex = recipeIndex,
                        amount = value.filter {
                            it.isDigit()
                        }
                    )
                },
                label = { Text("Recipe Craft Amount") }
            )
        }
        recipe.ingredients.forEachIndexed { index, ingredientViewModel ->
            IngredientRow(
                viewModel = viewModel,
                recipeIndex = recipeIndex,
                ingredientIndex = index,
                ingredient = ingredientViewModel
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                viewModel.onOpenItemPicker(ItemEditViewModel.ItemPickerType.AddNewIngredient(recipeIndex))
            }) {
                Text("Add Ingredient")
            }
            Button(
                onClick = {
                    viewModel.onRecipeDelete(recipeIndex)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = R.Color.deleteButton)
            ) {
                Text("Delete Recipe")
            }
        }
    }
}

@Composable
fun IngredientRow(
    viewModel: ItemEditViewModel,
    recipeIndex : Int,
    ingredientIndex : Int,
    ingredient : ItemEditViewModel.IngredientViewModel
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(if(ingredientIndex.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            modifier = Modifier.width(200.dp).padding(top = 10.dp),
            onClick = {
                viewModel.onOpenItemPicker(ItemEditViewModel.ItemPickerType.UpdateIngredient(recipeIndex,ingredientIndex ))
            },
            border = BorderStroke(1.dp, Color.Red),
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemIcon(ingredient.item.iconPath)
                Text(
                    ingredient.item.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.padding(start = 10.dp),
            value = if(ingredient.amount == 0) "" else  ingredient.amount.toString(),
            onValueChange = { value ->
                viewModel.onIngredientAmountChange(
                    recipeIndex,
                    ingredientIndex,
                    value.filter { it.isDigit() }
                )
            },
            label = { Text("count") }
        )
        IconButton(content = {
            Icon(Icons.Outlined.Delete, "delete", tint = Color.Black)
        }, onClick = {
            viewModel.onIngredientItemDelete(
                recipeIndex,
                ingredientIndex
            )
        })
    }
}