// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.jaehl.gametools.ui.navigation.NavHostComponent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import java.io.File
import com.jaehl.gametools.extensions.loadRes
import java.util.jar.JarFile

/*
@Composable
@Preview
fun App() {
    MaterialTheme {
        remember {
            DefaultComponentContext(
                LifecycleRegistry()
            ).let(::NavHostComponent)
        }
            .render()
    }
}
*/

fun main() = application {
    val windowState = rememberWindowState(width = 720.dp, height = 800.dp)

    Window(onCloseRequest = ::exitApplication, state = windowState) {
        //val dirURL = loadRes("images")
        //val jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"))
        //Users/ajmills/IdeaProjects/GameTools/build/libs/GameTools-jvm-1.0-SNAPSHOT.jar

//        val jarPath = dirURL.getPath()
//
//        val file = File(jarPath)
//        System.out.println(file.path)
//        if(file.exists()){
//            System.out.println("image exists")
//        } else {
//            System.out.println("image does not exist")
//        }
        MaterialTheme {
            remember {
                NavHostComponent(
                    DefaultComponentContext(
                    LifecycleRegistry(),
                    )
                )
            }.render()
        }
    }
}
