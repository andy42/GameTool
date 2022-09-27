package com.jaehl.gametools.ui.page.itemDetailsPage

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.Recipe
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.ui.util.ItemRecipeInverter
import com.jaehl.gametools.ui.viewModel.IngredientPickerItemViewModel
import com.jaehl.gametools.ui.viewModel.ItemIngredientViewModel
import com.jaehl.gametools.ui.viewModel.RecipePickerItemViewModel
import com.jaehl.gametools.util.Log
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.math.ceil

class ItemDetailsViewModel(private val itemRepo : ItemRepo, private val game : Game)  : ViewModel() {

    var item = mutableStateOf<Item>(Item.blankItem())
    var recipes = mutableStateListOf<RecipeViewModel>()


    var recipePicker = mutableStateListOf<RecipePickerItemViewModel>()
    var recipePickerItem : Item? = null
    var isRecipePickerOpen = mutableStateOf(false)

    fun init(viewModelScope: CoroutineScope, initItem : Item) {
        super.init(viewModelScope)

        viewModelScope.launch {
            try {
                val tempItem = itemRepo.getItem(initItem.id) ?: Item.blankItem()
                item.value = tempItem

                val tempRecipes = tempItem.recipes.map { recipe ->
                    val ingredientsTemp = buildItemRecipe(recipe, 1)
                    RecipeViewModel(
                        craftAmount = recipe.craftAmount,
                        ingredients = ingredientsTemp,
                        baseIngredients = ItemRecipeInverter.invertItemRecipe(ingredientsTemp)
                    )
                }
                recipes.postSwap(tempRecipes)
            } catch (t: Throwable){
                Log.e("test", t.message)
            }
        }
    }

    fun onCollapseListToggle(recipeIndex : Int){
        viewModelScope.launch {
            var recipesTemp = recipes.toList()
            recipesTemp[recipeIndex].collapseList = !recipesTemp[recipeIndex].collapseList
            recipes.postSwap(recipesTemp)
        }
    }

    fun onShowBaseCraftingToggle(recipeIndex : Int){
        viewModelScope.launch {
            var recipesTemp = recipes.toList()
            recipesTemp[recipeIndex].showBaseCrafting = !recipesTemp[recipeIndex].showBaseCrafting
            recipes.postSwap(recipesTemp)
        }
    }

    private suspend fun buildItemRecipe(recipe : Recipe, count : Int = 1, root : ItemIngredientViewModel? = null) : ArrayList<ItemIngredientViewModel>{
        var recipelist = arrayListOf<ItemIngredientViewModel>()
        recipe.ingredients.forEach { ingredient ->
            val tempItem = itemRepo.getItem(ingredient.itemId)
            if (tempItem != null) {

                val temp = ItemIngredientViewModel(
                    WeakReference( root),
                    tempItem,
                    ingredientAmount= ingredient.amount*count,
                    alternativeRecipe = tempItem.recipes.size > 1
                )
                temp.itemCost = buildItemRecipe(
                    tempItem.getRecipe(0),
                    ceil(ingredient.amount*count/recipe.craftAmount.toDouble()).toInt(),
                    temp
                    )
                recipelist.add(temp)
            }
        }
        return recipelist
    }

    fun onAlternativeRecipeSelected(selectedRecipeIndex : Int){
        viewModelScope.launch {
            recipePickerItem = null
            isRecipePickerOpen.value = false
        }
    }
    fun onAlternativeRecipeClick(item : Item){
        viewModelScope.launch{
            recipePicker.postSwap(
                item.recipes.map { recipe ->
                    RecipePickerItemViewModel(
                        recipe.craftAmount,
                        recipe.ingredients.mapNotNull { ingredient ->
                            val tempItem = itemRepo.getItem(ingredient.itemId) ?: return@mapNotNull null
                            IngredientPickerItemViewModel(
                                item = tempItem,
                                amount = ingredient.amount
                            )
                        }
                    )
                }
            )
            recipePickerItem = item
            isRecipePickerOpen.value = true
        }
    }

    data class RecipeViewModel(
        var craftAmount : Int = 1,
        var ingredients : List<ItemIngredientViewModel> = listOf(),
        var baseIngredients : List<ItemIngredientViewModel> = listOf(),
        var collapseList : Boolean = true,
        var showBaseCrafting : Boolean = false
    )
}
