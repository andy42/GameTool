package com.jaehl.gametools.data.local

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jaehl.gametools.data.model.CraftingList
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory

class CraftingListsFileImp : CraftingListsFile {
    override fun load(fileName: String): List<CraftingList> {
        val file = LocalFiles.getFile(fileName)
        if(!file.exists()) {
            println("ERROR : loadCraftingLists does not exist\n${file.absoluteFile} ")
            return listOf()
        }
        val gson = Gson().newBuilder().create()
        val fileString = file.inputStream().readBytes().toString(Charsets.UTF_8)
        return gson.fromJson<List<CraftingList>>(fileString, object : TypeToken<List<CraftingList>>() {}.type)
    }

    override fun save(fileName: String, craftingLists: List<CraftingList>): Boolean {
        return try {
            val file = LocalFiles.getFile(fileName)
            println("file : ${file.absoluteFile}")
            file.createNewFile()

            var gson = GsonBuilder().setPrettyPrinting().create()
            var jsonString = gson.toJson(craftingLists)
            file.writeText(jsonString, Charsets.UTF_8)
            true
        } catch (t : Throwable){
            false
        }
    }
}