package com.jaehl.gametools.data.model

data class CraftingList(
    var id : String = "",
    var name : String = "",
    var sectionList : ArrayList<Section> = arrayListOf()
){
    data class Section(
        var id : String = "",
        var name : String = "",
        val itemList : ArrayList<ItemIngredient> = arrayListOf()
    )

    companion object {
        fun blankItem() : CraftingList {
            return CraftingList(
                id = "",
                name = "",
                sectionList = arrayListOf()
            )
        }
    }
}