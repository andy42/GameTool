package com.jaehl.gametools.data.model

data class Game(
    var id : String = "",
    val name : String = ""
) {
    fun getDirectory() : String {
        return id
    }
}