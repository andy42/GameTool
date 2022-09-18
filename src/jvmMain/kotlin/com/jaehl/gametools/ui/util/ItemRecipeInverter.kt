package com.jaehl.gametools.ui.util

import com.jaehl.gametools.ui.viewModel.ItemIngredientViewModel
import java.lang.ref.WeakReference

object ItemRecipeInverter {
    private fun invert(new : ItemIngredientViewModel, ref :ItemIngredientViewModel){
        val parentItem = ref.parentItem.get() ?: return

        val first = new.itemCost.firstOrNull{ it.item.id == parentItem.item.id}
        if(first == null){
            val newChild = parentItem.copy()
            newChild.itemCost = arrayListOf()
            newChild.parentItem = WeakReference(new)
            new.itemCost.add(newChild)
            invert(newChild, parentItem)
        } else {
            first.ingredientAmount += parentItem.ingredientAmount
            invert(first, parentItem)
        }
    }

    private fun walk(map : HashMap<String,ItemIngredientViewModel>, recipe : List<ItemIngredientViewModel>){
        recipe.forEach {
            if(it.itemCost.isEmpty()){
                if(map.containsKey(it.item.id)){
                    val t = map[it.item.id]!!
                    t.ingredientAmount += it.ingredientAmount
                    invert(t, it)
                } else {
                    val new = it.copy()
                    new.parentItem = WeakReference(null)
                    new.itemCost = arrayListOf()
                    map[it.item.id] = new
                    invert(new, it)
                }
            }
            else {
                walk(map, it.itemCost)
            }
        }
    }

    fun invertItemRecipe(recipe : List<ItemIngredientViewModel>) : List<ItemIngredientViewModel>{
        var baseMap = hashMapOf<String, ItemIngredientViewModel>()
        walk(baseMap, recipe)
        return baseMap.values.toList()
    }
}