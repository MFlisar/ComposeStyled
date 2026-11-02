# Fluent 2 guidelines

This page summarizes official [Fluent 2](https://fluent2.microsoft.design/)
guidance relevant to reviewing ComposeStyled renderers. Fluent 2 guidance is
often documented for web/React; check platform applicability before treating
component APIs or accessibility details as universal Compose requirements.

## System-wide guidance

Fluent 2 names four design principles: **Natural on every platform**, **Built
for focus**, **One for all, all for one**, and **Unmistakably Microsoft**.
These emphasize fitting platform conventions, reducing noise, inclusive
design and a recognizable Microsoft expression.
[Design principles](https://fluent2.microsoft.design/design-principles/)

The public component guidance commonly includes explicit usage, accessibility
and content recommendations. In particular, label wording and capitalization
are part of the component experience, not merely implementation details.
See the relevant [component usage pages](https://fluent2.microsoft.design/components/web/react/core/button/usage/).

## Component guidance

### Actions and selectable content

- **Button / IconButton / FloatingActionButton:** Fluent distinguishes
  standard, split, menu and compound actions. Keep one primary button per
  layout; icon-only controls need adequate contrast and accessible naming.
  Toggle buttons suit toolbar interactions, while switches suit immediate
  settings. There is no dedicated Fluent FAB pattern.
  [Button](https://fluent2.microsoft.design/components/web/react/core/button/usage/)
- **Chip:** Fluent's closest equivalent is a Tag or Interaction tag; these
  represent user-picked values or actions rather than the system status
  communicated by a Badge. [Tag](https://fluent2.microsoft.design/components/web/react/core/tag/usage/)
- **Badge:** Use as a status indicator associated with another component;
  avoid mixing sizes or expecting automatic truncation.
  [Badge](https://fluent2.microsoft.design/components/web/react/core/badge/usage)
- **SegmentedButton:** No directly named Fluent 2 component was found.
  Tablist is a nearby pattern for switching among content views, but it is
  not necessarily equivalent to segmented selection.
  [Tablist](https://fluent2.microsoft.design/components/web/react/core/tablist/usage/)

### Entry, selection and value controls

- **Input:** Do not rely on placeholder text for essential information; use
  a visible label and size the field for expected content.
  [Input](https://fluent2.microsoft.design/components/web/react/core/input/usage/)
- **Checkbox:** Supports indeterminate parent/child selection; avoid
  confusing negative labels and keep option groups manageable.
  [Checkbox](https://fluent2.microsoft.design/components/web/react/core/checkbox/usage/)
- **Switch:** Represents a mutually exclusive setting that changes
  immediately. Use a short positive label, not a question or "Turn on/off"
  wording. [Switch](https://fluent2.microsoft.design/components/web/react/core/switch/usage/)
- **RadioButton:** Radio groups select one option; provide a sensible default,
  prefer vertical arrangement and keep the number of choices manageable.
  [Radio group](https://fluent2.microsoft.design/components/web/react/core/radiogroup/usage/)
- **Dropdown / Picker:** Dropdown supports single or multiple selection;
  consider a Select for a mobile-friendly selection pattern or a Combobox
  when free-text input is needed. [Dropdown](https://fluent2.microsoft.design/components/web/react/core/dropdown/usage/)
- **DatePicker / TimePicker:** No direct DatePicker or TimePicker guidance
  was found in the public Fluent 2 Web/React core component set. Windows
  controls are referenced separately, outside this Fluent 2 page set.
- **NumberPicker:** Spin buttons support keyboard-based increments and
  boundary navigation; use an input when the range is very small or very
  large. [Spin button](https://fluent2.microsoft.design/components/web/react/core/spin/usage)
- **Slider / RangeSlider:** Sliders may be continuous or stepped and support
  track interaction. Provide an accessible label and expose the current value.
  [Slider](https://fluent2.microsoft.design/components/web/react/core/slider/usage/)
- **SearchBar:** Searchbox adapts to small viewports and has recommended
  ordering for recent, suggested and related search content.
  [Searchbox](https://fluent2.microsoft.design/components/web/react/core/searchbox/usage/)

### Content, grouping and layout

- **Card:** Cards can be composed vertically or horizontally. Use a
  whole-card action only when there is one obvious action, and clearly signal
  selectable cards. [Card](https://fluent2.microsoft.design/components/web/react/core/card/usage/)
- **GroupBox:** No direct GroupBox, Box or Fieldset guidance was found in the
  Fluent 2 core set. Layout spacing is the closest grouping guidance.
  [Layout](https://fluent2.microsoft.design/layout)
- **LabeledInformation:** Field combines a label, control and supporting or
  validation text; Info label pairs a label with an information popover.
  [Field](https://fluent2.microsoft.design/components/web/react/core/field/usage/),
  [Info label](https://fluent2.microsoft.design/components/web/react/core/infolabel/usage/),
  [Label](https://fluent2.microsoft.design/components/web/react/core/label/usage/)
- **Divider:** Fluent distinguishes block and inset dividers; use decorative
  dividers without exposing them as meaningful content.
  [Divider](https://fluent2.microsoft.design/components/web/react/core/divider/usage/)
- **ListItem / LazyAndGridLayouts:** Use List for vertical item collections;
  use Tree for hierarchy and a data grid/table for columnar data. List
  selection patterns define keyboard behavior that should be reviewed
  explicitly. Layout guidance includes a grid system.
  [List](https://fluent2.microsoft.design/components/web/react/core/list/usage/),
  [layout](https://fluent2.microsoft.design/layout)
- **Surface:** Material and elevation guidance describes surface treatments;
  avoid applying a material effect without considering its purpose.
  [Material](https://fluent2.microsoft.design/material),
  [Elevation](https://fluent2.microsoft.design/elevation)

### Navigation, app structure and scrolling

- **NavigationItems / SideNavigation:** Fluent Nav supports one nested level;
  use Tree for deeper hierarchies. Collapsing a category affects the selected
  indicator. [Nav](https://fluent2.microsoft.design/components/web/react/core/nav/usage/)
- **BottomNavigationBar:** No direct bottom-navigation-bar specification was
  found in the Fluent 2 Web/React core set.
- **Tabs:** Tablist is horizontal and does not wrap; overflow is handled
  through an overflow menu. Icon-only tabs need an accessible name and a
  sighted-user tooltip. [Tablist](https://fluent2.microsoft.design/components/web/react/core/tablist/usage/)
- **TopBar / Scaffold:** No general scaffold or app-bar component equivalent
  was found in the reviewed Fluent 2 core guidance.
- **CollapsibleHeader:** No direct collapsing-header pattern was found;
  Toolbar is only an adjacent reference for grouped actions and overflow.
  [Toolbar](https://fluent2.microsoft.design/components/web/react/core/toolbar/usage/)
- **Panes:** Drawer guidance distinguishes inline drawers, which sit beside
  content, from overlay drawers. This is not a general resizable pane
  specification. [Drawer](https://fluent2.microsoft.design/components/web/react/core/drawer/usage/)
- **SplitPanel:** No direct resizable split-panel guidance was found.
- **ScrollbarContainers:** No direct scrollbar-container guidance was found
  in the reviewed Fluent 2 core set.

### Feedback and transient actions

- **Dialog:** Fluent distinguishes modal, non-modal and alert dialogs.
  Manage focus on open and close, do not nest dialogs, and keep actions and
  content focused. Non-modal dialogs retain an explicit close action.
  [Dialog](https://fluent2.microsoft.design/components/web/react/core/dialog/usage/)
- **PopupMenus:** Menus support action, checkbox and radio patterns. Avoid
  excessive submenu depth and allow long labels to remain readable.
  [Menu](https://fluent2.microsoft.design/components/web/react/core/menu/usage/)
- **ProgressIndicator:** Prefer determinate progress when possible; avoid
  moving a progress bar backward. Use a spinner when duration is unknown and
  provide status text for longer waits.
  [Progress bar](https://fluent2.microsoft.design/components/web/react/core/progressbar/usage/),
  [spinner](https://fluent2.microsoft.design/components/web/react/core/spinner/usage/)
- **Snackbar:** Toasts provide transient feedback; Message bars are more
  persistent. Do not treat these as interchangeable without considering
  urgency and duration. [Toast](https://fluent2.microsoft.design/components/web/react/core/toast/usage/),
  [Message bar](https://fluent2.microsoft.design/components/web/react/core/messagebar/usage/)
- **Tooltip:** Tooltips are triggered by hover or focus; use them for
  supplementary identification, not system feedback, and ensure the
  relationship is accessible. [Tooltip](https://fluent2.microsoft.design/components/web/react/core/tooltip/usage/)
- **SelectionToolbar:** No dedicated selection-toolbar pattern was found.
  List selection and Toolbar guidance are the nearest references.
  [List](https://fluent2.microsoft.design/components/web/react/core/list/usage/),
  [Toolbar](https://fluent2.microsoft.design/components/web/react/core/toolbar/usage/)
- **SwipeActions:** No direct swipe-action guidance was found in the reviewed
  Fluent 2 sections.

## Review translation

For ComposeStyled, use Fluent's explicit usage and accessibility sections to
check focus handling, keyboard interaction, accessible labels, selection
semantics, overflow and content wording. These are review prompts based on
Fluent guidance, not a claim that Web/React ARIA and keyboard contracts map
one-to-one to Compose semantics or platform behavior.
