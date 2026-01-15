package com.michaelflisar.composestyled.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.icons.IconCheckBox
import com.michaelflisar.composestyled.components.icons.IconCheckBoxOutline
import com.michaelflisar.composestyled.components.icons.KeyboardArrowLeft
import com.michaelflisar.composestyled.components.icons.KeyboardArrowRight
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.components.StyledHorizontalSeparator
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText
import com.michaelflisar.composestyled.core.disabled
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.LocalTextStyle
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.tokens.minimumInteractiveComponentSize

@LayoutScopeMarker
@Immutable
interface MenuScope {
    val hideLabels: Boolean
}

internal class MenuScopeInstance(
    override val hideLabels: Boolean = false,
) : MenuScope

@LayoutScopeMarker
@Immutable
interface MenuIconRowScope {
    val rowScope: RowScope
}

internal class MenuIconRowScopeInstance(override val rowScope: RowScope) : MenuIconRowScope

private val LocalMenuSetup = staticCompositionLocalOf<MenuSetup> { error("MenuSetup not initialised!") }
private val LocalMenuState = staticCompositionLocalOf<MenuState> { error("MenuState not initialised!") }
private val LocalMenuIndex = staticCompositionLocalOf<MutableIntState> { error("MenuIndex not initialised!") }
private val LocalMenuLevels = staticCompositionLocalOf<List<Int>> { error("MenuLevel not initialised!") }

object Menu {

    val shape: Shape
        @Composable get() = StyledTheme.shapes.control

    val itemPadding: Dp
        @Composable get() = StyledTheme.paddings.medium

    @Composable
    fun modifier(
        padding: Boolean = true,
        clip: Boolean = true,
    ) = Modifier
        .then(
            if (padding) {
                Modifier.padding(horizontal = itemPadding)
            } else Modifier
        )
        .then(
            if (clip) Modifier.clip(shape) else Modifier
        )
}


@Stable
class MenuSetup internal constructor(
    val autoDismiss: Boolean,
    val offset: DpOffset,
)

@Composable
fun rememberMenuSetup(
    autoDismiss: Boolean = true,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
): MenuSetup {
    return remember { MenuSetup(autoDismiss, offset) }
}

@Stable
data class MenuState(
    private val show: MutableState<Boolean>,
    internal val openedLevels: MutableState<List<Int>>,
    private val data: MutableState<Any?>,
    internal val offset: MutableState<IntOffset>,
) {
    val visible: Boolean
        get() = show.value

    fun show() {
        show.value = true
    }

    fun show(data: Any) {
        this.data.value = data
        show()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> requireData() = data.value as T

    fun hide() {
        show.value = false
        openedLevels.value = emptyList()
        this.data.value = null
    }

    fun goUp(): Boolean {
        return if (openedLevels.value.isNotEmpty()) {
            openedLevels.value = openedLevels.value.toMutableList().dropLast(1)
            true
        } else false
    }

    fun open(index: Int) {
        openedLevels.value = openedLevels.value.toMutableList().apply {
            add(index)
        }
    }
}

@Composable
fun rememberMenuState(
    show: Boolean = false,
    openedLevels: List<Int> = emptyList(),
    data: Any? = null,
): MenuState {
    return MenuState(
        show = remember { mutableStateOf(show) },
        openedLevels = remember { mutableStateOf(openedLevels) },
        data = remember { mutableStateOf(data) },
        offset = remember { mutableStateOf(IntOffset.Zero) }
    )
}

@Composable
internal fun ProvideMenuLocals(
    state: MenuState,
    setup: MenuSetup,
    level: Int,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMenuState provides state,
        LocalMenuSetup provides setup,
        LocalMenuLevels provides emptyList(),
        LocalMenuIndex provides remember { mutableIntStateOf(level) }
    ) {
        content()
    }
}

@Composable
internal fun ProvideUpdatedMenuLocals(
    levels: List<Int>,
    menuIndex: Int,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMenuLevels provides levels,
        LocalMenuIndex provides remember { mutableIntStateOf(menuIndex) }
    ) {
        content()
    }
}

