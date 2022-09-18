package com.jaehl.gametools.ui.page.itemEditPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import com.jaehl.gametools.data.model.ItemIngredient
import com.jaehl.gametools.data.model.Recipe
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect

class ItemEditViewModel(private val itemRepo : ItemRepo) {

    private var editingItem : Item? = null
    var pageTitle = mutableStateOf("")

    var pickerItems = mutableStateListOf<Item>()
    var pickerItemType = mutableStateOf<ItemPickerType>(ItemPickerType.CreatedAt)
    var closePage = mutableStateOf(false)

    var category = mutableStateOf(ItemCategory.Item)

    var recipeList = mutableStateListOf<RecipeViewModel>()

    var isItemPickOpen = mutableStateOf(false)

    var craftedAtItem : Item? = null

    private val _craftedAt = MutableStateFlow("")
    val craftedAt: StateFlow<String> = _craftedAt.asStateFlow()

    val searchText = mutableStateOf("")

    private var itemPickerType : ItemPickerType = ItemPickerType.CreatedAt

    fun setup(item : Item?){
        editingItem = item
        pageTitle.value = if (item != null) "Edit Item" else "New Item"

        GlobalScope.async {

            category.value = item?.category ?: ItemCategory.Item

            craftedAtItem = itemRepo.getItem(item?.craftedAt ?: null)
            _craftedAt.tryEmit(craftedAtItem?.name ?: "")
            val recipes = item?.recipes?.mapNotNull { recipe ->
                val ingredients = recipe.ingredients.mapNotNull { ingredient ->
                    val item = itemRepo.getItem(ingredient.itemId) ?: return@mapNotNull null
                    IngredientViewModel(
                        item,
                        ingredient.amount
                    )
                }
                RecipeViewModel(
                    name = "",
                    ingredients = ArrayList(ingredients),
                    craftAmount = recipe.craftAmount
                )
            } ?: listOf()

            recipeList.postSwap(recipes)
        }
    }

    private fun isInt(string : String) : Boolean{
        return try{
            string.toInt()
            true
        } catch (e : java.lang.NumberFormatException){
            false
        }
    }

    fun setCraftedAt(item : Item?){
        craftedAtItem = item
        _craftedAt.tryEmit(item?.name ?: "")
    }

    private fun toInt(value : String) : Int? {
        return try {
            value.toInt()
        } catch (t: Throwable){
            null
        }
    }

    fun onCraftAmountChange(recipeIndex : Int, amount : String){
        GlobalScope.async {
            var amountInt = 0
            if(amount.isNotEmpty()){
                amountInt = toInt(amount) ?: return@async
            }
            val temp = recipeList.toList()
            temp[recipeIndex].craftAmount = amountInt
            recipeList.postSwap(temp)
        }
    }
    fun onIngredientAmountChange(recipeIndex : Int, ingredientIndex : Int, amount : String){
        GlobalScope.async {

            var amountInt = 0
            if(amount.isNotEmpty()){
                amountInt = toInt(amount) ?: return@async
            }

            val temp = recipeList.toList()
            temp[recipeIndex].ingredients[ingredientIndex].amount = amountInt
            recipeList.postSwap(temp)
        }
    }

    fun onIngredientItemDelete(recipeIndex : Int, ingredientIndex : Int){
        GlobalScope.async {
            val temp = recipeList.toMutableList()
            temp[recipeIndex].ingredients.removeAt(ingredientIndex)
            recipeList.postSwap(temp)
        }
    }

    fun onRecipeAdd(){
        GlobalScope.async {
            val temp = recipeList.toMutableList()
            temp.add(
                RecipeViewModel(
                    name = "",
                    craftAmount = 1,
                    ingredients = arrayListOf()
                )
            )
            recipeList.postSwap(temp)
        }
    }

    fun onRecipeDelete(recipeIndex : Int){
        GlobalScope.async {
            val temp = recipeList.toMutableList()
            temp.removeAt(recipeIndex)
            recipeList.postSwap(temp)
        }
    }

