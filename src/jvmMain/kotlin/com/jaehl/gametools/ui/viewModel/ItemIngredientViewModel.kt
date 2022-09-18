package com.jaehl.gametools.ui.viewModel

import com.jaehl.gametools.data.model.Item
import java.lang.ref.WeakReference

data class ItemIngredientViewModel(
    var parentItem : WeakReference<ItemIngredientViewModel?>,
    var item : Item,
    var ingredientAmount : Int,
    var itemCost : ArrayList<ItemIngredientViewModel> = arrayListOf(),
    var alternativeRecipe : Boolean = false
)