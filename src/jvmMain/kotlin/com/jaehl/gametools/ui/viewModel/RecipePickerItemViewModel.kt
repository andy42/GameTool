package com.jaehl.gametools.ui.viewModel

import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsViewModel

data class RecipePickerItemViewModel(
    var craftAmount : Int = 1,
    var ingredients : List<IngredientPickerItemViewModel> = listOf(),
)

data class IngredientPickerItemViewModel(
    val item : Item,
    val amount : Int
)