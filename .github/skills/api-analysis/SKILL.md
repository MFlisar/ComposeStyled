---
name: api-analysis
description: Analyze the public API, API consistency or library design of ComposeStyled; use locally generated Dokka documentation when it helps map a broad API surface.
---

# API Analysis with Dokka

## 1. Choose the smallest useful source

For a question about a known symbol or a small number of APIs, read the Kotlin
declarations and relevant usage examples directly. Do not generate documentation
for every API question.

For a broad public API inventory, discoverability review, naming/overload
comparison or cross-module consistency analysis, consider locally generated
Dokka documentation first. It provides a navigable view of signatures, type
relationships and KDoc without requiring all renderer implementations.

Dokka is an additional view of the code, not a replacement for it. It does not
establish runtime behavior, renderer consistency, registry wiring, binary
compatibility or whether an API design is appropriate.

## 2. Generate current documentation when useful

The repository already applies the Dokka plugin in the library modules.
`library\dokka\build.gradle.kts` configures the documentation project through
`BuildFileUtil.setupDokka(project)`, and `settings.gradle.kts` includes it through
`SettingsFileUtil.includeDokkaModule(settings)`.

Use the Gradle wrapper from the repository root. Discover the actual project and
task paths rather than assuming paths from the physical directory layout:

```powershell
.\gradlew.bat projects --console=plain
.\gradlew.bat tasks --all --console=plain | Select-String -Pattern "dokka"
```

Select the smallest relevant HTML publication task from that output. For
example, use `.\gradlew.bat <project-path>:dokkaGeneratePublicationHtml
--console=plain` only if that task is listed. Prefer a module publication for
module-local questions and the aggregate publication for cross-module questions.
Do not run a full build merely to obtain API documentation.

Use the output directory reported by the task or its configuration. Do not assume
a fixed output path. Re-run the relevant task before relying on existing output
after source changes; Gradle can reuse up-to-date outputs. If generation fails,
state the limitation and analyze source directly, without presenting old output
as current.

Keep generated documentation in build output directories; do not commit it or
publish/upload it as part of analysis.

## 3. Read selectively to save context

Start with module/package indexes, then read only pages for the relevant types,
functions and related overloads. Convert selected HTML to readable text using
existing local tooling when available; omit navigation, scripts and repeated
page chrome while preserving signatures, links and KDoc.

Do not load the complete HTML tree, duplicate overload pages or large search
indexes into context. Token savings come from focused selection, not from HTML
itself. For a small scope, source is often shorter and faster.

Check which modules, visibilities and source sets the publication actually
covers. An API missing from Dokka may be excluded or undocumented, not absent.
Pay attention to platform-specific declarations and expect/actual differences.

## 4. Ground design conclusions in source

Use Dokka to identify candidate inconsistencies, then inspect the corresponding
Kotlin declarations and relevant consumers before making recommendations.
For behavior or design questions, follow the necessary defaults, implementation
contracts, registry entries, renderers and compound usages.

Follow the Styled component skill and applicable instruction files when the
analysis concerns Styled components or leads to component changes. Consult
`documentation\Components` and `demo\shared` for intended usage.

Cite source paths and symbols for findings, distinguish documentation gaps from
API defects, and state coverage limitations. Do not claim implementation or
runtime verification from generated documentation alone.
