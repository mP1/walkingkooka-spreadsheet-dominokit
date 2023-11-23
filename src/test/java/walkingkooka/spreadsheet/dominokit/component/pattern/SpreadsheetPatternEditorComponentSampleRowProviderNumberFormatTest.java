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

public final class SpreadsheetPatternEditorComponentSampleRowProviderNumberFormatTest extends SpreadsheetPatternEditorComponentSampleRowProviderTestCase<SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat> {

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
            },
            LOGGING_CONTEXT
    );

    @Test
    public void testValidPatternText() {
        final String patternText = "$#.##";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | $#.## | 1G234D56\\r\\nN9G876D54\\r\\n0D | AUD1234D56\\r\\nAUDN9876D54\\r\\nAUDD\n" +
                        "General | General | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1234D6\\r\\nN9876D5\\r\\n0\n" +
                        "Number | #,##0.### | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1G234D56\\r\\nN9G876D54\\r\\n0D\n" +
                        "Integer | #,##0 | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1G235\\r\\nN9G877\\r\\n0\n" +
                        "Percent | #,##0% | 1G234D56\\r\\nN9G876D54\\r\\n0D | 123G456P\\r\\nN987G654P\\r\\n0P\n" +
                        "Currency | $#,##0.00 | 1G234D56\\r\\nN9G876D54\\r\\n0D | AUD1G234D56\\r\\nAUDN9G876D54\\r\\nAUD0D00"
        );
    }

    @Test
    public void testValidPatternTextWithColor() {
        final String patternText = "[BLUE] #.000";

        this.applyAndCheck2(
                patternText,
                CONTEXT,
                "Edit Pattern | [BLUE] #.000 | 1G234D56\\r\\nN9G876D54\\r\\n0D |  1234D560\\r\\n N9876D540\\r\\n D000\n" +
                        "General | General | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1234D6\\r\\nN9876D5\\r\\n0\n" +
                        "Number | #,##0.### | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1G234D56\\r\\nN9G876D54\\r\\n0D\n" +
                        "Integer | #,##0 | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1G235\\r\\nN9G877\\r\\n0\n" +
                        "Percent | #,##0% | 1G234D56\\r\\nN9G876D54\\r\\n0D | 123G456P\\r\\nN987G654P\\r\\n0P\n" +
                        "Currency | $#,##0.00 | 1G234D56\\r\\nN9G876D54\\r\\n0D | AUD1G234D56\\r\\nAUDN9G876D54\\r\\nAUD0D00"
        );
    }

    @Test
    public void testInvalidPatternText() {
        this.applyAndCheck2(
                "\"Unclosed",
                CONTEXT,
                "Edit Pattern | | 1G234D56\\r\\nN9G876D54\\r\\n0D |\n" +
                        "General | General | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1234D6\\r\\nN9876D5\\r\\n0\n" +
                        "Number | #,##0.### | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1G234D56\\r\\nN9G876D54\\r\\n0D\n" +
                        "Integer | #,##0 | 1G234D56\\r\\nN9G876D54\\r\\n0D | 1G235\\r\\nN9G877\\r\\n0\n" +
                        "Percent | #,##0% | 1G234D56\\r\\nN9G876D54\\r\\n0D | 123G456P\\r\\nN987G654P\\r\\n0P\n" +
                        "Currency | $#,##0.00 | 1G234D56\\r\\nN9G876D54\\r\\n0D | AUD1G234D56\\r\\nAUDN9G876D54\\r\\nAUD0D00"
        );
    }

    @Override
    SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat createProvider() {
        return SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat.INSTANCE;
    }

    @Override
    public Class<SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat> type() {
        return SpreadsheetPatternEditorComponentSampleRowProviderNumberFormat.class;
    }
}
