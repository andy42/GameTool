package com.jaehl.gametools.data.local

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jaehl.gametools.data.model.Game

class GameListFileImp : GameListFile {
    override fun load(fileName: String): List<Game> {
        val file = LocalFiles.getFile(fileName)
        if(!file.exists()) {
            println("ERROR : GameListFileImp does not exist\n${file.absoluteFile} ")
            return listOf()
        }
        val gson = Gson().newBuilder().create()
        val fileString = file.inputStream().readBytes().toString(Charsets.UTF_8)
        return gson.fromJson<List<Game>>(fileString, object : TypeToken<List<Game>>() {}.type)
    }

    override fun save(fileName: String, games: List<Game>): Boolean {
        return try {
            val file = LocalFiles.getFile(fileName)
            println("file : ${file.absoluteFile}")
            file.createNewFile()

            var gson = GsonBuilder().setPrettyPrinting().create()
            var jsonString = gson.toJson(games)
            file.writeText(jsonString, Charsets.UTF_8)
            true
        } catch (t : Throwable){
            false
        }
    }
}