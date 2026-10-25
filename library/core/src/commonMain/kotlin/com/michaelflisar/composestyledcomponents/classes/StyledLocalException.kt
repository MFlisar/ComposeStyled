package com.michaelflisar.composestyledcomponents.classes

class StyledLocalMissingException(
    cls: String,
) : Exception(
    "$cls not defined. Did you forget to call StyledTheme()?"
)