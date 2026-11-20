package com.neilsayok.bluelabs.pages.portfolio.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkillWidget() {
    val skills = listOf("Android" to 5, "Kotlin" to 5, "Java" to 4, "Jetpack Compose" to 5)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        skills.forEach { (skill, level) ->
            Row(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(skill)
                Text("$level / 5", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
