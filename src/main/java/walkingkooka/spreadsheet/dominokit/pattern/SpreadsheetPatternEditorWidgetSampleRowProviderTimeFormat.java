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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * A {@link SpreadsheetPatternEditorWidgetSampleRowProvider} for {@link SpreadsheetTextFormatPattern}.
 */
final class SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat extends SpreadsheetPatternEditorWidgetSampleRowProvider {

    /**
     * Singleton
     */
    final static SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat INSTANCE = new SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat();

    private SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat() {
        super();
    }

    @Override
    public List<SpreadsheetPatternEditorWidgetSampleRow> apply(final String patternText,
                                                               final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final List<SpreadsheetPatternEditorWidgetSampleRow> rows = Lists.array();

        final LocalTime now = context.spreadsheetFormatterContext()
                .now()
                .toLocalTime();

        rows.add(
                row(
                        "Edit",
                        tryParsePatternText(
                                patternText,
                                SpreadsheetPattern::parseTimeFormatPattern
                        ),
                        now,
                        context
                )
        );
        rows.addAll(
                timeFormats(
                        "Now",
                        now,
                        context
                )
        );
        rows.addAll(
                timeFormats(
                        "12:58:59 AM",
                        LocalTime.of(12, 58, 59),
                        context
                )
        );
        rows.addAll(
                timeFormats(
                        "6:01:02 PM",
                        LocalTime.of(18, 1, 2),
                        context
                )
        );

        return Lists.readOnly(rows);
    }

    private List<SpreadsheetPatternEditorWidgetSampleRow> timeFormats(final String label,
                                                                      final LocalTime time,
                                                                      final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return Lists.of(
                this.shortSimpleDateFormat(
                        label,
                        time,
                        context
                ),
                this.longSimpleDateFormat(
                        label,
                        time,
                        context
                )
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow shortSimpleDateFormat(
            final String label,
            final LocalTime time,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Short",
                time,
                DateFormat.SHORT,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow longSimpleDateFormat(
            final String label,
            final LocalTime time,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.simpleDateFormat(
                label + " Long",
                time,
                DateFormat.LONG,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow simpleDateFormat(
            final String label,
            final LocalTime time,
            final int style,
            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.timeParsePattern(
                (SimpleDateFormat) DateFormat.getTimeInstance(
                        style,
                        context.spreadsheetFormatterContext()
                                .locale()
                )
        );

        return row(
                label,
                pattern.toFormat(),
                time,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                        final SpreadsheetFormatPattern pattern,
                                                        final LocalTime time,
                                                        final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                time,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                        final Optional<SpreadsheetFormatPattern> pattern,
                                                        final LocalTime time,
                                                        final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return SpreadsheetPatternEditorWidgetSampleRow.with(
                label,
                pattern.map(SpreadsheetPattern::text)
                        .orElse(""),
                context.defaultFormat(time),
                context.format(
                        pattern.map(SpreadsheetFormatPattern::formatter)
                                .orElse(SpreadsheetFormatters.emptyText()),
                        time
                )
        );
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        return SpreadsheetPatternKind.TIME_FORMAT_PATTERN.toString();
    }
}
