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

package walkingkooka.spreadsheet.dominokit.number;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;
import java.util.Optional;

public final class NumberComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ExpressionNumber, NumberComponent> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.BIG_DECIMAL;

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] id=TestId123-TextBox\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testOptionalClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testClearValueOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringEmpty() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] id=TestId123-TextBox\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testOptionalSetStringEmpty() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .setStringValue(
                    Optional.of("")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringValueZero() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("0")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [0] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringValueInteger() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("123")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [123] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringValueIntegerManyDigits() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("12345678901234567890")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [12345678901234567890] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringValueDecimal() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("45.75")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [45.75] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringValueScientific() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("8E+2")
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [8E+2] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "abcdef"
                    )
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [abcdef] id=TestId123-TextBox\n" +
                "      Errors\n" +
                "        Invalid character 'a' at 0\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid2() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "123XYZ"
                    )
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [123XYZ] id=TestId123-TextBox\n" +
                "      Errors\n" +
                "        Invalid character 'X' at 3\n"
        );
    }

    @Test
    public void testSetValueWithDecimal() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        EXPRESSION_NUMBER_KIND.create(123.5)
                    )
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [123.5] id=TestId123-TextBox\n"
        );
    }

    @Test
    public void testSetValueWithInteger() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        EXPRESSION_NUMBER_KIND.create(456)
                    )
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [456] id=TestId123-TextBox\n"
        );
    }


    @Test
    public void testSetValueWithZero() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        EXPRESSION_NUMBER_KIND.zero()
                    )
                ),
            "NumberComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [0] id=TestId123-TextBox\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public NumberComponent createComponent() {
        return NumberComponent.empty(
            "TestId123" + SpreadsheetElementIds.TEXT_BOX,
            new TestNumberComponentContext()
        );
    }

    final static class TestNumberComponentContext implements NumberComponentContext,
        DecimalNumberContextDelegator {

        // DecimalNumberContextDelegator................................................................................

        @Override
        public DecimalNumberContext decimalNumberContext() {
            return DecimalNumberContexts.american(MathContext.DECIMAL32);
        }

        @Override
        public ExpressionNumberKind expressionNumberKind() {
            return ExpressionNumberKind.BIG_DECIMAL;
        }

        @Override
        public int generalFormatNumberDigitCount() {
            return SpreadsheetFormatterContext.DEFAULT_GENERAL_FORMAT_NUMBER_DIGIT_COUNT;
        }
    }

    // class............................................................................................................

    @Override
    public Class<NumberComponent> type() {
        return NumberComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
