# Top bar

Top bars support `Standard` and `CenterAligned` variants, navigation and action
  slots, colors and window insets. Cupertino centers titles by default.

Top bars handle their safe-area insets themselves. Pass
`WindowInsets(0, 0, 0, 0)` when embedding a bar inside other content.

`StyledButton()` and `StyledIconButton()` use their default role in the
navigation and action slots. Themes render that default with the bar's actual
`contentColor`, so both icons and text remain legible on customized bar
containers. An explicitly supplied role is preserved and uses its normal
theme mapping instead of the context-specific default.

System-bar styling is caller-directed. From the containing Scaffold, call
`SystemBarStyleEffectIfNeeded` with this bar's `containerColor` as
`statusBarColor`; pass the actual Scaffold container as `backgroundColor` when
it differs from the theme surface. If the bar has an explicit `contentColor`,
pass it as `statusBarContentColor` as well. The bar does not change system-bar
appearance or its own inset handling.

## Expandable top bars

Use `StyledExpandableTopBar` when a scrolling screen needs a large title that
collapses into the regular top-bar title. Cupertino, Fluent 2 and Foundation
renderers reuse the active themed `StyledTopBar`; Material 3 uses its native
`LargeTopAppBar`, synchronized with the Styled scroll behavior so its title can
move outside the compact bar bounds while collapsing. Navigation and action
slots retain their normal context behavior in every theme.

The title is composed once and its inherited text style interpolates from
`headlineMedium` to `titleLarge` during the transition. Keep the title's style
inherited (for example, omit an explicit `StyledText.style`) to use the animated
text style. `expandedTitlePadding` controls the expanded title's bottom breathing
room and defaults to the theme's medium spacing. Set `expandedHeight` to control
the total expanded height, including the regular top bar and its window insets;
when omitted, the height is derived from the title and padding. Values too small
to fit the expanded title and padding use that natural minimum. The expanded
container and content colors animate to the regular `containerColor` and
`contentColor` as the bar collapses; they default to those same colors.

Attach the scroll behavior's nested-scroll connection to an ancestor of the
scrollable content, usually the `StyledScaffold` modifier:

```kotlin
val scrollBehavior = rememberStyledExpandableTopBarScrollBehavior()

StyledScaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
        StyledExpandableTopBar(
            behavior = scrollBehavior,
            title = { StyledText("Settings") },
        )
    },
) { contentPadding ->
    StyledLazyColumn(
        modifier = Modifier.padding(contentPadding),
        itemSpacing = StyledTheme.spacing.medium,
    ) {
        items(20) { index ->
            StyledText("Settings item ${index + 1}")
        }
    }
}
```

`rememberStyledExpandableTopBarScrollBehavior` uses
`StyledExpandableTopBarScrollMode.ExitUntilCollapsed` by default: upward scroll
collapses the expanded title, and downward scroll expands it once the content
reaches its start. Choose `EnterAlways` to expand immediately on downward
scroll. The behavior exposes a saveable `StyledExpandableTopBarState` through
`behavior.state`; call `expand()` or `collapse()` to animate it programmatically.
