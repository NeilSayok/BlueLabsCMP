package com.neilsayok.bluelabs.pages.blog.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.BuildKonfig
import com.neilsayok.bluelabs.common.constants.BLOG_PAGE
import com.neilsayok.bluelabs.common.ui.components.LoaderScaffold
import com.neilsayok.bluelabs.pages.blog.component.BlogComponent
import com.neilsayok.bluelabs.pages.blog.widgets.BlogCard
import com.neilsayok.bluelabs.util.Platform
import com.neilsayok.bluelabs.util.getPlatform
import com.neilsayok.bluelabs.util.layoutType
import com.neilsayok.bluelabs.util.setMetaTag
import com.neilsayok.bluelabs.util.setOpenGraphTags
import com.neilsayok.bluelabs.util.setPageTitle
import com.neilsayok.bluelabs.util.setTwitterCardTags

@Composable
fun BlogScreen(component: BlogComponent) {

    val blogList by component.blogState.subscribeAsState()
    val blog = blogList.firstOrNull { it?.urlStr?.stringValue == component.blogUrl }

    LaunchedEffect(blog){
        blog?.let {
            setPageTitle(it.title?.stringValue)
            setMetaTag("description" , it.title?.stringValue?:"")
            setMetaTag("viewport" , "width=device-width, initial-scale=1.0")
            setMetaTag("author" , it.author?.name?.stringValue?:"")
            setMetaTag("robots" , "index, follow")

            setOpenGraphTags(
                title = it.title?.stringValue,
                description = it.title?.stringValue,
                image = it.bigImg?.stringValue,
                url = "${BuildKonfig.BASE_URL}/$BLOG_PAGE/${it.urlStr?.stringValue}",
                type = "article"
            )

            setTwitterCardTags(
                title = it.title?.stringValue,
                description = it.title?.stringValue,
                image = it.bigImg?.stringValue
            )
        }
    }

    LoaderScaffold(
        isLoading = blog == null,
        isError = false
    ) {
        LazyColumn() {
            item {
                blog?.let {
                    BlogCard(it, component)
                }
            }
        }


    }


}