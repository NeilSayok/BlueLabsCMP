package com.neilsayok.bluelabs.ui.commonUi.markdown

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.elements.MarkdownCodeBackground
import com.mikepenz.markdown.compose.elements.MarkdownCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownCodeFence
import com.mikepenz.markdown.compose.elements.MarkdownText
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import org.intellij.markdown.ast.ASTNode

/** Default definition for the [MarkdownHighlightedCodeFence]. Uses default theme, attempts to apply language from markdown. */
val highlightedCodeFence: MarkdownComponent = { MarkdownHighlightedCodeFence(it.content, it.node) }

/** Default definition for the [MarkdownHighlightedCodeBlock]. Uses default theme, attempts to apply language from markdown. */
val highlightedCodeBlock: MarkdownComponent = { MarkdownHighlightedCodeBlock(it.content, it.node) }


@Composable
fun MarkdownHighlightedCodeFence(
    content: String,
    node: ASTNode,
    highlights: Highlights.Builder = Highlights.Builder().theme(SyntaxThemes.monokai())
) {
    MarkdownCodeFence(content, node) { code, language ->
        MarkdownHighlightedCode(code, language, highlights)

    }

}

@Composable
fun MarkdownHighlightedCodeBlock(
    content: String, node: ASTNode,
    highlights: Highlights.Builder = Highlights.Builder().theme(
        SyntaxThemes.monokai()
    ),
) {
    MarkdownCodeBlock(content, node) { code, language ->
        MarkdownHighlightedCode(code, language, highlights)
    }

}

@Composable
fun MarkdownHighlightedCode(
    code: String,
    language: String?,
    highlights: Highlights.Builder = Highlights.Builder(),
    style: TextStyle = LocalMarkdownTypography.current.code,
) {

    val backgroundCodeColor = LocalMarkdownColors.current.codeBackground
    val codeBackgroundCornerSize = LocalMarkdownDimens.current.codeBackgroundCornerSize
    val codeBlockPadding = LocalMarkdownPadding.current.codeBlock
    val syntaxLanguage = remember(language) { language?.let { SyntaxLanguage.getByName(it) } }

    val codeHighlights by remembering(code) {
        derivedStateOf {
            highlights.code(code)
                .let { if (syntaxLanguage != null) it.language(syntaxLanguage) else it }.build()
        }
    }

    MarkdownCodeBackground(
        color = backgroundCodeColor,
        shape = RoundedCornerShape(codeBackgroundCornerSize),
        modifier = Modifier.fillMaxWidth()
    ) {

        MarkdownBasicText(
            buildAnnotatedString {
                text(codeHighlights.getCode())
                codeHighlights.getHighlights().filterIsInstance<ColorHighlight>().forEach {
                        addStyle(
                            SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
                codeHighlights.getHighlights().filterIsInstance<BoldHighlight>().forEach {
                        addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
            },
            color = LocalMarkdownColors.current.codeText,
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(codeBlockPadding),
            style = style
        )


    }


}

@Composable
internal inline fun <T, K> remembering(
    key1: K,
    crossinline calculation: @DisallowComposableCalls (K) -> T,
): T = remember(key1) { calculation(key1) }

internal fun AnnotatedString.Builder.text(text: String, style: SpanStyle = SpanStyle()) =
    withStyle(style = style) {
        append(text)
    }