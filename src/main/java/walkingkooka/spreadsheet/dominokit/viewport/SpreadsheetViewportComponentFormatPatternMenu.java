/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.spreadsheet.dominokit.component.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetDateTimeFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTimeFormatPattern;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * Builds the context menu for format patterns.
 */
final class SpreadsheetViewportComponentFormatPatternMenu {

    static SpreadsheetViewportComponentFormatPatternMenu with(final HistoryToken historyToken,
                                                              final Locale locale) {
        return new SpreadsheetViewportComponentFormatPatternMenu(
                historyToken,
                locale
        );
    }

    private SpreadsheetViewportComponentFormatPatternMenu(final HistoryToken historyToken,
                                                          final Locale locale) {
        this.historyToken = historyToken.clearAction();
        this.locale = locale;
    }

    void build(final SpreadsheetContextMenu menu) {
        this.date(
                menu.subMenu("Date")
        );
        this.dateTime(
                menu.subMenu("Date Time")
        );
        this.number(
                menu.subMenu("Number")
        );
        this.text(
                menu.subMenu("Text")
        );
        this.time(
                menu.subMenu("Time")
        );
    }

    private void date(final SpreadsheetContextMenu menu) {
        this.dateMenuItem(
                "Short",
                DateFormat.SHORT,
                menu
        );
        this.dateMenuItem(
                "Medium",
                DateFormat.MEDIUM,
                menu
        );
        this.dateMenuItem(
                "Long",
                DateFormat.LONG,
                menu
        );
        this.dateMenuItem(
                "Full",
                DateFormat.FULL,
                menu
        );
        this.edit(
                menu,
                SpreadsheetPatternKind.DATE_FORMAT_PATTERN
        );
    }

    private void dateMenuItem(final String label,
                              final int style,
                              final SpreadsheetContextMenu menu) {
        final SpreadsheetDateFormatPattern pattern = SpreadsheetPattern.dateParsePattern(
                (SimpleDateFormat) DateFormat.getDateInstance(
                        style,
                        this.locale
                )
        ).toFormat();

        menu.item(
                label + " " + pattern,
                this.historyToken.setPattern(pattern)
        );
    }

    private void dateTime(final SpreadsheetContextMenu menu) {
        this.dateTimeMenuItem(
                "Short",
                DateFormat.SHORT,
                menu
        );
        this.dateTimeMenuItem(
                "Medium",
                DateFormat.MEDIUM,
                menu
        );
        this.dateTimeMenuItem(
                "Long",
                DateFormat.LONG,
                menu
        );
        this.dateTimeMenuItem(
                "Full",
                DateFormat.FULL,
                menu
        );
        this.edit(
                menu,
                SpreadsheetPatternKind.TIME_FORMAT_PATTERN
        );
    }

    private void dateTimeMenuItem(final String label,
                                  final int style,
                                  final SpreadsheetContextMenu menu) {
        final SpreadsheetDateTimeFormatPattern pattern = SpreadsheetPattern.dateTimeParsePattern(
                (SimpleDateFormat) DateFormat.getDateTimeInstance(
                        style,
                        style,
                        this.locale
                )
        ).toFormat();

        menu.item(
                label + " " + pattern,
                this.historyToken.setPattern(pattern)
        );
    }

    private void number(final SpreadsheetContextMenu menu) {
        this.numberMenuItemGeneral(
                menu
        );
        this.numberMenuItem(
                "Number",
                DecimalFormat::getInstance,
                menu
        );
        this.numberMenuItem(
                "Integer",
                DecimalFormat::getIntegerInstance,
                menu
        );
        this.numberMenuItem(
                "Percent",
                DecimalFormat::getPercentInstance,
                menu
        );
        this.numberMenuItem(
                "Currency",
                DecimalFormat::getCurrencyInstance,
                menu
        );
        this.edit(
                menu,
                SpreadsheetPatternKind.NUMBER_FORMAT_PATTERN
        );
    }

    private final static SpreadsheetFormatPattern GENERAL = SpreadsheetPattern.parseNumberFormatPattern("General");

    private void numberMenuItemGeneral(final SpreadsheetContextMenu menu) {
        this.numberMenuItem(
                "General",
                SpreadsheetPattern.numberFormatPattern(
                        GENERAL.toFormat()
                        .value()
                ),
                menu
        );
    }

    private void numberMenuItem(final String label,
                                final Function<Locale, NumberFormat> decimalFormat,
                                final SpreadsheetContextMenu menu) {
        this.numberMenuItem(
            label,
                SpreadsheetPattern.numberFormatPattern(
                        SpreadsheetPattern.decimalFormat(
                                        (DecimalFormat) decimalFormat.apply(
                                                this.locale
                                        )
                                ).toFormat()
                                .value()
                ),
                menu
        );
    }

    private void numberMenuItem(final String label,
                                final SpreadsheetPattern pattern,
                                final SpreadsheetContextMenu menu) {
        menu.item(
                label + " " + pattern,
                this.historyToken.setPattern(pattern)
        );
    }

    private void text(final SpreadsheetContextMenu menu) {
        menu.item(
                "Default " + SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN,
                this.historyToken.setPattern(
                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                )
        );

        this.edit(
                menu,
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN
        );
    }

    private void time(final SpreadsheetContextMenu menu) {
        this.timeMenuItem(
                "Short",
                DateFormat.SHORT,
                menu
        );
        this.timeMenuItem(
                "Medium",
                DateFormat.MEDIUM,
                menu
        );
        this.timeMenuItem(
                "Long",
                DateFormat.LONG,
                menu
        );
        this.timeMenuItem(
                "Full",
                DateFormat.FULL,
                menu
        );
        this.edit(
                menu,
                SpreadsheetPatternKind.TIME_FORMAT_PATTERN
        );
    }

    private void timeMenuItem(final String label,
                              final int style,
                              final SpreadsheetContextMenu menu) {
        final SpreadsheetTimeFormatPattern pattern = SpreadsheetPattern.timeParsePattern(
                (SimpleDateFormat) DateFormat.getTimeInstance(
                        style,
                        this.locale
                )
        ).toFormat();

        menu.item(
                label + " " + pattern,
                this.historyToken.setPattern(pattern)
        );
    }

    private void edit(final SpreadsheetContextMenu menu,
                      final SpreadsheetPatternKind kind) {
        menu.item(
                "Edit...",
                this.historyToken.setPatternKind(
                        Optional.of(kind)
                )
        );
    }

    private final HistoryToken historyToken;
    private final Locale locale;
}
