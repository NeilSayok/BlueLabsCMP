package com.neilsayok.bluelabs.pages.home.component

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.neilsayok.bluelabs.data.bloglist.BlogLoadedFields
import com.neilsayok.bluelabs.util.layoutType
import org.koin.core.component.KoinComponent

class HomeComponent(
    componentContext: ComponentContext,
    private val navigateBlogScreen: (BlogLoadedFields) -> Unit,
    val blogState: Value<List<BlogLoadedFields?>>
) : ComponentContext by componentContext, KoinComponent {


    val containerPadding: Dp
        @Composable get() = when (layoutType) {
            NavigationSuiteType.NavigationBar -> 16.dp
            NavigationSuiteType.NavigationRail,
            NavigationSuiteType.NavigationDrawer -> 64.dp

            else -> 0.dp
        }

    val columnCount: Int
        @Composable get() = when (layoutType) {
            NavigationSuiteType.NavigationBar -> 1
            NavigationSuiteType.NavigationRail,
            NavigationSuiteType.NavigationDrawer -> 4

            else -> 0
        }


    fun navigateToBlogScreen(blog: BlogLoadedFields?) {
        blog?.let {
            navigateBlogScreen(blog)
        }
    }
}

sealed interface HomeScreenEvent {
    data object ClickButton : HomeScreenEvent
}