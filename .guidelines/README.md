# Design system guidelines

This directory is a local reference for analyzing ComposeStyled's Material 3,
Cupertino and Fluent 2 renderers. It summarizes official online design
guidance; it is reference material, not a set of automatically applied coding
instructions. The platform-specific pages separate direct guidance from
reasonable Compose review questions.

The sources were reviewed on 2026-10-30. Design-system guidance evolves, so
check the linked official pages before relying on details such as component
variants, numeric thresholds or platform availability.

## Platform references

- [Material 3](material3.md) — Google's Material Design 3 guidance.
- [Cupertino](cupertino.md) — Apple's Human Interface Guidelines (HIG).
- [Fluent 2](fluent2.md) — Microsoft's Fluent 2 guidance.

## Shared principles

These are common themes in the three systems, not a claim that the systems
recommend identical visual or interaction patterns:

- **Accessibility is part of the component contract.** Review semantics,
  keyboard and assistive-technology access, contrast, target size, focus and
  state communication—not just appearance. See [Material accessibility
  principles](https://m3.material.io/foundations/overview/principles),
  [Apple accessibility](https://developer.apple.com/design/human-interface-guidelines/accessibility)
  and [Fluent 2 principles](https://fluent2.microsoft.design/design-principles/).
- **Use the least disruptive interaction that accomplishes the task.**
  Minimize unnecessary choices and reserve interruptive dialogs and alerts for
  important moments. See [Apple layout](https://developer.apple.com/design/human-interface-guidelines/layout),
  [Apple modality](https://developer.apple.com/design/human-interface-guidelines/modality),
  [Material dialogs](https://m3.material.io/components/dialogs/guidelines)
  and [Fluent dialogs](https://fluent2.microsoft.design/components/web/react/core/dialog/usage/).
- **Make state and progress understandable.** Do not depend on color alone to
  communicate a state; prefer determinate progress when the amount of work is
  known, and avoid progress feedback that misleads users. See
  [Apple toggles](https://developer.apple.com/design/human-interface-guidelines/toggles),
  [Apple progress indicators](https://developer.apple.com/design/human-interface-guidelines/progress-indicators),
  [Material progress indicators](https://m3.material.io/components/progress-indicators/guidelines)
  and [Fluent progress bars](https://fluent2.microsoft.design/components/web/react/core/progressbar/usage/).
- **Choose components by intent, not superficial similarity.** A switch,
  checkbox, tab, menu item and button may look similar across systems while
  expressing different selection, navigation or action semantics. Consult the
  relevant system page before treating variants as interchangeable.
- **Preserve each system's platform conventions.** Material 3 uses responsive
  window classes and color roles; Apple's HIG distinguishes platform behavior
  and uses platform-specific materials; Fluent 2 emphasizes natural platform
  behavior, focus and inclusive design. Avoid flattening these into one
  supposedly universal renderer.

## ComposeStyled component coverage

These links map the component documentation to the most relevant local
reference. A linked topic is a research starting point, not a claim that every
platform has an identically named component. The platform pages call out
missing direct equivalents and relevant caveats.

| ComposeStyled component guides | Reference topics |
|---|---|
| [Badge](../documentation/Components/Badge.md), [Chip](../documentation/Components/Chip.md), [Labeled Information](../documentation/Components/LabeledInformation.md), [Surface](../documentation/Components/Surface.md) | Status, tags, labels, information and visual materials |
| [Button](../documentation/Components/Button.md), [Icon Button](../documentation/Components/IconButton.md), [Floating Action Button](../documentation/Components/FloatingActionButton.md), [Segmented Button](../documentation/Components/SegmentedButton.md) | Actions, icon actions, prominent actions and segmented selection |
| [Input](../documentation/Components/Input.md), [Search Bar](../documentation/Components/SearchBar.md), [Dropdown](../documentation/Components/Dropdown.md), [Picker](../documentation/Components/Picker.md), [Date Picker](../documentation/Components/DatePicker.md), [Time Picker](../documentation/Components/TimePicker.md), [Number Picker](../documentation/Components/NumberPicker.md) | Text entry, search and value selection |
| [Checkbox](../documentation/Components/Checkbox.md), [Radio Button](../documentation/Components/RadioButton.md), [Switch](../documentation/Components/Switch.md), [Slider](../documentation/Components/Slider.md), [Range Slider](../documentation/Components/RangeSlider.md) | Selection and value controls |
| [Card](../documentation/Components/Card.md), [Group Box](../documentation/Components/GroupBox.md), [Divider](../documentation/Components/Divider.md), [List Item](../documentation/Components/ListItem.md), [Lazy and Grid Layouts](../documentation/Components/LazyAndGridLayouts.md) | Content grouping, lists and collections |
| [Bottom Navigation Bar](../documentation/Components/BottomNavigationBar.md), [Navigation Items](../documentation/Components/NavigationItems.md), [Side Navigation](../documentation/Components/SideNavigation.md), [Tabs](../documentation/Components/Tabs.md), [Top Bar](../documentation/Components/TopBar.md), [Collapsible Header](../documentation/Components/CollapsibleHeader.md), [Scaffold](../documentation/Components/Scaffold.md), [Panes](../documentation/Components/Panes.md), [Split Panel](../documentation/Components/SplitPanel.md), [Scrollbar Containers](../documentation/Components/ScrollbarContainers.md) | Navigation, app structure, adaptive layouts and scrolling |
| [Dialog](../documentation/Components/Dialog.md), [Popup Menus](../documentation/Components/PopupMenus.md), [Selection Toolbar](../documentation/Components/SelectionToolbar.md), [Snackbar](../documentation/Components/Snackbar.md), [Progress Indicator](../documentation/Components/ProgressIndicator.md), [Tooltip](../documentation/Components/Tooltip.md), [Swipe Actions](../documentation/Components/SwipeActions.md) | Overlays, contextual actions and feedback |

## Using this reference for implementation analysis

The cited pages describe product design; the following are review questions
derived from that guidance, not quotations or additional official requirements:

- Does the component preserve the intended action, selection, navigation or
  status semantics in its Compose semantics tree?
- Are selected, disabled, pressed, loading, error and other applicable states
  visible and accessible, including when color is unavailable?
- Do focus, keyboard interaction, dismissal and focus restoration match the
  platform and component role?
- Are sizing, layout adaptation, overflow and scrolling appropriate for the
  target platform and available window size?
- Does the renderer use its theme's tokens and platform patterns rather than
  importing a different system's treatment?
- Is the component a genuine counterpart to the source guidance, or is the
  nearest analogy being applied cautiously?

## Source and scope notes

Use the official sources linked from each platform page. Some design systems
do not document a component that exists in ComposeStyled, and some guidance is
web-, mobile- or desktop-specific. Those gaps are stated rather than filled in
by assumptions. In particular, a visual analogy does not establish equivalent
behavior or accessibility semantics.
