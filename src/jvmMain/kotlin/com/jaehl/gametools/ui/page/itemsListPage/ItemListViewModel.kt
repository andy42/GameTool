package com.jaehl.gametools.ui.page.itemsListPage

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.jaehl.gametools.data.mock.ItemsMock
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class ItemListViewModel(val itemRepo : ItemRepo) : ViewModel() {
    var items = mutableStateListOf<Item>()
        private set

    val searchText = mutableStateOf("")
    val filterCategory = mutableStateOf(ItemCategory.All)

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)
        updateItems()
    }

    private fun updateItems(){
        viewModelScope.launch {
            itemRepo.getItems().collect { itemList ->
                items.postSwap(
                    itemList
                        .filter {
                            it.name.contains(searchText.value, ignoreCase = true)
                        }
                        .filter {
                            if(filterCategory.value == ItemCategory.All){
                                true
                            } else {
                                it.category == filterCategory.value
                            }

                        }
                )
            }
        }
    }

    fun onSearchTextChange(search : String){
        searchText.value = search
        updateItems()
    }

    fun onItemCategoryClick(category : ItemCategory){
        filterCategory.value = category
        updateItems()
    }
}