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
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextNode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

abstract class SpreadsheetPatternComponentTableComponentRowProviderNumber extends SpreadsheetPatternComponentTableComponentRowProvider {

    SpreadsheetPatternComponentTableComponentRowProviderNumber() {
        super();
    }

    @Override
    public final List<SpreadsheetPatternComponentTableComponentRow> apply(final String patternText,
                                                                          final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        final List<SpreadsheetPatternComponentTableComponentRow> rows = Lists.array();

        rows.add(
                numberRow(
                        LABEL,
                        tryParsePatternText(
                                patternText,
                                this.kind()::parse
                        ),
                        context
                )
        );

        if (this instanceof SpreadsheetPatternComponentTableComponentRowProviderNumberFormat) {
            rows.add(
                    numberRow(
                            "General",
                            GENERAL,
                            context
                    )
            );
        }

        rows.add(
                numberRow(
                        "Number",
                        DecimalFormat::getInstance,
                        context
                )
        );
        rows.add(
                numberRow(
                        "Integer",
                        DecimalFormat::getIntegerInstance,
                        context
                )
        );
        rows.add(
                numberRow(
                        "Percent",
                        DecimalFormat::getPercentInstance,
                        context
                )
        );
        rows.add(
                numberRow(
                        "Currency",
                        DecimalFormat::getCurrencyInstance,
                        context
                )
        );

        return Lists.readOnly(rows);
    }

    private final static SpreadsheetFormatPattern GENERAL = SpreadsheetPattern.parseNumberFormatPattern("General");

    private SpreadsheetPatternComponentTableComponentRow numberRow(final String label,
                                                                   final Function<Locale, NumberFormat> decimalFormat,
                                                                   final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        return numberRow(
                label,
                SpreadsheetPattern.decimalFormat(
                        (DecimalFormat) decimalFormat.apply(
                                context.spreadsheetFormatterContext()
                                        .locale()
                        )
                ).toFormat(),
                context
        );
    }

    private SpreadsheetPatternComponentTableComponentRow numberRow(final String label,
                                                                   final SpreadsheetPattern pattern,
                                                                   final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        return this.numberRow(
                label,
                Optional.of(pattern),
                context
        );
    }

    private SpreadsheetPatternComponentTableComponentRow numberRow(final String label,
                                                                   final Optional<SpreadsheetPattern> pattern,
                                                                   final SpreadsheetPatternComponentTableComponentRowProviderContext context) {
        final String patternText = pattern.map(SpreadsheetPattern::text)
                .orElse("");

        final ExpressionNumberKind kind = context.spreadsheetFormatterContext().expressionNumberKind();

        final ExpressionNumber positive = positive(kind);
        final ExpressionNumber negative = negative(kind);
        final ExpressionNumber zero = zero(kind);

        final SpreadsheetText formattedPositive = context.format(
                pattern.map(SpreadsheetPattern::formatter)
                        .orElse(SpreadsheetFormatters.emptyText()),
                positive
        );

        final SpreadsheetText formattedNegative = context.format(
                pattern.map(SpreadsheetPattern::formatter)
                        .orElse(SpreadsheetFormatters.emptyText()),
                negative
        );

        final SpreadsheetText formattedZero = context.format(
                pattern.map(SpreadsheetPattern::formatter)
                        .orElse(SpreadsheetFormatters.emptyText()),
                zero
        );

        context.debug(
                this.getClass().getSimpleName() +
                        " " +
                        label +
                        " " +
                        CharSequences.quoteAndEscape(patternText) +
                        " " +
                        formattedPositive +
                        " " +
                        formattedNegative +
                        " " +
                        formattedZero
        );

        return SpreadsheetPatternComponentTableComponentRow.with(
                label,
                pattern,
                Lists.of(
                        textNode(
                                formattedPositive
                        ),
                        textNode(
                                formattedNegative
                        ),
                        textNode(
                                formattedZero
                        )
                )
        );
    }

    /**
     * Creates three lines, with each line holding a single formatted number.
     */
    private static TextNode textNode(final SpreadsheetText text) {
        return TextNode.style(
                Lists.of(
                        text.toTextNode()
                )
        );
    }

    private ExpressionNumber positive(final ExpressionNumberKind kind) {
        return kind.create(1234.56);
    }

    private ExpressionNumber negative(final ExpressionNumberKind kind) {
        return kind.create(-9876.54);
    }

    private ExpressionNumber zero(final ExpressionNumberKind kind) {
        return kind.zero();
    }

    // toString.........................................................................................................

    @Override
    public final String toString() {
        return this.kind().toString();
    }

    abstract SpreadsheetPatternKind kind();
}
