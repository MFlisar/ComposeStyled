# Scrollbar Containers

For positioning a heading below the viewport's top when it opens, see
[Expandable section](expandable-section.md#scroll-to-the-heading-when-opening).

`StyledVerticalScrollbarContainer` and `StyledHorizontalScrollbarContainer`
share their scrolling, alignment and overlap behavior across platforms. Android
uses a custom draggable scrollbar from `featureCustomScrollbarMain`. iOS,
Windows (JVM), macOS and Wasm use Compose Foundation scrollbars from
`featureFoundationScrollbarMain`, selected through an internal `expect`/`actual`
function. The public container APIs are identical on all targets.

## Content padding

Both containers accept `contentPadding: PaddingValues`, defaulting to zero.
Padding is inside the scrollable content: top/bottom padding scrolls away in a
vertical container, and start/end padding scrolls away in a horizontal container.
It does not add extra items or item spacing, and start/end respect layout direction.
The scrollbar track is not inset by content padding.

```kotlin
StyledVerticalScrollbarContainer(
    modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .consumeWindowInsets(paddingValues),
    contentPadding = StyledTheme.padding.content,
) {
    StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
        StyledText("First item")
        StyledText("Second item")
    }
}
```

Use `Modifier.padding(...)` for fixed outer space, such as Scaffold insets, and
`contentPadding` for space that belongs to the scrolling content. No leading or
trailing spacers are needed.

`StyledLazyColumn`, `StyledLazyRow`, `StyledLazyVerticalGrid` and
`StyledLazyHorizontalGrid` also accept `contentPadding`. Do not wrap them in a
scrollbar container: they already manage scrolling and scrollbars.
`StyledVerticalScrollbarLayout` and `StyledHorizontalScrollbarLayout` only
decorate content that manages its own scrolling; set content padding on that
scrollable content instead.

## Scrollbar visibility

The global `StyledTheme.behavior.showScrollbars` policy controls indicators
throughout the theme subtree. It defaults to visible on desktop and web and
hidden on mobile. The low-level container functions keep `showScrollbar = true`
as their local default; the theme policy can still hide their indicators without
disabling scrolling or reserving scrollbar gutter space. The same policy is
used by scrollable navigation, popup menus, tab rows and lazy layouts.

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = rememberMaterial3ThemeProvider(),
    behavior = StyledTheme.Behavior(showScrollbars = false),
) {
    // Scrollable Styled components remain scrollable, without indicators.
}
```
