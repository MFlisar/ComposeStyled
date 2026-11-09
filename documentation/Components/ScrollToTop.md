# Scroll to top

The scroll-to-top utility lives in
`com.michaelflisar.composestyled.utils`. A `ScrollToTopTarget` can be
requested independently, for example from an expandable section's
`onExpansionAnimationFinished` callback.

## Regular vertical scroll containers

Create one host for the `ScrollState`, attach support to the scroll viewport,
then create and register a target for each item that can request scrolling.
`topPadding` is measured from the viewport, after outer padding:

```kotlin
val scrollState = rememberScrollState()
val scrollHost = rememberScrollToTopRequester(
    scrollState = scrollState,
    topPadding = 16.dp,
)

Column(
    Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .supportScrollToTopRequester(scrollHost)
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val target = rememberScrollToTopTarget(scrollHost)

    StyledExpandableSection(
        modifier = Modifier.registerScrollToTopTarget(target),
        expanded = expanded,
        onToggle = { expanded = !expanded },
        onExpansionAnimationFinished = { isExpanded ->
            if (isExpanded) scope.launch { target.request() }
        },
        title = { StyledText("Details") },
    ) {
        StyledText("Expanded content")
    }
}
```

Attach `supportScrollToTopRequester` once to the regular scroll viewport. Each
target modifier must be attached to the item whose top should be positioned.
The available scroll range can limit the requested position, particularly near
the end of the content.

## Lazy vertical lists

Create a target per item using its lazy-list index. Lazy targets use
`LazyListState` directly, so the list and item do not need support or registration
modifiers. The target item must begin with the heading to position:

```kotlin
val listState = rememberLazyListState()

StyledLazyColumn(state = listState) {
    itemsIndexed(sections) { index, section ->
        var expanded by rememberSaveable { mutableStateOf(false) }
        val target = rememberScrollToTopTarget(
            lazyListState = listState,
            itemIndex = index,
            topPadding = 16.dp,
        )

        StyledExpandableSection(
            expanded = expanded,
            onToggle = { expanded = !expanded },
            onExpansionAnimationFinished = { isExpanded ->
                if (isExpanded) scope.launch { target.request() }
            },
            title = { StyledText(section.title) },
        ) {
            StyledText("Expanded content")
        }
    }
}
```

The lazy-list index must identify the actual item containing the target (including
any preceding headers or other items). Lazy lists must be vertical and not
reversed. List content padding is included in the requested viewport offset.

## Request timing and lower-level scrolling

Targets expose `suspend fun request(keepOnTop: Boolean = false)`,
`suspend fun requestImmediately()`, and `stopKeepOnTop()`. A keep-on-top request
first scrolls with the configured animation, then follows size changes in either
direction until `stopKeepOnTop()` is called. The tracking is managed by the
target itself. For expandable sections, start it when expanding and stop it
from `onExpansionAnimationFinished`. Initial expansion does not automatically
request scrolling.

For lower-level use, the `ScrollState.scrollToTop(target, container, topPaddingPx)`
and `LazyListState.scrollToTop(itemIndex, topPaddingPx)` extensions use pixels.
The remembered target factories accept `Dp` and convert it with the current density.
