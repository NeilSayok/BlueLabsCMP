---
version: 1
slug: "com-neilsayok-bluelabs-pages-me-screen-mescreen-kt"
primary_target: "composeApp/src/commonMain/kotlin/com/neilsayok/bluelabs/pages/me/screen/MeScreen.kt"
related_targets: []
---

# /me

Scope: a simple personal link page inside the existing Compose app. Visitor mode: Operate.

## Direction contract

THESIS: make the supplied personal links immediately available as buttons.

OWN-WORLD: inherit Blue Labs' Material 3 components, Poppins typography, and light/dark themes.

STORY: visitors choose the Play Store, portfolio, resume, phone dialer, or email composer.

FIRST VIEWPORT: one centered column, at most 420dp wide, containing the name, three full-width links, and equal-width Call and Email buttons side by side. Buttons are at least 56dp high. Allow scrolling on short screens.

FORM: user-specified narrow extension; no concept seed or new visual identity.

FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance

Verification limit: browser host is unavailable. No new images or design-system changes are required.

## Verification note

Source checks passed after the tonal change: web/Android compilation, normal web bundle, exact URL/XML self-check, and diff check. Source review passes after the local `FilledTonalButton` change: existing theme token pairs measure 12.83:1 light and 9.87:1 dark. Browser layout, activation, focus, and rendered contrast are unverified because the host is unavailable. Surface documentation is in `../me/DESIGN.md` and `../me/design.json`; no raster assets were added.
