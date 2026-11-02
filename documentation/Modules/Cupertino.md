# Cupertino Implementations

The Cupertino renderer is split into two independently customizable modules:
`cupertino-flat` retains the classic, flat Cupertino styling, while
`cupertino-liquid` provides a distinct Liquid Glass renderer for the shared Core
components. Both share Cupertino color themes and icon sets, but Liquid adjusts
component shapes, grouped list styling and functional materials.

Use `rememberCupertinoFlatThemeProvider()` from `cupertino-flat`, or
`rememberCupertinoLiquidThemeProvider()` from `cupertino-liquid`, to create the
corresponding theme provider. The Liquid provider uses Haze 2.0.1's
cross-platform Glass effect for functional surfaces such as buttons, navigation
bars, popup menus and snackbars; it also captures the styled content as a
backdrop source automatically. Cards and general surfaces remain content
materials rather than being blurred indiscriminately.

Renderer implementations are available in distinct `cupertino.flat.components`
and `cupertino.liquid.components` packages. Their classes use the
`CupertinoFlat` and `CupertinoLiquid` prefixes, respectively.

The Liquid renderer adjusts component radii and uses Liquid Glass for
interactive/functional layers. Haze includes platform-dependent fallbacks, so
the rendering is not guaranteed to be pixel-identical to native iOS Liquid
Glass. Floating action buttons and snackbars are adaptive Compose components,
not native UIKit controls.
