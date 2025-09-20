package com.laomuji1999.compose.core.ui.view


import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

data class ClickableAnnotatedText(
    val text: String,
    val onClick: () -> Unit,
    val style: SpanStyle,
    val replaceText: String? = null,
)

@Composable
fun ClickableAnnotatedText(
    modifier: Modifier = Modifier,
    fullText: String,
    parts: List<ClickableAnnotatedText>,
    normalStyle: SpanStyle,
    textStyle: TextStyle,
    ignoreCase: Boolean = true,
) {
    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        var safetyCounter = 0

        while (currentIndex < fullText.length && safetyCounter < fullText.length) {
            safetyCounter++
            val nextMatch = parts.mapNotNull { part ->
                val index =
                    fullText.indexOf(part.text, startIndex = currentIndex, ignoreCase = ignoreCase)
                if (index >= 0) index to part else null
            }.minByOrNull { it.first }

            if (nextMatch == null) {
                withStyle(normalStyle) {
                    append(fullText.substring(currentIndex))
                }
                break
            } else {
                val (matchIndex, matchedPart) = nextMatch
                if (matchIndex > currentIndex) {
                    withStyle(normalStyle) {
                        append(fullText.substring(currentIndex, matchIndex))
                    }
                }
                val displayText = matchedPart.replaceText ?: matchedPart.text
                val tagId = "${matchedPart.text}-${matchIndex}"

                pushStringAnnotation(tag = tagId, annotation = matchedPart.text)
                withStyle(matchedPart.style) {
                    append(displayText)
                }
                pop()
                currentIndex = matchIndex + matchedPart.text.length
            }
        }
    }

    val text = annotatedString.ifEmpty {
        buildAnnotatedString {
            withStyle(normalStyle) {
                append(fullText)
            }
        }
    }

    @Suppress("Deprecation") ClickableText(
        text = text, style = textStyle, modifier = modifier, onClick = { offset ->
            annotatedString.getStringAnnotations(start = offset, end = offset).firstOrNull()
                ?.let { annotation ->
                    parts.firstOrNull { part ->
                        annotation.item.equals(
                            part.text, ignoreCase = ignoreCase
                        )
                    }?.onClick?.invoke()
                }
        })
}
