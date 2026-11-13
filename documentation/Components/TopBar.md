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
text style. A title can contain multiple elements, such as a subtitle; the
Foundation renderer measures the composed title content when positioning it and
calculating its minimum expanded space. `expandedTitlePadding` controls the
expanded title's bottom breathing room and defaults to the theme's medium
spacing. Set `expandedHeight` to control the total expanded height, including
the regular top bar and its window insets; when omitted, it uses the Material 3
large-app-bar content height of 152 dp, plus the top inset. Values too small to
fit the expanded title and padding use the larger natural minimum. Material 3
also keeps its native large-app-bar minimum height, so a smaller requested
height is raised as needed to let the title finish its collapse transition.
The expanded container and content colors animate to the regular
`containerColor` and `contentColor` as the bar collapses; they default to those
same colors.

Attach the scroll behavior's nested-scroll connection to an ancestor of the
scrollable content, usually the `StyledScaffold` modifier:

```kotlin
val scrollBehavior = rememberStyledExpandableTopBarScrollBehavior()

StyledScaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
        StyledExpandableTopBar(
            behavior = scrollBehavior,
            title = {
                StyledColumn(itemSpacing = StyledTheme.spacing.small) {
                    StyledText("Settings")
                    StyledText(
                        "Account preferences",
                        style = StyledTheme.typography.bodySmall,
                    )
                }
            },
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
collapses the expanded title. When fully collapsed, downward scroll expands it
once the content reaches its start; when partially expanded, downward scroll
continues expanding the bar before scrolling the content. Choose `EnterAlways`
to expand immediately on downward scroll regardless of the content position.
Expansion remains at its current fraction when scrolling pauses; further
scrolling continues from that position rather than snapping to an endpoint. The
behavior exposes a saveable `StyledExpandableTopBarState` through
`behavior.state`; call `expand()` or `collapse()` to animate it programmatically.
