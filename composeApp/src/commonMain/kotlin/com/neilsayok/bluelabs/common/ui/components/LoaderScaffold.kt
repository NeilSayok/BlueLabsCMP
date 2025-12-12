package com.neilsayok.bluelabs.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.value.MutableValue
import com.neilsayok.bluelabs.domain.util.Response


@Composable
fun LoaderScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit) = {},
    bottomBar: @Composable (() -> Unit) = {},
    snackbarHost: @Composable (() -> Unit) = {},
    floatingActionButton: @Composable (() -> Unit) = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    calledApis: List<Response<Any>>,
    content: @Composable ((PaddingValues) -> Unit),

    ) {

    val allApisDone = derivedStateOf {
        calledApis.all { api ->
            when (api) {
                is Response.Loading -> false
                is Response.SuccessResponse -> true
                is Response.ExceptionResponse -> true
                is Response.None -> true
            }
        }
    }

    val hasError = derivedStateOf {
        calledApis.any { api ->
            api is Response.ExceptionResponse
        }
    }

    LoaderScaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        isLoading = allApisDone.value.not(),
        isError = hasError.value,
        content = content
    )


}


@Composable
fun LoaderScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    isLoading: Boolean? = false,
    isError: Boolean? = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    LaunchedEffect(isError) {
        if (isError == true) {
            //TODO Toast
            //showToast(context, SOMETHING_WENT_WRONG)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = { padding ->

            content.invoke(padding)

            if (isLoading == true)
                CommonLoader(modifier = Modifier.padding(paddingValues = padding))

        }
    )

}