package com.jaehl.gametools.ui.page.craftingListsPage

import androidx.compose.runtime.mutableStateListOf
import com.jaehl.gametools.data.model.CraftingList
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.CraftingListRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CraftingListsViewModel(
    private val craftingListRepo : CraftingListRepo,
    private val game : Game
) : ViewModel() {

    var list = mutableStateListOf<CraftingListViewModel>()
        private set

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)

        viewModelScope.launch {
            craftingListRepo.getCraftingList().collect { craftingLists ->
                list.postSwap(craftingLists.map { CraftingListViewModel(it.id, it.name) })
            }
        }
    }

    fun removeCraftingList(id : String){
        viewModelScope.launch {
            craftingListRepo.removeItem(id)
            craftingListRepo.getCraftingList().collect { craftingLists ->
                list.postSwap(craftingLists.map { CraftingListViewModel(it.id, it.name) })
            }
        }
    }

    data class CraftingListViewModel(val id : String, val name : String)
}