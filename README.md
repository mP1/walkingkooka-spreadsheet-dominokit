[![Build Status](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-spreadsheet-dominokit/badge.svg?branch=master)](https://coveralls.io/repos/github/mP1/walkingkooka-spreadsheet-dominokit?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-spreadsheet-dominokit.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-dominokit/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-spreadsheet-dominokit.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-dominokit/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

# walkingkooka-spreadsheet-dominokit

A fat browser spreadsheet using the wonderful [dominoKit](https://github.com/DominoKit/domino-ui) library.

# Client / Server

The spreadsheet is a combination of a rich web application which executes in the browser and a variety of services
provided by a server. All the server code transpiles to javascript so it should be possible to move the server into web
workers.

## Server

Run jetty server [HERE](https://github.com/mP1/walkingkooka-spreadsheet-server-platform)
```
git clone walkingkooka-spreadsheet-server-platform

java walkingkooka.spreadsheet.server.platform.JettyHttpServerSpreadsheetHttpServer2
```

This will provide a variety of services that are consumed by the rich web (this repo).

### Expression Functions

About 150 functions are currently implemented with automated tests, and available within any expression. For more info
including a list of available functions
goto [HERE](https://github.com/mP1/walkingkooka-spreadsheet-server-expression-function)

## Client

Run GWT code server.
```bash
mvn build-helper:add-source gwt:codeserver -X -f pom.xml
```

Open in a browser, this will load the rich web-app, ready to be used.
```bash
http://localhost:12345/walkingkooka.spreadsheet.dominokit.App/index.html
```

# Architecture | Design

- [Clipboard JSON Format](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/clipboard/ClipboardTextItem.java).
- [HistoryToken](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/history/HistoryToken.java#L71)
- [SpreadsheetDeltaFetcher](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/net/SpreadsheetDeltaFetcher.java#L34)
- [SpreadsheetMetadataFetcher](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/blob/master/src/main/java/walkingkooka/spreadsheet/dominokit/net/SpreadsheetMetadataFetcher.java#L34)

# Features

This provides an overview of currently available features and future goals for this spreadsheet. Note it should not be
considered complete but can be considered attempt at sharing knowledge of what has been done and what is outstanding.
Individual issues that breakdown outstanding tasks will be added.

## Formulas

- Formulas editing *DONE*
- Formula evaluation when ENTER hit.
- Interactive function code completion when entering
  expression [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2062)
- Expression error messages are shown, without any point to the actual position of the
  mistake [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2064)

## Viewport (grid of cells)

- Keyboard
  - Cursor key navigation *DONE*
  - ENTER gives focus to Formula for editing
- Mouse
  - Click cell, column, row to select *DONE*
  - Mouse SHIFT click to extend cell/column/row range.
  - Support dragging mouse to select/extend a
    range [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2063)
- Cell formatting:
  - Alignment: Left, Center, Right, Justify *DONE*
  - Vertical alignment: Top, Middle, Bottom *DONE*
  - Text Color *DONE*
  - Background color *DONE*
  - Bold *DONE*
  - Italics *DONE*
  - Strikethru *DONE*
  - Underline *DONE*
  - Format patterns: select & edit Date, DateTime, Number, Text, Time *DONE*
  - Parse patterns: select & edit Date, DateTime, Number, Text, Time *DONE*
  - Text case: Normal, Capitalize, Lowercase, Uppercase *DONE*
  - Border
    - Top, Left, Right, Bottom, All: color, style, width *DONE*
  - Wrapping: clip, Overflow, Wrap *DONE*
- Column, Row, Cell actions:
  - Insert columns before *DONE*
  - Insert columns after *DONE*
  - Clear *DONE*
  - Delete *DONE*
- Sort
  - cells any rectangular cell range of at least 2 cells *DONE*
  - columns *DONE*
  - rows *DONE*
  - Edit sort comparators expressions [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2620)
  - Sort by by Date, Date-time Day of month, Day of week, Hour of am, Hour of day, Minute of Hour, Month of Year, Nano
    of Second, Number, Second of Minute, Text, Text case-insensitive, Time, Year *DONE*
  - Up and down *DONE*
- Clipboard cell, formula, format-pattern, parse-pattern, formatted-value
  - CUT *DONE*
  - COPY *DONE*
  - PASTE *DONE*
  - Additional clipboard text formats such as
    - CSV [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2182)
    - plain text [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2688)

## Toolbar

- Many context menu items are also available as buttons in the toolbar.
- Add a Font drop down [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/647)
- Add a Font size drop down [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/646)
- Print spreadsheet to PDF [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2066)
- Find icon, feature to find and highlight cells using an expression. *DONE* Needs polish.

## Global settings (aka SpreadsheetMetadata)

Global settings that may be added or updated appear in a panel that appears on the right edge of the web-app

- View audit info such as creator, last updated by, timestamps *DONE*
- Locale editing, affects default format and parse patterns and various symbols decimal point character
  etc. [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2065)
- Support for changing various locale sourced characters such as decimal point, currency sign etc *DONE*
- Editing of default format & parse patterns, eg the default the Number format pattern when a cell is missing one *DONE*

## File management

- Browser Dialog
  - open *DONE*
  - rename *DONE*
  - delete *DONE*
  - print [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2330)
  - download [TODO](https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2329)

## UI, Theme, icons

- The UI can be described as spartan as it uses the default DominoKit theme. No work has been done to improve.

# CC Acknowledgements

- [Swagger Icon](https://iconduck.com/icons/94965/swagger)