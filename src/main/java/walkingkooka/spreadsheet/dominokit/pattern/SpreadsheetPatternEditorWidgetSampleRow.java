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
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a row of data for the sample table that appears within a {@link SpreadsheetPatternEditorWidget}.
 */
final class SpreadsheetPatternEditorWidgetSampleRow {

    /**
     * Helper that provides a {@link SpreadsheetFormatPattern} if the {@link Supplier patternText} is present and can be parsed.
     */
    static Supplier<Optional<? extends SpreadsheetFormatPattern>> formatPatternSupplier(final Supplier<Optional<String>> patternText,
                                                                                        final Function<String, ? extends SpreadsheetFormatPattern> parser) {
        Objects.requireNonNull(patternText, "patternText");

        return () -> {
            SpreadsheetFormatPattern spreadsheetFormatPattern = null;

            final Optional<String> maybePatternText = patternText.get();
            if (maybePatternText.isPresent()) {
                try {
                    spreadsheetFormatPattern = parser.apply(maybePatternText.get());
                } catch (final Exception fail) {
                    // ignore
                }
            }

            return Optional.ofNullable(spreadsheetFormatPattern);
        };
    }

    /**
     * Factory that creates a new {@link SpreadsheetPatternEditorWidgetSampleRow}.
     */
    static SpreadsheetPatternEditorWidgetSampleRow with(final String label,
                                                        final Supplier<Optional<String>> patternText,
                                                        final Object value,
                                                        final SpreadsheetFormatter defaultFormatter,
                                                        final Supplier<Optional<? extends SpreadsheetFormatPattern>> formatPattern,
                                                        final SpreadsheetFormatterContext context) {
        return new SpreadsheetPatternEditorWidgetSampleRow(
                CharSequences.failIfNullOrEmpty(label, "label"),
                Objects.requireNonNull(patternText, "patternText"),
                Objects.requireNonNull(value, "value"),
                Objects.requireNonNull(defaultFormatter, "defaultFormatter"),
                Objects.requireNonNull(formatPattern, "formatPattern"),
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow(final String label,
                                                    final Supplier<Optional<String>> patternText,
                                                    final Object value,
                                                    final SpreadsheetFormatter defaultFormatter,
                                                    final Supplier<Optional<? extends SpreadsheetFormatPattern>> formatPattern,
                                                    final SpreadsheetFormatterContext context) {
        this.label = label;
        this.patternText = patternText;
        this.value = value;
        this.defaultFormatter = defaultFormatter;
        this.formatPattern = formatPattern;
        this.context = context;
    }

    /**
     * The label that appears in the first column.
     */
    String label() {
        return this.label;
    }

    private final String label;

    /**
     * The pattern text that appears in the 2nd column.
     */
    String patternText() {
        return this.patternText.get()
                .orElse("");
    }

    private Supplier<Optional<String>> patternText;

    /**
     * The value default formatted.
     */
    String defaultFormattedValue() {
        final SpreadsheetText formatted = this.defaultFormatter.format(
                this.value,
                this.context
        ).orElse(SpreadsheetText.EMPTY);

        return formatted.text();
    }

    private final Object value;

    private final SpreadsheetFormatter defaultFormatter;

    /**
     * The value formatted using the {@link #patternText()}.
     */
    SpreadsheetText patternFormattedValue() {
        SpreadsheetText formatted = SpreadsheetText.EMPTY;

        final Optional<? extends SpreadsheetFormatPattern> formatPattern = this.formatPattern.get();

        if (formatPattern.isPresent()) {
            formatted = formatPattern.get()
                    .formatter()
                    .format(this.value, this.context)
                    .orElse(SpreadsheetText.EMPTY);
        }

        return formatted;
    }

    private final Supplier<Optional<? extends SpreadsheetFormatPattern>> formatPattern;

    private final SpreadsheetFormatterContext context;

    @Override
    public String toString() {
        return this.label();
    }
}
