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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

abstract class SpreadsheetPatternEditorComponentSampleRowProviderDate extends SpreadsheetPatternEditorComponentSampleRowProvider {

    SpreadsheetPatternEditorComponentSampleRowProviderDate() {
        super();
    }

    @Override
    public final List<SpreadsheetPatternEditorComponentSampleRow> apply(final String patternText,
                                                                        final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        final List<SpreadsheetPatternEditorComponentSampleRow> rows = Lists.array();

        final LocalDate today = context.spreadsheetFormatterContext()
                .now()
                .toLocalDate();

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
                        "31 December 1999",
                        LocalDate.of(1999, 12, 31),
                        context
                )
        );

        return Lists.readOnly(rows);
    }

    private List<SpreadsheetPatternEditorComponentSampleRow> dateFormats(final String label,
                                                                         final LocalDate date,
                                                                         final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        return Lists.of(
                this.shortSimpleDateFormat(
                        label,
                        date,
                        context
                ),
                this.mediumSimpleDateFormat(
                        label,
                        date,
                        context
                ),
                this.longSimpleDateFormat(
                        label,
                        date,
                        context
                ),
                this.fullSimpleDateFormat(
                        label,
                        date,
                        context
                )
        );
    }

    private SpreadsheetPatternEditorComponentSampleRow shortSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Short",
                date,
                DateFormat.SHORT,
                context
        );
    }

    private SpreadsheetPatternEditorComponentSampleRow mediumSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Medium",
                date,
                DateFormat.MEDIUM,
                context
        );
    }

    private SpreadsheetPatternEditorComponentSampleRow longSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Long",
                date,
                DateFormat.LONG,
                context
        );
    }

    private SpreadsheetPatternEditorComponentSampleRow fullSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Full",
                date,
                DateFormat.FULL,
                context
        );
    }

    private SpreadsheetPatternEditorComponentSampleRow simpleDateFormat(
            final String label,
            final LocalDate date,
            final int style,
            final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.dateParsePattern(
                (SimpleDateFormat) DateFormat.getDateInstance(
                        style,
                        context.spreadsheetFormatterContext()
                                .locale()
                )
        );

        return row(
                label,
                pattern,
                date,
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
