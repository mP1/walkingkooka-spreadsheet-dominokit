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

package walkingkooka.spreadsheet.dominokit.pattern;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

abstract class SpreadsheetPatternEditorWidgetSampleRowProviderDateTime extends SpreadsheetPatternEditorWidgetSampleRowProvider {

    SpreadsheetPatternEditorWidgetSampleRowProviderDateTime() {
        super();
    }

    @Override
    public final List<SpreadsheetPatternEditorWidgetSampleRow> apply(final String patternText,
                                                                     final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final List<SpreadsheetPatternEditorWidgetSampleRow> rows = Lists.array();

        final LocalDateTime today = context.spreadsheetFormatterContext()
                .now();

        rows.add(
                row(
                        "Edit",
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

    private List<SpreadsheetPatternEditorWidgetSampleRow> dateFormats(final String label,
                                                                      final LocalDateTime dateTime,
                                                                      final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
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

    private SpreadsheetPatternEditorWidgetSampleRow shortSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Short",
                dateTime,
                DateFormat.SHORT,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow mediumSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Medium",
                dateTime,
                DateFormat.MEDIUM,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow longSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Long",
                dateTime,
                DateFormat.LONG,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow fullSimpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Full",
                dateTime,
                DateFormat.FULL,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow simpleDateFormat(
            final String label,
            final LocalDateTime dateTime,
            final int style,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
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
