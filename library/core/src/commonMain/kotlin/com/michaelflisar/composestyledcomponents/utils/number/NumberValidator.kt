package com.michaelflisar.composestyledcomponents.utils.number

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val message: String) : ValidationResult
}

@Stable
class NumberValidator<T : Number>(
    val error: MutableState<String?> = mutableStateOf(null),
    private val validate: (value: T?) -> ValidationResult = { ValidationResult.Valid },
) {
    val isValid = derivedStateOf {
        error.value == null
    }

    // -------------------------
    // validation
    // -------------------------

    fun result(value: T?): ValidationResult {
        return validate(value)
    }

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

    fun errorMessage(value: T?): String? {
        return result(value).messageOrNull()
    }

    fun update(value: T?) {
        error.value = errorMessage(value)
    }

    fun clear() {
        error.value = null
    }
}

@Composable
fun <T : Number> rememberNumberValidator(
    value: T,
    validate: (value: T?) -> ValidationResult = { ValidationResult.Valid },
): NumberValidator<T> {
    val error = remember { mutableStateOf<String?>(null) }
    return remember(validate) {
        NumberValidator(
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