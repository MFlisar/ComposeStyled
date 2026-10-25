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
trace convenience APIs, variants, shared bases, compound slots and equivalent
legacy implementations.

Choose the model described in the component guides: registered component,
generic layout or compound. Identify compatibility and behavior changes before
editing.

## 3. Implement across the affected paths

Follow the applicable Core and renderer contracts for registered components,
including all theme factories and registry wiring. For layouts and compounds,
follow the shared-composition guidance and reuse public Styled controls wherever
they represent themed parts. Preserve existing API semantics, callback
ownership, content scopes and interaction behavior.

Register each theme's default renderer in its `registerXComponents()` helper.
Do not add a separate per-component implementation parameter to the theme
factory; renderer replacements belong in the custom registration callback.

## 4. Document and verify

For API or behavior changes, update the relevant `demo\shared` examples and
`documentation\_partials\usage.md.partial`. Check structural section-comment
spacing in changed Kotlin files and keep function KDoc attached to its
declaration. Run the smallest existing build or check that covers the affected
modules and consumers. Follow the consistency checklist in the compound guide;
compilation alone does not establish visual or runtime behavior.

## 5. Report

State the scope inspected, meaningful changes, verification performed and any
remaining limitations. Do not imply runtime verification when only static
inspection was performed.
