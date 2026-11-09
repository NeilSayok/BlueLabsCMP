package com.neilsayok.bluelabs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.common.ui.appbar.MainAppBar
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.data.bloglist.FirebaseResponse
import com.neilsayok.bluelabs.data.documents.BlogFields
import com.neilsayok.bluelabs.domain.util.Response
import com.neilsayok.bluelabs.navigation.RootComponent
import com.neilsayok.bluelabs.pages.blog.screen.BlogScreen
import com.neilsayok.bluelabs.pages.editor.screen.EditorScreen
import com.neilsayok.bluelabs.pages.pagenotfound.screen.PageNotFoundScreen
import com.neilsayok.bluelabs.pages.home.screen.HomeScreen
import com.neilsayok.bluelabs.pages.indexer.screen.IndexerScreen
import com.neilsayok.bluelabs.pages.me.screen.MeScreen
import com.neilsayok.bluelabs.pages.portfolio.screen.PortfolioScreen
import com.neilsayok.bluelabs.pages.privacy.screen.PrivacyPolicyScreen
import com.neilsayok.bluelabs.pages.search.screen.SearchScreen
import com.neilsayok.bluelabs.theme.BlueLabsTheme
import com.neilsayok.bluelabs.util.ObserverAsEvents
import com.neilsayok.bluelabs.util.SnackBarController
import com.neilsayok.bluelabs.util.SnackBarEvent
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kodein.emoji.compose.EmojiService


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App(root: RootComponent) {

    remember { EmojiService.initialize() } //(1)


    val systemTheme = isSystemInDarkTheme()

    var isDark by remember {
        mutableStateOf(
            if (Settings().keys.contains("isDark")) {
                Settings().getBoolean("isDark", false)
            } else {
                systemTheme
            }
        )
    }


    val blogListState: Response<FirebaseResponse<BlogFields>> by root.blogListState.subscribeAsState()
    val authorListState by root.authorListState.subscribeAsState()
    val indexListState by root.indexListState.subscribeAsState()
    val profileListState by root.profileListState.subscribeAsState()
    val genreListState by root.genreListState.subscribeAsState()

    val currentScreen by root.currentScreen.subscribeAsState()


    val isLoading = blogListState.isLoading()
            || authorListState.isLoading()
            || indexListState.isLoading()
            || profileListState.isLoading()
            || genreListState.isLoading()

    if (!isLoading)
        root.mergeData()

    val state = blogListState
    if (state is Response.SuccessResponse) {
        state.data
    }
    root.observeDestinationChanges()

    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    ObserverAsEvents(flow = SnackBarController.event, snackBarState) { event ->
        scope.launch {
            snackBarState.currentSnackbarData?.dismiss()

            val result = snackBarState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed){
                event.action?.action?.invoke()
            }
        }
    }
    BlueLabsTheme(darkTheme = isDark) {
        LoaderScaffold(
            isLoading = isLoading && currentScreen != RootComponent.Configuration.MeScreen,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarState,
                )
            },
            topBar = {
                MainAppBar(
                    isDark = isDark,
                    navigate = { event -> root.onNavigationEvent(event) },
                    currentScreen = currentScreen,
                ) {
                    //delay(400)
                    Settings().putBoolean("isDark", !isDark)
                    isDark = !isDark
                }
            }) { padding ->

            SelectionContainer {
                Children(
                    stack = root.stack,
                    modifier = Modifier.padding(padding),
                    animation = stackAnimation(slide()),
                ) { child ->
                    when (val instance = child.instance) {
                        is RootComponent.Child.Home -> HomeScreen(instance.component)
                        is RootComponent.Child.Blog -> BlogScreen(instance.component)
                        is RootComponent.Child.Editor -> EditorScreen(instance.component)
                        is RootComponent.Child.Indexer -> IndexerScreen(instance.component)
                        is RootComponent.Child.Portfolio -> PortfolioScreen(instance.component)
                        is RootComponent.Child.Me -> MeScreen(instance.component)
                        is RootComponent.Child.PrivacyPolicy -> PrivacyPolicyScreen(instance.component)
                        is RootComponent.Child.Search -> SearchScreen(instance.component)
                        is RootComponent.Child.PageNotFound -> PageNotFoundScreen(instance.component)
                    }
                }
            }
        }
    }
}

