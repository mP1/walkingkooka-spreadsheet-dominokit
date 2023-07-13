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
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetTextFormatPattern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A {@link SpreadsheetPatternEditorWidgetSampleRowProvider} for {@link SpreadsheetTextFormatPattern}.
 */
final class SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat extends SpreadsheetPatternEditorWidgetSampleRowProvider {

    /**
     * Singleton
     */
    final static SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat INSTANCE = new SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat();

    private SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat() {
        super();
    }

    @Override
    public List<SpreadsheetPatternEditorWidgetSampleRow> apply(final String patternText,
                                                               final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final List<SpreadsheetPatternEditorWidgetSampleRow> rows = Lists.array();

        final LocalDate today = context.spreadsheetFormatterContext()
                .now()
                .toLocalDate();

        rows.add(
                row(
                        "Edit",
                        tryParsePatternText(
                                patternText,
                                SpreadsheetPattern::parseDateFormatPattern
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

    private List<SpreadsheetPatternEditorWidgetSampleRow> dateFormats(final String label,
                                                                      final LocalDate date,
                                                                      final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
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

    private SpreadsheetPatternEditorWidgetSampleRow shortSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Short",
                date,
                DateFormat.SHORT,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow mediumSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Medium",
                date,
                DateFormat.MEDIUM,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow longSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Long",
                date,
                DateFormat.LONG,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow fullSimpleDateFormat(
            final String label,
            final LocalDate date,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Full",
                date,
                DateFormat.FULL,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow simpleDateFormat(
            final String label,
            final LocalDate date,
            final int style,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.dateParsePattern(
                (SimpleDateFormat) DateFormat.getDateInstance(
                        style,
                        context.spreadsheetFormatterContext()
                                .locale()
                )
        );

        return row(
                label,
                pattern.toFormat(),
                date,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                        final SpreadsheetFormatPattern pattern,
                                                        final LocalDate date,
                                                        final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                date,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                        final Optional<SpreadsheetFormatPattern> pattern,
                                                        final LocalDate date,
                                                        final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return SpreadsheetPatternEditorWidgetSampleRow.with(
                label,
                pattern.map(SpreadsheetPattern::text)
                        .orElse(""),
                context.defaultFormat(date),
                context.format(
                        pattern.map(SpreadsheetFormatPattern::formatter)
                                .orElse(SpreadsheetFormatters.emptyText()),
                        date
                )
        );
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        return SpreadsheetPatternKind.DATE_FORMAT_PATTERN.toString();
    }
}
