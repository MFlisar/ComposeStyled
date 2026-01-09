package com.michaelflisar.composestyled.core.runtime

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message =
        "This is internal API meant to be used by theme implementations only and may change without further notice.",
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
@Retention(AnnotationRetention.BINARY)
public annotation class InternalComposeStyledApi