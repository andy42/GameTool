package com.jaehl.gametools.ui.page.gameListPage

import androidx.compose.runtime.mutableStateListOf
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.repo.CraftingListRepo
import com.jaehl.gametools.data.repo.GamesRepo
import com.jaehl.gametools.data.repo.ItemRepo
import com.jaehl.gametools.extensions.postSwap
import com.jaehl.gametools.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GameListViewModel(
    private val gamesRepo : GamesRepo,
    private val itemRepo : ItemRepo,
    private val craftingListRepo : CraftingListRepo,
    private val onSelectedGameClick : (game : Game) -> Unit
) : ViewModel() {

    var games = mutableStateListOf<Game>()
        private set

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)

        viewModelScope.launch {
            gamesRepo.getGames().collect { games ->
                this@GameListViewModel.games.postSwap(games)
            }
        }
    }

    fun onGameClick(game : Game) {
        GlobalScope.async {
            itemRepo.setGame(game)
            craftingListRepo.setGame(game)
            onSelectedGameClick(game)
        }
    }

    fun removeCraftingList(id : String){
        viewModelScope.launch {
            gamesRepo.removeItem(id)
            gamesRepo.getGames().collect { games ->
                this@GameListViewModel.games.postSwap(games)
            }
        }
    }
}