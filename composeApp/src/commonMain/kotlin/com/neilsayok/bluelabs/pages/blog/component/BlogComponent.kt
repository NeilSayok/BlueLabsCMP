package com.neilsayok.bluelabs.pages.blog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.neilsayok.bluelabs.data.bloglist.BlogLoadedFields
import com.neilsayok.bluelabs.data.github.GithubResponse
import com.neilsayok.bluelabs.domain.github.GithubRepo
import com.neilsayok.bluelabs.domain.util.Response
import com.neilsayok.bluelabs.util.BackgroundDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BlogComponent(
    val blogUrl: String,
    val blogState: Value<List<BlogLoadedFields?>>,
    private val navigateBack: () -> Unit,
    componentContext: ComponentContext,
) : ComponentContext by componentContext, KoinComponent {

    val githubRepo: GithubRepo by inject()
    private val coroutineScope: CoroutineScope = CoroutineScope(BackgroundDispatcher)

    private val _readmeContentState =
        MutableValue<Response<GithubResponse>>(Response.None)
    val readmeContentState: Value<Response<GithubResponse>> = _readmeContentState

    // Computed property that reactively gets the blog from the state
    val blog: BlogLoadedFields?
        get() = blogState.value.firstOrNull { it?.urlStr?.stringValue == blogUrl }

    fun getBlogContent(fileName: String) {
        coroutineScope.launch {
            val readmeDeferred = async { githubRepo.getContent(fileName) }
            readmeDeferred.await().onEach { resp -> _readmeContentState.value = resp }
                .launchIn(this)
        }
    }


}