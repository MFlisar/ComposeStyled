# Cupertino guidelines

This page summarizes Apple's official [Human Interface Guidelines
(HIG)](https://developer.apple.com/design/human-interface-guidelines/) as they
relate to ComposeStyled's Cupertino renderers. Apple's guidance is explicitly
platform-aware; check the relevant iOS, iPadOS, macOS, watchOS, tvOS or
visionOS section rather than assuming one behavior applies everywhere.

## System-wide guidance

- **Design for accessibility from the start.** Review target sizes, contrast,
  labels, focus and accessible state communication.
  [Accessibility](https://developer.apple.com/design/human-interface-guidelines/accessibility)
- **Use progressive disclosure and reduce visual clutter.**
  [Layout](https://developer.apple.com/design/human-interface-guidelines/layout)
- **Treat modality as a deliberate choice.** Present content modally only
  when it has a clear benefit. [Modality](https://developer.apple.com/design/human-interface-guidelines/modality)
- **Respect platform-specific materials.** Liquid Glass is a functional layer
  for controls and navigation, while content uses content-layer materials;
  do not apply Liquid Glass indiscriminately to content.
  [Materials](https://developer.apple.com/design/human-interface-guidelines/materials)
- **Prefer system behaviors and controls where appropriate.** The HIG's
  platform-specific recommendations are part of the interaction contract, not
  only visual styling.

## Component guidance

### Actions and selectable content

- **Button / IconButton / FloatingActionButton:** A button combines style,
  content and semantic role. Include a press state for custom buttons, keep
  prominent actions limited, and meet Apple's 44×44 pt minimum hit region
  (60×60 pt for visionOS). There is no dedicated HIG Floating Action Button
  pattern; use the relevant button guidance rather than importing its
  Material-specific behavior. [Buttons](https://developer.apple.com/design/human-interface-guidelines/buttons)
- **SegmentedButton:** Apple's segmented controls may represent a selection
  or a momentary action, depending on platform and use. Keep the segment count
  appropriate to the available space. [Segmented controls](https://developer.apple.com/design/human-interface-guidelines/segmented-controls)
- **Chip:** No direct HIG chip component was found. Labels are static text,
  not a substitute for selectable or dismissible chips.
  [Labels](https://developer.apple.com/design/human-interface-guidelines/labels)
- **Badge:** No direct HIG badge component was found; consult status guidance
  for nearby progress/status patterns.
  [Status](https://developer.apple.com/design/human-interface-guidelines/status)

### Entry, selection and value controls

- **Input:** Use text fields for short, specific text and text views for
  longer content. Keep labels available when placeholders disappear, and
  choose validation timing appropriate to the field.
  [Text fields](https://developer.apple.com/design/human-interface-guidelines/text-fields)
- **Checkbox / Switch:** Apple's toggle guidance is platform-dependent:
  switch styling is appropriate in particular contexts such as iOS list rows.
  Do not rely on color alone to communicate toggle state.
  [Toggles](https://developer.apple.com/design/human-interface-guidelines/toggles)
- **Dropdown / Picker / DatePicker / TimePicker:** Pickers are suited to
  choosing values; menus are for commands or short choice lists. Date and time
  pickers are covered by the picker guidance, with platform-specific styles.
  [Pickers](https://developer.apple.com/design/human-interface-guidelines/pickers),
  [menus](https://developer.apple.com/design/human-interface-guidelines/menus)
- **NumberPicker:** Steppers pair increment/decrement controls with a value
  field; pickers are another option depending on the value domain.
  [Steppers](https://developer.apple.com/design/human-interface-guidelines/steppers),
  [pickers](https://developer.apple.com/design/human-interface-guidelines/pickers)
- **SearchBar:** Search fields may include scope controls, tokens and a clear
  action; search behavior should match the user's task.
  [Search fields](https://developer.apple.com/design/human-interface-guidelines/search-fields),
  [Searching](https://developer.apple.com/design/human-interface-guidelines/searching)
- **Slider / RangeSlider:** Keep minimum and maximum directions consistent
  (leading/bottom to trailing/top). Pair a slider with direct value entry when
  precision or a wide range matters. iOS audio volume should use its dedicated
  system view, not a generic slider.
  [Sliders](https://developer.apple.com/design/human-interface-guidelines/sliders)
- **RadioButton:** No standalone radio-button page was found in the HIG.
  Apply selection and picker guidance carefully rather than assuming a
  Material-style radio control is a direct Apple pattern.

### Content, grouping and layout

- **Card / GroupBox:** The HIG does not define a general-purpose Card
  component. Its Boxes guidance is the direct reference for `GroupBox`-like
  grouping: keep groups small and avoid nested boxes.
  [Boxes](https://developer.apple.com/design/human-interface-guidelines/boxes)
- **LabeledInformation:** Labels represent static text and support visual
  hierarchy; use text fields for editable values.
  [Labels](https://developer.apple.com/design/human-interface-guidelines/labels),
  [text fields](https://developer.apple.com/design/human-interface-guidelines/text-fields)
- **Divider:** No dedicated divider page was found; layout guidance treats
  separator lines as one of several ways to group content.
  [Layout](https://developer.apple.com/design/human-interface-guidelines/layout)
- **ListItem / LazyAndGridLayouts:** Use lists and tables for text-heavy data
  and collections for image-heavy or varied-size content. On iOS/iPadOS,
  selection and reordering may be gated behind edit mode; distinguish
  persistent navigation selection from transient option selection.
  [Lists and tables](https://developer.apple.com/design/human-interface-guidelines/lists-and-tables),
  [collections](https://developer.apple.com/design/human-interface-guidelines/collections)
- **Surface:** Materials distinguish functional controls/navigation from
  content. Avoid treating Liquid Glass as a generic surface fill.
  [Materials](https://developer.apple.com/design/human-interface-guidelines/materials)

### Navigation, app structure and scrolling

- **BottomNavigationBar / NavigationItems:** Tab bars are for navigation, not
  actions. Keep them available across sections and do not hide or disable
  items merely because their destination is unavailable; explain the
  unavailable content instead. [Tab bars](https://developer.apple.com/design/human-interface-guidelines/tab-bars)
- **SideNavigation / Panes / SplitPanel:** Sidebars support hierarchical
  navigation; split views present related panes and can adapt pane visibility
  and sizing by platform. [Sidebars](https://developer.apple.com/design/human-interface-guidelines/sidebars),
  [split views](https://developer.apple.com/design/human-interface-guidelines/split-views)
- **Tabs:** Use the tab-bar guidance for top-level navigation and segmented
  controls for an in-place selection; these patterns are not interchangeable.
  [Tab bars](https://developer.apple.com/design/human-interface-guidelines/tab-bars),
  [segmented controls](https://developer.apple.com/design/human-interface-guidelines/segmented-controls)
- **TopBar / CollapsibleHeader:** Apple does not define a single cross-platform
  app-bar component equivalent. Toolbars, title bars and scroll views provide
  related, platform-specific guidance. [Toolbars](https://developer.apple.com/design/human-interface-guidelines/toolbars),
  [scroll views](https://developer.apple.com/design/human-interface-guidelines/scroll-views)
- **Scaffold:** No direct HIG scaffold component exists. Compose app structure
  from platform-specific navigation, toolbar and layout patterns.
- **ScrollbarContainers:** Scroll views describe scroll indicators and edge
  effects; do not assume a permanently visible scrollbar across platforms.
  [Scroll views](https://developer.apple.com/design/human-interface-guidelines/scroll-views)

### Feedback and transient actions

- **Dialog:** Alerts are for important information or decisions; use them
  sparingly and do not interrupt merely to provide routine information.
  [Alerts](https://developer.apple.com/design/human-interface-guidelines/alerts)
- **Snackbar:** No direct HIG snackbar component was found. Alerts are not a
  drop-in equivalent for low-priority, transient feedback. Review notification
  guidance for broader interruption levels.
  [Alerts](https://developer.apple.com/design/human-interface-guidelines/alerts),
  [managing notifications](https://developer.apple.com/design/human-interface-guidelines/managing-notifications)
- **ProgressIndicator:** Prefer determinate progress when the duration is
  known; keep reported progress accurate and avoid misleading jumps.
  [Progress indicators](https://developer.apple.com/design/human-interface-guidelines/progress-indicators)
- **PopupMenus:** Use action-oriented labels; unavailable menu items are
  generally shown dimmed. Context menus have distinct rules, including how
  unavailable and destructive actions are presented.
  [Menus](https://developer.apple.com/design/human-interface-guidelines/menus),
  [context menus](https://developer.apple.com/design/human-interface-guidelines/context-menus)
- **SelectionToolbar:** No dedicated selection-toolbar page was found.
  Toolbars provide the closest guidance for edit-mode actions and action
  grouping. [Toolbars](https://developer.apple.com/design/human-interface-guidelines/toolbars)
- **SwipeActions:** No dedicated swipe-actions page was found. Use list
  editing and gesture guidance as adjacent references, not as a complete
  swipe-action specification. [Lists and tables](https://developer.apple.com/design/human-interface-guidelines/lists-and-tables),
  [gestures](https://developer.apple.com/design/human-interface-guidelines/gestures)
- **Tooltip:** No dedicated tooltip HIG page was found; treat this as a source
  gap rather than assuming a web-style tooltip pattern.

## Review translation

For ComposeStyled, verify that renderer behavior follows the relevant Apple
platform guidance, especially hit regions, toggle semantics, list editing,
navigation roles and materials. These are review questions derived from the
HIG, not extra Apple requirements. Cupertino styling on a non-Apple target
does not automatically make that target behave like a native Apple app.
