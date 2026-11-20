package com.neilsayok.bluelabs.pages.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.constants.BLOG_PAGE
import com.neilsayok.bluelabs.data.bloglist.BlogLoadedFields
import com.neilsayok.bluelabs.util.SnackBarController
import com.neilsayok.bluelabs.util.SnackBarEvent
import kotlinx.coroutines.launch

@Composable
fun HomeCard(blog: BlogLoadedFields?, navigateToBlogPage: (BlogLoadedFields?) -> Unit) {
    if (blog == null) return
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val colors = MaterialTheme.colorScheme
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val stacked = maxWidth < 620.dp
        Card(
            onClick = { navigateToBlogPage(blog) },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            if (stacked) {
                Column {
                    BlogCover(blog, Modifier.fillMaxWidth().height(125.dp))
                    StoryText(blog, true, onShare = {
                        clipboard.setText(buildAnnotatedString { append("${BuildKonfig.BASE_URL}/$BLOG_PAGE/${blog.urlStr?.stringValue}") })
                        scope.launch { SnackBarController.sendEvent(SnackBarEvent("Link copied to clipboard")) }
                    })
                }
            } else {
                Row(Modifier.fillMaxWidth()) {
                    BlogCover(blog, Modifier.width(260.dp).height(185.dp))
                    StoryText(blog, false, onShare = {
                        clipboard.setText(buildAnnotatedString { append("${BuildKonfig.BASE_URL}/$BLOG_PAGE/${blog.urlStr?.stringValue}") })
                        scope.launch { SnackBarController.sendEvent(SnackBarEvent("Link copied to clipboard")) }
                    })
                }
            }
        }
    }
}

@Composable
private fun BlogCover(blog: BlogLoadedFields, modifier: Modifier) {
    Box(modifier.background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.BottomStart) {
        val url = blog.tinyImg?.stringValue ?: blog.bigImg?.stringValue
        if (!url.isNullOrBlank()) {
            AsyncImage(
                model = url,
                contentDescription = blog.title?.stringValue?.let { "$it cover" },
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = blog.title?.stringValue?.split(' ')?.take(2)?.joinToString(" ") ?: "Blue Labs",
                modifier = Modifier.padding(20.dp),
                fontSize = 34.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StoryText(blog: BlogLoadedFields, stacked: Boolean, onShare: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxWidth().heightIn(min = if (stacked) 170.dp else 185.dp)
            .padding(horizontal = if (stacked) 20.dp else 30.dp, vertical = if (stacked) 20.dp else 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            blog.title?.stringValue ?: "Untitled",
            fontSize = if (stacked) 24.sp else 30.sp,
            lineHeight = if (stacked) 28.sp else 35.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            blog.genre?.type?.stringValue?.let {
                Text(it, style = MaterialTheme.typography.labelSmall, color = colors.primary)
            }
            Text("${blog.readTime?.integerValue ?: "0"} min read", style = MaterialTheme.typography.labelSmall,
                color = colors.onSurfaceVariant)
        }
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(blog.author?.name?.stringValue ?: "Blue Labs", style = MaterialTheme.typography.labelSmall,
                color = colors.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f))
            TextButton(onClick = onShare, modifier = Modifier.heightIn(min = 44.dp)) {
                Text("Share", color = colors.onSurface)
                Icon(Icons.Default.NorthEast, null, modifier = Modifier.padding(start = 6.dp), tint = colors.onSurface)
            }
        }
    }
}
