package com.jaehl.gametools.data.mock

import com.jaehl.gametools.data.model.CraftingList
import com.jaehl.gametools.data.model.ItemIngredient

class CraftingListsMock {
    companion object {
        var craftingLists = ArrayList<CraftingList>()

        init {
            craftingLists.addAll(
                listOf(
                    CraftingList(
                        id = "0",
                        name = "test",
                        sectionList = arrayListOf(
                            CraftingList.Section(
                                id = "0",
                                name = "Section1",
                                itemList = arrayListOf(
                                    ItemIngredient(
                                        itemId = "Oxite_Dissolver",
                                        amount = 1
                                    ),
                                    ItemIngredient(
                                        itemId = "Crafting_Bench",
                                        amount = 1
                                    ),
                                    ItemIngredient(
                                        itemId = "Stone_Furnace",
                                        amount = 1
                                    ),
                                    ItemIngredient(
                                        itemId = "Anvil_Bench",
                                        amount = 1
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}