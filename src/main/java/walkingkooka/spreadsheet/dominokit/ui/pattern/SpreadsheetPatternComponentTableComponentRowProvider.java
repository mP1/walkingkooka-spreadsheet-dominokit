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
abstract class SpreadsheetPatternComponentTableComponentRowProvider implements BiFunction<String, SpreadsheetPatternComponentTableComponentRowProviderContext, List<SpreadsheetPatternComponentTableComponentRow>> {

    /**
     * Selects the correct {@link SpreadsheetPatternComponentTableComponentRowProvider} using the given {@link SpreadsheetPatternKind}.
     */
    static SpreadsheetPatternComponentTableComponentRowProvider spreadsheetPatternKind(final SpreadsheetPatternKind kind) {
        Objects.requireNonNull(kind, "kind");

        final SpreadsheetPatternComponentTableComponentRowProvider provider;

        switch (kind) {
            case DATE_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.dateFormat();
                break;
            case DATE_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.dateParse();
                break;
            case DATE_TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.dateTimeFormat();
                break;
            case DATE_TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.dateTimeParse();
                break;
            case NUMBER_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.numberFormat();
                break;
            case NUMBER_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.numberParse();
                break;
            case TEXT_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.textFormat();
                break;
            case TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.timeFormat();
                break;
            case TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableComponentRowProvider.timeParse();
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
     * {@see SpreadsheetPatternComponentTableComponentRowProviderDateFormat}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider dateFormat() {
        return SpreadsheetPatternComponentTableComponentRowProviderDateFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderDateParse}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider dateParse() {
        return SpreadsheetPatternComponentTableComponentRowProviderDateParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderDateTimeFormat}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider dateTimeFormat() {
        return SpreadsheetPatternComponentTableComponentRowProviderDateTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderDateTimeParse}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider dateTimeParse() {
        return SpreadsheetPatternComponentTableComponentRowProviderDateTimeParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderNumberFormat}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider numberFormat() {
        return SpreadsheetPatternComponentTableComponentRowProviderNumberFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderNumberParse}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider numberParse() {
        return SpreadsheetPatternComponentTableComponentRowProviderNumberParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderTextFormat}
     */
    static SpreadsheetPatternComponentTableComponentRowProviderTextFormat textFormat() {
        return SpreadsheetPatternComponentTableComponentRowProviderTextFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderTimeFormat}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider timeFormat() {
        return SpreadsheetPatternComponentTableComponentRowProviderTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableComponentRowProviderTimeParse}
     */
    static SpreadsheetPatternComponentTableComponentRowProvider timeParse() {
        return SpreadsheetPatternComponentTableComponentRowProviderTimeParse.INSTANCE;
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
    SpreadsheetPatternComponentTableComponentRowProvider() {
        super();
    }

    final SpreadsheetPatternComponentTableComponentRow row(final String label,
                                                           final SpreadsheetPattern pattern,
                                                           final Object value,
                                                           final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                value,
                context
        );
    }

    final SpreadsheetPatternComponentTableComponentRow row(final String label,
                                                           final Optional<SpreadsheetPattern> pattern,
                                                           final Object value,
                                                           final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        final String patternText = pattern.map(SpreadsheetPattern::text)
                .orElse("");

        final SpreadsheetText formatted = context.format(
                pattern.map(SpreadsheetPattern::formatter)
                        .orElse(SpreadsheetFormatters.emptyText()),
                value
        );

        context.debug(this.getClass().getSimpleName() + " " + label + " " + CharSequences.quoteAndEscape(patternText) + " " + formatted);

        return SpreadsheetPatternComponentTableComponentRow.with(
                label,
                pattern,
                formatted.toTextNode()
        );
    }
}
