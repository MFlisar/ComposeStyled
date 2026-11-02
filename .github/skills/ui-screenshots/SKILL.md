---
name: ui-screenshots
description: Generate standalone PNG screenshots of UI library components using UI tests or an offscreen rendering harness. Supports component-name filters and exclusions such as all Styled components without layouts, with default and meaningful complex examples saved to documentation/screenshots.
---

# UI Component Screenshots

Create actual, locally rendered component images, not instructions for taking
screenshots manually. This skill is framework- and repository-independent:
discover the available renderer, test runner, themes and public component APIs
before choosing an implementation. Do not require an IDE preview or a browser
when the library has a usable native/offscreen UI test harness.

Example requests:

- "Mach mir Screenshots aller Styled-Komponenten ohne Layouts."
- "Generate screenshots of all public controls."
- "Capture Button and Dropdown in the light and dark themes."

## 1. Resolve the requested scope

Read the repository's contributor instructions and any applicable component,
test and documentation guides before making changes.

Inventory public visual components from source and existing examples. Apply the
user's prefix, module, category and exclusion filters. Group overloads,
convenience functions and named visual variants into their logical component
family: do not create an image for every overload, role or variant.

Exclude nonvisual APIs such as themes, providers, tokens, state objects,
defaults, registry contracts and implementation classes. Include visible
primitives and compound controls unless excluded by the request.

Interpret "without layouts" by purpose, not just package names: omit structural
containers, rows/columns/grids, scaffolds, navigation shells and modal/sheet
containers even if they live alongside controls. Keep content controls such as
cards and list items. Still use small layout wrappers where necessary to render
an included component. Do not exclude a control just because its implementation
uses layout primitives.

Follow explicit user choices for theme, platform, color mode and output path.
Otherwise use the library's existing demo/example defaults and one consistent
rendering target. If there is no clear default, ask for the relevant theme or
platform rather than multiplying every screenshot across all combinations.
Do not silently expand the request to every theme or light/dark mode.

Keep a working inventory of included families and expected captures so that
completion can be checked against the requested scope. Do not add a Markdown
planning file to the repository.

## 2. Choose representative examples

Normally create two images per component family:

| Example | Contents |
|---|---|
| `default` | The simplest useful configuration, retaining public API defaults and supplying only required data, callbacks, labels and size constraints. |
| `complex` | One coherent, realistic richer example using supported features, only when it meaningfully differs from the default. |

A richer example might combine a leading icon, supporting text and trailing
action, show a selected multi-value control, or display a populated/open picker.
Do not create a matrix of every state or variant, and do not treat an arbitrary
color change as a meaningful complex example. If the component is too simple
to benefit, generate only `default` and record that decision in the result.

Render the requested component standalone, not an entire demo screen or an
expanded demo section. Reuse existing example data and small composable samples
where useful, but remove demo navigation, configuration panels, section headings
and unrelated controls. Include only the context required for a useful image:
for example, short labels around a divider or an anchor for an open popup.

Use the public component API and its real theme/renderer/provider. Do not imitate
the component with lower-level primitives, replace it with a mock renderer or
change production defaults to make capture easier.

## 3. Use a real rendering harness

Inspect existing test source sets, dependencies and runner configuration first.
Reuse them and add the smallest reusable screenshot test/harness needed. Keep
example fixtures and capture helpers in test or screenshot tooling code, not in
the production component API. Avoid adding screenshot frameworks or dependencies
when the existing UI test tooling can already export pixels.

For Compose Multiplatform/Desktop:

- Prefer the existing JVM Compose UI test target, such as a
  `runDesktopComposeUiTest` harness or an existing Compose test rule.
- Set content to the actual theme/provider and the standalone sample.
- Give a bounded capture container a stable test tag. Wait for composition,
  measure/layout and drawing; use `captureToImage()` on the intended semantics
  node where supported by the installed version.
- Export the resulting `ImageBitmap` losslessly as PNG using the available
  desktop conversion/encoder, such as `toAwtImage()` with `ImageIO.write`, or a
  compatible Skia encoder. Verify the exact imports and API availability against
  the project's dependencies rather than assuming a particular Compose version.
- Handle popup, tooltip and dialog roots explicitly. An ordinary content-root
  capture may omit overlay windows; select the relevant root or use a renderer
  capture that includes the required layers. Do not claim an open-overlay image
  when it contains only its trigger.

For other UI frameworks, use the equivalent mounted UI test, native view capture
or offscreen rendering API. Use browser automation only when appropriate for the
framework and locally available. Never upload source or images to an external
rendering service.

If capture support is uncertain, first prove the approach with one small
component: execute the test, write a PNG and inspect it. Then expand the same
working harness to the requested inventory. Do not stop at compiling tests or
creating empty output directories.

## 4. Make output deterministic and useful

Use fixed viewport constraints, density/scale, locale, example data and color
mode. Keep the framework's normal typography and theme metrics. Use a suitable
theme background with modest, consistent padding around the component; avoid
full-window screenshots with mostly empty space.

Choose dimensions appropriate to the content and verify that labels, borders,
shadows and overlays are not clipped. Keep comparable examples at the same scale.
Do not resize different components to misleading apparent sizes.

Control animations and asynchronous state with the test runner's clock and
synchronization APIs. Advance to a known useful frame before capture; infinitely
animated progress indicators must not hang the idle wait. Open menus/tooltips
and populate selections through their supported state or test interactions.
Avoid arbitrary sleeps, network data and current-date-dependent samples.

## 5. Write persistent PNGs

The default destination is `documentation/screenshots`, relative to the
repository root. Resolve paths with the host platform's path APIs rather than
assuming the test runner's working directory is the repository root.

Use predictable lowercase kebab-case names:

```text
documentation/screenshots/styled-button-default.png
documentation/screenshots/styled-button-complex.png
documentation/screenshots/styled-divider-default.png
```

If the user requests multiple themes/platforms/modes, use stable subdirectories
for those dimensions to avoid overwriting captures. Reuse existing repository
image conventions if they already establish an unambiguous equivalent.

Make generation explicitly runnable with a targeted test filter or a dedicated
task. Ordinary unrelated test runs should not unexpectedly regenerate tracked
documentation assets. Pass output configuration through the repository's normal
test/task mechanism if needed; avoid machine-specific absolute paths in code.

Check directory creation, encoder success and file writes; propagate failures.
Do not silently skip failed components or count pre-existing files as captures
from the current execution. Preserve unrelated images and user changes. Never
recursively clear the destination as preparation or cleanup.

## 6. Verify coverage and report accurately

Run the smallest screenshot-generation command covering the requested inventory.
Regenerate the requested images even if the build would otherwise consider the
tests up to date; use the runner's targeted rerun mechanism when necessary.

Compare successful captures from this execution with the expected inventory.
Verify that each PNG decodes, has nonzero dimensions and contains the intended
visible content rather than a blank/transparent frame. Programmatic file checks
alone do not prove that the correct component was rendered: inspect the rendered
output, including complex examples, overlays and any clipping-prone controls.
If visual inspection tooling is unavailable, state that limitation explicitly.

Do not present a partial run as complete. Report failures and excluded component
families separately, including components with no useful complex example.
Keep the final handoff concise: destination, actual component/image counts,
the exact reproduction command and any genuine limitations. Do not describe
the captures as visual regression tests unless baseline comparison and failure
thresholds have actually been implemented.