// --------------
// Hierarchy Management
// --------------

@Composable
private fun MenuScope.WrappedItem(
    content: @Composable () -> Unit,
    subContent: @Composable (() -> Unit)? = null,
) {
    val state = LocalMenuState.current
    val levels = LocalMenuLevels.current
    val index = LocalMenuIndex.current

    ProvideUpdatedMenuLocals(levels, index.value++) {
        if (state.openedLevels.value == levels) {
            content()
        }
        subContent?.invoke()
    }
}

@Composable
private fun MenuScope.WrappedSubMenu(
    content: @Composable MenuScope.() -> Unit,
) {
    val levels = LocalMenuLevels.current
    val index = LocalMenuIndex.current
    ProvideUpdatedMenuLocals(levels + index.value, 0) {
        content()
    }
}

// --------------
// Items
// --------------

@Composable
fun MenuScope.MenuItem(
    text: @Composable () -> Unit,
    imageVector: ImageVector? = null,
    endIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    WrappedItem(
        content = {
            val setup = LocalMenuSetup.current
            val state = LocalMenuState.current
            DropdownMenuItem(
                modifier = Menu.modifier(),
                text = if (hideLabels) (@Composable {}) else text,
                enabled = enabled,
                leadingIcon = imageVector?.let { { StyledIcon(it, null, modifier = Modifier.size(24.dp)) } },
                trailingIcon = endIcon,
                onClick = {
                    onClick()
                    if (setup.autoDismiss) {
                        state.hide()
                    }
                }
            )
        }
    )
}

@Composable
fun MenuScope.MenuItem(
    content: @Composable () -> Unit,
    endIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    WrappedItem(
        content = {
            val setup = LocalMenuSetup.current
            val state = LocalMenuState.current
            DropdownMenuItem(
                modifier = Menu.modifier(),
                text = content,
                enabled = enabled,
                leadingIcon = null,
                trailingIcon = endIcon,
                onClick = {
                    onClick()
                    if (setup.autoDismiss) {
                        state.hide()
                    }
                }
            )
        }
    )
}

@Composable
fun MenuScope.MenuCheckbox(
    text: @Composable () -> Unit,
    checked: MutableState<Boolean>,
    imageVector: ImageVector? = null,
    enabled: Boolean = true,
) {
    MenuCheckbox(
        text = text,
        checked = checked.value,
        onCheckChange = { checked.value = it },
        imageVector = imageVector,
        enabled = enabled
    )
}

@Composable
fun MenuScope.MenuCheckbox(
    text: @Composable () -> Unit,
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    imageVector: ImageVector?,
    enabled: Boolean = true,
) {
    WrappedItem(content = {
        DropdownMenuItem(
            modifier = Menu.modifier(),
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        text()
                    }
                    StyledIcon(
                        imageVector = if (checked) IconCheckBox else IconCheckBoxOutline,
                        contentDescription = null
                    )
                }
            },
            enabled = enabled,
            leadingIcon = imageVector?.let { { StyledIcon(it, null, modifier = Modifier.size(24.dp)) } },
            trailingIcon = null,
            onClick = {
                onCheckChange(!checked)
            }
        )
    }
    )
}

@Composable
fun MenuScope.MenuSeparator(
    text: String = "",
    textColor: Color? = null,
    textAlign: TextAlign = TextAlign.Start,
) {
    WrappedItem(
        content = {
            if (text.isEmpty()) {
                StyledHorizontalSeparator(
                    modifier = Modifier.padding(vertical = StyledTheme.paddings.medium),
                    color = LocalContentColor.current
                )
            } else {
                StyledText(
                    modifier = Modifier.fillMaxWidth().padding(all = StyledTheme.paddings.medium),
                    text = text,
                    style = StyledTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor ?: LocalContentColor.current.disabled(DisableFactorType.Content),
                    textAlign = textAlign
                )
            }
        }
    )
}

