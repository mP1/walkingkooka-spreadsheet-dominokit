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

import walkingkooka.NeverError;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A provider that uses the user's {@link java.util.Locale} and active {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} to prepare rows
 * for the sample.
 */
abstract class SpreadsheetPatternEditorWidgetSampleRowProvider implements BiFunction<String, SpreadsheetPatternEditorWidgetSampleRowProviderContext, List<SpreadsheetPatternEditorWidgetSampleRow>> {

    /**
     * Selects the correct {@link SpreadsheetPatternEditorWidgetSampleRowProvider} using the given {@link SpreadsheetPatternKind}.
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider spreadsheetPatternKind(final SpreadsheetPatternKind kind) {
        Objects.requireNonNull(kind, "kind");

        final SpreadsheetPatternEditorWidgetSampleRowProvider provider;

        switch (kind) {
            case DATE_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.dateFormat();
                break;
            case DATE_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.dateParse();
                break;
            case DATE_TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.dateTimeFormat();
                break;
            case DATE_TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.dateTimeParse();
                break;
            case NUMBER_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.numberFormat();
                break;
            case NUMBER_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.numberParse();
                break;
            case TEXT_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.textFormat();
                break;
            case TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.timeFormat();
                break;
            case TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternEditorWidgetSampleRowProvider.timeParse();
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
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateTimeFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider dateTimeParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderDateTimeParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider numberFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderNumberParse}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider numberParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderNumberParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat textFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTextFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider timeFormat() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternEditorWidgetSampleRowProviderTimeParse}
     */
    static SpreadsheetPatternEditorWidgetSampleRowProvider timeParse() {
        return SpreadsheetPatternEditorWidgetSampleRowProviderTimeParse.INSTANCE;
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
    SpreadsheetPatternEditorWidgetSampleRowProvider() {
        super();
    }

    final SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                      final SpreadsheetPattern pattern,
                                                      final Object value,
                                                      final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                value,
                context
        );
    }

    final SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                      final Optional<SpreadsheetPattern> pattern,
                                                      final Object value,
                                                      final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return SpreadsheetPatternEditorWidgetSampleRow.with(
                label,
                pattern.map(SpreadsheetPattern::text)
                        .orElse(""),
                context.defaultFormat(value),
                context.format(
                        pattern.map(SpreadsheetPattern::formatter)
                                .orElse(SpreadsheetFormatters.emptyText()),
                        value
                )
        );
    }
}
