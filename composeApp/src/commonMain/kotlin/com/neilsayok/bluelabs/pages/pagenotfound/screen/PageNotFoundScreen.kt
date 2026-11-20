package com.neilsayok.bluelabs.pages.pagenotfound.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.common.ui.LocalEditorialNavigation
import com.neilsayok.bluelabs.navigation.NavigationEvent
import com.neilsayok.bluelabs.pages.editor.component.PageNotFoundComponent
import com.neilsayok.bluelabs.util.setPageTitle

@Composable
fun PageNotFoundScreen(component: PageNotFoundComponent) {
    setPageTitle("404 - Page Not Found")
    val navigate = LocalEditorialNavigation.current
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val compact = maxWidth < 700.dp
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                Modifier.widthIn(max = 1440.dp).fillMaxWidth().weight(1f)
                    .padding(horizontal = if (compact) 20.dp else 64.dp, vertical = if (compact) 60.dp else 100.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Text("Page not\nfound.", fontSize = if (compact) 58.sp else 110.sp,
                    lineHeight = if (compact) 64.sp else 116.sp, fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-3).sp)
                Text("This address doesn’t lead to a page on Blue Labs.", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { navigate(NavigationEvent.NavigateHome) }, shape = RectangleShape,
                    modifier = Modifier.heightIn(min = 48.dp)) {
                    Text("Back to the journal")
                    Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                }
            }
            EditorialFooter()
        }
    }
}
