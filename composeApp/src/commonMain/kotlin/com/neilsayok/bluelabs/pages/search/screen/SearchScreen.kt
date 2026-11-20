package com.neilsayok.bluelabs.pages.search.screen

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.common.ui.LocalEditorialNavigation
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.navigation.NavigationEvent
import com.neilsayok.bluelabs.pages.home.widgets.HomeCard
import com.neilsayok.bluelabs.pages.search.component.SearchComponent
import com.neilsayok.bluelabs.util.setPageTitle

@Composable
fun SearchScreen(component: SearchComponent) {
    val searchResult by component.searchState.subscribeAsState()
    val navigate = LocalEditorialNavigation.current

    LaunchedEffect(component.key) {
        component.search()
        setPageTitle("Search : ${component.key}")
    }

    LoaderScaffold { paddingValues ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(paddingValues)) {
            val compact = maxWidth < 700.dp
            val horizontal = if (compact) 20.dp else 64.dp
            LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Column(pageModifier(horizontal).padding(top = if (compact) 32.dp else 52.dp, bottom = 60.dp),
                        verticalArrangement = Arrangement.spacedBy(22.dp)) {
                        Text("Search the journal", fontSize = if (compact) 42.sp else 92.sp,
                            lineHeight = if (compact) 45.sp else 96.sp, fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-3).sp)
                        Text("Results for “${component.key}”. Search article titles, tags, author names, and content.",
                            style = MaterialTheme.typography.bodyLarge)
                    }
                }
                item {
                    Row(pageModifier(horizontal).padding(bottom = 22.dp),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Text("${searchResult.size} matching articles", style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f))
                        TextButton(onClick = { navigate(NavigationEvent.NavigateHome) }) {
                            Text("Browse all")
                            Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 6.dp))
                        }
                    }
                }
                items(searchResult) { item ->
                    Column(pageModifier(horizontal).padding(bottom = 16.dp)) {
                        HomeCard(item.blog) { component.navigateToBlogScreen(it) }
                    }
                }
                if (searchResult.isEmpty()) {
                    item {
                        Column(pageModifier(horizontal).padding(vertical = 32.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("No articles found", style = MaterialTheme.typography.headlineSmall)
                            Text("Try another word or browse the journal.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                item { EditorialFooter() }
            }
        }
    }
}

private fun pageModifier(horizontal: Dp) = Modifier.widthIn(max = 1440.dp).fillMaxWidth().padding(horizontal = horizontal)