@Composable
fun MenuScope.MenuIconRow(
    enabled: Boolean = true,
    content: @Composable MenuIconRowScope.() -> Unit,
) {
    WrappedItem(
        content = {
            DropdownMenuItem(
                modifier = Menu.modifier(padding = false),
                text = {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.small)
                    ) {
                        MenuIconRowScopeInstance(this).content()
                    }
                },
                enabled = enabled,
                leadingIcon = null,
                trailingIcon = null,
                onClick = {}
            )
        }
    )
}

@Composable
fun MenuIconRowScope.MenuIcon(
    enabled: Boolean = true,
    tooltip: String,
    imageVector: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val setup = LocalMenuSetup.current
    val state = LocalMenuState.current

    Box(
        modifier = Modifier
            .then(with(rowScope) { Modifier.weight(1f) })
            .minimumInteractiveComponentSize()
            .fillMaxHeight()
            .clip(Menu.shape)
            .clickable(
                onClick = {
                    onClick()
                    if (setup.autoDismiss) {
                        state.hide()
                    }
                },
                enabled = enabled,
                role = Role.Button,
            ),
        contentAlignment = Alignment.Center
    ) {
        //MyTooltipBox(
        //    tooltip = tooltip,
        //    offset = state.offset.value
        //) {
            imageVector()
        //}
    }
}

@Composable
fun MenuScope.MenuSubMenu(
    text: @Composable () -> Unit,
    imageVector: ImageVector? = null,
    enabled: Boolean = true,
    content: @Composable MenuScope.() -> Unit,
) {
    WrappedItem(
        content = {
            val state = LocalMenuState.current
            val index = LocalMenuIndex.current
            DropdownMenuItem(
                modifier = Menu.modifier(),
                text = text,
                enabled = enabled,
                leadingIcon = imageVector?.let { { StyledIcon(it, null, modifier = Modifier.size(24.dp)) } },
                onClick = {
                    state.open(index.value)
                },
                trailingIcon = {
                    val layoutDirection = LocalLayoutDirection.current
                    StyledIcon(
                        imageVector = if (layoutDirection == LayoutDirection.Ltr)
                            KeyboardArrowRight
                        else
                            KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        },
        subContent = {
            WrappedSubMenu {
                MenuBack(text)
                content()
            }
        }
    )
}

@Composable
private fun MenuScope.MenuBack(
    text: @Composable () -> Unit,
) {
    WrappedItem(
        content = {
            val state = LocalMenuState.current
            Column {
                val color = LocalContentColor.current.copy(alpha = .7f)
                CompositionLocalProvider(
                    //LocalMinimumInteractiveComponentSize provides 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(Menu.modifier())
                            .clickable {
                                state.goUp()
                            }
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //val fontSize = MaterialTheme.typography.titleSmall.fontSize * .8f
                        val style = StyledTheme.typography.titleSmall//.copy(fontSize = fontSize)
                        val layoutDirection = LocalLayoutDirection.current
                        StyledIcon(
                            imageVector = if (layoutDirection == LayoutDirection.Ltr)
                                KeyboardArrowLeft
                            else
                                KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(horizontal = 4.dp),
                            tint = color
                        )
                        ProvideStyledLocals(
                            contentColor = color,
                            textStyle = LocalTextStyle.current.merge(style)
                        ) {
                            text()
                        }
                    }
                }
                StyledHorizontalSeparator(
                    modifier = Modifier.padding(vertical = StyledTheme.paddings.small),
                    color = color
                )
            }
        }
    )
}

@Composable
fun DropdownMenuItem(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier
                        .minimumInteractiveComponentSize()
                        .clickable(
                            onClick = onClick,
                            role = Role.Button,
                            enabled = true
                        )
                } else {
                    Modifier
                }
            )
            .padding(
                vertical = StyledTheme.paddings.small,
                horizontal = StyledTheme.paddings.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium)
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                leadingIcon()
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            text()
        }
        if (trailingIcon != null) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                trailingIcon()
            }
        }
    }
}