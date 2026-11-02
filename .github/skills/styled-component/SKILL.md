---
name: styled-component
description: Create, migrate, review or refactor Styled Compose components, including theme renderers, compounds, registry wiring and consistency checks.
---

# Styled Component Workflow

Use this skill for changes to Styled components. For review-only requests, inspect
and report without modifying files unless fixes are requested.

## 1. Read the source of truth

Read `.github\copilot-instructions.md` and
`.github\instructions\styled-component.instructions.md` before component work.
The component guide links to the normative Core, renderer and compound guides;
read every guide that applies to the files and behavior being changed. Keep
technical rules in those instruction files rather than duplicating them here.
Follow the structural section-comment spacing rule in the component guide and
keep function KDoc attached directly to its declaration.

## 2. Map and classify the change

Find the public API, implementation contract, defaults, renderers, theme
factories, registry entries and all affected consumers. For migrations, also
trace generic role parameters and their context providers, variants, shared
bases, compound slots and equivalent legacy implementations. Button-like APIs
use a generic public role with `Role.Default`; do not create named
Primary/Secondary/Tertiary convenience functions, and keep explicit roles
authoritative across contexts.

For theme-specific visual or interaction behavior, consult the
[design-system guide](../../../.guidelines/README.md) and the relevant Material
3, Cupertino or Fluent 2 page. Use its component-level guidance in context, and
distinguish official recommendations from the Compose-oriented review
questions derived from them.

Classify folder placement by purpose (layout/container versus control/content)
independently of the rendering model (registered renderer versus shared
composition), as described in the component guides. Identify compatibility and
behavior changes before editing.

## 3. Implement across the affected paths

Follow the applicable Core and renderer contracts for registered controls and
layouts, including all theme factories and registry wiring. For shared
compositions and compounds, follow the shared-composition guidance and reuse
public Styled controls wherever they represent themed parts. Preserve existing API semantics, callback
ownership, content scopes and interaction behavior.

Register each theme's default renderer in its `registerXComponents()` helper
with a single `register(...)` call; a component's fixed defaults are registered
with it via `StyledComponentWithDefaults`. Place a component's `DefaultsKey`
directly below `Key` in the `// 1) key` section. Do not add a separate
per-component implementation parameter to the theme factory; renderer
replacements and default changes belong in `StyledTheme(customization)`
(`register(...)` first, then `update(DefaultsKey) { ... }`).

## 4. Document and verify

For API or behavior changes, update the relevant `demo\shared` examples and
`documentation\Components\<Component>.md`; keep the general overview in
`documentation\_partials\usage.md.partial` aligned when needed. Check structural section-comment
spacing in changed Kotlin files and keep function KDoc attached to its
declaration. Run the smallest existing build or check that covers the affected
modules and consumers. Follow the consistency checklist in the compound guide;
compilation alone does not establish visual or runtime behavior.

## 5. Report

State the scope inspected, meaningful changes, verification performed and any
remaining limitations. Do not imply runtime verification when only static
inspection was performed.
