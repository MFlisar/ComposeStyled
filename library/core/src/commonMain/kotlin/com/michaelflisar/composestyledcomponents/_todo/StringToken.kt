package com.michaelflisar.composestyledcomponents.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Stable
sealed class StringToken {

    @Immutable
    data class Text(val text: String) : StringToken()

    @Immutable
    data class Resource(val resource: StringResource) : StringToken()

    @Composable
    fun resolve(): String {
        return when (this) {
            is Text -> text
            is Resource -> stringResource(resource)
        }
    }
}

fun String.toStringToken(): StringToken = StringToken.Text(this)

fun StringResource.toStringToken(): StringToken = StringToken.Resource(this)