package com.jaehl.gametools.ui.page.itemEditPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import com.jaehl.gametools.data.model.ItemIngredient
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.ui.viewModel.ItemRecipeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import java.lang.ref.WeakReference

class ItemEditViewModel(private val itemRepo : ItemRepo) {

    private var editingItem : Item? = null
    var pageTitle = mutableStateOf("")

    var pickerItems = mutableStateListOf<Item>()
    var pickerItemType = mutableStateOf<ItemPickerType>(ItemPickerType.CreatedAt)
    var closePage = mutableStateOf(false)

    var category = mutableStateOf(ItemCategory.Item)

    var itemRecipeList = mutableStateListOf<ItemRecipeViewModel>()

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

            val temp = item?.recipe?.map {
                val item = itemRepo.getItem(it.itemId)
                if (item != null){
                    ItemRecipeViewModel(item = item, ingredientAmount= it.amount, parentItem = WeakReference(null))
                } else null
            }?.filterNotNull() ?: listOf()

            itemRecipeList.postSwap(temp)
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

    fun onCraftingCountChange(index : Int, count : String){
        GlobalScope.async {

            var countInt = 0
            if(count.isNotEmpty()){
                countInt = toInt(count) ?: return@async
            }

            val temp = itemRecipeList.toList()
            itemRecipeList[index].ingredientAmount = countInt
            itemRecipeList.postSwap(temp)
        }
    }

    fun onCraftingItemDelete(index : Int){
        GlobalScope.async {
            val temp = itemRecipeList.toMutableList()
            temp.removeAt(index)
            itemRecipeList.postSwap(temp)
        }
    }

    fun onItemPickerItemClick(item : Item?){
        GlobalScope.async {
            when(val itemPickerType = this@ItemEditViewModel.itemPickerType){
                is ItemPickerType.CreatedAt -> {
                    setCraftedAt(item)
                }
                is ItemPickerType.AddNewRecipe -> {
                    if(item != null) {
                        val temp = itemRecipeList.toMutableList()
                        temp.add(ItemRecipeViewModel(item = item, ingredientAmount = 1, parentItem = WeakReference(null)))
                        itemRecipeList.postSwap(temp)
                    }
                }
                is ItemPickerType.UpdateRecipe -> {
                    if(item != null) {
                        val temp = itemRecipeList.toMutableList()
                        val tempRecipe = temp[itemPickerType.recipeIndex]
                        temp[itemPickerType.recipeIndex] = ItemRecipeViewModel(item = item, ingredientAmount= tempRecipe.ingredientAmount, parentItem = WeakReference(null))
                        itemRecipeList.postSwap(temp)
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
                    is ItemPickerType.AddNewRecipe -> {
                        pickerItems.postSwap(items.filter { isRecipeItem(it) }.filter {
                            it.name.contains(searchText.value, ignoreCase = true)
                        })
                    }
                    is ItemPickerType.UpdateRecipe -> {
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

    fun setItemPickerType(type : ItemPickerType){
        itemPickerType = type
        pickerItemType.value = type
        updatePickerItems()
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
            recipeCraftAmount = if(recipeCraftAmount.isNullOrBlank()) 1 else recipeCraftAmount?.toInt(),
            recipe = itemRecipeList.toList().map {
                ItemIngredient(
                    itemId = it.item.id,
                    amount = it.ingredientAmount)
            }
        )
        itemRepo.updateItem(item)
        closePage.value = true
    }

    sealed class ItemPickerType {
        object CreatedAt : ItemPickerType()
        object AddNewRecipe : ItemPickerType()
        data class UpdateRecipe(val recipeIndex : Int) : ItemPickerType()
    }
}