package com.neilsayok.bluelabs.pages.portfolio.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neilsayok.bluelabs.common.ui.markdown.MarkdownHandler
import com.neilsayok.bluelabs.data.portfolio.PortfolioFileContents
import com.neilsayok.bluelabs.util.loadImage

@Composable
fun WorkedAtWidget(jobs: List<PortfolioFileContents>, getJobsIcon: (PortfolioFileContents) -> String?) {
    val colors = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth()) {
        jobs.forEachIndexed { index, job ->
            var expanded by remember(job.path) { mutableStateOf(index == 0) }
            val summary = job.content?.lineSequence()?.map(String::trim)?.firstOrNull {
                it.isNotBlank() && !it.startsWith("#") && !it.startsWith("!") && !it.startsWith("<")
            }?.replace("**", "")?.take(170) ?: job.subTitle.orEmpty()
            BoxWithConstraints(Modifier.fillMaxWidth().animateContentSize()) {
                val compact = maxWidth < 700.dp
                Column(Modifier.fillMaxWidth().background(
                    if (expanded) colors.primaryContainer else if (index % 2 == 0) colors.surface else colors.surfaceVariant
                )) {
                    Row(
                        Modifier.fillMaxWidth().clickable { expanded = !expanded }
                            .semantics { role = Role.Button; stateDescription = if (expanded) "Expanded" else "Collapsed" }
                            .padding(horizontal = if (compact) 20.dp else 30.dp, vertical = 22.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        getJobsIcon(job)?.let { iconUrl ->
                            Image(loadImage(iconUrl), null, modifier = Modifier.size(36.dp))
                        }
                        if (compact) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(job.fileName, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                                job.subTitle?.let { Text(it, style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant) }
                                if (summary.isNotBlank()) Text(summary, style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        } else {
                            Column(Modifier.weight(.8f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(job.fileName, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                job.subTitle?.let { Text(it, style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant) }
                            }
                            Text(summary, modifier = Modifier.weight(1.2f), maxLines = 2,
                                overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                        }
                        Icon(Icons.Default.ExpandMore, null,
                            modifier = Modifier.rotate(if (expanded) 180f else 0f), tint = colors.primary)
                    }
                    AnimatedVisibility(expanded) { job.content?.let { MarkdownHandler(it) } }
                }
            }
        }
    }
}
