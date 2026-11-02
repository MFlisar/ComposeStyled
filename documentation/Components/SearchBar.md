# Search Bar

`StyledSearchBar` is a single-line search control built on `StyledInput`. It
uses the active theme's input renderer and accepts an optional placeholder,
shape and `StyledInput.Customization`.

The query can be controlled with either a `MutableState<String>` or a
`value`/`onValueChange` pair:

```kotlin
val query = remember { mutableStateOf("") }
var submittedQuery by remember { mutableStateOf("") }

StyledSearchBar(
    value = query,
    placeholder = { StyledText("Search") },
    onSearch = { submittedQuery = query.value },
)
```

The search icon calls `onSearch` when provided, and the keyboard's Search action
calls the same callback. The clear icon appears when the query is non-empty;
activating it clears the query and then invokes the optional `onClear` callback.
When `onSearch` is omitted, the search icon is decorative.

An optional `suggestions` slot is rendered in a popup aligned to the search
field. Clicking or tapping the enabled field opens it; another click while it
is open leaves it open so the cursor can be repositioned. Editing the query
with the keyboard also opens the popup and keeps it open while suggestions are
filtered. Its above/below placement is kept stable while the query filters, and is
re-evaluated as the available window size and measured content change, such as
when the keyboard opens or closes. It closes when focus is lost, when an item is
selected, when Escape is pressed, or when the clear action is used. Outside
taps dismiss the popup without activating the content behind it. While the
popup is open, tapping the search field itself also dismisses it; tap the field
again to reposition the cursor.
The popup remains non-focusable so the software keyboard stays available while
editing. It is placed below the field when there is room, or above it when
needed to avoid the visible keyboard. Its height is limited to the available
safe space on that side, including system insets, so the input remains visible.
On Android, suggestions can grow beyond the generic menu height limit when more
space becomes available, and their size and placement update as the keyboard or
system insets change. The slot has a `ColumnScope` receiver and a
`selectSuggestion` callback, so it can contain existing Styled controls such as
`StyledDropdownMenuItem`:

```kotlin
StyledSearchBar(
    value = query,
    placeholder = { StyledText("Search components") },
    suggestions = { selectSuggestion ->
        StyledDropdownMenuItem(
            text = { StyledText("Buttons") },
            onClick = { selectSuggestion("Buttons") },
        )
    },
)
```

Suggestions are optional. Without the slot, the search bar renders only the
input field and its actions.

`popupWidthConfig` defaults to `StyledPopupMenuDefaults.widthConfig`, sharing
the popup menu's width policy. Override it per search bar, for example with
`popupWidthConfig = StyledPopupMenu.WidthConfig.WrapContent` or
`StyledPopupMenu.WidthConfig.Fixed(320.dp)`.

Call `selectSuggestion(text)` when an item is selected. It updates the query,
places the cursor at the end without selecting text, and closes the popup while
keeping the input focused. This also closes the popup when the selected text
already equals the query. The suggestions are removed immediately, so closing
the popup cannot briefly show results filtered by the selected text.
After dismissal, focus alone does not reopen suggestions; click or tap the
input, or edit the query, to open them again. Keyboard cursor movement or
selection changes alone do not affect popup visibility. Escape dismisses the
popup. Outside taps dismiss it without activating the content behind it.
Clearing the query closes it.

External query changes update the displayed value and move the cursor to the
end without changing popup visibility. If the popup is open, it stays open and
the suggestions can update; if it is closed, an external change alone does not
open it. Normal typing, cursor movement and text selection are preserved.

## Matching and ranking local suggestions

`SearchUtil` lives in Core's `com.michaelflisar.composestyled.utils.search`
package. It is a stateless Kotlin utility with no Compose dependencies, so it
can also serve dropdown filtering or ordinary result lists. It is not required
by `StyledSearchBar`; server-side searches can continue using the slot directly.

The text-extractor overload matches case-insensitively and ranks exact matches
before prefixes and substrings:

```kotlin
val result = SearchUtil.search(
    items = listOf("Numeric Inputs", "Inputs", "Input Actions"),
    query = query.value,
    text = { it },
)

StyledSearchBar(
    value = query,
    suggestions = { selectSuggestion ->
        result.forEach { item ->
            StyledDropdownMenuItem(
                text = { StyledText(item) },
                onClick = { selectSuggestion(item) },
            )
        }
        StyledText("${result.size} matches")
    },
)
```

For custom matching, multiple fields or synonyms, supply a score function instead
of `text`. Null excludes an item; larger scores rank first. Zero and negative
scores are valid matches.

```kotlin
val result = SearchUtil.search(items, query.value) { item, searchQuery ->
    when {
        item.name.equals(searchQuery, ignoreCase = true) -> 100
        item.name.startsWith(searchQuery, ignoreCase = true) -> 50
        item.description.contains(searchQuery, ignoreCase = true) -> 10
        else -> null
    }
}
```

Equal scores preserve the input order, including duplicate entries. Blank
queries return the input order without invoking the text extractor or scorer.
Otherwise the query is used as supplied, without automatic trimming.
Both overloads return all matching items as a `List<T>`; use `.size` for the
match count. The helper does not implement pagination or asynchronous search.
For repeated Compose calls, callers can use `remember` keyed by their data,
query and scoring configuration.
