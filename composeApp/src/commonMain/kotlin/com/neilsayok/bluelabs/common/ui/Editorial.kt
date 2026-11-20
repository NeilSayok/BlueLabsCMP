package com.neilsayok.bluelabs.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bluelabscmp.composeapp.generated.resources.Res
import bluelabscmp.composeapp.generated.resources.blue_labs_icon
import bluelabscmp.composeapp.generated.resources.blue_labs_icon_white
import com.neilsayok.bluelabs.navigation.NavigationEvent
import org.jetbrains.compose.resources.painterResource

val LocalEditorialNavigation = staticCompositionLocalOf<(NavigationEvent) -> Unit> { {} }
val LocalEditorialDark = staticCompositionLocalOf { false }

@Composable
fun editorialPadding(): Dp {
    val width = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    return if (width < 700.dp) 20.dp else 64.dp
}

@Composable
fun EditorialSectionHeading(title: String, detail: String? = null) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth < 520.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium)
                if (detail != null) Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium)
                if (detail != null) Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun EditorialFooter() {
    val navigate = LocalEditorialNavigation.current
    val dark = LocalEditorialDark.current
    val color = MaterialTheme.colorScheme.onBackground
    BoxWithConstraints(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant)) {
        val compact = maxWidth < 700.dp
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val content = Modifier.widthIn(max = 1320.dp).fillMaxWidth()
                .padding(horizontal = if (compact) 20.dp else 64.dp, vertical = 32.dp)
            if (compact) {
                Column(content, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    FooterBrand(dark)
                    Text("Engineering, written down.", style = MaterialTheme.typography.bodySmall)
                    FooterLinks(navigate)
                }
            } else {
                Row(content, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    FooterBrand(dark)
                    Text("Engineering, written down.", style = MaterialTheme.typography.bodySmall)
                    FooterLinks(navigate)
                }
            }
        }
    }
}

@Composable
private fun FooterBrand(dark: Boolean) {
    val navigate = LocalEditorialNavigation.current
    Row(Modifier.clickable { navigate(NavigationEvent.NavigateHome) }.heightIn(min = 44.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(if (dark) Res.drawable.blue_labs_icon_white else Res.drawable.blue_labs_icon), null, Modifier.size(25.dp, 32.dp))
        Text("Blue Labs", fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp)
    }
}

@Composable
private fun FooterLinks(navigate: (NavigationEvent) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
        listOf(
            "Editor" to NavigationEvent.NavigateEditor,
            "Indexer" to NavigationEvent.NavigateIndexer,
            "Privacy policy" to NavigationEvent.NavigatePrivacyPolicy
        ).forEach { (label, event) ->
            Box(Modifier.clickable { navigate(event) }.heightIn(min = 44.dp), contentAlignment = Alignment.CenterStart) {
                Text(label, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
