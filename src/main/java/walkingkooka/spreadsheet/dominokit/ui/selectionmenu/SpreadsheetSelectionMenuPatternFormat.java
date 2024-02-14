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

package walkingkooka.spreadsheet.dominokit.ui.selectionmenu;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Builds the context menu for format patterns.
 */
final class SpreadsheetSelectionMenuPatternFormat extends SpreadsheetSelectionMenuPattern<SpreadsheetFormatPattern> {

    static SpreadsheetSelectionMenuPatternFormat with(final HistoryToken historyToken,
                                                      final Locale locale,
                                                      final List<SpreadsheetFormatPattern> recents,
                                                      final String idPrefix) {
        return new SpreadsheetSelectionMenuPatternFormat(
                historyToken,
                locale,
                recents,
                idPrefix
        );
    }

    private SpreadsheetSelectionMenuPatternFormat(final HistoryToken historyToken,
                                                  final Locale locale,
                                                  final List<SpreadsheetFormatPattern> recents,
                                                  final String idPrefix) {
        super(
                historyToken,
                locale,
                recents,
                idPrefix
        );
    }

    @Override
    SpreadsheetFormatPattern datePattern(final int style) {
        return SpreadsheetPattern.dateParsePattern(
                (SimpleDateFormat) DateFormat.getDateInstance(
                        style,
                        this.locale
                )
        ).toFormat();
    }


    @Override
    SpreadsheetPatternKind editDatePatternKind() {
        return SpreadsheetPatternKind.DATE_FORMAT_PATTERN;
    }

    @Override
    SpreadsheetFormatPattern dateTimePattern(final int style) {
        return SpreadsheetPattern.dateTimeParsePattern(
                (SimpleDateFormat) DateFormat.getDateTimeInstance(
                        style,
                        style,
                        this.locale
                )
        ).toFormat();
    }

    @Override
    SpreadsheetPatternKind editDateTimePatternKind() {
        return SpreadsheetPatternKind.DATE_TIME_FORMAT_PATTERN;
    }

    @Override
    SpreadsheetFormatPattern numberPattern(final Function<Locale, NumberFormat> decimalFormat) {
        return SpreadsheetPattern.numberFormatPattern(
                SpreadsheetPattern.decimalFormat(
                                (DecimalFormat) decimalFormat.apply(
                                        this.locale
                                )
                        ).toFormat()
                        .value()
        );
    }

    @Override
    SpreadsheetPatternKind editNumberPatternKind() {
        return SpreadsheetPatternKind.NUMBER_FORMAT_PATTERN;
    }

    @Override
    SpreadsheetFormatPattern timePattern(final int style) {
        return SpreadsheetPattern.timeParsePattern(
                (SimpleDateFormat) DateFormat.getTimeInstance(
                        style,
                        this.locale
                )
        ).toFormat();
    }

    @Override
    SpreadsheetPatternKind editTimePatternKind() {
        return SpreadsheetPatternKind.TIME_FORMAT_PATTERN;
    }

    @Override
    boolean isFormat() {
        return true;
    }
}
