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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionRightParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetParserComponentTest implements ValueComponentTesting<HTMLFieldSetElement, ParserToken, SpreadsheetParserComponent>,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetParser SPREADSHEET_PARSER = SpreadsheetParsers.conditionRight(
            METADATA_EN_AU.spreadsheetParser(
                    SPREADSHEET_PARSER_PROVIDER,
                    PROVIDER_CONTEXT
            )
    );

    // empty............................................................................................................

    @Test
    public void testEmptyNullParserFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetParserComponent.empty(
                        null,
                        SPREADSHEET_PARSER_CONTEXT
                )
        );
    }

    @Test
    public void testEmptyNullSpreadsheetParserContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetParserComponent.empty(
                        SPREADSHEET_PARSER,
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
                "=\"Hello\"",
                SpreadsheetParserToken.text(
                        Lists.of(
                                SpreadsheetParserToken.doubleQuoteSymbol("\"", "\""),
                                SpreadsheetParserToken.textLiteral("Hello", "Hello"),
                                SpreadsheetParserToken.doubleQuoteSymbol("\"", "\"")
                        ),
                        "\"Hello\""
                )
        );
    }

    @Test
    public void testValueWithNumber() {
        this.valueAndCheck(
                "=123.5",
                SpreadsheetParserToken.number(
                        Lists.of(
                                SpreadsheetParserToken.digits("123", "123"),
                                SpreadsheetParserToken.decimalSeparatorSymbol(".", "."),
                                SpreadsheetParserToken.digits("5", "5")
                        ),
                        "123.5"
                )
        );
    }

    @Test
    public void testValueWithDate() {
        this.valueAndCheck(
                "=1999/12/31",
                SpreadsheetParserToken.date(
                        Lists.of(
                                SpreadsheetParserToken.year(1999, "1999"),
                                SpreadsheetParserToken.textLiteral("/", "/"),
                                SpreadsheetParserToken.monthNumber(12, "12"),
                                SpreadsheetParserToken.textLiteral("/", "/"),
                                SpreadsheetParserToken.dayNumber(31, "31")
                        ),
                        "1999/12/31"
                )
        );
    }

    @Test
    public void testValueWithDateTime() {
        this.valueAndCheck(
                "=1999/12/31 12:58",
                SpreadsheetParserToken.dateTime(
                        Lists.of(
                                SpreadsheetParserToken.year(1999, "1999"),
                                SpreadsheetParserToken.textLiteral("/", "/"),
                                SpreadsheetParserToken.monthNumber(12, "12"),
                                SpreadsheetParserToken.textLiteral("/", "/"),
                                SpreadsheetParserToken.dayNumber(31, "31"),
                                SpreadsheetParserToken.whitespace(" ", " "),
                                SpreadsheetParserToken.hour(12, "12"),
                                SpreadsheetParserToken.textLiteral(":", ":"),
                                SpreadsheetParserToken.minute(58, "58")
                        ),
                        "1999/12/31 12:58"
                )
        );
    }

    @Test
    public void testValueWithTime() {
        this.valueAndCheck(
                "=12:58:59",
                SpreadsheetParserToken.time(
                        Lists.of(
                                SpreadsheetParserToken.hour(12, "12"),
                                SpreadsheetParserToken.textLiteral(":", ":"),
                                SpreadsheetParserToken.minute(58, "58"),
                                SpreadsheetParserToken.textLiteral(":", ":"),
                                SpreadsheetParserToken.seconds(59, "59")
                        ),
                        "12:58:59"
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
                               final ParserToken... expected) {
        final List<ParserToken> tokens = Lists.array();
        tokens.add(
                SpreadsheetParserToken.equalsSymbol("=", "=")
        );
        tokens.addAll(
                Lists.of(expected)
        );

        this.valueAndCheck(
                text,
                SpreadsheetParserToken.conditionRight(
                        tokens,
                        text
                )
        );
    }

    private void valueAndCheck(final String text,
                               final SpreadsheetConditionRightParserToken expected) {
        this.valueAndCheck(
                text,
                Optional.of(expected)
        );
    }

    private void valueAndCheck(final String text,
                               final Optional<SpreadsheetConditionRightParserToken> expected) {
        final SpreadsheetParserComponent component = valueExpressionParserComponent()
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
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      []\n" +
                        "      Errors\n" +
                        "        Empty \"text\"\n"
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
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=1+2]\n"
        );
    }

    @Test
    public void testTreePrintWithConditionTest() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        ">3"
                                )
                        ),
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [>3]\n"
        );
    }

    @Test
    public void testTreePrintWithString() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=\"Hello\""
                                )
                        ),
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=\"Hello\"]\n"
        );
    }

    @Test
    public void testTreePrintWithNumber() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=123.0"
                                )
                        ),
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=123.0]\n"
        );
    }

    @Test
    public void testTreePrintWithDate() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=1999/12/31"
                                )
                        ),
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=1999/12/31]\n"
        );
    }

    @Test
    public void testTreePrintWithDateTime() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=1999/12/31 12:58"
                                )
                        ),
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=1999/12/31 12:58]\n"
        );
    }

    @Test
    public void testTreePrintWithTime() {
        this.treePrintAndCheck(
                this.valueExpressionParserComponent()
                        .setStringValue(
                                Optional.of(
                                        "=12:58:59"
                                )
                        ),
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=12:58:59]\n"
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
                "SpreadsheetParserComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [=1.25+]\n" +
                        "      Errors\n" +
                        "        Invalid character '+' at 5\n"
        );
    }

    private SpreadsheetParserComponent valueExpressionParserComponent() {
        return SpreadsheetParserComponent.empty(
                SPREADSHEET_PARSER,
                SPREADSHEET_PARSER_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetParserComponent> type() {
        return SpreadsheetParserComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
