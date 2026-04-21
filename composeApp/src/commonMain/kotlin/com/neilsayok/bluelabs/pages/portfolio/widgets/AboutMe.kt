package com.neilsayok.bluelabs.pages.portfolio.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import org.kodein.emoji.compose.m3.TextWithNotoImageEmoji

@Composable
fun AboutMe(onProjectClick : () -> Unit, onContactMeClick: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    val fileId = "1iCpE7Fr72y9SwoZuKQqOVyEZg2MeREgoiKpEJ3BkN0Q"
//    val resume = "https://docs.google.com/document/d/${fileId}/export?format=pdf"
    val resume = "https://neilsayok.github.io/imagelib/resume/Resume_Sayok_Dey_Majumder_Android_Developer.pdf"
    Column {
        TextWithNotoImageEmoji("Hey there \uD83D\uDC4B I’m an Android & Kotlin Multiplatform Developer who loves building fast, beautiful, and scalable apps using Jetpack Compose and Compose Multiplatform. I specialize in creating shared Kotlin codebases that power seamless experiences across Android, iOS, Desktop, and Web — with clean architecture, smooth animations, and pixel-perfect UI.\n" +
                "\n" +
                "I’m passionate about turning complex ideas into elegant, intuitive apps that not only perform well but also feel great to use. Whether it’s exploring the latest Compose Multiplatform libraries, integrating APIs, or optimizing app performance, I’m always excited to push what’s possible with Kotlin.\n" +
                "\n" +
                "When I’m not coding, you’ll probably find me sketching new UI ideas, reading up on tech trends, or enjoying a strong cup of coffee ☕ while dreaming up my next project.\n" +
                "\n" +
                "\uD83D\uDCA1 Let’s build something awesome together!")

        TextWithNotoImageEmoji("Check out my latest projects below \uD83D\uDC47", modifier = Modifier.clickable{onProjectClick()})

        Spacer(modifier = Modifier.size(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {uriHandler.openUri(resume)},
            ){
                Text("Download Resume")
            }

            Button(
                onClick = {onContactMeClick()},
            ){
                Text("Contact Me")
            }
        }
    }
}