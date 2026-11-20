package com.neilsayok.bluelabs.pages.indexer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.domain.util.Response
import com.neilsayok.bluelabs.pages.indexer.component.IndexerComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexerScreen(component: IndexerComponent) {
    val state by component.uiState.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState(rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden, skipHiddenState = false))
    var password by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var shouldHideSheet by remember { mutableStateOf(false) }
    LaunchedEffect(resultMessage) {
        resultMessage?.let { snackbarHostState.showSnackbar(it); resultMessage = null }
    }
    LaunchedEffect(shouldHideSheet) {
        if (shouldHideSheet) {
            sheetState.bottomSheetState.hide()
            password = ""
            shouldHideSheet = false
        }
    }
    BottomSheetScaffold(
        scaffoldState = sheetState,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text("Generate index", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Enter your administrator password to continue.")
                OutlinedTextField(password, { password = it }, label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = { shouldHideSheet = true }, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(onClick = {
                        scope.launch {
                            component.handleSubmit(password) { _, message -> resultMessage = message }
                            shouldHideSheet = true
                        }
                    }, enabled = password.isNotBlank(), shape = RectangleShape,
                        modifier = Modifier.weight(1f)) { Text("Generate") }
                }
            }
        }
    ) { paddingValues ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(paddingValues)) {
            val compact = maxWidth < 800.dp
            val horizontal = if (compact) 20.dp else 64.dp
            val blogCount = (state.blogResponse as? Response.SuccessResponse)?.data?.documents?.size ?: 0
            val authorCount = (state.authorResponse as? Response.SuccessResponse)?.data?.documents?.size ?: 0
            LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Column(Modifier.widthIn(max = 1100.dp).fillMaxWidth().padding(horizontal = horizontal, vertical = 45.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Blog index generator", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Medium)
                        Text("Keep the journal’s search content up to date.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                item {
                    Column(Modifier.widthIn(max = 1100.dp).fillMaxWidth().padding(horizontal = horizontal),
                        verticalArrangement = Arrangement.spacedBy(28.dp)) {
                        if (state.isLoading) {
                            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                CircularProgressIndicator()
                                Text("Loading index data…")
                            }
                        } else {
                            Text("The index uses the published articles and authors currently loaded.",
                                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer).padding(20.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Stat("Blogs loaded", blogCount, Modifier.weight(1f))
                                Stat("Authors loaded", authorCount, Modifier.weight(1f))
                            }
                            if (state.error != null) {
                                Text("Index data couldn’t load: ${state.error}", color = MaterialTheme.colorScheme.error)
                            }
                            val ready: @Composable () -> Unit = {
                                Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(28.dp),
                                    verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    Text("Ready to generate", style = MaterialTheme.typography.headlineSmall)
                                    Text("The index includes published article text, tags, titles, authors, and URLs.")
                                    Text("• Reads article Markdown from GitHub\n• Converts content to searchable text\n• Updates the stored search index")
                                    Button(onClick = { scope.launch { sheetState.bottomSheetState.expand() } },
                                        enabled = state.blogResponse is Response.SuccessResponse && state.authorResponse is Response.SuccessResponse,
                                        shape = RectangleShape) {
                                        Text("Generate index")
                                        Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                                    }
                                }
                            }
                            val authorisation: @Composable () -> Unit = {
                                Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(28.dp),
                                    verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    Text("Authorisation", style = MaterialTheme.typography.headlineSmall)
                                    Text("Your administrator password is requested before generation.")
                                }
                            }
                            if (compact) {
                                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) { ready(); authorisation() }
                            } else {
                                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                    Column(Modifier.weight(2f)) { ready() }
                                    Column(Modifier.weight(1f)) { authorisation() }
                                }
                            }
                        }
                    }
                }
                item { Column(Modifier.padding(top = 80.dp)) { EditorialFooter() } }
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: Int, modifier: Modifier) {
    Column(modifier.background(MaterialTheme.colorScheme.surfaceVariant).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value.toString(), fontSize = 36.sp)
    }
}
