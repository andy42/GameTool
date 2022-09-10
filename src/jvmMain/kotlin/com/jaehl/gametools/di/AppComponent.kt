package com.jaehl.gametools.di

import com.jaehl.gametools.ui.page.itemDetailsPage.ItemDetailsPageComponent
import com.jaehl.gametools.ui.page.itemEditPage.ItemEditPageComponent
import com.jaehl.gametools.ui.page.itemsListPage.ItemListPageComponent


interface AppComponent {
    fun inject(itemDetailsPageComponent: ItemDetailsPageComponent)
    fun inject(itemEditPageComponent: ItemEditPageComponent)
    fun inject(itemListPageComponent: ItemListPageComponent)
}