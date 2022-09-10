package com.jaehl.gametools.data.mock

import com.jaehl.gametools.data.model.Item
import com.jaehl.gametools.data.model.ItemCategory

class ItemsMock {
    companion object {
        var items = ArrayList<Item>()

        init {
            items.addAll(listOf<Item>(
                Item(
                    id = "character",
                    name = "Character",
                    category = ItemCategory.Item,
                    techTier = 1,
                    allowsCrafting = true,
                    craftedAt = null,
                    iconPath = "images/character.png"
                ),
                Item(
                    id = "fiber",
                    name = "Fiber",
                    category = ItemCategory.Resources,
                    techTier = 1,
                    allowsCrafting = false,
                    craftedAt = "character",
                    iconPath = "images/fiber.png"
                ),
                Item(
                    id = "wood",
                    name = "Wood",
                    category = ItemCategory.Resources,
                    techTier = 1,
                    allowsCrafting = false,
                    craftedAt = null,
                    iconPath = "images/wood.png"
                ),
                Item(
                    id = "leather",
                    name = "Leather",
                    category = ItemCategory.Resources,
                    techTier = 1,
                    allowsCrafting = false,
                    craftedAt = null,
                    iconPath = "images/leather.png"
                ),
                Item(
                    id = "stone",
                    name = "Stone",
                    category = ItemCategory.Resources,
                    techTier = 1,
                    allowsCrafting = false,
                    craftedAt = null,
                    iconPath = "images/stone.png"
                ),
                Item(
                    id = "stick",
                    name = "Stick",
                    category = ItemCategory.Resources,
                    techTier = 1,
                    allowsCrafting = false,
                    craftedAt = null,
                    iconPath = "images/stick.png"
                ),
                Item(
                    id = "campfire",
                    name = "Campfire",
                    category = ItemCategory.Deployable,
                    techTier = 1,
                    allowsCrafting = true,
                    craftedAt = null,
                    iconPath = "images/campfire.png"
                )
            ))
        }
    }
}