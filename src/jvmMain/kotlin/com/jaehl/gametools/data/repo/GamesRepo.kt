package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.GameListFile
import com.jaehl.gametools.data.local.GameListFileImp
import com.jaehl.gametools.data.model.Game
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow

class GamesRepo (
    private val  gameListFile : GameListFile = GameListFileImp()
) {
    private val gameMap = LinkedHashMap<String, Game>()
    private var loaded = false
    private var games = MutableSharedFlow<List<Game>>(replay = 1)

    fun getGames() : SharedFlow<List<Game>> = games

    init {
        GlobalScope.async {
            loadLocal(true)
            games.tryEmit(gameMap.values.toList())
        }
    }

    private fun createTemp() : List<Game> {
        return listOf<Game>(
            Game(
                id = "icarus",
                name = "Icarus"
            ),
            Game(
                id = "dyson_sphere_program",
                name = "Dyson Sphere Program"
            ),
            Game(
                id = "tribes_of_midgard",
                name = "Tribes Of Midgard"
            )
        )
    }

    fun getGameAsync(id : String, forceReload : Boolean = false) = flow<Game?>{
        GlobalScope.async {
            loadLocal(forceReload)
            emit(gameMap[id])
        }
    }

    fun updateGame(gameList : Game){
        GlobalScope.async {
            gameMap[gameList.id] = gameList
            gameListFile.save(fileName, gameMap.values.toList())
            games.tryEmit(gameMap.values.toList())
        }
    }

    fun addGame(game : Game){
        GlobalScope.async {
            gameListFile.save(fileName, gameMap.values.toList())
            games.tryEmit(gameMap.values.toList())
        }
    }

    fun removeItem(id : String){
        if(gameMap.containsKey(id)) {
            gameMap.remove(id)
            gameListFile.save(fileName, gameMap.values.toList())
            games.tryEmit(gameMap.values.toList())
        }
    }

    suspend fun getCraftingList(id : String?) : Game? {
        if (id == null) return null
        return gameMap[id]
    }

    private fun loadLocal(forceReload : Boolean = false){
        if(loaded && !forceReload) return

        //gameListFile.save(fileName, createTemp())

        try {
            gameListFile.load(fileName).forEach {
                gameMap[it.id] = it
            }
        } catch (t : Throwable){
            System.out.println(t.message)
        }
    }

    companion object {
        private const val fileName = "games.json"
    }
}