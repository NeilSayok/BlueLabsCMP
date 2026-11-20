package com.neilsayok.bluelabs.common.ui.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bluelabscmp.composeapp.generated.resources.Res
import bluelabscmp.composeapp.generated.resources.blue_labs_icon
import bluelabscmp.composeapp.generated.resources.blue_labs_icon_white
import com.neilsayok.bluelabs.navigation.NavigationEvent
import com.neilsayok.bluelabs.navigation.RootComponent
import com.neilsayok.bluelabs.util.Platform
import com.neilsayok.bluelabs.util.getPlatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainAppBar(
    isDark: Boolean,
    navigate: (NavigationEvent) -> Unit,
    currentScreen: RootComponent.Configuration,
    onThemeChange: (Boolean) -> Unit
) {
    var searchKey by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    val navItems = listOf(
        "Journal" to NavigationEvent.NavigateHome,
        "Portfolio" to NavigationEvent.NavigatePortfolio,
        "Me" to NavigationEvent.NavigateMe,
        "Privacy" to NavigationEvent.NavigatePrivacyPolicy
    )
    fun search() {
        if (searchKey.isNotBlank()) {
            navigate(NavigationEvent.NavigateSearch(searchKey.trim()))
            searchKey = ""
            menuOpen = false
        }
    }
    BoxWithConstraints(Modifier.fillMaxWidth().background(colors.background)) {
        val compact = maxWidth < 900.dp
        val horizontal = if (compact) 20.dp else 64.dp
        Column(Modifier.fillMaxWidth().padding(horizontal = horizontal, vertical = if (compact) 14.dp else 20.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (currentScreen != RootComponent.Configuration.HomeScreen && getPlatform() == Platform.DESKTOP) {
                    IconButton(onClick = { navigate(NavigationEvent.NavigateUp) }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Go back")
                    }
                }
                TextButton(
                    onClick = { navigate(NavigationEvent.NavigateHome); menuOpen = false },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    modifier = Modifier.heightIn(min = 48.dp)
                ) {
                    Image(
                        painter = painterResource(if (isDark) Res.drawable.blue_labs_icon_white else Res.drawable.blue_labs_icon),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp, 32.dp)
                    )
                    Text("Blue Labs", fontSize = if (compact) 22.sp else 25.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = (-1.5).sp, color = colors.onBackground,
                        modifier = Modifier.padding(start = 7.dp))
                }
                Spacer(Modifier.weight(1f))
                if (!compact) {
                    navItems.forEach { (label, event) ->
                        val selected = when (event) {
                            NavigationEvent.NavigateHome -> currentScreen is RootComponent.Configuration.HomeScreen ||
                                currentScreen is RootComponent.Configuration.BlogScreen || currentScreen is RootComponent.Configuration.SearchScreen
                            NavigationEvent.NavigatePortfolio -> currentScreen is RootComponent.Configuration.PortfolioScreen
                            NavigationEvent.NavigateMe -> currentScreen is RootComponent.Configuration.MeScreen
                            else -> currentScreen is RootComponent.Configuration.PrivacyPolicyScreen
                        }
                        TextButton(onClick = { navigate(event) }) {
                            Text(label, color = if (selected) colors.primary else colors.onBackground)
                        }
                    }
                    SearchField(searchKey, { searchKey = it }, ::search, Modifier.width(220.dp))
                }
                IconButton(onClick = { onThemeChange(!isDark) }) {
                    Icon(if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDark) "Switch to light theme" else "Switch to dark theme")
                }
                if (compact) {
                    IconButton(onClick = { menuOpen = !menuOpen }) {
                        Icon(Icons.Default.Menu, contentDescription = if (menuOpen) "Close navigation" else "Open navigation")
                    }
                }
            }
            if (compact) {
                SearchField(searchKey, { searchKey = it }, ::search, Modifier.fillMaxWidth().padding(top = 8.dp))
                if (menuOpen) {
                    Column(Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        navItems.forEach { (label, event) ->
                            TextButton(onClick = { navigate(event); menuOpen = false }, modifier = Modifier.fillMaxWidth()) {
                                Text(label, modifier = Modifier.fillMaxWidth(), color = colors.onBackground)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(value: String, onValueChange: (String) -> Unit, onSearch: () -> Unit, modifier: Modifier) {
    val colors = MaterialTheme.colorScheme
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        placeholder = { Text("Search", style = MaterialTheme.typography.bodySmall) },
        trailingIcon = {
            IconButton(onClick = onSearch) { Icon(Icons.Default.Search, contentDescription = "Search articles") }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colors.surfaceVariant,
            unfocusedContainerColor = colors.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
