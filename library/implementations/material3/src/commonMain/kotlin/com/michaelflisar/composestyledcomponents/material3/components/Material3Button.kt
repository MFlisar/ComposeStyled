package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.material3.CopyStyledLocals
import com.michaelflisar.composestyledcomponents.material3.components.Material3Button.Companion.DefaultRoleMapping
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.runtime.StyledContext
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledDialog
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbar
import com.michaelflisar.composestyledcomponents.ui.specials.StyledGroupBox

class Material3Button internal constructor(
    val roleMapping: RoleMapping,
    override val defaults: StyledButton.Defaults,
) : StyledButton.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        /**
         * Creates a Material3Button implementation for StyledButton with the given role mapping and defaults.
         * @param roleMapping The mapping of StyledButton roles to MaterialButton variants. Defaults to [DefaultRoleMapping].
         * @param defaults The default values for the StyledButton. Defaults to [defaults()].
         * @return A Material3Button implementation for StyledButton.
         */
        @Composable
        fun create(
            roleMapping: RoleMapping = DefaultRoleMapping,
            defaults: StyledButton.Defaults = defaults(),
        ) = remember(roleMapping, defaults) {
            Material3Button(
                roleMapping = roleMapping,
                defaults = defaults
            )
        }

        // -------------
        // 2) mapping
        // -------------

        typealias RoleMapping = (StyledButton.Role, StyledContext) -> Variant

        /**
         * The default mapping of StyledButton roles to MaterialButton variants.
         * - DialogAction and SnackbarAction contexts map to Text variant.
         * - Other contexts map by role:
         *   - Primary role maps to Primary variant.
         *   - Secondary role maps to Outlined variant.
         *   - Tertiary role maps to Text variant.
         */
        val DefaultRoleMapping: RoleMapping = { role, context ->
            when {
                context == StyledGroupBox.Context.Clickable -> Variant.Outlined
                context == StyledSnackbar.Context.SnackbarAction ||
                    context == StyledDialog.Context.DialogAction -> Variant.Text
                else -> when (role) {
                    StyledButton.Role.Primary -> Variant.Primary
                    StyledButton.Role.Secondary -> Variant.Outlined
                    StyledButton.Role.Tertiary -> Variant.Text
                }
            }
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = RoundedCornerShape(percent = 50),
        ) = StyledButton.Defaults(shape = shape)
    }

    enum class Variant {
        Primary,
        Secondary,
        FilledTonal,
        Elevated,
        Outlined,
        Text
    }


    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        role: StyledButton.Role,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        interactionSource: MutableInteractionSource?,
        customization: StyledButton.Customization?,
        content: @Composable (RowScope.() -> Unit),
    ) {
        val context = LocalStyledContext.current
        val variant = roleMapping(role, context)
        val isGroupBox = context == StyledGroupBox.Context.Clickable

        when (variant) {
            Variant.FilledTonal -> {
                FilledTonalButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: Color.Unspecified,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: Color.Unspecified,
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    // copy styles from M3 Button to styled locals

                    CopyMaterialThemeLocals(
                        contentColor = LocalContentColor.current
                    ) {
                        content()
                    }
                }
            }

            Variant.Primary -> {
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: StyledTheme.colorScheme.primary,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: MaterialTheme.colorScheme.onPrimary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    // copy styles from M3 Button to styled locals

                    CopyMaterialThemeLocals(
                        contentColor = LocalContentColor.current
                    ) {
                        content()
                    }
                }
            }

            Variant.Secondary -> {
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    // default: primary button

                    colors = ButtonDefaults.buttonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: StyledTheme.colorScheme.secondary,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: StyledTheme.colorScheme.onSecondary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    // copy styles from M3 Button to styled locals

                    CopyMaterialThemeLocals(
                        contentColor = LocalContentColor.current
                    ) {
                        content()
                    }
                }
            }

            Variant.Elevated -> {
                ElevatedButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = customization?.colors?.filledBackground?.resolve()
                            ?: Color.Unspecified,
                        contentColor = customization?.colors?.filledForeground?.resolve()
                            ?: Color.Unspecified
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.ContentPadding
                    },
                ) {
                    // copy styles from M3 Button to styled locals

                    CopyMaterialThemeLocals(
                        contentColor = LocalContentColor.current
                    ) {
                        content()
                    }
                }
            }

            Variant.Outlined -> {
                // button does not allow to change border color via parameter
                // so we change the base local color instead

                CopyStyledLocals(
                    outlineVariant = customization?.border?.resolve()
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        modifier = modifier,
                        enabled = enabled,
                        shape = shape,
                        interactionSource = interactionSource,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Unspecified,
                            contentColor = customization?.colors?.textForeground?.resolve()
                                ?: MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = if (isGroupBox) {
                            null
                        } else {
                            ButtonDefaults.outlinedButtonBorder(enabled)
                        },
                        contentPadding = if (isGroupBox) {
                            PaddingValues(0.dp)
                        } else {
                            ButtonDefaults.ContentPadding
                        },
                    ) {
                        // copy styles from M3 Button to styled locals

                        CopyMaterialThemeLocals(
                            contentColor = LocalContentColor.current
                        ) {
                            content()
                        }
                    }
                }
            }

            Variant.Text -> {
                TextButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Unspecified,
                        contentColor = customization?.colors?.textForeground?.resolve()
                            ?: MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = if (isGroupBox) {
                        PaddingValues(0.dp)
                    } else {
                        ButtonDefaults.TextButtonContentPadding
                    },
                ) {
                    // copy styles from M3 Button to styled locals

                    CopyMaterialThemeLocals(
                        contentColor = LocalContentColor.current
                    ) {
                        content()
                    }
                }

            }
        }
    }
}
