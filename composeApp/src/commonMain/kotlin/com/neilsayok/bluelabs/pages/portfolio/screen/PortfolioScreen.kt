package com.neilsayok.bluelabs.pages.portfolio.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.pages.portfolio.component.PortfolioComponent
import com.neilsayok.bluelabs.pages.portfolio.widgets.AboutMe
import com.neilsayok.bluelabs.pages.portfolio.widgets.ContactMeWidget
import com.neilsayok.bluelabs.pages.portfolio.widgets.ProjectWidget
import com.neilsayok.bluelabs.pages.portfolio.widgets.SectionTitle
import com.neilsayok.bluelabs.pages.portfolio.widgets.SkillWidget
import com.neilsayok.bluelabs.pages.portfolio.widgets.WorkedAtWidget
import com.neilsayok.bluelabs.util.setMetaTag
import com.neilsayok.bluelabs.util.setOpenGraphTags
import com.neilsayok.bluelabs.util.setPageTitle
import com.neilsayok.bluelabs.util.setTwitterCardTags
import kotlinx.coroutines.launch

@Composable
fun PortfolioScreen(component: PortfolioComponent) {

    val uiState by component.uiState.subscribeAsState()

    val projects = uiState.projectsFileContents.subscribeAsState()
    val jobs = uiState.jobsFileContents.subscribeAsState()

    // Subscribe to API states to trigger recomposition
    val jobsApiState by uiState.jobs.subscribeAsState()
    val projectsApiState by uiState.projects.subscribeAsState()

    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()


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
            url = "${BuildKonfig.BASE_URL}/portfolio",
            type = "profile"
        )

        setTwitterCardTags(
            title = "Portfolio : Sayok Dey Majumder",
            description = "A portfolio website of Sayok Dey Majumder",
            image = "https://avatars.githubusercontent.com/u/21328143?v=4",
        )
    }

    val visibleItemsInfo: List<LazyListItemInfo> by remember {
        derivedStateOf { scrollState.layoutInfo.visibleItemsInfo }
    }

    LoaderScaffold(
        calledApis = listOf(jobsApiState, projectsApiState)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Hi I am Sayok", style = MaterialTheme.typography.headlineLarge)
            }

            item {
                SectionTitle("About Me")
            }

            item {
                AboutMe(onProjectClick = {
                    scope.launch {
                        val targetItem = visibleItemsInfo.find { it.key == "projects" }
                        if (scrollState.layoutInfo.totalItemsCount > 0 && targetItem != null) {
                            scrollState.animateScrollToItem(targetItem.index)
                        }
                    }
                }) {
                    scope.launch {
                        if (scrollState.layoutInfo.totalItemsCount > 0) {
                            scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount + 1)
                        }
                    }
                }
            }

            item {
                SectionTitle("Skills")
            }

            item(key = "projects") {
                SkillWidget()
            }

            if (projects.value.isNotEmpty()) {
                item {
                    SectionTitle("Projects")
                }
                item {
                    ProjectWidget(
                        projectsFileContent = projects.value.values,
                        getProjectIcon = { content -> component.getProjectsIcon(content) })
                }
            }




            if (jobs.value.isNotEmpty()) {
                item {
                    SectionTitle("Where I've worked")
                }

                item {
                    WorkedAtWidget(
                        jobs = jobs.value.values.toList().sortedBy { it.order },
                        getJobsIcon = { content -> component.getJobsIcon(content) })
                }
            }



            item {
                SectionTitle("Contact Me")
            }

            item {
                ContactMeWidget()
            }

            item { Spacer(Modifier.size(4.dp)) }

        }


    }
}