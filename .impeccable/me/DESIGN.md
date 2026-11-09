---
name: "Blue Labs /me"
description: "Source-derived record of the personal link page, scoped to /me."
colors:
  light-secondary-container: "#DDF3FF"
  light-on-secondary-container: "#002C3E"
  dark-secondary-container: "#00374C"
  dark-on-secondary-container: "#B5EBFF"
  light-background: "#F0F8FF"
  light-on-background: "#2E2E2E"
  dark-background: "#121212"
  dark-on-background: "#E0E0E0"
typography:
  headline: {fontFamily: "Poppins"}
  label: {fontFamily: "Poppins"}
components:
  button-tonal-light: {backgroundColor: "{colors.light-secondary-container}", textColor: "{colors.light-on-secondary-container}"}
  button-tonal-dark: {backgroundColor: "{colors.dark-secondary-container}", textColor: "{colors.dark-on-secondary-container}"}
---

# Design System: Blue Labs /me

## Overview
The page inherits Blue Labs' Material 3 components, Poppins typography, and light/dark themes. This records the implemented surface only; it establishes no global design rules.
Sources: `pages/me/screen/MeScreen.kt`, `theme/Theme.kt`, `theme/Type.kt`, and `theme/Colors.kt` under the common Kotlin source tree.

## Colors
Tonal buttons inherit secondaryContainer/onSecondaryContainer; the surrounding scaffold uses background/onBackground from the active theme. Values above come from `theme/Colors.kt`.

## Typography
The centered name uses `MaterialTheme.typography.headlineMedium`; button labels inherit Material 3 button typography. `theme/Type.kt` applies the bundled Poppins family to all roles.

## Layout
A centered column fills the available width up to 420dp, with 24dp outer padding and 16dp between children. The page scrolls on short screens. The first three buttons fill the column; Call and Email share a row with equal widths and a 16dp gap. All buttons have a 56dp minimum height.

## Elevation & Depth
The page adds no elevation override; existing Material 3 defaults apply.

## Shapes
Buttons use the existing Material 3 shape without a page-specific override.

## Components
The name is followed by “Visit my Play Store”, “Visit my portfolio”, “Download my resume”, and a row containing “Call” and “Email”. Each `FilledTonalButton` opens its destination through `LocalUriHandler`. Call uses `tel:+919051880247` to open the dialer; Email uses `mailto:sayokdeymajumder@gmail.com` to open an email composer with the recipient set. Button interaction states remain Material 3 defaults; the app's shared navigation and theme controls remain present.

## Do's and Don'ts
Do preserve the existing theme and supplied destinations. Don't introduce a new visual identity for this surface.
Source verification passed after the tonal change: web/Android compilation, normal web bundle, exact URL/XML self-check, and diff check. Tonal button source-token contrast is 12.83:1 light and 9.87:1 dark. No raster assets were added. Browser visual checks, activation, and rendered contrast remain unverified because the host is unavailable.
