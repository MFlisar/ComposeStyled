# Expandable section

`StyledExpandableSection` displays a clickable heading and animated content.
Use the `MutableState<Boolean>` overload for a saveable expanded state, or the
`Boolean` / `onToggle` overload to manage expansion externally.

Scrolling is independent of the section. In the controlled overload,
`onExpansionAnimationFinished` reports the settled expanded state after an
expansion or collapse, including changes made externally. Use it to request
scrolling after the section has reached its final size. It does not fire for
the initial state. See [Scroll to top](ScrollToTop.md) for examples with regular
and lazy vertical lists.
