package com.jaehl.gametools.ui.page.craftingListDetailsPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemIngredient
import com.jaehl.gametools.data.repo.CraftingListRepo
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.ui.util.ItemRecipeInverter
import com.jaehl.gametools.ui.viewModel.ItemIngredientViewModel
import com.jaehl.gametools.util.Log
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.math.ceil

class CraftingListDetailsViewModel(
    private val craftingListId : String,
    private val itemRepo : ItemRepo,
    private val craftingListRepo : CraftingListRepo
) : ViewModel() {

    var title = mutableStateOf("")
    var sections = mutableStateListOf<SectionViewModel>()
    private var sectionMap = LinkedHashMap<String, SectionViewModel>()

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)

        viewModelScope.launch {
            try {

                val tempCraftingList = craftingListRepo.getCraftingList(craftingListId)

                title.value =tempCraftingList?.name ?: ""

                val tempSections = tempCraftingList?.sectionList?.map { section ->
                    val items = section.itemList.mapNotNull { itemIngredient ->

                        val item = itemRepo.getItem(itemIngredient.itemId) ?: return@mapNotNull null
                        ItemViewModel(
                            item,
                            itemIngredient.amount
                        )

                    }
                    val ingredients = mergeItemRecipes(items)
                    val baseIngredients = ItemRecipeInverter.invertItemRecipe(ingredients)
                    SectionViewModel(
                        section.id,
                        section.name,
                        true,
                        false,
                        items,
                        ingredients,
                        baseIngredients
                    )
                } ?: listOf()

                tempSections.forEach { section ->
                    sectionMap[section.id] = section
                }

                sections.postSwap(sectionMap.values.toList())

            } catch (t: Throwable){
                Log.e("test", t.message)
            }
        }
    }

    private suspend fun mergeItemRecipes(items : List<ItemViewModel>) : List<ItemIngredientViewModel>{
        var recipeMap = HashMap<String, ItemIngredient>()
        items.forEach { item ->
            val recipe =item.item.getRecipe(0)
            recipe.ingredients.forEach {
                if(recipeMap.containsKey(it.itemId)){
                    recipeMap[it.itemId]?.amount = recipeMap[it.itemId]!!.amount + it.amount*item.amount
                } else {
                    recipeMap[it.itemId] = it.copy()
                    recipeMap[it.itemId]?.amount = it.amount*item.amount
                }
            }
        }
        return buildItemRecipe(recipeMap.values.toList(), 1)
    }

    private suspend fun buildItemRecipe(recipe : List<ItemIngredient>, count : Int = 1, root : ItemIngredientViewModel? = null) : ArrayList<ItemIngredientViewModel>{
        var recipelist = arrayListOf<ItemIngredientViewModel>()
        recipe.forEach {itemRecipe ->
            val tempItem = itemRepo.getItem(itemRecipe.itemId)
            if (tempItem != null) {
                val recipe =tempItem.getRecipe(0)
                val temp = ItemIngredientViewModel(
                    WeakReference(root),
                    tempItem,
                    ingredientAmount= itemRecipe.amount*count,
                    itemCost = buildItemRecipe(
                        recipe.ingredients,
                        ceil(itemRecipe.amount*count/recipe.craftAmount.toDouble()).toInt()
                    ),
                    alternativeRecipe = tempItem.recipes.size > 1
                )
                temp.itemCost = buildItemRecipe(
                    recipe.ingredients,
                    ceil(itemRecipe.amount*count/recipe.craftAmount.toDouble()).toInt(),
                    temp
                )
                recipelist.add(temp)
            }
        }
        return recipelist
    }

    fun collapseIngredientList(sectionId : String){
        viewModelScope.launch {
            val section = sectionMap[sectionId] ?: return@launch
            section.collapseIngredientList = !section.collapseIngredientList
            sections.postSwap(sectionMap.values.toList())
        }
    }

    fun showBaseCrafting(sectionId : String){
        viewModelScope.launch {
            val section = sectionMap[sectionId] ?: return@launch
            section.showBaseCrafting = !section.showBaseCrafting
            sections.postSwap(sectionMap.values.toList())
        }
    }

    data class SectionViewModel(
        val id : String,
        val name : String,
        var collapseIngredientList : Boolean,
        var showBaseCrafting : Boolean,
        val itemList : List<ItemViewModel>,
        val ingredient : List<ItemIngredientViewModel>,
        val baseIngredient : List<ItemIngredientViewModel>
    )

    data class ItemViewModel(
        val item : Item,
        val amount : Int
    )
}