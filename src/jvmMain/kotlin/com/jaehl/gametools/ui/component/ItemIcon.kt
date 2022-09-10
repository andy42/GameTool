package com.jaehl.gametools.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.File
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.jaehl.gametools.data.local.LocalFiles

@Composable
fun ItemIcon(iconPath : String?, modifier : Modifier = Modifier, size : Dp = 40.dp){
    val file = LocalFiles.getFile(iconPath ?: "")
    if (file.exists() && !iconPath.isNullOrBlank()) {
        val imageBitmap: ImageBitmap = remember(file) {
            loadImageBitmap(file.inputStream())
        }
        Image(
            painter = BitmapPainter(image = imageBitmap),
            contentDescription = "",
            modifier = modifier.width(size).height(size)
        )
    } else {
        Box(modifier = modifier.width(size).height(size))
    }
}