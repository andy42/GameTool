package com.jaehl.gametools.data.local

import java.io.File
import java.nio.file.Paths
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

class LocalFiles {
    companion object {
        private val projectUserDir = "gameTools"
        fun getFile(fileName : String) : File{
            val directory = Paths.get(System.getProperty("user.home"), projectUserDir)
            if( !directory.exists()){
                directory.createDirectory()
            }
            return Paths.get(System.getProperty("user.home"), projectUserDir, fileName).toFile()
        }
    }
}