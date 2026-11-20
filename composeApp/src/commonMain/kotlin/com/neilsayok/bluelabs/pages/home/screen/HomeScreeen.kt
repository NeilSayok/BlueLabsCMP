package com.neilsayok.bluelabs.pages.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.common.ui.EditorialSectionHeading
import com.neilsayok.bluelabs.common.ui.LocalEditorialNavigation
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.navigation.NavigationEvent
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
    val navigate = LocalEditorialNavigation.current

    LaunchedEffect(Unit) {
        setPageTitle("Blue Labs : Home")
        setMetaTag("description", "Blue Labs is a blog website where I share my thoughts on technology, programming, and other topics.")
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
        BoxWithConstraints(Modifier.fillMaxSize().padding(paddingValues)) {
            val compact = maxWidth < 700.dp
            val horizontal = if (compact) 20.dp else 64.dp
            LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Column(pageModifier(horizontal).padding(top = if (compact) 32.dp else 52.dp, bottom = 48.dp),
                        verticalArrangement = Arrangement.spacedBy(28.dp)) {
                        Text("Engineering,\nwritten down.", fontSize = if (compact) 42.sp else 92.sp,
                            lineHeight = if (compact) 43.sp else 92.sp, fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-3).sp)
                        if (compact) {
                            Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                                Text("Notes on Kotlin, Android, and the craft of building across platforms.",
                                    style = MaterialTheme.typography.bodyLarge)
                                Button(onClick = { navigate(NavigationEvent.NavigatePortfolio) }, shape = RectangleShape) {
                                    Text("Meet the developer")
                                    Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                                }
                            }
                        } else {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom) {
                                Text("Notes on Kotlin, Android, and the craft of building across platforms.",
                                    style = MaterialTheme.typography.bodyLarge, modifier = Modifier.widthIn(max = 500.dp))
                                Button(onClick = { navigate(NavigationEvent.NavigatePortfolio) }, shape = RectangleShape) {
                                    Text("Meet the developer")
                                    Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                    }
                }
                item {
                    Column(pageModifier(horizontal).padding(bottom = 24.dp)) {
                        EditorialSectionHeading("From the journal", "Kotlin · Android · Multiplatform")
                    }
                }
                items(state) { blog ->
                    Column(pageModifier(horizontal).padding(bottom = 16.dp)) {
                        HomeCard(blog) { component.navigateToBlogScreen(it) }
                    }
                }
                if (state.isEmpty()) {
                    item {
                        Text("The journal is empty for now.", modifier = pageModifier(horizontal).padding(vertical = 32.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                item {
                    Column(pageModifier(horizontal).padding(top = 40.dp, bottom = 72.dp)) {
                        Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer).padding(if (compact) 26.dp else 42.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            Text("One codebase.\nMore possibilities.", style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("See the shared Kotlin code, projects, and experience behind Blue Labs.",
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Button(onClick = { navigate(NavigationEvent.NavigatePortfolio) }, shape = RectangleShape) {
                                Text("Explore the portfolio")
                                Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                            }
                            Text("Blue\nLabs.", fontSize = if (compact) 72.sp else 120.sp,
                                lineHeight = if (compact) 72.sp else 115.sp,
                                letterSpacing = (-5).sp, fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
                item { EditorialFooter() }
            }
        }
    }
}

private fun pageModifier(horizontal: Dp) = Modifier.widthIn(max = 1440.dp).fillMaxWidth().padding(horizontal = horizontal)
