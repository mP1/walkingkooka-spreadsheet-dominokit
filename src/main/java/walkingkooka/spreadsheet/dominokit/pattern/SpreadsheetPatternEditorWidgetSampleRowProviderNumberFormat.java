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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link SpreadsheetPatternEditorWidgetSampleRowProvider} for {@link SpreadsheetNumberFormatPattern}.
 */
final class SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat extends SpreadsheetPatternEditorWidgetSampleRowProvider {

    /**
     * Singleton
     */
    final static SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat INSTANCE = new SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat();

    private SpreadsheetPatternEditorWidgetSampleRowProviderNumberFormat() {
        super();
    }

    @Override
    public List<SpreadsheetPatternEditorWidgetSampleRow> apply(final String patternText,
                                                               final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final List<SpreadsheetPatternEditorWidgetSampleRow> rows = Lists.array();

        rows.addAll(
                row(
                        "Pattern",
                        SpreadsheetPatternEditorWidgetSampleRow.tryParsePatternText(patternText, SpreadsheetPattern::parseNumberFormatPattern),
                        context
                )
        );

        rows.addAll(
                row(
                        "Number",
                        DecimalFormat::getInstance,
                        context
                )
        );
        rows.addAll(
                row(
                        "Integer",
                        DecimalFormat::getIntegerInstance,
                        context
                )
        );
        rows.addAll(
                row(
                        "Percent",
                        DecimalFormat::getPercentInstance,
                        context
                )
        );
        rows.addAll(
                row(
                        "Currency",
                        DecimalFormat::getCurrencyInstance,
                        context
                )
        );

        return Lists.readOnly(rows);
    }

    private List<SpreadsheetPatternEditorWidgetSampleRow> row(final String label,
                                                              final Function<Locale, NumberFormat> decimalFormat,
                                                              final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return row(
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

    private List<SpreadsheetPatternEditorWidgetSampleRow> row(final String label,
                                                              final Optional<SpreadsheetFormatPattern> pattern,
                                                              final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final ExpressionNumberKind kind = context.spreadsheetFormatterContext()
                .expressionNumberKind();

        return Lists.of(
                this.row(
                        "Positive " + label,
                        pattern,
                        positive(kind),
                        context
                ),
                this.row(
                        "Negative " + label,
                        pattern,
                        negative(kind),
                        context
                ),
                this.row(
                        "Zero " + label,
                        pattern,
                        zero(kind),
                        context
                )
        );
    }

    private List<SpreadsheetPatternEditorWidgetSampleRow> row(final String label,
                                                              final SpreadsheetFormatPattern pattern,
                                                              final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        final ExpressionNumberKind kind = context.spreadsheetFormatterContext()
                .expressionNumberKind();

        return Lists.of(
                this.row(
                        "Positive " + label,
                        pattern,
                        positive(kind),
                        context
                ),
                this.row(
                        "Negative " + label,
                        pattern,
                        negative(kind),
                        context
                ),
                this.row(
                        "Zero " + label,
                        pattern,
                        zero(kind),
                        context
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

//    private SpreadsheetPatternEditorWidgetSampleRow decimalFormat(
//            final String label,
//            final ExpressionNumber number,
//            final Function<Locale, NumberFormat> decimalFormat,
//            final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
//        final SpreadsheetParsePattern pattern = SpreadsheetPattern.decimalFormat(
//                (DecimalFormat) decimalFormat.apply(
//                        context.spreadsheetFormatterContext()
//                                .locale()
//                )
//        );
//
//        return row(
//                label,
//                pattern.toFormat(),
//                number,
//                context
//        );
//    }

    private SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                        final SpreadsheetFormatPattern pattern,
                                                        final ExpressionNumber number,
                                                        final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return this.row(
                label,
                Optional.of(pattern),
                number,
                context
        );
    }

    private SpreadsheetPatternEditorWidgetSampleRow row(final String label,
                                                        final Optional<SpreadsheetFormatPattern> pattern,
                                                        final ExpressionNumber number,
                                                        final SpreadsheetPatternEditorWidgetSampleRowProviderContext context) {
        return SpreadsheetPatternEditorWidgetSampleRow.with(
                label,
                pattern.map(SpreadsheetPattern::text)
                        .orElse(""),
                context.defaultFormat(number),
                context.format(
                        pattern.map(SpreadsheetFormatPattern::formatter)
                                .orElse(SpreadsheetFormatters.emptyText()),
                        number
                )
        );
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        return SpreadsheetPatternKind.NUMBER_FORMAT_PATTERN.toString();
    }
}
