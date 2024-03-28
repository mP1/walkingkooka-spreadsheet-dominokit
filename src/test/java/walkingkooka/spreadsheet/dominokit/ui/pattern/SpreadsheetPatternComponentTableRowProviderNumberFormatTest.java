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

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetPatternComponentTableRowProviderNumberFormatTest extends SpreadsheetPatternComponentTableRowProviderTestCase<SpreadsheetPatternComponentTableRowProviderNumberFormat> {

    private final static Color BLUE = Color.parse("#0F1");

    private final SpreadsheetPatternComponentTableRowProviderContext CONTEXT = SpreadsheetPatternComponentTableRowProviderContexts.basic(
            SpreadsheetPatternKind.NUMBER_FORMAT_PATTERN,
            new FakeSpreadsheetFormatterContext() {
                @Override
                public Locale locale() {
                    return Locale.forLanguageTag("EN-AU");
                }

                @Override
                public boolean canConvert(final Object value,
                                          final Class<?> type) {
                    return value instanceof ExpressionNumber && ExpressionNumber.class == type;
                }

                @Override
                public <T> Either<T, String> convert(final Object value,
                                                     final Class<T> target) {
                    this.canConvertOrFail(value, target);

                    return this.successfulConversion(
                            value,
                            target
                    );
                }

                @Override
                public Optional<Color> colorName(final SpreadsheetColorName name) {
                    checkEquals("BLUE", name.value());
                    return Optional.of(BLUE);
                }

                @Override
                public String currencySymbol() {
                    return "AUD";
                }

                @Override
                public char decimalSeparator() {
                    return 'D';
                }

                @Override
                public ExpressionNumberKind expressionNumberKind() {
                    return ExpressionNumberKind.BIG_DECIMAL;
                }

                @Override
                public int generalFormatNumberDigitCount() {
                    return SpreadsheetFormatterContext.DEFAULT_GENERAL_FORMAT_NUMBER_DIGIT_COUNT;
                }

                @Override
                public char groupSeparator() {
                    return 'G';
                }

                @Override
                public char negativeSign() {
                    return 'N';
                }

                @Override
                public char percentageSymbol() {
                    return 'P';
                }

                @Override
                public MathContext mathContext() {
                    return MathContext.DECIMAL32;
                }
            },
            LOGGING_CONTEXT
    );

    @Test
    public void testValidPatternText() {
        final String patternText = "$#.##";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | $#.## | AUD1234D56 | AUDN9876D54 | AUDD\n" +
                        "General | General | 1234D56 | N9876D54 | 0\n" +
                        "Number | #,##0.### | 1G234D56 | N9G876D54 | 0D\n" +
                        "Integer | #,##0 | 1G235 | N9G877 | 0\n" +
                        "Percent | #,##0% | 123G456P | N987G654P | 0P\n" +
                        "Currency | $#,##0.00 | AUD1G234D56 | AUDN9G876D54 | AUD0D00"
        );
    }

    @Test
    public void testValidPatternTextWithColor() {
        final String patternText = "[BLUE] #.000";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | [BLUE] #.000 |  1234D560 |  N9876D540 |  D000\n" +
                        "General | General | 1234D56 | N9876D54 | 0\n" +
                        "Number | #,##0.### | 1G234D56 | N9G876D54 | 0D\n" +
                        "Integer | #,##0 | 1G235 | N9G877 | 0\n" +
                        "Percent | #,##0% | 123G456P | N987G654P | 0P\n" +
                        "Currency | $#,##0.00 | AUD1G234D56 | AUDN9G876D54 | AUD0D00"
        );
    }

    @Test
    public void testInvalidPatternText() {
        this.applyAndCheck2(
                "\"Unclosed",
                CONTEXT,
                "Edit Pattern | |  |  |\n" +
                        "General | General | 1234D56 | N9876D54 | 0\n" +
                        "Number | #,##0.### | 1G234D56 | N9G876D54 | 0D\n" +
                        "Integer | #,##0 | 1G235 | N9G877 | 0\n" +
                        "Percent | #,##0% | 123G456P | N987G654P | 0P\n" +
                        "Currency | $#,##0.00 | AUD1G234D56 | AUDN9G876D54 | AUD0D00"
        );
    }

    @Override
    SpreadsheetPatternComponentTableRowProviderNumberFormat createProvider() {
        return SpreadsheetPatternComponentTableRowProviderNumberFormat.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternComponentTableRowProviderNumberFormat> type() {
        return SpreadsheetPatternComponentTableRowProviderNumberFormat.class;
    }
}
