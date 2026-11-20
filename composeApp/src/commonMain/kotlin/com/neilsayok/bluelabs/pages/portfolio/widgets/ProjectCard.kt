package com.neilsayok.bluelabs.pages.portfolio.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neilsayok.bluelabs.common.ui.components.LoaderBox
import com.neilsayok.bluelabs.common.ui.markdown.MarkdownHandler
import com.neilsayok.bluelabs.data.portfolio.PortfolioFileContents
import com.neilsayok.bluelabs.util.loadImage

@Composable
fun ProjectWidget(
    projectsFileContent: MutableCollection<PortfolioFileContents>,
    getProjectIcon: (PortfolioFileContents) -> String?
) {
    Column(Modifier.fillMaxWidth()) {
        projectsFileContent.sortedBy { it.order }.forEachIndexed { index, file ->
            ProjectCard(file, getProjectIcon, index)
        }
    }
}

@Composable
fun ProjectCard(
    fileContents: PortfolioFileContents,
    getProjectIcon: (PortfolioFileContents) -> String?,
    index: Int = 0
) {
    var isExpanded by remember(fileContents.path) { mutableStateOf(index == 0) }
    val colors = MaterialTheme.colorScheme
    val summary = fileContents.content?.lineSequence()?.map(String::trim)?.firstOrNull {
        it.isNotBlank() && !it.startsWith("#") && !it.startsWith("!") && !it.startsWith("<") && !it.startsWith("[")
    }?.replace("**", "")?.take(170) ?: fileContents.subTitle.orEmpty()
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = if (isExpanded) colors.primaryContainer else if (index % 2 == 0) colors.surface else colors.surfaceVariant)
    ) {
        LoaderBox(isLoading = fileContents.response.isLoading(), isError = fileContents.response.isError()) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val compact = maxWidth < 700.dp
                Column {
                    Row(
                        Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded }
                            .semantics { role = Role.Button; stateDescription = if (isExpanded) "Expanded" else "Collapsed" }
                            .padding(horizontal = if (compact) 20.dp else 30.dp, vertical = 22.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        getProjectIcon(fileContents)?.let { iconUrl ->
                            Image(loadImage(iconUrl), contentDescription = null,
                                contentScale = ContentScale.Fit, modifier = Modifier.size(36.dp))
                        }
                        if (compact) {
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(fileContents.fileName, fontSize = 23.sp, fontWeight = FontWeight.Bold,
                                    lineHeight = 27.sp)
                                fileContents.subTitle?.let { Text(it, style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant) }
                                if (summary.isNotBlank()) Text(summary, style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        } else {
                            Column(Modifier.weight(.8f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(fileContents.fileName, fontSize = 28.sp, fontWeight = FontWeight.Bold,
                                    lineHeight = 32.sp)
                                fileContents.subTitle?.let { Text(it, style = MaterialTheme.typography.bodySmall,
                                    color = colors.onSurfaceVariant) }
                            }
                            Text(summary, modifier = Modifier.weight(1.2f), maxLines = 2,
                                overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
                        }
                        Icon(Icons.Default.ExpandMore, contentDescription = null,
                            modifier = Modifier.rotate(if (isExpanded) 180f else 0f), tint = colors.primary)
                    }
                    AnimatedVisibility(isExpanded) {
                        fileContents.content?.let { MarkdownHandler(it) }
                    }
                }
            }
        }
    }
}
