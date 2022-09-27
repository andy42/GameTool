package com.jaehl.gametools.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.AppColor
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsViewModel
import com.jaehl.gametools.ui.viewModel.RecipePickerItemViewModel


@Composable
fun RecipePickDialog(
    title: String,
    recipeList : List<RecipePickerItemViewModel>,
    onItemClick: (Item?) -> Unit,
    onClose : () -> Unit
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .clickable {}
        .background(AppColor.dialogBackground)) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .fillMaxHeight()
                .padding(top = 20.dp, bottom = 20.dp)
                .align(Alignment.Center)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogTitleBar(
                title = title,
                onClose = onClose
            )
            LazyColumn {
                itemsIndexed(recipeList) { index, item ->
                    RecipePickerRow(index, item, onItemClick)
                }
            }
        }
    }
}

@Composable
fun RecipePickerRow(
    recipeIndex : Int,
    recipe : RecipePickerItemViewModel,
    onItemClick: (Item) -> Unit
){
//    Row (
//        modifier = Modifier
//            .clickable {  onItemClick(item) }
//            .background(if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        ItemIcon(item.iconPath)
//        Text(
//            item.name,
//            modifier = Modifier
//                .weight(1f)
//                .padding(start = 10.dp)
//        )
//    }
}