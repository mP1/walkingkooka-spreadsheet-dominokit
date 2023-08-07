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

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.format.FakeSpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetPatternEditorComponentSampleRowProviderNumberParseTest extends SpreadsheetPatternEditorComponentSampleRowProviderTestCase<SpreadsheetPatternEditorComponentSampleRowProviderNumberParse> {

    private final static Color BLUE = Color.parse("#0F1");

    private final SpreadsheetPatternEditorComponentSampleRowProviderContext CONTEXT = SpreadsheetPatternEditorComponentSampleRowProviderContexts.basic(
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
            }
    );

    @Test
    public void testValidPatternText() {
        final String patternText = "$#.##";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Positive Edit Pattern | $#.## | 1G234D56 | AUD1234D56\n" +
                        "Negative Edit Pattern | $#.## | N9G876D54 | AUDN9876D54\n" +
                        "Zero Edit Pattern | $#.## | 0D | AUDD\n" +
                        "Positive General | General | 1G234D56 | 1234D6\n" +
                        "Negative General | General | N9G876D54 | N9876D5\n" +
                        "Zero General | General | 0D | 0\n" +
                        "Positive Number | #,##0.### | 1G234D56 | 1G234D56\n" +
                        "Negative Number | #,##0.### | N9G876D54 | N9G876D54\n" +
                        "Zero Number | #,##0.### | 0D | 0D\n" +
                        "Positive Integer | #,##0 | 1G234D56 | 1G235\n" +
                        "Negative Integer | #,##0 | N9G876D54 | N9G877\n" +
                        "Zero Integer | #,##0 | 0D | 0\n" +
                        "Positive Percent | #,##0% | 1G234D56 | 123G456P\n" +
                        "Negative Percent | #,##0% | N9G876D54 | N987G654P\n" +
                        "Zero Percent | #,##0% | 0D | 0P\n" +
                        "Positive Currency | $#,##0.00 | 1G234D56 | AUD1G234D56\n" +
                        "Negative Currency | $#,##0.00 | N9G876D54 | AUDN9G876D54\n" +
                        "Zero Currency | $#,##0.00 | 0D | AUD0D00"
        );
    }

    @Test
    public void testValidPatternText2() {
        final String patternText = "[BLUE] #.000";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Positive Edit Pattern | | 1G234D56 |\n" +
                        "Negative Edit Pattern | | N9G876D54 |\n" +
                        "Zero Edit Pattern | | 0D |\n" +
                        "Positive General | General | 1G234D56 | 1234D6\n" +
                        "Negative General | General | N9G876D54 | N9876D5\n" +
                        "Zero General | General | 0D | 0\n" +
                        "Positive Number | #,##0.### | 1G234D56 | 1G234D56\n" +
                        "Negative Number | #,##0.### | N9G876D54 | N9G876D54\n" +
                        "Zero Number | #,##0.### | 0D | 0D\n" +
                        "Positive Integer | #,##0 | 1G234D56 | 1G235\n" +
                        "Negative Integer | #,##0 | N9G876D54 | N9G877\n" +
                        "Zero Integer | #,##0 | 0D | 0\n" +
                        "Positive Percent | #,##0% | 1G234D56 | 123G456P\n" +
                        "Negative Percent | #,##0% | N9G876D54 | N987G654P\n" +
                        "Zero Percent | #,##0% | 0D | 0P\n" +
                        "Positive Currency | $#,##0.00 | 1G234D56 | AUD1G234D56\n" +
                        "Negative Currency | $#,##0.00 | N9G876D54 | AUDN9G876D54\n" +
                        "Zero Currency | $#,##0.00 | 0D | AUD0D00"
        );
    }

    @Test
    public void testInvalidPatternText() {
        this.applyAndCheck2(
                "\"Unclosed",
                CONTEXT,
                "Positive Edit Pattern | | 1G234D56 |\n" +
                        "Negative Edit Pattern | | N9G876D54 |\n" +
                        "Zero Edit Pattern | | 0D |\n" +
                        "Positive General | General | 1G234D56 | 1234D6\n" +
                        "Negative General | General | N9G876D54 | N9876D5\n" +
                        "Zero General | General | 0D | 0\n" +
                        "Positive Number | #,##0.### | 1G234D56 | 1G234D56\n" +
                        "Negative Number | #,##0.### | N9G876D54 | N9G876D54\n" +
                        "Zero Number | #,##0.### | 0D | 0D\n" +
                        "Positive Integer | #,##0 | 1G234D56 | 1G235\n" +
                        "Negative Integer | #,##0 | N9G876D54 | N9G877\n" +
                        "Zero Integer | #,##0 | 0D | 0\n" +
                        "Positive Percent | #,##0% | 1G234D56 | 123G456P\n" +
                        "Negative Percent | #,##0% | N9G876D54 | N987G654P\n" +
                        "Zero Percent | #,##0% | 0D | 0P\n" +
                        "Positive Currency | $#,##0.00 | 1G234D56 | AUD1G234D56\n" +
                        "Negative Currency | $#,##0.00 | N9G876D54 | AUDN9G876D54\n" +
                        "Zero Currency | $#,##0.00 | 0D | AUD0D00"
        );
    }

    @Override
    SpreadsheetPatternEditorComponentSampleRowProviderNumberParse createProvider() {
        return SpreadsheetPatternEditorComponentSampleRowProviderNumberParse.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternEditorComponentSampleRowProviderNumberParse> type() {
        return SpreadsheetPatternEditorComponentSampleRowProviderNumberParse.class;
    }
}
