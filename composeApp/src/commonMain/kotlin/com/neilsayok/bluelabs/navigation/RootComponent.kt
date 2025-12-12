package com.neilsayok.bluelabs.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.childStackWebNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.webhistory.WebNavigation
import com.arkivanov.decompose.router.webhistory.WebNavigationOwner
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.neilsayok.bluelabs.common.constants.BLOG_PAGE
import com.neilsayok.bluelabs.common.constants.EDITOR_PAGE
import com.neilsayok.bluelabs.common.constants.INDEXER_PAGE
import com.neilsayok.bluelabs.common.constants.PAGE_NOT_FOUND_PAGE
import com.neilsayok.bluelabs.common.constants.PORTFOLIO_PAGE
import com.neilsayok.bluelabs.common.constants.PRIVACY_POLICY_PAGE
import com.neilsayok.bluelabs.common.constants.SEARCH_PAGE
import com.neilsayok.bluelabs.data.bloglist.BlogLoadedFields
import com.neilsayok.bluelabs.data.bloglist.Document
import com.neilsayok.bluelabs.data.bloglist.FirebaseResponse
import com.neilsayok.bluelabs.data.bloglist.toBlogLoadedDate
import com.neilsayok.bluelabs.data.documents.AuthorFields
import com.neilsayok.bluelabs.data.documents.BlogFields
import com.neilsayok.bluelabs.data.documents.GenreFields
import com.neilsayok.bluelabs.data.documents.IndexFields
import com.neilsayok.bluelabs.data.documents.ProfileFields
import com.neilsayok.bluelabs.domain.firebase.FirebaseRepo
import com.neilsayok.bluelabs.domain.util.Response
import com.neilsayok.bluelabs.pages.blog.component.BlogComponent
import com.neilsayok.bluelabs.pages.editor.component.EditorComponent
import com.neilsayok.bluelabs.pages.editor.component.PageNotFoundComponent
import com.neilsayok.bluelabs.pages.home.component.HomeComponent
import com.neilsayok.bluelabs.pages.indexer.component.IndexerComponent
import com.neilsayok.bluelabs.pages.portfolio.component.PortfolioComponent
import com.neilsayok.bluelabs.pages.privacy.component.PrivacyPolicyComponent
import com.neilsayok.bluelabs.pages.search.component.SearchComponent
import com.neilsayok.bluelabs.util.BackgroundDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@OptIn(ExperimentalDecomposeApi::class)
interface MyStackComponent : WebNavigationOwner {
    val stack: Value<ChildStack<*, RootComponent.Child>>
}

