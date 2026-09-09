# Symboard

A symbol-swipe Android keyboard. 36 layouts, ~1,200 symbol keys, one IME.

## How it works

- Every layout's bottom row has **◀ ▶** keys that cycle through all 36
  categories; the current category name is shown in the strip above the keys.
- Symbol keys commit via `android:keyOutputText` → `onText()`, so what you
  tap is exactly what lands in the field.
- No internet permission, no logging, no account — everything stays on-device.

## Layouts (cycle order)

| # | Category | # | Category |
|---|----------|---|----------|
| 1 | Bold | 19 | Fullwidth |
| 2 | Bold Italic | 20 | Upside Down |
| 3 | Italic | 21 | Small Caps |
| 4 | Script | 22 | Superscript |
| 5 | Bold Script | 23 | Subscript |
| 6 | Fraktur | 24 | Combining Deco |
| 7 | Bold Fraktur | 25 | Arrows |
| 8 | Double-Struck | 26 | Math |
| 9 | Monospace | 27 | Greek |
| 10 | Sans | 28 | Box Drawing |
| 11 | Sans Bold | 29 | Shapes |
| 12 | Sans Italic | 30 | Cards & Chess |
| 13 | Sans Bold Italic | 31 | Music & Sky |
| 14 | Circled | 32 | Currency & Legal |
| 15 | Parenthesized | 33 | Punctuation |
| 16 | Negative Circled | 34 | Zodiac |
| 17 | Squared | 35 | Fancy Numbers |
| 18 | Negative Squared | 36 | Roman & Fractions |

## Build

GitHub Actions builds a debug APK on every push (see `.github/workflows/build-apk.yml`).
Locally: open in Android Studio, or `./gradlew assembleDebug` (Gradle 8.7, JDK 17,
compileSdk 34, minSdk 26).

## Enable

Settings → System → Languages & input → On-screen keyboard → enable **Symboard**,
then switch keyboards with the globe/keyboard icon in any text field.

## Honest limits

- The legacy `KeyboardView` is deprecated since API 29 but fully functional;
  it's the simplest self-contained way to render an IME keyboard.
- Combining marks (Combining Deco page) attach to whatever you type next —
  most apps render them; a few sanitize them away.
- The Script page has no plain script lowercase **g** (Unicode never encoded
  one); it falls back to the bold-script glyph so nothing is missing.

## License

SYMBOARD COMMERCIAL LICENSE AGREEMENT — All Rights Reserved. See LICENSE.
