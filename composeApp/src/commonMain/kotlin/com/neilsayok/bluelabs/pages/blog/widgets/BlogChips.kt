package com.neilsayok.bluelabs.pages.blog.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BlogChips(tags: List<String?>?) {
    if (tags.isNullOrEmpty()) return
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tags.filterNotNull().forEach { tag ->
            Box(Modifier.background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 10.dp, vertical = 6.dp)) {
                Text(tag, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
