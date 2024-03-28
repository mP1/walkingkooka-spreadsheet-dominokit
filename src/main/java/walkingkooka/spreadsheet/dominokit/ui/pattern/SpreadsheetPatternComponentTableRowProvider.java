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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A provider that uses the user's {@link java.util.Locale} and active {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} to prepare rows
 * for the sample.
 */
abstract class SpreadsheetPatternComponentTableRowProvider implements BiFunction<String, SpreadsheetPatternComponentTableRowProviderContext, List<SpreadsheetPatternComponentTableRow>> {

    /**
     * Selects the correct {@link SpreadsheetPatternComponentTableRowProvider} using the given {@link SpreadsheetPatternKind}.
     */
    static SpreadsheetPatternComponentTableRowProvider spreadsheetPatternKind(final SpreadsheetPatternKind kind) {
        Objects.requireNonNull(kind, "kind");

        final SpreadsheetPatternComponentTableRowProvider provider;

        switch (kind) {
            case DATE_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.dateFormat();
                break;
            case DATE_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.dateParse();
                break;
            case DATE_TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.dateTimeFormat();
                break;
            case DATE_TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.dateTimeParse();
                break;
            case NUMBER_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.numberFormat();
                break;
            case NUMBER_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.numberParse();
                break;
            case TEXT_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.textFormat();
                break;
            case TIME_FORMAT_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.timeFormat();
                break;
            case TIME_PARSE_PATTERN:
                provider = SpreadsheetPatternComponentTableRowProvider.timeParse();
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
     * {@see SpreadsheetPatternComponentTableRowProviderDateFormat}
     */
    static SpreadsheetPatternComponentTableRowProvider dateFormat() {
        return SpreadsheetPatternComponentTableRowProviderDateFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderDateParse}
     */
    static SpreadsheetPatternComponentTableRowProvider dateParse() {
        return SpreadsheetPatternComponentTableRowProviderDateParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderDateTimeFormat}
     */
    static SpreadsheetPatternComponentTableRowProvider dateTimeFormat() {
        return SpreadsheetPatternComponentTableRowProviderDateTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderDateTimeParse}
     */
    static SpreadsheetPatternComponentTableRowProvider dateTimeParse() {
        return SpreadsheetPatternComponentTableRowProviderDateTimeParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderNumberFormat}
     */
    static SpreadsheetPatternComponentTableRowProvider numberFormat() {
        return SpreadsheetPatternComponentTableRowProviderNumberFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderNumberParse}
     */
    static SpreadsheetPatternComponentTableRowProvider numberParse() {
        return SpreadsheetPatternComponentTableRowProviderNumberParse.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderTextFormat}
     */
    static SpreadsheetPatternComponentTableRowProviderTextFormat textFormat() {
        return SpreadsheetPatternComponentTableRowProviderTextFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderTimeFormat}
     */
    static SpreadsheetPatternComponentTableRowProvider timeFormat() {
        return SpreadsheetPatternComponentTableRowProviderTimeFormat.INSTANCE;
    }

    /**
     * {@see SpreadsheetPatternComponentTableRowProviderTimeParse}
     */
    static SpreadsheetPatternComponentTableRowProvider timeParse() {
        return SpreadsheetPatternComponentTableRowProviderTimeParse.INSTANCE;
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
    SpreadsheetPatternComponentTableRowProvider() {
        super();
    }

    final SpreadsheetPatternComponentTableRow row(final String label,
                                                  final SpreadsheetPattern pattern,
                                                  final Object value,
                                                  final SpreadsheetPatternComponentTableRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                value,
                context
        );
    }

    final SpreadsheetPatternComponentTableRow row(final String label,
                                                  final Optional<SpreadsheetPattern> pattern,
                                                  final Object value,
                                                  final SpreadsheetPatternComponentTableRowProviderContext context) {
        return this.row(
                label,
                pattern,
                Lists.of(value),
                context
        );
    }

    final SpreadsheetPatternComponentTableRow row(final String label,
                                                  final Optional<SpreadsheetPattern> pattern,
                                                  final List<Object> values,
                                                  final SpreadsheetPatternComponentTableRowProviderContext context) {
        final String patternText = pattern.map(SpreadsheetPattern::text)
                .orElse("");

        final List<TextNode> formatted =
                values.stream()
                        .map(v ->
                                context.format(
                                        pattern.map(SpreadsheetPattern::formatter)
                                                .orElse(SpreadsheetFormatters.emptyText()),
                                        v
                                ).toTextNode()
                        ).collect(Collectors.toList());

        context.debug(this.getClass().getSimpleName() + " " + label + " " + CharSequences.quoteAndEscape(patternText) + " " + formatted);

        return SpreadsheetPatternComponentTableRow.with(
                label,
                pattern,
                formatted
        );
    }
}
