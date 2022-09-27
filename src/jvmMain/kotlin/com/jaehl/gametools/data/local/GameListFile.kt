package com.jaehl.gametools.data.local

import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.data.model.Item

interface GameListFile {
    fun load(fileName : String)  : List<Game>
    fun save(fileName : String, games : List<Game>) : Boolean
}