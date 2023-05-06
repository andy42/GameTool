package com.jaehl.gametools.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.viewModel.ItemIngredientViewModel

private fun backgroundColor(color : Color, depth : Int, index : Int) : Color {
    if(depth != 0){
        return color
    }
    return if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd
}

@Composable
fun IngredientList(modifier: Modifier, collapseList : Boolean, recipeList : List<ItemIngredientViewModel>, depth : Int = 0, background : Color = Color.White, onRecipeChange : ((item : Item) -> Unit)? = null) {
    Column(
        modifier = modifier.background(background)
    ) {
        recipeList.forEachIndexed { index, recipe ->
            IngredientItem(recipe, depth, backgroundColor(background, depth, index))
            if(!collapseList) {
                IngredientList(
                    Modifier,
                    collapseList,
                    recipe.itemCost,
                    depth + 1,
                    backgroundColor(background, depth, index),
                    onRecipeChange = onRecipeChange
                )
            }
        }
    }
}

@Composable
private fun IngredientItem(recipe : ItemIngredientViewModel, depth : Int, background : Color, onRecipeChange : ((item : Item) -> Unit)? = null) {
    Row (
        modifier = Modifier
            //.padding(start = 30.dp*depth),
            .background(background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..depth){
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(R.Color.recipeCircle)
                )
            }
        }
        Text(
            recipe.ingredientAmount.toString(),
            modifier = Modifier
                .width(40.dp)
                .padding(start = 10.dp),
            textAlign = TextAlign.Center

        )
        ItemIcon(
            recipe.item.iconPath,
            modifier = Modifier
                .background(if(recipe.alternativeRecipe) R.Color.debugGreen else R.Color.transparent)
                .clickable {
                    if(recipe.alternativeRecipe){
                        onRecipeChange?.invoke(recipe.item)
                    }
                }
        )
        Text(
            recipe.item.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        )
    }
}