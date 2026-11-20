package com.neilsayok.bluelabs.pages.privacy.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.common.ui.markdown.MarkdownHandler
import com.neilsayok.bluelabs.pages.privacy.component.PrivacyPolicyComponent
import com.neilsayok.bluelabs.util.setPageTitle

@Composable
fun PrivacyPolicyScreen(component: PrivacyPolicyComponent) {
    setPageTitle("Privacy Policy")
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val compact = maxWidth < 700.dp
        val horizontal = if (compact) 20.dp else 64.dp
        LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Column(Modifier.widthIn(max = 900.dp).fillMaxWidth()
                    .padding(horizontal = horizontal, vertical = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Privacy policy", style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Medium)
                    Text("BlueLabs.in · Effective 15 May 2024", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                Column(Modifier.widthIn(max = 900.dp).fillMaxWidth().padding(horizontal = horizontal)) {
                    MarkdownHandler(PrivacyPolicyText.trimIndent())
                    Text(
                        "All the data on the website are provided to the best of the author's knowledge, and the author is not legally bound if the data is incorrect.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item { Column(Modifier.padding(top = 80.dp)) { EditorialFooter() } }
        }
    }
}
