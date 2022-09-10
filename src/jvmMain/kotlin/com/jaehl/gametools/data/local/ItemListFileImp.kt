package com.jaehl.gametools.data.local

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jaehl.gametools.data.model.Item
import java.io.File
import java.nio.file.Paths

class ItemListFileImp : ItemListFile {
    override fun load(fileName: String): List<Item> {
        val file = LocalFiles.getFile(fileName)
        if(!file.exists()) {
            println("ERROR : ItemListFileImp does not exist\n${file.absoluteFile} ")
            return listOf()
        }
        val gson = Gson().newBuilder().create()
        val fileString = file.inputStream().readBytes().toString(Charsets.UTF_8)
        return gson.fromJson<List<Item>>(fileString, object : TypeToken<List<Item>>() {}.type)
    }

    override fun save(fileName: String, items: List<Item>): Boolean {
        return try {
            val file = LocalFiles.getFile(fileName)
            println("file : ${file.absoluteFile}")
            file.createNewFile()

            var gson = GsonBuilder().setPrettyPrinting().create()
            var jsonString = gson.toJson(items)
            file.writeText(jsonString, Charsets.UTF_8)
            true
        } catch (t : Throwable){
            false
        }
    }
}