    fun onItemPickerItemClick(item : Item?){
        GlobalScope.async {
            onCloseItemPicker()
            when(val itemPickerType = this@ItemEditViewModel.itemPickerType){
                is ItemPickerType.CreatedAt -> {
                    setCraftedAt(item)
                }
                is ItemPickerType.AddNewIngredient -> {
                    if(item != null) {
                        val temp = recipeList.toMutableList()
                        temp[itemPickerType.recipeIndex].ingredients.add(
                            IngredientViewModel(
                                item = item,
                                amount = 1
                            )
                        )

                        recipeList.postSwap(temp)
                    }
                }
                is ItemPickerType.UpdateIngredient -> {
                    if(item != null) {
                        val temp = recipeList.toMutableList()
                        val tempIngredient = temp[itemPickerType.recipeIndex].ingredients[itemPickerType.ingredientIndex]
                        temp[itemPickerType.recipeIndex].ingredients[itemPickerType.ingredientIndex] =
                            IngredientViewModel(item = item, amount = tempIngredient.amount)
                        recipeList.postSwap(temp)
                    }
                }
            }
        }
    }

    fun onItemCategoryClick(value : ItemCategory){
        category.value = value
    }

    private fun isRecipeItem(item : Item) : Boolean{
        return (
                item.category == ItemCategory.Resources
                        || item.category == ItemCategory.Item
                        || item.category == ItemCategory.CraftedResources
                        ||item.category == ItemCategory.Ammo)
    }

    private fun updatePickerItems(){
        GlobalScope.async {
            itemRepo.getItems().collect { items ->
                when(itemPickerType){
                    is ItemPickerType.CreatedAt -> {
                        pickerItems.postSwap(items.filter { it.allowsCrafting }.filter {
                            it.name.contains(searchText.value, ignoreCase = true)
                        })
                    }
                    is ItemPickerType.AddNewIngredient -> {
                        pickerItems.postSwap(items.filter { isRecipeItem(it) }.filter {
                            it.name.contains(searchText.value, ignoreCase = true)
                        })
                    }
                    is ItemPickerType.UpdateIngredient -> {
                        pickerItems.postSwap(items.filter { isRecipeItem(it) }.filter {
                            it.name.contains(searchText.value, ignoreCase = true)
                        })
                    }
                }
            }
        }
    }

    fun onSearchTextChange(search : String){
        searchText.value = search
        updatePickerItems()
    }

    fun onOpenItemPicker(type : ItemPickerType){
        itemPickerType = type
        pickerItemType.value = type
        updatePickerItems()
        isItemPickOpen.value = true
    }

    fun onCloseItemPicker(){
        isItemPickOpen.value = false
    }

    fun save(
        id : String,
        name : String,
        techTier : String,
        allowsCrafting : Boolean,
        recipeCraftAmount : String?,
        iconPath : String
    ) {
        if(!isInt(techTier)){
            return
        }

        val item = Item(
            id = id,
            name = name,
            category = category.value,
            techTier = if(techTier.isNullOrBlank()) 1 else techTier.toInt(),
            allowsCrafting = allowsCrafting,
            craftedAt = craftedAtItem?.id,
            iconPath = "images/${id}.png", //iconPath.ifEmpty { null },
            recipes = recipeList.mapNotNull { recipe ->
                if(recipe.ingredients.isEmpty()) return@mapNotNull null

                Recipe(
                    recipe.craftAmount,
                    ingredients = recipe.ingredients.map { ingredient ->
                        ItemIngredient(
                            itemId = ingredient.item.id,
                            amount = ingredient.amount
                        )
                    }
                )
            }
        )
        itemRepo.updateItem(item)
        closePage.value = true
    }

    sealed class ItemPickerType {
        object CreatedAt : ItemPickerType()
        data class AddNewIngredient(val recipeIndex : Int) : ItemPickerType()
        data class UpdateIngredient(val recipeIndex : Int, val ingredientIndex : Int) : ItemPickerType()
    }

    data class RecipeViewModel(
        var name : String = "",
        var craftAmount : Int = 1,
        var ingredients : ArrayList<IngredientViewModel> = arrayListOf()
    )

    data class IngredientViewModel(
        var item : Item,
        var amount : Int
    )
}