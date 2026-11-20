package com.neilsayok.bluelabs.pages.portfolio.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun AboutMe(onProjectClick: () -> Unit, onContactMeClick: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val resume = "https://neilsayok.github.io/imagelib/resume/Resume_Sayok_Dey_Majumder_Android_Developer.pdf"
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(
            "I’m an Android & Kotlin Multiplatform developer building shared Kotlin codebases for Android, iOS, desktop, and web. I enjoy turning complex ideas into intuitive apps, integrating APIs, and improving app performance.",
            style = MaterialTheme.typography.bodyLarge
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { uriHandler.openUri(resume) }, shape = RectangleShape) {
                Text("Download resume")
                Icon(Icons.Default.Download, null, Modifier.padding(start = 8.dp))
            }
            TextButton(onClick = onContactMeClick) { Text("Contact me") }
            TextButton(onClick = onProjectClick) {
                Text("Explore projects")
                Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 6.dp))
            }
        }
    }
}
