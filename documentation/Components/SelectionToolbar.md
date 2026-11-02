# Selection and selection toolbar

`SelectionState` stores stable item keys and exposes selection operations for
lists, grids and other caller-owned content. `StyledSelectionToolbar` provides
a shared top-bar composition while a selection is active. Selection stays in
your screen state; no list or renderer is coupled to a particular item model.

## Selection state

Create state with `rememberSelectionState`:

```kotlin
val selection = rememberSelectionState<String>()
val keyboardModifiers = LocalKeyboardModifiers.current
val items = listOf("Design notes", "Image assets", "Release checklist")
```

For selections that should survive host recreation, use
`rememberSaveableSelectionState` and provide a `Saver` for each key type. The
selected keys, range anchor and active-selection flag are restored. Prefer
stable keys over positional indices when item order can change.

`select`, `deselect`, `toggle`, `set`, `selectAll` and `invert` update selected
keys. `clearSelection` clears keys but leaves selection mode active;
`clearAndClose` clears the keys and closes selection mode.

`handleListItemClick` defaults to
`behavior = SelectionClickBehavior.PlatformDefault`. On Android and iOS, a
click toggles only the clicked item, preserving all other selected keys.
Keyboard modifiers are ignored for this toggle behavior.

On desktop platforms and Web/Wasm, the default is conventional desktop selection:

- Plain click selects only the clicked item, even if it was already selected.
- Ctrl-click or Meta-click toggles the clicked item.
- Shift-click selects the range between the anchor and clicked item.
- Ctrl/Meta+Shift-click adds the range to the existing selection.

Supply the current item order and a stable key selector so the range can be
computed against the list's current contents:

```kotlin
items.forEachIndexed { index, item ->
    StyledListItem(
        headline = { StyledText(item) },
        onClick = {
            selection.handleListItemClick(
                index = index,
                items = items,
                keyboardModifierState = keyboardModifiers,
                key = { it },
            )
        },
    )
}
```

The overload that accepts `PointerKeyboardModifiers` is useful when handling
pointer input directly: Compose provides the modifier snapshot captured for
that pointer event, which is more precise than cached key-down/up state.

All overloads accept an explicit behavior override:

```kotlin
selection.handleListItemClick(
    index = index,
    items = items,
    keyboardModifierState = keyboardModifiers,
    behavior = SelectionClickBehavior.Toggle,
    key = { it },
)
```

Use `Toggle` to change only the clicked item's membership regardless of keyboard
modifiers, or `Desktop` to retain desktop selection rules on any platform.
Both behaviors keep selection mode active, including when the last item is
deselected. A toggle click sets the range anchor to the clicked item.

The platform default uses `currentPlatform()`, independently of the active
theme. It does not detect the actual input device: touch on a desktop hybrid
device still uses desktop rules, a mouse on Android/iOS still uses toggle rules,
and mobile browsers use Web/Wasm's desktop default. Override `behavior` when
your screen needs a different policy. Even the pointer-modifier overload carries
only modifier flags, not the pointer type; automatic per-interaction input
detection is not provided.

## Keyboard modifier state

Keyboard tracking is opt-in. Create a remembered `KeyboardModifierState` and
provide it around the content that needs to read it:

```kotlin
val keyboardModifiers = rememberKeyboardModifierState()

ProvideKeyboardModifierState(keyboardModifiers) {
    StyledTheme(platform = platform, themeProvider = themeProvider) {
        // Content can read LocalKeyboardModifiers.current.
    }
}
```

Without a provider, `LocalKeyboardModifiers.current` returns a neutral state
with no modifiers pressed. The fallback is read-only. The provider shares the
state but does not install a key listener.

On Compose Desktop, the window host can update the same state before Compose
content handles each event:

```kotlin
val keyboardModifiers = rememberKeyboardModifierState()

Window(
    onPreviewKeyEvent = keyboardModifiers::onKeyEvent,
) {
    ProvideKeyboardModifierState(keyboardModifiers) {
        StyledTheme(platform = platform, themeProvider = themeProvider) {
            Screen()
        }
    }
}
```

`onKeyEvent` records the modifier flags and returns `false`, leaving event
handling to focused controls. Clear the state when the native window loses
focus to prevent stuck keys. A window activated while a modifier is already
held may not receive its original key-down event, so use pointer-event modifier
snapshots for exact click behavior.

For hosts without a window-level callback, attach
`Modifier.onKeyboardModifierEvent()` to an existing root or focused subtree
inside `ProvideKeyboardModifierState`. It observes only events routed through
that Compose focus path; it is not an app-wide listener.

## Selection toolbar

Pass the same selection state to `StyledSelectionToolbar`. It appears while
`selection.isActive`; its close action calls `clearAndClose`. Add screen-specific
actions through the row-scoped `actions` slot:

```kotlin
StyledSelectionToolbar(
    selection = selection,
    shape = StyledSelectionToolbarDefaults.shape,
    actions = {
        StyledButton(onClick = { selection.selectAll(items) }) {
            StyledText("Select all")
        }
    },
)
```

The default shape is `StyledTheme.shapes.large`, which suits a floating toolbar;
override `shape` for a less rounded appearance. The `modifier` controls its placement, while
`windowInsets` defaults to none so a toolbar placed in content does not gain
top-bar safe-area padding. Pass the insets explicitly when placing it directly
at a window edge.

The toolbar composes `StyledSurface`, `StyledTopBar` and `StyledIconButton`, so
their active theme renderers and interaction behavior apply. It does not install
a platform-specific back handler or modify system-bar styling.
