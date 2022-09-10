package com.jaehl.gametools.data.local

import com.jaehl.gametools.data.model.Item

interface ItemListFile {
    fun load(fileName : String)  : List<Item>
    fun save(fileName : String, stories : List<Item>) : Boolean
}