# Buttons

`StyledButton()` uses `Role.Default`. Each theme resolves that role using the
current component context, so callers do not need to choose a visual role for
menu-bar items, top-bar actions, dialog actions or other context-sensitive
controls. Set `role = StyledButton.Role.Primary`,
`StyledButton.Role.Secondary` or `StyledButton.Role.Tertiary` only when you
intentionally want that semantic
emphasis; an explicit role is not replaced by the context. Concrete styles are
selected by each theme's configurable `roleMapping(role, context)` lambda, not
by a visual variant in the Core API.
Context is owned by its source component and exposed through the open
`StyledContext` contract. Top-bar navigation and action slots provide their own
contexts; the default role uses the bar's actual `contentColor` for icons and
text. Menu bars provide `StyledMenuBar.Context.MenuItem`.
`StyledSnackbar.Context.SnackbarAction` is provided around the snackbar action
and dismiss-action slots. `StyledDialog.Context.DialogAction`,
`FullscreenAction` and `BottomSheetAction` are supplied by the
[dialog family](Dialog.md), only around its action buttons. Dialog buttons also
provide an action-slot context
(positive, negative or neutral) so Fluent 2 can retain its established action
hierarchy while Material 3 and Cupertino use their text/plain defaults.
Clickable `StyledGroupBox` content maps the default
role to each theme's outlined button style. Input leading/trailing slots provide
their own contexts; icon-button defaults select an input-friendly style there.

| Theme | Default role outside a context | Default in navigation and action slots | Default in menu-bar items | Default in snackbar and dialog actions |
|-------|--------------------------------|----------------------------------------|----------------------------|---------------------------------------|
| Material 3 | Primary | Text, using the bar content color | Text | Text |
| Cupertino | Tinted | Plain, using the bar content color | Plain | Plain |
| Fluent 2 | Primary | Transparent, using the bar content color | Transparent | Transparent in snackbars; Primary/Secondary/Subtle by dialog action slot |

Explicit roles keep their normal role mapping in all these contexts. A theme's
custom mapping can choose a different result.

Cupertino Primary uses a translucent accent background with accent-colored
text and icons; Secondary uses a neutral gray background with accent-colored
content, and Tertiary is unfilled. An explicit `filledForeground` override
remains supported for the Filled style.

Cupertino buttons retain `bodyMedium` typography and use compact vertical padding.
Buttons and icon buttons have a minimum 44 dp footprint, increased when the
platform's minimum interactive size is larger, subject to caller constraints.
Button content padding is inside that minimum size, not added on top of it.
Larger content and explicit caller sizes can still expand the surface.
This also applies to dropdown anchors and dialog/snackbar actions, which reuse
the same button renderers. Floating action buttons retain their Small, Standard
and Large sizes.

The snackbar and other context sources supply their context through an
internal-API local. The context-specific default mapping applies only when the
role is `Default`; normal role mapping remains active outside that scope and
explicit roles retain their meaning. All three renderer factories accept a
custom mapping; delegate to `DefaultRoleMapping` for cases that should retain
the theme defaults:

```kotlin
Material3Button.create(
    roleMapping = { role, context ->
        if (context == StyledContext.Default &&
            role == StyledButton.Role.Secondary
        ) {
            Material3Button.Variant.Outlined
        } else {
            Material3Button.DefaultRoleMapping(role, context)
        }
    },
)
```

Each theme ships a fixed `StyledButton.Defaults` (currently only `shape`) that is
registered together with the renderer under `StyledButton.DefaultsKey`. Adjust them
in the same block, `StyledTheme(customization = ...)`, or in a scoped
`UpdateStyledRegistry`. Register a replacement renderer first, then update:

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = themeProvider,
    customization = {
        register(Material3Button.create(roleMapping = myMapping))
        update(StyledButton.DefaultsKey) { copy(shape = RectangleShape) }
    },
) { /* ... */ }
```
