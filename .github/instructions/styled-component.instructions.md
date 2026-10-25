---
applyTo: "**/*.kt"
---

# Styled component instructions

These linked guides are the complete, normative guidance for Styled component
work. Follow every guide applicable to the files being changed; for cross-cutting
changes, read all three. The scoped guides are also loaded automatically for
matching Kotlin files.

- [Core API and component structure](styled-component-core.instructions.md)
- [Theme renderers and mappings](styled-component-renderer.instructions.md)
- [Layouts, compounds and consistency review](styled-component-compounds.instructions.md)

## Kotlin comment spacing

Keep structural section-comment blocks used by the component and renderer file
layouts separated from surrounding code by one blank line before and after.
Function KDoc must stay directly attached to its declaration: do not put a blank
line between the comment and its annotation or function. Inline parameter-group
labels remain attached to the parameters they describe.
