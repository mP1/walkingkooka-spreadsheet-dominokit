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

import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

final class SpreadsheetPatternEditorWidgetSampleRowFormat<T> implements SpreadsheetPatternEditorWidgetSampleRow {

    static SpreadsheetPatternEditorWidgetSampleRowFormat<LocalDate> dateFormat(final String label,
                                                                               final Supplier<String> pattern,
                                                                               final LocalDate value,
                                                                               final SpreadsheetFormatter defaultFormatter,
                                                                               final SpreadsheetFormatterContext context) {
        return new SpreadsheetPatternEditorWidgetSampleRowFormat<LocalDate>(
                label,
                pattern,
                value,
                SpreadsheetPattern::parseDateFormatPattern,
                defaultFormatter,
                context
        );
    }

    static SpreadsheetPatternEditorWidgetSampleRowFormat<LocalDateTime> dateTimeFormat(final String label,
                                                                                       final Supplier<String> pattern,
                                                                                       final LocalDateTime value,
                                                                                       final SpreadsheetFormatter defaultFormatter,
                                                                                       final SpreadsheetFormatterContext context) {
        return new SpreadsheetPatternEditorWidgetSampleRowFormat<LocalDateTime>(
                label,
                pattern,
                value,
                SpreadsheetPattern::parseDateTimeFormatPattern,
                defaultFormatter,
                context
        );
    }

    static SpreadsheetPatternEditorWidgetSampleRowFormat<ExpressionNumber> numberFormat(final String label,
                                                                                        final Supplier<String> pattern,
                                                                                        final ExpressionNumber value,
                                                                                        final SpreadsheetFormatter defaultFormatter,
                                                                                        final SpreadsheetFormatterContext context) {
        return new SpreadsheetPatternEditorWidgetSampleRowFormat<ExpressionNumber>(
                label,
                pattern,
                value,
                SpreadsheetPattern::parseNumberFormatPattern,
                defaultFormatter,
                context
        );
    }

    static SpreadsheetPatternEditorWidgetSampleRowFormat<String> textFormat(final String label,
                                                                            final Supplier<String> pattern,
                                                                            final String value,
                                                                            final SpreadsheetFormatter defaultFormatter,
                                                                            final SpreadsheetFormatterContext context) {
        return new SpreadsheetPatternEditorWidgetSampleRowFormat<String>(
                label,
                pattern,
                value,
                SpreadsheetPattern::parseTextFormatPattern,
                defaultFormatter,
                context
        );
    }

    static SpreadsheetPatternEditorWidgetSampleRowFormat<LocalTime> timeFormat(final String label,
                                                                               final Supplier<String> pattern,
                                                                               final LocalTime value,
                                                                               final SpreadsheetFormatter defaultFormatter,
                                                                               final SpreadsheetFormatterContext context) {
        return new SpreadsheetPatternEditorWidgetSampleRowFormat<LocalTime>(
                label,
                pattern,
                value,
                SpreadsheetPattern::parseTimeFormatPattern,
                defaultFormatter,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRowFormat(final String label,
                                                          final Supplier<String> pattern,
                                                          final T value,
                                                          final Function<String, ? extends SpreadsheetFormatPattern> formatPatternParser,
                                                          final SpreadsheetFormatter defaultFormatter,
                                                          final SpreadsheetFormatterContext context) {
        this.label = CharSequences.failIfNullOrEmpty(label, "label");
        this.pattern = Objects.requireNonNull(pattern, "pattern");
        this.value = Objects.requireNonNull(value, "value");
        this.defaultFormatter = Objects.requireNonNull(defaultFormatter, "defaultFormatter");
        this.formatPatternParser = Objects.requireNonNull(formatPatternParser, "formatPatternParser");
        this.context = Objects.requireNonNull(context, "context");
    }


    @Override
    public String label() {
        return this.label;
    }

    private final String label;

    @Override
    public String pattern() {
        return this.pattern.get();
    }

    private Supplier<String> pattern;

    @Override
    public String defaultFormattedValue() {
        final SpreadsheetText formatted = this.defaultFormatter.format(
                this.value,
                this.context
        ).orElse(SpreadsheetText.EMPTY);

        return formatted.text();
    }

    private final T value;

    private final SpreadsheetFormatter defaultFormatter;

    @Override
    public SpreadsheetText parsedOrFormatted() {
        SpreadsheetText formatted = SpreadsheetText.EMPTY;

        SpreadsheetFormatPattern formatPattern;
        try {
            formatPattern = this.formatPatternParser.apply(this.pattern());
        } catch (final Exception cause) {
            formatPattern = null;
        }

        if (null != formatPattern) {
            formatted = formatPattern.formatter()
                    .format(this.value, this.context)
                    .orElse(SpreadsheetText.EMPTY);
        }

        return formatted;
    }

    private final Function<String, ? extends SpreadsheetFormatPattern> formatPatternParser;

    private final SpreadsheetFormatterContext context;

    @Override
    public String toString() {
        return this.label();
    }
}
