package com.jaehl.gametools.data.model

data class Item(
    val id : String = "",
    val name : String ="",
    val category : ItemCategory = ItemCategory.Item,
    val techTier : Int = 1,
    val allowsCrafting : Boolean = false,
    val craftedAt : String? = null,
    var iconPath : String? = null,
    val recipes : List<Recipe> = listOf()
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

    fun getRecipe(index : Int) : Recipe {
        if(index >= recipes.size || index < 0) return Recipe()
        return recipes[index]
    }
}

data class Recipe(
    val craftAmount : Int = 1,
    val ingredients : List<ItemIngredient> = listOf()
)

enum class ItemCategory(val value : String){
    All("All"),
    None("None"),
    Resources("Resources"),
    CraftedResources("Crafted Resources"),
    Deployable("Deployables"),
    Item("Item"),
    Armor("Armor"),
    Building("Building"),
    Tool("Tool"),
    Weapon("Weapon"),
    Ammo("Ammo"),
    Consumable("Consumable");

    override fun toString(): String {
        return value
    }

    companion object {
        fun from(value: String): ItemCategory {
            return values().find { value == it.value } ?: Item
        }
    }
}