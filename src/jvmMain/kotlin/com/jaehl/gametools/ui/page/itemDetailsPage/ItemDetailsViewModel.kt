package com.jaehl.gametools.ui.page.itemDetailsPage

import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.ui.util.ItemRecipeInverter
import com.jaehl.gametools.ui.viewModel.ItemRecipeViewModel
import com.jaehl.gametools.util.Log
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.math.ceil

class ItemDetailsViewModel(val itemRepo : ItemRepo)  : ViewModel() {

    var item = mutableStateOf<Item>(Item.blankItem())
    var recipe = mutableStateOf<List<ItemRecipeViewModel>>(listOf())
    var baseRecipe = mutableStateOf<List<ItemRecipeViewModel>>(listOf())

    fun init(viewModelScope: CoroutineScope, initItem : Item) {
        super.init(viewModelScope)

        viewModelScope.launch {
            try {
                val tempItem = itemRepo.getItem(initItem.id)
                item.value = tempItem ?: Item.blankItem()

                val tempRecipe= buildItemRecipe(tempItem ?: Item.blankItem(), 1)

                recipe.value = tempRecipe
                baseRecipe.value = ItemRecipeInverter.invertItemRecipe(tempRecipe)

            } catch (t: Throwable){
                Log.e("test", t.message)
            }
        }
    }

    private suspend fun buildItemRecipe(item : Item, count : Int = 1, root : ItemRecipeViewModel? = null) : ArrayList<ItemRecipeViewModel>{
        var recipelist = arrayListOf<ItemRecipeViewModel>()
        item.recipe.forEach {itemRecipe ->
            val tempItem = itemRepo.getItem(itemRecipe.itemId)
            if (tempItem != null) {

                val temp = ItemRecipeViewModel(
                    WeakReference( root),
                    tempItem,
                    ingredientAmount= itemRecipe.amount*count
                )
                temp.itemCost = buildItemRecipe(
                    tempItem,
                    ceil(itemRecipe.amount*count/tempItem.recipeCraftAmount.toDouble()).toInt(),
                    temp
                    )
                recipelist.add(temp)
            }
        }
        return recipelist
    }
}
