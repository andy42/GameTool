package com.jaehl.gametools.data.model

data class Item(
    val id : String = "",
    val name : String ="",
    val category : ItemCategory = ItemCategory.Item,
    val techTier : Int = 1,
    val allowsCrafting : Boolean = false,
    val craftedAt : String? = null,
    val iconPath : String? = null,
    var recipeCraftAmount : Int = 1,
    val recipe : List<ItemIngredient> = listOf()
){
    companion object{
        fun blankItem() : Item {
            return Item(
                id = "",
                name = "",
                category = ItemCategory.Item,
                techTier = 1,
                allowsCrafting = false,
                craftedAt = null,
                iconPath = ""
            )
        }
    }
}

enum class ItemCategory(val value : String){
    All("All"),
    Resources("Resources"),
    CraftedResources("Crafted Resources"),
    Deployable("Deployables"),
    Item("Item"),
    Armor("Armor"),
    Building("Building"),
    Tool("Tool"),
    Weapon("Weapon"),
    Ammo("Ammo");

    override fun toString(): String {
        return value
    }

    companion object {
        fun from(value: String): ItemCategory {
            return values().find { value == it.value } ?: Item
        }
    }
}