@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    componentContext: ComponentContext,
    deepLinkUrl: String? = null,
) : MyStackComponent, KoinComponent, ComponentContext by componentContext {

    private val coroutineScope: CoroutineScope = CoroutineScope(BackgroundDispatcher)
    private val firebaseRepo: FirebaseRepo by inject()

    private val _blogState = MutableValue<List<BlogLoadedFields?>>(emptyList())
    val blogState: Value<List<BlogLoadedFields?>> = _blogState

    private val _blogListState = MutableValue<Response<FirebaseResponse<BlogFields>>>(Response.None)
    val blogListState: Value<Response<FirebaseResponse<BlogFields>>> = _blogListState


    private val _authorListState =
        MutableValue<Response<FirebaseResponse<AuthorFields>>>(Response.None)
    val authorListState: Value<Response<FirebaseResponse<AuthorFields>>> = _authorListState


    private val _indexListState =
        MutableValue<Response<FirebaseResponse<IndexFields>>>(Response.None)
    val indexListState: Value<Response<FirebaseResponse<IndexFields>>> = _indexListState

    private val _profileListState =
        MutableValue<Response<FirebaseResponse<ProfileFields>>>(Response.None)
    val profileListState: Value<Response<FirebaseResponse<ProfileFields>>> = _profileListState

    private val _genreListState =
        MutableValue<Response<FirebaseResponse<GenreFields>>>(Response.None)
    val genreListState: Value<Response<FirebaseResponse<GenreFields>>> = _genreListState

    private val _currentScreen =
        MutableValue<Configuration>(Configuration.HomeScreen)
    val currentScreen: Value<Configuration> = _currentScreen

    init {
        coroutineScope.launch {
            try {
                coroutineScope {
                    val blogsDeferred = async { firebaseRepo.getAllBlogs() }
                    val authorsDeferred = async { firebaseRepo.getAllAuthor() }
                    val indexesDeferred = async { firebaseRepo.getAllIndex() }
                    val profilesDeferred = async { firebaseRepo.getAllProfiles() }
                    val genresDeferred = async { firebaseRepo.getAllGenre() }

                    blogsDeferred.await().onEach { resp -> _blogListState.value = resp }
                        .launchIn(this)
                    authorsDeferred.await().onEach { resp -> _authorListState.value = resp }
                        .launchIn(this)
                    indexesDeferred.await().onEach { resp -> _indexListState.value = resp }
                        .launchIn(this)
                    profilesDeferred.await().onEach { resp -> _profileListState.value = resp }
                        .launchIn(this)
                    genresDeferred.await().onEach { resp -> _genreListState.value = resp }
                        .launchIn(this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private val navigation = StackNavigation<Configuration>()

    private val _stack: Value<ChildStack<Configuration, Child>> = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialStack = { initialConfig(deepLinkUrl) },
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val stack: Value<ChildStack<*, Child>> = _stack

    override val webNavigation: WebNavigation<*> = childStackWebNavigation(
        navigator = navigation,
        stack = _stack,
        serializer = Configuration.serializer(),
        pathMapper = { child -> child.configuration.path },
        childSelector = { child -> child.instance as? WebNavigationOwner },
    )

    private fun createChild(
        config: Configuration, context: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.HomeScreen -> Child.Home(
                HomeComponent(
                    componentContext = context,
                    navigateBlogScreen = { blog -> navigation.pushNew(Configuration.BlogScreen(blog.urlStr?.stringValue ?: "")) },
                    blogState = blogState,
                )
            )

            is Configuration.BlogScreen -> {
                Child.Blog(
                    BlogComponent(
                        blogUrl = config.blogUrl,
                        blogState = blogState,
                        componentContext = context,
                        navigateBack = { navigation.pop() })
                )
            }

            Configuration.EditorScreen -> Child.Editor(
                EditorComponent(
                    componentContext = context,
                    genreResponse = genreListState,
                    authorResponse = authorListState
                )
            )
            Configuration.IndexerScreen -> Child.Indexer(
                IndexerComponent(
                    componentContext = context,
                    blogResponse = blogListState,
                    authorResponse = authorListState
                )
            )
            Configuration.PortfolioScreen -> Child.Portfolio(PortfolioComponent(componentContext = context))
            Configuration.PrivacyPolicyScreen -> Child.PrivacyPolicy(
                PrivacyPolicyComponent(
                    componentContext = context
                )
            )

            is Configuration.SearchScreen -> Child.Search(
                SearchComponent(
                    componentContext = context,
                    key = config.key,
                    index = indexListState,
                    blogState = blogState,
                    authorState = authorListState,
                    navigateBlogScreen = { blog -> navigation.pushNew(Configuration.BlogScreen(blog.urlStr?.stringValue ?: "")) },
                )
            )

            else -> Child.PageNotFound(
                PageNotFoundComponent(
                    componentContext = context
                )
            )


        }


    }

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
        class Blog(val component: BlogComponent) : Child()
        class Editor(val component: EditorComponent) : Child()
        class Indexer(val component: IndexerComponent) : Child()
        class Portfolio(val component: PortfolioComponent) : Child()
        class PrivacyPolicy(val component: PrivacyPolicyComponent) : Child()
        class Search(val component: SearchComponent) : Child()
        class PageNotFound(val component: PageNotFoundComponent) : Child()

    }

    @Serializable
    sealed class Configuration(val path: String) {
        @Serializable
        data object HomeScreen : Configuration("/")

        @Serializable
        data object EditorScreen : Configuration("/$EDITOR_PAGE")

        @Serializable
        data object IndexerScreen : Configuration("/$INDEXER_PAGE")

        @Serializable
        data object PrivacyPolicyScreen : Configuration("/$PRIVACY_POLICY_PAGE")

        @Serializable
        data object PortfolioScreen : Configuration("/$PORTFOLIO_PAGE")

        @Serializable
        data object PageNotFoundScreen : Configuration("/$PAGE_NOT_FOUND_PAGE")

        @Serializable
        data class BlogScreen(val blogUrl: String) :
            Configuration("/$BLOG_PAGE/$blogUrl")

        @Serializable
        data class SearchScreen(val key: String) : Configuration("/$SEARCH_PAGE/$key")
    }

    private fun initialConfig(deepLinkUrl: String?): List<Configuration> {
        // Parse the deep link and initialize navigation state
        val deepLink = deepLinkUrl?.let { parseDeepLink(it) }
        return if (deepLink != null) {
            listOf(deepLink)
        } else {
            listOf(Configuration.HomeScreen)
        }
    }

    private fun parseDeepLink(url: String): Configuration? {
        val pathSegments = url.split("/").filter { it.isNotEmpty() }.drop(2)

        return when {
            pathSegments.isEmpty() -> Configuration.HomeScreen
            pathSegments.contains(EDITOR_PAGE) -> Configuration.EditorScreen
            pathSegments.contains(INDEXER_PAGE) -> Configuration.IndexerScreen
            pathSegments.contains(PRIVACY_POLICY_PAGE) -> Configuration.PrivacyPolicyScreen
            pathSegments.contains(PORTFOLIO_PAGE) -> Configuration.PortfolioScreen

            pathSegments.size == 2 && pathSegments[0] == BLOG_PAGE -> {
                val blogUrl = pathSegments[1]
                Configuration.BlogScreen(blogUrl)
            }

            pathSegments.size == 2 && pathSegments[0] == SEARCH_PAGE -> {
                val key = pathSegments[1]
                Configuration.SearchScreen(key)
            }

            else -> Configuration.PageNotFoundScreen
        }

    }

    fun mergeData() {
        coroutineScope.launch {

            val blogs = mutableListOf<BlogLoadedFields?>()
            val blogState = blogListState.value
            val authorState = authorListState.value
            val genreState = genreListState.value
            val profileState = profileListState.value


            if (blogState is Response.SuccessResponse) {
                blogState.data?.documents?.forEach { doc: Document<BlogFields>? ->
                    val blog = doc?.fields

                    val author = if (authorState is Response.SuccessResponse) {
                        authorState.data?.documents?.firstOrNull { d: Document<AuthorFields>? -> d?.name == blog?.author?.referenceValue }?.fields
                    } else null

                    val profile: ProfileFields? = if (profileState is Response.SuccessResponse) {
                        profileState.data?.documents?.firstOrNull { d: Document<ProfileFields>? -> d?.name == author?.profiles?.referenceValue }?.fields
                    } else null

                    val genre: GenreFields? = if (genreState is Response.SuccessResponse) {
                        genreState.data?.documents?.firstOrNull { d: Document<GenreFields>? -> d?.name == blog?.genre?.referenceValue }?.fields
                    } else null

                    blogs.add(blog?.toBlogLoadedDate(author, genre, profile))
                }
            }

            _blogState.value = emptyList()
            _blogState.value = blogs.toList()

        }


    }

    @OptIn(DelicateDecomposeApi::class)
    fun onNavigationEvent(event: NavigationEvent) {
        when (event) {
            NavigationEvent.NavigateHome -> navigation.replaceAll(Configuration.HomeScreen)
            NavigationEvent.NavigateUp -> navigation.pop()
            NavigationEvent.NavigatePrivacyPolicy -> navigation.pushToFront(Configuration.PrivacyPolicyScreen)
            NavigationEvent.NavigatePortfolio -> navigation.pushToFront(Configuration.PortfolioScreen)
            is NavigationEvent.NavigateSearch -> navigation.pushToFront(Configuration.SearchScreen(key = event.key))
        }
    }

    fun observeDestinationChanges() {
        stack.subscribe { stack ->
            val currentConfig: Configuration = stack.active.configuration as Configuration
            _currentScreen.value = currentConfig
        }
    }

}

sealed class NavigationEvent {
    data object NavigateUp : NavigationEvent()
    data object NavigateHome : NavigationEvent()
    data object NavigatePrivacyPolicy : NavigationEvent()
    data object NavigatePortfolio : NavigationEvent()
    data class NavigateSearch(val key : String) : NavigationEvent()
}







