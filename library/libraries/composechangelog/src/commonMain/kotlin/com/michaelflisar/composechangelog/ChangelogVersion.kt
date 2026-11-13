package com.michaelflisar.composechangelog

/** A display name paired with the numeric code used for ordering and update checks. */
data class ChangelogVersion(
    val name: String,
    val code: Int,
) {
    init {
        require(name.isNotBlank()) { "Version name must not be blank." }
        require(code >= 0) { "Version code must not be negative." }
    }
}
