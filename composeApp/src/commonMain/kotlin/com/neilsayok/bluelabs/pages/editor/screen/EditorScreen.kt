package com.neilsayok.bluelabs.pages.editor.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.neilsayok.bluelabs.common.ui.EditorialFooter
import com.neilsayok.bluelabs.domain.util.Response
import com.neilsayok.bluelabs.pages.editor.component.EditorComponent
import com.neilsayok.bluelabs.pages.editor.component.state.EditorState
import com.neilsayok.bluelabs.pages.editor.component.state.ValidationError
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(component: EditorComponent) {
    val state by component.uiState.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState(rememberStandardBottomSheetState(skipHiddenState = false))
    var password by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { snackbarHostState.showSnackbar(it); snackbarMessage = null }
    }
    BottomSheetScaffold(
        scaffoldState = sheetState,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text("Submit blog", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Enter your author password to continue.")
                OutlinedTextField(password, { password = it }, label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = { scope.launch { sheetState.bottomSheetState.hide(); password = "" } },
                        modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(onClick = {
                        scope.launch {
                            component.submitBlog(password) { success, message ->
                                snackbarMessage = message
                                if (success) password = ""
                            }
                            sheetState.bottomSheetState.hide()
                        }
                    }, enabled = password.isNotBlank() && !state.isSubmitting,
                        shape = RectangleShape, modifier = Modifier.weight(1f)) {
                        if (state.isSubmitting) CircularProgressIndicator(Modifier.size(16.dp), color = MaterialTheme.colorScheme.onPrimary)
                        else Text("Submit")
                    }
                }
            }
        }
    ) { paddingValues ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(paddingValues)) {
            val compact = maxWidth < 900.dp
            val horizontal = if (compact) 20.dp else 64.dp
            LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Column(Modifier.widthIn(max = 1228.dp).fillMaxWidth()
                        .padding(horizontal = horizontal, vertical = 42.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Create a blog", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Medium)
                        Text("Add the details for your next article.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                item {
                    if (state.isLoading) {
                        Column(Modifier.widthIn(max = 1100.dp).fillMaxWidth().padding(horizontal = horizontal)
                            .background(MaterialTheme.colorScheme.surface).padding(28.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            CircularProgressIndicator()
                            Text("Loading editor data…")
                        }
                    } else {
                        val form: @Composable () -> Unit = {
                            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(if (compact) 22.dp else 28.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                Text("Article details", style = MaterialTheme.typography.headlineSmall)
                                Text("All fields marked with * are mandatory", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                EditorForm(component, state)
                                Button(onClick = { scope.launch { sheetState.bottomSheetState.expand() } },
                                    enabled = !state.isSubmitting && state.validationErrors.isEmpty(),
                                    shape = RectangleShape) {
                                    Text("Submit blog")
                                    Icon(Icons.Default.NorthEast, null, Modifier.padding(start = 8.dp))
                                }
                                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                            }
                        }
                        val preview: @Composable () -> Unit = {
                            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(if (compact) 22.dp else 28.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                Text("Article preview", style = MaterialTheme.typography.headlineSmall)
                                EditorPreview(component, state)
                            }
                        }
                        if (compact) {
                            Column(Modifier.widthIn(max = 1228.dp).fillMaxWidth().padding(horizontal = horizontal),
                                verticalArrangement = Arrangement.spacedBy(24.dp)) { form(); preview() }
                        } else {
                            Row(Modifier.widthIn(max = 1228.dp).fillMaxWidth().padding(horizontal = horizontal),
                                horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                Column(Modifier.weight(7f)) { form() }
                                Column(Modifier.weight(4f)) { preview() }
                            }
                        }
                    }
                }
                item { Column(Modifier.padding(top = 90.dp)) { EditorialFooter() } }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorForm(component: EditorComponent, state: EditorState) {
    var genreExpanded by remember { mutableStateOf(false) }
    var authorExpanded by remember { mutableStateOf(false) }
    var tagInput by remember { mutableStateOf("") }
    val form = state.formData
    val genres = (state.genreResponse as? Response.SuccessResponse)?.data?.documents ?: emptyList()
    val authors = (state.authorResponse as? Response.SuccessResponse)?.data?.documents ?: emptyList()
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        ExposedDropdownMenuBox(expanded = genreExpanded, onExpandedChange = { genreExpanded = !genreExpanded }) {
            OutlinedTextField(
                value = genres.find { it?.name?.substringAfterLast("/") == form.selectedGenre }?.fields?.type?.stringValue ?: "",
                onValueChange = {}, readOnly = true, label = { Text("Genre *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(genreExpanded) },
                isError = ValidationError.GENRE_EMPTY in state.validationErrors,
                supportingText = if (ValidationError.GENRE_EMPTY in state.validationErrors) {{ Text("Genre is required") }} else null,
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = genreExpanded, onDismissRequest = { genreExpanded = false }) {
                genres.forEach { genre -> genre?.let {
                    val id = it.name?.substringAfterLast("/") ?: ""
                    DropdownMenuItem(text = { Text(it.fields?.type?.stringValue ?: "") }, onClick = {
                        component.updateFormData(form.copy(selectedGenre = id)); genreExpanded = false
                    })
                } }
            }
        }
        ExposedDropdownMenuBox(expanded = authorExpanded, onExpandedChange = { authorExpanded = !authorExpanded }) {
            OutlinedTextField(
                value = authors.find { it?.name?.substringAfterLast("/") == form.selectedAuthor }?.fields?.name?.stringValue ?: "",
                onValueChange = {}, readOnly = true, label = { Text("Author *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(authorExpanded) },
                isError = ValidationError.AUTHOR_EMPTY in state.validationErrors,
                supportingText = if (ValidationError.AUTHOR_EMPTY in state.validationErrors) {{ Text("Author is required") }} else null,
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = authorExpanded, onDismissRequest = { authorExpanded = false }) {
                authors.forEach { author -> author?.let {
                    val id = it.name?.substringAfterLast("/") ?: ""
                    DropdownMenuItem(text = { Text("${it.fields?.name?.stringValue ?: ""} (${it.fields?.uid?.stringValue ?: ""})") },
                        onClick = { component.updateFormData(form.copy(selectedAuthor = id)); authorExpanded = false })
                } }
            }
        }
        OutlinedTextField(form.title, { if (it.length <= 2000) component.updateFormData(form.copy(title = it)) },
            label = { Text("Title *") }, placeholder = { Text("Give your article a title") },
            isError = ValidationError.TITLE_EMPTY in state.validationErrors || ValidationError.TITLE_TOO_LONG in state.validationErrors,
            supportingText = { Text("${2000 - form.title.length} characters available") },
            modifier = Modifier.fillMaxWidth())
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(tagInput, { tagInput = it }, label = { Text("Tags *") },
                placeholder = { Text("Type a tag and press Enter") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (tagInput.isNotBlank()) { component.addTag(tagInput.trim()); tagInput = "" }
                }),
                isError = ValidationError.TAGS_EMPTY in state.validationErrors || ValidationError.TAGS_TOO_MANY in state.validationErrors,
                supportingText = { Text("At least one tag, up to four. Tap a tag to remove it.") },
                modifier = Modifier.fillMaxWidth())
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                form.tags.forEachIndexed { index, tag ->
                    AssistChip(onClick = { component.removeTag(index) }, label = { Text(tag) },
                        trailingIcon = { Icon(Icons.Default.Close, "Remove $tag", Modifier.size(16.dp)) })
                }
            }
        }
        OutlinedTextField(form.tinyImageUrl, { component.updateFormData(form.copy(tinyImageUrl = it)) },
            label = { Text("Small image URL *") }, modifier = Modifier.fillMaxWidth(),
            isError = ValidationError.TINY_IMAGE_URL_EMPTY in state.validationErrors)
        OutlinedTextField(form.bigImageUrl, { component.updateFormData(form.copy(bigImageUrl = it)) },
            label = { Text("Header image URL *") }, modifier = Modifier.fillMaxWidth(),
            isError = ValidationError.BIG_IMAGE_URL_EMPTY in state.validationErrors)
        OutlinedTextField(form.mdFileName, { fileName ->
            component.updateFormData(form.copy(mdFileName = fileName))
            if (fileName.endsWith(".md")) component.loadMarkdownFromFile(fileName)
        }, label = { Text("Markdown file name *") },
            supportingText = { Text(if (form.markdownContent.isNotBlank()) "Markdown loaded" else "A .md file loads content from GitHub.") },
            isError = ValidationError.MD_FILE_EMPTY in state.validationErrors,
            modifier = Modifier.fillMaxWidth())
        if (form.markdownContent.isNotBlank()) {
            Text("${component.calculateReadTime(form.markdownContent)} min read",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun EditorPreview(component: EditorComponent, state: EditorState) {
    val form = state.formData
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(Modifier.fillMaxWidth().height(170.dp).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.BottomStart) {
            if (form.bigImageUrl.isNotBlank()) {
                AsyncImage(form.bigImageUrl, contentDescription = "Header image preview",
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                Text("Blue Labs", modifier = Modifier.padding(20.dp), fontSize = 38.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        Text(form.title.ifBlank { "Your article title" }, style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Text("Sayok Dey Majumder", style = MaterialTheme.typography.bodySmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            form.tags.forEach { tag -> Text(tag, modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(6.dp),
                style = MaterialTheme.typography.labelSmall) }
        }
        Text(if (form.markdownContent.isNotBlank()) "${component.calculateReadTime(form.markdownContent)} min read"
            else "Reading time appears after Markdown loads.", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (form.tinyImageUrl.isNotBlank()) {
            AsyncImage(form.tinyImageUrl, contentDescription = "Small image preview",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp))
        }
    }
}
