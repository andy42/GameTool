package com.jaehl.gametools.ui.viewModel

import com.jaehl.gametools.data.model.Item
import java.lang.ref.WeakReference

data class ItemRecipeViewModel(
    var parentItem : WeakReference<ItemRecipeViewModel?>,
    var item : Item,
    var ingredientAmount : Int,
    var itemCost : ArrayList<ItemRecipeViewModel> = arrayListOf()
)