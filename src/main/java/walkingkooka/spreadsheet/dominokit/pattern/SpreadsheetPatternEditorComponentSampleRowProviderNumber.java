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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
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

abstract class SpreadsheetPatternEditorComponentSampleRowProviderNumber extends SpreadsheetPatternEditorComponentSampleRowProvider {

    SpreadsheetPatternEditorComponentSampleRowProviderNumber() {
        super();
    }

    @Override
    public final List<SpreadsheetPatternEditorComponentSampleRow> apply(final String patternText,
                                                                        final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
        final List<SpreadsheetPatternEditorComponentSampleRow> rows = Lists.array();

        rows.addAll(
                row(
                        LABEL,
                        tryParsePatternText(
                                patternText,
                                this.kind()::parse
                        ),
                        context
                )
        );

        if (this instanceof SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat) {
            rows.addAll(
                    row(
                            "General",
                            GENERAL,
                            context
                    )
            );
        }

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

    private final static SpreadsheetFormatPattern GENERAL = SpreadsheetPattern.parseNumberFormatPattern("General");

    private List<SpreadsheetPatternEditorComponentSampleRow> row(final String label,
                                                                 final Function<Locale, NumberFormat> decimalFormat,
                                                                 final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
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

    private List<SpreadsheetPatternEditorComponentSampleRow> row(final String label,
                                                                 final Optional<SpreadsheetPattern> pattern,
                                                                 final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
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

    private List<SpreadsheetPatternEditorComponentSampleRow> row(final String label,
                                                                 final SpreadsheetFormatPattern pattern,
                                                                 final SpreadsheetPatternEditorComponentSampleRowProviderContext context) {
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

    // toString.........................................................................................................

    @Override
    public final String toString() {
        return this.kind().toString();
    }

    abstract SpreadsheetPatternKind kind();
}
