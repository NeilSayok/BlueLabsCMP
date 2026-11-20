package com.neilsayok.bluelabs.pages.portfolio.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bluelabscmp.composeapp.generated.resources.Res
import bluelabscmp.composeapp.generated.resources.kaffeine_02
import bluelabscmp.composeapp.generated.resources.kaffeine_03
import bluelabscmp.composeapp.generated.resources.watchdog_01
import bluelabscmp.composeapp.generated.resources.watchdog_02
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.common.ui.EditorialSectionHeading
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.pages.portfolio.component.PortfolioComponent
import com.neilsayok.bluelabs.pages.portfolio.widgets.AboutMe
import com.neilsayok.bluelabs.pages.portfolio.widgets.ContactMeWidget
import com.neilsayok.bluelabs.pages.portfolio.widgets.ProjectWidget
import com.neilsayok.bluelabs.pages.portfolio.widgets.SkillWidget
import com.neilsayok.bluelabs.pages.portfolio.widgets.WorkedAtWidget
import com.neilsayok.bluelabs.util.setMetaTag
import com.neilsayok.bluelabs.util.setOpenGraphTags
import com.neilsayok.bluelabs.util.setPageTitle
import com.neilsayok.bluelabs.util.setTwitterCardTags
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun PortfolioScreen(component: PortfolioComponent) {
    val uiState by component.uiState.subscribeAsState()
    val projects = uiState.projectsFileContents.subscribeAsState()
    val jobs = uiState.jobsFileContents.subscribeAsState()
    val jobsApiState by uiState.jobs.subscribeAsState()
    val projectsApiState by uiState.projects.subscribeAsState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val hasProjects = projects.value.isNotEmpty()
    val hasJobs = jobs.value.isNotEmpty()
    val contactIndex = 2 + (if (hasProjects) 2 else 0) + (if (hasJobs) 2 else 0)

    LaunchedEffect(Unit) {
        component.getFolderContents()
        setPageTitle("Portfolio : Sayok Dey Majumder")
        setMetaTag("description", "A portfolio website of Sayok Dey Majumder")
        setMetaTag("viewport", "width=device-width, initial-scale=1.0")
        setMetaTag("author", " Sayok Dey Majumder")
        setMetaTag("robots", "index, follow")
        setOpenGraphTags(
            title = "Portfolio : Sayok Dey Majumder",
            description = "A portfolio website of Sayok Dey Majumder",
            image = "https://avatars.githubusercontent.com/u/21328143?v=4",
            url = "${BuildKonfig.BASE_URL}/portfolio", type = "profile"
        )
        setTwitterCardTags(
            title = "Portfolio : Sayok Dey Majumder",
            description = "A portfolio website of Sayok Dey Majumder",
            image = "https://avatars.githubusercontent.com/u/21328143?v=4"
        )
    }

    LoaderScaffold(calledApis = listOf(jobsApiState, projectsApiState)) { paddingValues ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(paddingValues)) {
            val compact = maxWidth < 800.dp
            val horizontal = if (compact) 20.dp else 64.dp
            LazyColumn(Modifier.fillMaxSize(), state = scrollState,
                horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    val body: @Composable () -> Unit = {
                        Column(verticalArrangement = Arrangement.spacedBy(26.dp)) {
                            Text("Sayok Dey\nMajumder.", fontSize = if (compact) 44.sp else 84.sp,
                                lineHeight = if (compact) 48.sp else 89.sp,
                                fontWeight = FontWeight.ExtraBold, letterSpacing = (-3).sp)
                            AboutMe(
                                onProjectClick = {
                                    if (hasProjects) scope.launch { scrollState.animateScrollToItem(2) }
                                },
                                onContactMeClick = {
                                    scope.launch { scrollState.animateScrollToItem(contactIndex) }
                                }
                            )
                        }
                    }
                    if (compact) {
                        Column(pageModifier(horizontal).padding(top = 32.dp)) {
                            body()
                            SharedCodeCard(Modifier.fillMaxWidth().padding(top = 46.dp))
                        }
                    } else {
                        Row(pageModifier(horizontal).padding(top = 60.dp),
                            horizontalArrangement = Arrangement.spacedBy(60.dp)) {
                            Column(Modifier.weight(1f)) { body() }
                            SharedCodeCard(Modifier.width(300.dp))
                        }
                    }
                }
                item {
                    val about: @Composable () -> Unit = {
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            EditorialSectionHeading("About me")
                            Text("I’m passionate about turning complex ideas into elegant, intuitive apps that perform well and feel great to use.",
                                style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    val skills: @Composable () -> Unit = {
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            EditorialSectionHeading("Skills")
                            SkillWidget()
                        }
                    }
                    if (compact) {
                        Column(pageModifier(horizontal).padding(top = 70.dp),
                            verticalArrangement = Arrangement.spacedBy(50.dp)) { about(); skills() }
                    } else {
                        Row(pageModifier(horizontal).padding(top = 100.dp),
                            horizontalArrangement = Arrangement.spacedBy(70.dp)) {
                            Column(Modifier.weight(1f)) { about() }
                            Column(Modifier.weight(1f)) { skills() }
                        }
                    }
                }
                if (hasProjects) {
                    item {
                        Column(pageModifier(horizontal).padding(top = if (compact) 90.dp else 120.dp),
                            verticalArrangement = Arrangement.spacedBy(25.dp)) {
                            EditorialSectionHeading("Projects", "${projects.value.size} projects across Android and shared Kotlin.")
                            AppGallery(compact)
                        }
                    }
                    item {
                        Column(pageModifier(horizontal).padding(top = 18.dp)) {
                            ProjectWidget(projects.value.values) { component.getProjectsIcon(it) }
                        }
                    }
                }
                if (hasJobs) {
                    item {
                        Column(pageModifier(horizontal).padding(top = if (compact) 90.dp else 120.dp, bottom = 25.dp)) {
                            EditorialSectionHeading("Where I’ve worked", "Mobile products, commerce, and platform work.")
                        }
                    }
                    item {
                        Column(pageModifier(horizontal)) {
                            WorkedAtWidget(jobs.value.values.toList().sortedBy { it.order }) { component.getJobsIcon(it) }
                        }
                    }
                }
                item {
                    Column(pageModifier(horizontal).padding(top = 110.dp, bottom = 90.dp)) {
                        ContactMeWidget()
                    }
                }
                item { EditorialFooter() }
            }
        }
    }
}

