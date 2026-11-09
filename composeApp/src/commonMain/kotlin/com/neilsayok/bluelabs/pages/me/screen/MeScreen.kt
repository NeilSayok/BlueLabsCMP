package com.neilsayok.bluelabs.pages.me.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    Box(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sayok Dey Majumder",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            FilledTonalButton(
                onClick = { uriHandler.openUri("https://play.google.com/store/apps/developer?id=BlueLabs+Org") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            ) {
                Text("Visit my Play Store")
            }

            FilledTonalButton(
                onClick = { uriHandler.openUri("https://bluelabs.in/portfolio") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            ) {
                Text("Visit my portfolio")
            }

            FilledTonalButton(
                onClick = { uriHandler.openUri("https://neilsayok.bluelabs.in/imagelib/resume/Resume_Sayok_Dey_Majumder_Android_Developer.pdf") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            ) {
                Text("Download my resume")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTonalButton(
                    onClick = { uriHandler.openUri("tel:+919051880247") },
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp)
                ) {
                    Text("Call")
                }

                FilledTonalButton(
                    onClick = { uriHandler.openUri("mailto:sayokdeymajumder@gmail.com") },
                    modifier = Modifier.weight(1f).heightIn(min = 56.dp)
                ) {
                    Text("Email")
                }
            }
        }
    }
}
