package com.neilsayok.bluelabs

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.neilsayok.bluelabs.di.initKoin
import com.neilsayok.bluelabs.navigation.RootComponent

fun MainViewController() = ComposeUIViewController {
    initKoin()

    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    App(root)
}