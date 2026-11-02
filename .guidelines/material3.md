# Material 3 guidelines

This page summarizes official [Material 3](https://m3.material.io/) guidance
relevant to reviewing ComposeStyled renderers. It is not a substitute for the
linked specifications. Material's site can update component guidance and
variants over time.

## System-wide guidance

- **Color is role-based and adaptable.** Material 3 uses color roles and
  algorithmic schemes, including dynamic color inputs and dark themes. Prefer
  semantic theme roles over hard-coded colors.
  [Color system](https://m3.material.io/styles/color/system/overview)
- **Accessibility is a design concern.** Material frames accessibility as
  honoring individuals, learning before and during design, and treating
  requirements as a starting point.
  [Principles](https://m3.material.io/foundations/overview/principles)
- **Adapt navigation and layout to window size.** Navigation patterns change
  with compact, medium and larger windows; do not assume a phone-only layout.
  [Navigation bar](https://m3.material.io/components/navigation-bar/guidelines),
  [navigation rail](https://m3.material.io/components/navigation-rail/guidelines),
  [navigation drawer](https://m3.material.io/components/navigation-drawer/guidelines)
- **Keep hierarchy clear.** Avoid excess actions and unnecessary containers;
  cards should not be used when spacing, headings or dividers make a clearer
  structure.
  [Buttons](https://m3.material.io/components/buttons/guidelines),
  [cards](https://m3.material.io/components/cards/guidelines)

## Component guidance

### Actions and selectable content

- **Button / IconButton:** Material provides filled, tonal, outlined, elevated
  and text buttons with different emphasis. Icon buttons suit compact,
  supplementary actions such as toolbars. Select style by hierarchy rather
  than decoration. [Buttons](https://m3.material.io/components/buttons/guidelines),
  [icon buttons](https://m3.material.io/components/icon-buttons/overview)
- **FloatingActionButton:** Reserve it for the primary screen action; it may
  also appear in a navigation rail. [FAB](https://m3.material.io/components/floating-action-button/overview)
- **Chip:** Material distinguishes assist, filter, input and suggestion
  chips; choose by interaction intent rather than treating chips as miniature
  buttons. [Chips](https://m3.material.io/components/chips/overview)
- **SegmentedButton:** Supports single- and multi-selection, generally for
  two to five segments; consider chips when there are more choices.
  [Segmented buttons](https://m3.material.io/components/segmented-buttons/overview)
- **Badge:** Small badges communicate a dot; larger badges carry a short
  label and attach to a related element. [Badges](https://m3.material.io/components/badges/overview)

### Entry, selection and value controls

- **Input:** Filled fields provide more visual emphasis; outlined fields are
  suited to denser forms. Avoid mixing variants within one form or region.
  [Text fields](https://m3.material.io/components/text-fields/guidelines)
- **Checkbox, RadioButton and Switch:** Checkboxes support independent or
  multiple selection; radios express one choice among a small visible set;
  switches express binary settings that take effect immediately.
  [Checkboxes](https://m3.material.io/components/checkbox/guidelines),
  [radio buttons](https://m3.material.io/components/radio-button/guidelines),
  [switches](https://m3.material.io/components/switch/guidelines)
- **Dropdown / Picker / PopupMenus:** Material's menus cover dropdown and
  contextual action patterns. Use menus for choices/actions that should remain
  out of the way until requested. [Menus](https://m3.material.io/components/menus/guidelines)
- **DatePicker / TimePicker:** Date pickers provide docked, modal and
  input-oriented variants; time pickers offer dial and input variants.
  [Date pickers](https://m3.material.io/components/date-pickers/overview),
  [time pickers](https://m3.material.io/components/time-pickers/overview)
- **NumberPicker:** No dedicated Material 3 number-picker component was
  found. Text fields or sliders are only nearby patterns, not direct
  equivalents. [Text fields](https://m3.material.io/components/text-fields/guidelines),
  [sliders](https://m3.material.io/components/sliders/guidelines)
- **Slider / RangeSlider:** Standard, centered and range variants express
  different value models; values update as the thumb moves. Avoid vertical
  range sliders. [Sliders](https://m3.material.io/components/sliders/guidelines)
- **SearchBar:** Material search can expand to suggestions and results; it is
  distinct from a generic text field.
  [Search](https://m3.material.io/components/search/overview)

### Content, grouping and layout

- **Card:** Elevated, filled and outlined styles are functionally equivalent
  visual treatments. Do not force content into a card when simpler grouping
  works. [Cards](https://m3.material.io/components/cards/guidelines)
- **ListItem / LazyAndGridLayouts:** Keep list-row element positions
  consistent. Lists and carousel patterns are the closest official references
  for collections; the latter is not a general-purpose grid specification.
  [Lists](https://m3.material.io/components/lists/guidelines),
  [carousel](https://m3.material.io/components/carousel/overview)
- **Divider:** Use dividers to distinguish groups when spacing alone is not
  sufficient; avoid separating every item by default.
  [Divider](https://m3.material.io/components/divider/overview)
- **Surface:** Surface is a color-role/elevation concept, not a standalone
  component specification. Material uses tonal surface roles as well as
  elevation. [Color system](https://m3.material.io/styles/color/system/overview),
  [elevation](https://m3.material.io/styles/elevation/overview)
- **GroupBox / LabeledInformation:** No direct Material 3 component guidance
  was found. Lists and text-field supporting text are only possible analogies.
  [Lists](https://m3.material.io/components/lists/overview),
  [text fields](https://m3.material.io/components/text-fields/guidelines)

### Navigation, app structure and scrolling

- **BottomNavigationBar / NavigationItems:** Navigation bars are for three to
  five top-level destinations on compact and medium windows. For other
  destination counts or larger layouts, consider rail or drawer patterns;
  preserve labels. [Navigation bar](https://m3.material.io/components/navigation-bar/guidelines),
  [navigation rail](https://m3.material.io/components/navigation-rail/guidelines),
  [navigation drawer](https://m3.material.io/components/navigation-drawer/guidelines)
- **SideNavigation:** Rails suit mid-size layouts; drawers support larger
  navigation hierarchies. Avoid showing competing navigation components
  together. [Navigation rail](https://m3.material.io/components/navigation-rail/guidelines),
  [navigation drawer](https://m3.material.io/components/navigation-drawer/guidelines)
- **Tabs:** Primary and secondary tabs organize related peer content, not a
  sequential workflow or ordered chapters.
  [Tabs](https://m3.material.io/components/tabs/guidelines)
- **TopBar / CollapsibleHeader:** App bars have small, medium and large
  flexible variants. Scroll separation is expressed through color rather than
  an added drop shadow. Material's app-bar guidance is the closest reference
  for collapsing headers. [App bars](https://m3.material.io/components/app-bars/overview)
- **Scaffold / Panes / SplitPanel:** These are layout compositions rather than
  a single component specification. For adaptive list-detail layouts, Material
  describes one pane in compact windows and two panes in larger windows.
  [List-detail](https://m3.material.io/m3/pages/canonical-examples/list-detail/),
  [side sheets](https://m3.material.io/components/side-sheets/overview)
- **ScrollbarContainers:** No direct scrollbar-container guidance was found
  in the Material 3 component set.

### Feedback and transient actions

- **Dialog:** Use sparingly for important decisions; a dialog blocks app
  functionality while shown. Prefer a snackbar for lower-priority feedback.
  [Dialogs](https://m3.material.io/components/dialogs/guidelines)
- **Snackbar:** Suitable for low-priority feedback and optional actions, in
  contrast to a dialog's high-priority interruption.
  [Snackbars](https://m3.material.io/components/snackbar/overview)
- **ProgressIndicator:** Prefer determinate progress when possible and change
  from indeterminate to determinate when duration becomes known. Material's
  guidance distinguishes very short waits from longer progress.
  [Progress indicators](https://m3.material.io/components/progress-indicators/guidelines)
- **Tooltip:** Material distinguishes plain tooltips from richer tooltips;
  these should not be confused with persistent instructions.
  [Tooltips](https://m3.material.io/components/tooltips/overview)
- **SelectionToolbar:** No dedicated component page was found; Material's
  toolbar guidance includes a floating style associated with contextual
  actions. [Toolbars](https://m3.material.io/components/toolbars/guidelines)
- **SwipeActions:** No direct swipe-action specification was found. List
  trailing actions are only an adjacent pattern.
  [Lists](https://m3.material.io/components/lists/overview)

## Review translation

For ComposeStyled, treat the guidance above as prompts to check renderer
variants, theme color-role usage, semantics, state behavior and adaptive
navigation/layout. These are implementation-review questions derived from the
design guidance, not additional Material requirements. In particular, a
Material component name alone does not establish that a custom Compose
implementation has the same behavior as the documented component.
