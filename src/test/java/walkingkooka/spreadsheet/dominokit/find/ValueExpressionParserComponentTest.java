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

package walkingkooka.spreadsheet.dominokit.find;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.net.Url;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.spreadsheet.store.SpreadsheetCellStores;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.Expression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueExpressionParserComponentTest implements ValueComponentTesting<HTMLFieldSetElement, Expression, ValueExpressionParserComponent>,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetParser SPREADSHEET_PARSER = SpreadsheetParsers.valueOrExpression(
            METADATA_EN_AU.spreadsheetParser(
                    SPREADSHEET_PARSER_PROVIDER,
                    PROVIDER_CONTEXT
            )
    );

    private final static SpreadsheetExpressionEvaluationContext EVALUATION_CONTEXT = SpreadsheetExpressionEvaluationContexts.basic(
            Optional.empty(), // cell
            SpreadsheetCellStores.fake(),
            Url.parseAbsolute("https://example.com"),
            (e) -> {
                throw new UnsupportedOperationException(e.toString());
            },
            METADATA_EN_AU,
            SPREADSHEET_FORMULA_CONVERTER_CONTEXT,
            EXPRESSION_FUNCTION_PROVIDER,
            PROVIDER_CONTEXT
    );

    // empty............................................................................................................

    @Test
    public void testEmptyNullParserFails() {
        assertThrows(
                NullPointerException.class,
                () -> ValueExpressionParserComponent.empty(
                        null,
                        SPREADSHEET_PARSER_CONTEXT,
                        EVALUATION_CONTEXT
                )
        );
    }

    @Test
    public void testEmptyNullSpreadsheetParserContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ValueExpressionParserComponent.empty(
                        SPREADSHEET_PARSER,
                        null,
                        EVALUATION_CONTEXT
                )
        );
    }


    @Test
    public void testEmptyNullSpreadsheetExpressionEvaluationContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ValueExpressionParserComponent.empty(
                        SPREADSHEET_PARSER,
                        SPREADSHEET_PARSER_CONTEXT,
                        null
                )
        );
    }

    // value............................................................................................................

    @Test
    public void testValueWithEmptyText() {
        this.valueAndCheck(
                ""
        );
    }

    @Test
    public void testValueWithWhitespace() {
        this.valueAndCheck(
                "  "
        );
    }

    @Test
    public void testValueWithString() {
        this.valueAndCheck(
                "'Hello",
                Expression.value("Hello")
        );
    }

    @Test
    public void testValueWithNumber() {
        this.valueAndCheck(
                "123.5",
                Expression.value(
                        EXPRESSION_NUMBER_KIND.create(123.5)
                )
        );
    }

    @Test
    public void testValueWithDate() {
        this.valueAndCheck(
                "1999/12/31",
                Expression.value(
                        LocalDate.of(1999, 12, 31)
                )
        );
    }

    @Test
    public void testValueWithDateTime() {
        this.valueAndCheck(
                "1999/12/31 12:58",
                Expression.value(
                        LocalDateTime.of(1999, 12, 31, 12, 58)
                )
        );
    }

    @Test
    public void testValueWithTime() {
        this.valueAndCheck(
                "12:58:59",
                Expression.value(
                        LocalTime.of(12, 58, 59)
                )
        );
    }

    @Test
    public void testValueWithAdditionExpression() {
        this.valueAndCheck(
                "=12.0 + 34.0",
                Expression.add(
                        Expression.value(
                                EXPRESSION_NUMBER_KIND.create(12.0)
                        ),
                        Expression.value(
                                EXPRESSION_NUMBER_KIND.create(34.0)
                        )
                )
        );
    }

    private void valueAndCheck(final String text) {
        this.valueAndCheck(
                text,
                Optional.empty()
        );
    }

    private void valueAndCheck(final String text,
                               final Expression expected) {
        this.valueAndCheck(
                text,
                Optional.of(expected)
        );
    }

    private void valueAndCheck(final String text,
                               final Optional<Expression> expected) {
        final ValueExpressionParserComponent component = valueExpressionParserComponent()
                .setStringValue(
                        Optional.of(text)
                );

        this.checkEquals(
                expected,
                component.value(),
                () -> component.treeToString(
                        Indentation.SPACES2,
                        LineEnding.NL
                )
        );
    }

    // TreePrint........................................................................................................

    @Test
    public void testTreePrintWithEmptyText() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent(),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      []\n" +
                        "      Errors\n" +
                        "        text is empty\n"
        );
    }

    @Test
    public void testTreePrintWithAdditionExpression() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=1+2"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=1+2]\n"
        );
    }

    @Test
    public void testTreePrintWithString() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "'Hello"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      ['Hello]\n"
        );
    }

    @Test
    public void testTreePrintWithNumber() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "123.0"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [123.0]\n"
        );
    }

    @Test
    public void testTreePrintWithDate() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "1999/12/31"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [1999/12/31]\n"
        );
    }

    @Test
    public void testTreePrintWithDateTime() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "1999/12/31 12:58"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [1999/12/31 12:58]\n"
        );
    }

    @Test
    public void testTreePrintWithTime() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "12:58:59"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [12:58:59]\n"
        );
    }

    @Test
    public void testTreePrintWithInvalidExpression() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=1.25+"
                                )
                        ),
                "ValueExpressionParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=1.25+]\n" +
                        "      Errors\n" +
                        "        Invalid character '+' at 5\n"
        );
    }

    private ValueExpressionParserComponent valueExpressionParserComponent() {
        return ValueExpressionParserComponent.empty(
                SPREADSHEET_PARSER,
                SPREADSHEET_PARSER_CONTEXT,
                EVALUATION_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValueExpressionParserComponent> type() {
        return ValueExpressionParserComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
