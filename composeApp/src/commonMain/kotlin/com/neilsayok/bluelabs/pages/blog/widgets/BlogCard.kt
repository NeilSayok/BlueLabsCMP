package com.neilsayok.bluelabs.pages.blog.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.constants.BLOG_PAGE
import com.neilsayok.bluelabs.common.constants.EMPTY_STRING
import com.neilsayok.bluelabs.common.ui.LocalEditorialNavigation
import com.neilsayok.bluelabs.common.ui.markdown.MarkdownHandler
import com.neilsayok.bluelabs.data.bloglist.BlogLoadedFields
import com.neilsayok.bluelabs.navigation.NavigationEvent
import com.neilsayok.bluelabs.pages.blog.component.BlogComponent
import com.neilsayok.bluelabs.util.SnackBarController
import com.neilsayok.bluelabs.util.SnackBarEvent
import kotlinx.coroutines.launch

@Composable
fun BlogCard(blog: BlogLoadedFields, component: BlogComponent) {
    val navigate = LocalEditorialNavigation.current
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val title = blog.title?.stringValue ?: EMPTY_STRING
    fun share() {
        clipboard.setText(buildAnnotatedString { append("${BuildKonfig.BASE_URL}/$BLOG_PAGE/${blog.urlStr?.stringValue}") })
        scope.launch { SnackBarController.sendEvent(SnackBarEvent("Link copied to clipboard")) }
    }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val compact = maxWidth < 700.dp
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                Modifier.widthIn(max = 1178.dp).fillMaxWidth().padding(horizontal = if (compact) 20.dp else 64.dp)
                    .padding(top = if (compact) 28.dp else 50.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                TextButton(onClick = { navigate(NavigationEvent.NavigateHome) }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                    Text("Back to journal", modifier = Modifier.padding(start = 8.dp))
                }
                Text(title, fontSize = if (compact) 36.sp else 70.sp,
                    lineHeight = if (compact) 41.sp else 76.sp, fontWeight = FontWeight.Medium,
                    letterSpacing = (-2).sp)
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    AuthorCard(blog.author, blog.posted?.timestampValue)
                    Text("${blog.readTime?.integerValue ?: "0"} min read", style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurfaceVariant)
                }
                BlogChips(blog.tags?.arrayValue?.values?.map { it?.stringValue })
                Box(Modifier.fillMaxWidth().height(if (compact) 220.dp else 270.dp)
                    .background(colors.primaryContainer), contentAlignment = Alignment.BottomStart) {
                    if (!blog.bigImg?.stringValue.isNullOrBlank()) {
                        AsyncImage(blog.bigImg?.stringValue, contentDescription = "$title cover",
                            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(if (compact) 220.dp else 270.dp))
                    } else {
                        Text(title.take(24), modifier = Modifier.padding(28.dp), fontSize = 48.sp,
                            fontWeight = FontWeight.Bold, maxLines = 2)
                    }
                }
            }
            Column(Modifier.widthIn(max = 840.dp).fillMaxWidth().padding(horizontal = if (compact) 4.dp else 40.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)) {
                TextButton(onClick = ::share, modifier = Modifier.padding(start = 12.dp)) {
                    Text("Share this article")
                    Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 6.dp))
                }
                blog.readmeFile?.stringValue?.let { MarkdownHandler(it, component) }
                Column(Modifier.fillMaxWidth().background(colors.surfaceVariant).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(blog.author?.name?.stringValue ?: "Blue Labs", style = MaterialTheme.typography.headlineSmall)
                    blog.author?.bio?.stringValue?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                }
            }
        }
    }
}
