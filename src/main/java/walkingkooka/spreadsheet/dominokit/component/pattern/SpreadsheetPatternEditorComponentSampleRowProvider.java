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

package walkingkooka.spreadsheet.dominokit.component.pattern;

import walkingkooka.NeverError;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A provider that uses the user's {@link java.util.Locale} and active {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} to prepare rows
 * for the sample.
 */
abstract class SpreadsheetPatternEditorComponentSampleRowProvider implements BiFunction<String, SpreadsheetPatternEditorComponentSampleRowProviderContext, List<SpreadsheetPatternEditorComponentSampleRow>> {

    /**
     * Selects the correct {@link SpreadsheetPatternEditorComponentSampleRowProvider} using the given {@link SpreadsheetPatternKind}.
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider spreadsheetPatternKind(final SpreadsheetPatternKind kind) {
        Objects.requireNonNull(kind, "kind");

        final SpreadsheetPatternEditorComponentSampleRowProvider provider;

        switch (kind) {
            case DATE_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.dateFormat();
                break;
            case DATE_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.dateParse();
                break;
            case DATE_TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.dateTimeFormat();
                break;
            case DATE_TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.dateTimeParse();
                break;
            case NUMBER_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.numberFormat();
                break;
            case NUMBER_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.numberParse();
                break;
            case TEXT_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.textFormat();
                break;
            case TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.timeFormat();
                break;
            case TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorComponentSampleRowProvider.timeParse();
                break;
            default:
                provider = NeverError.unhandledEnum(
                        kind,
                        SpreadsheetPatternKind.values()
                );
        }

        return provider;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderDateFormat}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider dateFormat() {
        return SpreadsheetPatternEditorComponentSampleRowProviderDateFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderDateParse}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider dateParse() {
        return SpreadsheetPatternEditorComponentSampleRowProviderDateParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderDateTimeFormat}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider dateTimeFormat() {
        return SpreadsheetPatternEditorComponentSampleRowProviderDateTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderDateTimeParse}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider dateTimeParse() {
        return SpreadsheetPatternEditorComponentSampleRowProviderDateTimeParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider numberFormat() {
        return SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderNumberParse}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider numberParse() {
        return SpreadsheetPatternEditorComponentSampleRowProviderNumberParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderTextFormat}
     */
    static SpreadsheetPatternEditorComponentSampleRowProviderTextFormat textFormat() {
        return SpreadsheetPatternEditorComponentSampleRowProviderTextFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderTimeFormat}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider timeFormat() {
        return SpreadsheetPatternEditorComponentSampleRowProviderTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorComponentSampleRowProviderTimeParse}
     */
    static SpreadsheetPatternEditorComponentSampleRowProvider timeParse() {
        return SpreadsheetPatternEditorComponentSampleRowProviderTimeParse.INSTANCE;
    }

    /**
     * Helper that provides a {@link SpreadsheetPattern} using the given {@link String patternText}.
     */
    static Optional<SpreadsheetPattern> tryParsePatternText(final String patternText,
                                                            final Function<String, SpreadsheetPattern> parser) {
        Objects.requireNonNull(patternText, "patternText");

        SpreadsheetPattern spreadsheetPattern = null;

        try {
            spreadsheetPattern = parser.apply(patternText);
        } catch (final Exception fail) {
            // ignore
        }

        return Optional.ofNullable(spreadsheetPattern);
    }

    /**
     * The label used for the row displaying the pattern text box.
     */
    final static String LABEL = "Edit Pattern";

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetPatternEditorComponentSampleRowProvider() {
        super();
    }

    final SpreadsheetPatternEditorComponentSampleRow row(final String label,
                                                         final SpreadsheetPattern pattern,
                                                         final Object value,
                                                         final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                value,
                context
        );
    }

    final SpreadsheetPatternEditorComponentSampleRow row(final String label,
                                                         final Optional<SpreadsheetPattern> pattern,
                                                         final Object value,
                                                         final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        final String patternText = pattern.map(SpreadsheetPattern::text)
                .orElse("");

        final SpreadsheetText defaultFormatted = context.defaultFormat(value);

        final SpreadsheetText formatted = context.format(
                pattern.map(SpreadsheetPattern::formatter)
                        .orElse(SpreadsheetFormatters.emptyText()),
                value
        );

        context.debug(this.getClass().getSimpleName() + " " + label + " " + CharSequences.quoteAndEscape(patternText) + " " + defaultFormatted + " " + formatted);

        return SpreadsheetPatternEditorComponentSampleRow.with(
                label,
                pattern,
                defaultFormatted.toTextNode(),
                formatted.toTextNode()
        );
    }
}
