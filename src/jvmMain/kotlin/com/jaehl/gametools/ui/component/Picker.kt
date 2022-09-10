package com.jaehl.gametools.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Picker(
    title : String,
    value : String,
    onClick : () -> Unit
) {
    Box {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(title) }
        )
        Box(modifier = Modifier
            .matchParentSize()
            .clickable {
                onClick()
            })
    }
}