private fun pageModifier(horizontal: Dp) = Modifier.widthIn(max = 1440.dp).fillMaxWidth().padding(horizontal = horizontal)

@Composable
private fun SharedCodeCard(modifier: Modifier) {
    Column(modifier.background(MaterialTheme.colorScheme.primaryContainer).height(260.dp).padding(28.dp),
        verticalArrangement = Arrangement.SpaceBetween) {
        Text("Shared\ncode.", fontSize = 58.sp, lineHeight = 58.sp,
            fontWeight = FontWeight.Bold, letterSpacing = (-3).sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text("Across platforms.", color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun AppGallery(compact: Boolean) {
    val images = listOf(
        Res.drawable.watchdog_01 to "WatchDog blocked app screen",
        Res.drawable.watchdog_02 to "WatchDog rules screen",
        Res.drawable.kaffeine_02 to "Kaffeine permission screen",
        Res.drawable.kaffeine_03 to "Kaffeine Quick Settings control"
    )
    val intro: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("Focus rules.\nScreen awake.", fontSize = if (compact) 32.sp else 42.sp,
                lineHeight = if (compact) 36.sp else 46.sp,
                fontWeight = FontWeight.Bold, color = Color.White)
            Text("WatchDog blocks distractions on a schedule. Kaffeine keeps the screen on when a task needs it.",
                color = Color.White)
        }
    }
    val gallery: @Composable () -> Unit = {
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            images.forEach { (image, description) ->
                Image(painterResource(image), contentDescription = description,
                    contentScale = ContentScale.Fit, modifier = Modifier.width(120.dp).height(230.dp))
            }
        }
    }
    if (compact) {
        Column(Modifier.fillMaxWidth().background(Color(0xFF172431)).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)) { intro(); gallery() }
    } else {
        Row(Modifier.fillMaxWidth().background(Color(0xFF172431)).padding(34.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(.7f)) { intro() }
            Column(Modifier.weight(1.3f)) { gallery() }
        }
    }
}
