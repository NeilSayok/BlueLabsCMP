package com.neilsayok.bluelabs.pages.me.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.pages.me.component.MeComponent
import com.neilsayok.bluelabs.util.setMetaTag
import com.neilsayok.bluelabs.util.setPageTitle

@Composable
fun MeScreen(component: MeComponent) {
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(component) {
        setPageTitle("Sayok Dey Majumder : Blue Labs")
        setMetaTag("description", "Visit my Play Store, explore my portfolio, download my resume, or contact me.")
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val compact = maxWidth < 800.dp
        val horizontal = if (compact) 20.dp else 64.dp
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {
            val content = Modifier.widthIn(max = 1440.dp).fillMaxWidth().padding(horizontal = horizontal)
                .padding(top = if (compact) 40.dp else 110.dp, bottom = if (compact) 90.dp else 130.dp)
            if (compact) {
                Column(content, verticalArrangement = Arrangement.spacedBy(52.dp)) {
                    Intro(true)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        LinkRow("Visit my Play Store", "https://play.google.com/store/apps/developer?id=BlueLabs+Org", true) { uriHandler.openUri(it) }
                        LinkRow("Visit my portfolio", "https://bluelabs.in/portfolio", false) { uriHandler.openUri(it) }
                        LinkRow("Download my resume", "https://neilsayok.bluelabs.in/imagelib/resume/Resume_Sayok_Dey_Majumder_Android_Developer.pdf", false) { uriHandler.openUri(it) }
                        ContactLinks { uriHandler.openUri(it) }
                    }
                }
            } else {
                Row(content, horizontalArrangement = Arrangement.spacedBy(80.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) { Intro(false) }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        LinkRow("Visit my Play Store", "https://play.google.com/store/apps/developer?id=BlueLabs+Org", true) { uriHandler.openUri(it) }
                        LinkRow("Visit my portfolio", "https://bluelabs.in/portfolio", false) { uriHandler.openUri(it) }
                        LinkRow("Download my resume", "https://neilsayok.bluelabs.in/imagelib/resume/Resume_Sayok_Dey_Majumder_Android_Developer.pdf", false) { uriHandler.openUri(it) }
                        ContactLinks { uriHandler.openUri(it) }
                    }
                }
            }
            Spacer(Modifier.weight(1f, fill = false))
            EditorialFooter()
        }
    }
}

@Composable
private fun Intro(compact: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
        Text("Sayok Dey\nMajumder.", fontSize = if (compact) 44.sp else 80.sp,
            lineHeight = if (compact) 48.sp else 86.sp,
            letterSpacing = (-3).sp, fontWeight = FontWeight.ExtraBold)
        Text("Android & Kotlin Multiplatform developer. Find my apps, work, and ways to get in touch.",
            style = MaterialTheme.typography.bodyLarge)
        Text("Blue Labs", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun LinkRow(label: String, url: String, primary: Boolean, open: (String) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val foreground = if (primary) colors.onPrimary else colors.onBackground
    Row(
        Modifier.fillMaxWidth().background(if (primary) colors.primary else colors.primaryContainer)
            .clickable { open(url) }.heightIn(min = 74.dp).padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 17.sp, color = foreground)
        Icon(Icons.Default.NorthEast, null, tint = foreground)
    }
}

@Composable
private fun ContactLinks(open: (String) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        listOf("Call" to "tel:+919051880247", "Email" to "mailto:sayokdeymajumder@gmail.com").forEach { (label, url) ->
            Row(
                Modifier.weight(1f).background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { open(url) }.heightIn(min = 66.dp).padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label)
                Icon(Icons.Default.NorthEast, null)
            }
        }
    }
}
