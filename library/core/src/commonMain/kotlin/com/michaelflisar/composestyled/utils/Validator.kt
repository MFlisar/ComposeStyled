package com.michaelflisar.composestyled.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/** Result of validating a value, optionally carrying a user-facing error message. */
sealed interface ValidationResult {
    /** The value passed validation. */
    data object Valid : ValidationResult

    /** The value failed validation with [message]. */
    data class Invalid(val message: String) : ValidationResult
}

/** Holds validation state and exposes the latest error for a value. */
@Stable
class Validator<T>(
    val error: MutableState<String?> = mutableStateOf(null),
    private val validate: (value: T?) -> ValidationResult = { ValidationResult.Valid },
) {
    val isValid = derivedStateOf {
        error.value == null
    }

    // -------------------------
    // validation
    // -------------------------

    /** Evaluates [value] without changing [error]. */
    fun result(value: T?): ValidationResult {
        return validate(value)
    }

    /** Validates [value], optionally updating [error], and returns whether it is valid. */
    fun check(
        value: T?,
        updateError: Boolean = true,
    ): Boolean {
        val message = result(value).messageOrNull()

        if (updateError) {
            error.value = message
        }

        return message == null
    }

    /** Returns the validation message for [value] without changing [error]. */
    fun errorMessage(value: T?): String? {
        return result(value).messageOrNull()
    }

    /** Updates [error] to match [value]. */
    fun update(value: T?) {
        error.value = errorMessage(value)
    }

    /** Clears the current error. */
    fun clear() {
        error.value = null
    }
}

/** Remembers a validator and keeps its error state synchronized with [value]. */
@Composable
fun <T> rememberValidator(
    value: T,
    validate: (value: T?) -> ValidationResult = { ValidationResult.Valid },
): Validator<T> {
    val error = remember { mutableStateOf<String?>(null) }
    return remember(validate) {
        Validator<T>(
            error = error,
            validate = validate,
        )
    }.also {
        it.update(value)
    }
}

private fun ValidationResult.messageOrNull(): String? {
    return (this as? ValidationResult.Invalid)?.message
}
