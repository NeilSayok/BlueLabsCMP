package com.neilsayok.bluelabs.pages.portfolio.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.thauvin.erik.urlencoder.UrlEncoderUtil

@Composable
fun ContactMeWidget() {
    val uriHandler = LocalUriHandler.current
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val whatsappMessage by remember { derivedStateOf { "*Subject : ${subject.trim()}*\n\n${message}" } }
    val whatsappLink by remember { derivedStateOf { "https://wa.me/+919051880247?text=${whatsappMessage}" } }
    val mailToLink by remember {
        derivedStateOf {
            "mailto:sayokdeymajumder1998@gmail.com?subject=${UrlEncoderUtil.encode(subject.trim())}&body=${UrlEncoderUtil.encode(message.trim())}"
        }
    }
    val isEnabled by remember { derivedStateOf { subject.isNotBlank() && message.isNotBlank() } }

    BoxWithConstraints(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer)) {
        val compact = maxWidth < 700.dp
        val intro: @Composable () -> Unit = {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text("Let’s talk.", fontSize = if (compact) 34.sp else 46.sp,
                    fontWeight = FontWeight.Medium, letterSpacing = (-1).sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("Have a project or a question? Send me a message.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                TextButton(onClick = { uriHandler.openUri("tel:+919051880247") }) {
                    Text("+91 90518 80247")
                    Icon(Icons.Default.Call, null, Modifier.padding(start = 8.dp))
                }
            }
        }
        val form: @Composable () -> Unit = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(subject, { subject = it }, label = { Text("Subject") },
                    placeholder = { Text("What would you like to discuss?") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(message, { message = it }, label = { Text("Message") },
                    placeholder = { Text("Tell me a little about it") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 130.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { uriHandler.openUri(mailToLink) }, enabled = isEnabled,
                        shape = RectangleShape) {
                        Text("Email")
                        Icon(Icons.Default.Email, null, Modifier.padding(start = 8.dp))
                    }
                    TextButton(onClick = { uriHandler.openUri(whatsappLink) }, enabled = isEnabled) {
                        Text("WhatsApp")
                        Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                    }
                    TextButton(onClick = { uriHandler.openUri("tel:+919051880247") }) {
                        Text("Call")
                        Icon(Icons.Default.Call, null, Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
        if (compact) {
            Column(Modifier.padding(25.dp), verticalArrangement = Arrangement.spacedBy(30.dp)) { intro(); form() }
        } else {
            Row(Modifier.padding(40.dp), horizontalArrangement = Arrangement.spacedBy(64.dp)) {
                Column(Modifier.weight(1f)) { intro() }
                Column(Modifier.weight(1f)) { form() }
            }
        }
    }
}
