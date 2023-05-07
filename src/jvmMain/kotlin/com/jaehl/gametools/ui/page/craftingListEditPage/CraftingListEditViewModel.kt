package com.jaehl.gametools.ui.page.craftingListEditPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.model.CraftingList
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemIngredient
import com.jaehl.gametools.data.repo.CraftingListRepo
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.ui.navigation.NavBackListener
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CraftingListEditViewModel @Inject constructor(
    private val itemRepo : ItemRepo,
    private val craftingListRepo : CraftingListRepo
) : ViewModel() {

    private var craftingListId : String? = null
    private var navBackListener : NavBackListener? = null

    var windowTitle = mutableStateOf("")
    var craftingListTitle = mutableStateOf("")
    var isItemPickOpen = mutableStateOf(false)
    var pickerItems = mutableStateListOf<Item>()
    val searchText = mutableStateOf("")

    private var sectionMap = LinkedHashMap<String, SectionViewModel>()

    var sections = mutableStateListOf<SectionViewModel>()

    fun init(viewModelScope: CoroutineScope, craftingListId : String?, navBackListener : NavBackListener) {
        super.init(viewModelScope)

        this.craftingListId = craftingListId
        this.navBackListener = navBackListener

        viewModelScope.launch {
            val tempCraftingList = craftingListRepo.getCraftingList(craftingListId)

            windowTitle.value = if(tempCraftingList == null) "New" else "Edit"
            craftingListTitle.value = tempCraftingList?.name ?: ""

            val tempSections = tempCraftingList?.sectionList?.map { section ->

                var itemMap = LinkedHashMap<String, ItemViewModel>()
                section.itemList.mapNotNull { itemIngredient ->
                    val item = itemRepo.getItem(itemIngredient.itemId) ?: return@mapNotNull null
                    ItemViewModel(
                        item,
                        itemIngredient.amount
                    )

                }.forEach {
                    itemMap[it.item.id] = it
                }

                SectionViewModel(
                    section.id,
                    section.name,
                    itemMap
                )
            } ?: listOf()
            tempSections.forEach { section ->
                sectionMap[section.id] = section
            }

            sections.postSwap(sectionMap.values.toList())
        }
    }

    private fun newSectionId() : String {
        var id = 0
        while (true){
            if(!sectionMap.containsKey(id.toString())){
                return id.toString()
            }
            id++
        }
    }

    fun addNewSection() {
        viewModelScope.launch {
            val id = newSectionId()
            sectionMap[id] = SectionViewModel(
                id = id,
                name = "",
                itemList = LinkedHashMap()
            )
            sections.postSwap(sectionMap.values.toList())
        }
    }

    fun removeSection(sectionId : String){
        viewModelScope.launch {
            sectionMap.remove(sectionId)
            sections.postSwap(sectionMap.values.toList())
        }
    }

    fun updateSectionName(id : String, name : String){
        viewModelScope.launch {
            sectionMap[id]?.name = name
            sections.postSwap(sectionMap.values.toList())
        }
    }

    fun updateItemAmount(sectionId : String, itemId: String, amount : String){
        viewModelScope.launch {
            if(amount.isEmpty()){
                sectionMap[sectionId]?.itemList?.get(itemId)?.amount = 0
            } else {
                sectionMap[sectionId]?.itemList?.get(itemId)?.amount = amount.toInt()
            }

            sections.postSwap(sectionMap.values.toList())
        }
    }

    fun addItem(sectionId : String, item: Item, amount : Int){
        viewModelScope.launch {
            val tempItem = sectionMap[sectionId]?.itemList?.get(item.id)
            if(tempItem == null){
                sectionMap[sectionId]?.itemList?.put(item.id, ItemViewModel(item, amount))
            }
            sections.postSwap(sectionMap.values.toList())
        }
    }

    fun removeItem(sectionId : String, item: Item){
        viewModelScope.launch {
            sectionMap[sectionId]?.itemList?.remove(item.id)
            sections.postSwap(sectionMap.values.toList())
        }
    }
    private var sectionIDItemPicker : String = ""

    private suspend fun updatePickerItems(){
        itemRepo.getItems().collect { items ->
            pickerItems.postSwap(
                items
                    .filter {
                        !(sectionMap[sectionIDItemPicker]?.itemList?.containsKey(it.id) ?: true)
                    }
                    .filter {
                        it.name.contains(searchText.value, ignoreCase = true)
                    }
            )
        }
    }

    fun openItemPicker(sectionID : String){
        sectionIDItemPicker = sectionID
        isItemPickOpen.value = true
        searchText.value = ""

        viewModelScope.async {
            updatePickerItems()
        }
    }

    fun onItemPickerItemClick(item : Item?) {

        viewModelScope.async {
            isItemPickOpen.value = false
            if(item == null) return@async

            addItem(sectionIDItemPicker, item, 1 )
        }
    }

    fun onItemPickerSearchChange(value : String){
        searchText.value = value
        viewModelScope.async {
            updatePickerItems()
        }
    }

    fun save(){
        viewModelScope.async {
            val temp = CraftingList(
                id = craftingListId ?: "",
                name = craftingListTitle.value,
                sectionList = ArrayList(sectionMap.values.toList().map { sectionViewModel ->
                    CraftingList.Section(
                        id = sectionViewModel.id,
                        name = sectionViewModel.name,
                        itemList = ArrayList(
                            sectionViewModel.itemList.values.toList().map { itemViewModel ->
                                ItemIngredient(
                                    itemId = itemViewModel.item.id,
                                    amount = itemViewModel.amount
                                )
                            }
                        )
                    )
                })
            )
            if(craftingListId == null){
                craftingListRepo.addItem(temp)
            } else {
                craftingListRepo.updateItem(temp)
            }
            navBackListener?.navigateBack()
        }
    }

    data class SectionViewModel(
        val id : String,
        var name : String,
        var itemList : LinkedHashMap<String, ItemViewModel>
    )

    data class ItemViewModel(
        var item : Item,
        var amount : Int
    )
}