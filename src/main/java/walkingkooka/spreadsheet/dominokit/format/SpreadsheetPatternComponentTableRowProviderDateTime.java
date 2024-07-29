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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

abstract class SpreadsheetPatternComponentTableRowProviderDateTime extends SpreadsheetPatternComponentTableRowProvider {

    SpreadsheetPatternComponentTableRowProviderDateTime() {
        super();
    }

    @Override
    public final List<SpreadsheetPatternComponentTableRow> apply(final String patternText,
                                                                 final SpreadsheetPatternComponentTableRowProviderContext context) {
        final List<SpreadsheetPatternComponentTableRow> rows = Lists.array();

        final LocalDateTime today = context.spreadsheetFormatterContext()
                .now();

        rows.add(
                row(
                        LABEL,
                        tryParsePatternText(
                                patternText,
                                this.kind()::parse
                        ),
                        today,
                        context
                )
        );
        rows.addAll(
                dateFormats(
                        "Today",
                        today,
                        context
                )
        );
        rows.addAll(
                dateFormats(
                        "31 December 1999 12:58:59",
                        LocalDateTime.of(1999, 12, 31, 12, 58, 59),
                        context
                )
        );

        return Lists.readOnly(rows);
    }

    private List<SpreadsheetPatternComponentTableRow> dateFormats(final String label,
                                                                  final LocalDateTime dateTime,
                                                                  final SpreadsheetPatternComponentTableRowProviderContext context) {
        return Lists.of(
                this.shortSimpleDateFormat(
                        label,
                        dateTime,
                        context
                ),
                this.mediumSimpleDateFormat(
                        label,
                        dateTime,
                        context
                ),
                this.longSimpleDateFormat(
                        label,
                        dateTime,
                        context
                ),
                this.fullSimpleDateFormat(
                        label,
                        dateTime,
                        context
                )
        );
    }

    private SpreadsheetPatternComponentTableRow shortSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternComponentTableRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Short",
                dateTime,
                DateFormat.SHORT,
                context
        );
    }

    private SpreadsheetPatternComponentTableRow mediumSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternComponentTableRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Medium",
                dateTime,
                DateFormat.MEDIUM,
                context
        );
    }

    private SpreadsheetPatternComponentTableRow longSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternComponentTableRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Long",
                dateTime,
                DateFormat.LONG,
                context
        );
    }

    private SpreadsheetPatternComponentTableRow fullSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternComponentTableRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Full",
                dateTime,
                DateFormat.FULL,
                context
        );
    }

    private SpreadsheetPatternComponentTableRow simpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final int style,
            final SpreadsheetPatternComponentTableRowProviderContext context) {
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.dateTimeParsePattern(
                (SimpleDateFormat) DateFormat.getDateTimeInstance(
                        style,
                        style,
                        context.spreadsheetFormatterContext()
                                .locale()
                )
        );

        return row(
                label,
                pattern,
                dateTime,
                context
        );
    }

    // toString.........................................................................................................

    @Override
    public final String toString() {
        return this.kind().toString();
    }

    abstract SpreadsheetPatternKind kind();
}
