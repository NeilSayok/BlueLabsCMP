package com.neilsayok.bluelabs.pages.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.data.bloglist.BlogLoadedFields
import com.neilsayok.bluelabs.pages.home.component.HomeComponent
import com.neilsayok.bluelabs.pages.home.widgets.HomeCard
import com.neilsayok.bluelabs.util.setMetaTag
import com.neilsayok.bluelabs.util.setOpenGraphTags
import com.neilsayok.bluelabs.util.setPageTitle
import com.neilsayok.bluelabs.util.setTwitterCardTags

@Composable
fun HomeScreen(component: HomeComponent) {
    val blogListState by component.blogState.subscribeAsState()
    val state = blogListState.sortedBy { it?.title?.stringValue }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        setPageTitle("Blue Labs : Home")

        setMetaTag(
            "description",
            "Blue Labs is a blog website where I share my thoughts on technology, programming, and other topics."
        )
        setMetaTag("viewport", "width=device-width, initial-scale=1.0")
        setMetaTag("author", " Sayok Dey Majumder")
        setMetaTag("robots", "index, follow")

        setOpenGraphTags(
            title = "Home : Blue Labs",
            description = "Blue Labs is a blog website where I share my thoughts on technology, programming, and other topics.",
            url = BuildKonfig.BASE_URL,
            image = "${BuildKonfig.BASE_URL}/meta_images/og-bluelabs.png",
            type = "website"
        )

        setTwitterCardTags(
            title = "Home : Blue Labs",
            description = "Blue Labs is a blog website where I share my thoughts on technology, programming, and other topics.",
            image = "${BuildKonfig.BASE_URL}/meta_images/og-bluelabs.png",
        )
    }




    LoaderScaffold { paddingValues ->

        val columnCount = component.columnCount
        val containerPadding = component.containerPadding

        LazyVerticalGrid(
            modifier = Modifier.padding(horizontal = containerPadding),
            columns = GridCells.Fixed(columnCount),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = paddingValues
        ) {
            items(columnCount) {
                Spacer(modifier = Modifier.height(containerPadding))
            }

            items(state) {
                HomeCard(blog = it) { blog ->
                    component.navigateToBlogScreen(blog)
                }
            }
            items(columnCount) {
                Spacer(modifier = Modifier.height(containerPadding))
            }

        }


    }
}