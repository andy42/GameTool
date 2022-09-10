package com.jaehl.gametools.extensions

import java.io.File

//https://stackoverflow.com/questions/55608635/access-to-library-resource-in-jar-at-runtime

fun loadRes(filePath : String) : File {
   val contextClassLoader = Thread.currentThread().contextClassLoader!!
   val filePath = contextClassLoader.getResource(filePath)
   return File(filePath.path)
    //return File("src/main/resources/$filePath")
}