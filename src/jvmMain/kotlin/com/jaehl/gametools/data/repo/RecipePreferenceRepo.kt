package com.jaehl.gametools.data.repo

import com.jaehl.gametools.data.local.ObjectListLoader
import com.jaehl.gametools.data.model.RecipePreference
import com.jaehl.gametools.util.Logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RecipePreferenceRepo(
    private val logger: Logger,
    private val recipePreferenceListLoader : ObjectListLoader<RecipePreference>,
) {
    private var loaded = false

    private val recipePreferenceMap = LinkedHashMap<String, Int>()

    @Synchronized
    fun getRecipePreference(itemId : String) : Int {
        return recipePreferenceMap[itemId] ?: 0
    }

    @Synchronized
    fun updateRecipePreference(itemId : String, recipeIndex : Int)  {
        recipePreferenceMap[itemId] = recipeIndex
        save()
    }

    private fun save() = GlobalScope.async {
        recipePreferenceListLoader.save(
            recipePreferenceMap.map { entry: Map.Entry<String, Int> ->
                RecipePreference(entry.key, entry.value)
            }
        )
    }

    @Synchronized
    private fun loadLocal(forceReload : Boolean = false){
        if(loaded && !forceReload) return
        try {
            recipePreferenceMap.clear()
            recipePreferenceListLoader.load().forEach { recipePreference ->
                recipePreferenceMap[recipePreference.itemId] = recipePreference.recipeIndex
            }
        } catch (t : Throwable){
            logger.error("RecipePreferenceRepo ${t.message}")
        }
    }
}