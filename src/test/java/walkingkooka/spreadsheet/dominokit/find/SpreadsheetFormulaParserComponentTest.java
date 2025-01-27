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
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.formula.ConditionRightEqualsSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormulaParsers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFormulaParserComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetFormulaParserToken, SpreadsheetFormulaParserComponent>,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetParser SPREADSHEET_PARSER = SpreadsheetFormulaParsers.conditionRight(
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
            () -> SpreadsheetFormulaParserComponent.empty(
                null,
                SPREADSHEET_PARSER_CONTEXT
            )
        );
    }

    @Test
    public void testEmptyNullSpreadsheetParserContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormulaParserComponent.empty(
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
            SpreadsheetFormulaParserToken.text(
                Lists.of(
                    SpreadsheetFormulaParserToken.doubleQuoteSymbol("\"", "\""),
                    SpreadsheetFormulaParserToken.textLiteral("Hello", "Hello"),
                    SpreadsheetFormulaParserToken.doubleQuoteSymbol("\"", "\"")
                ),
                "\"Hello\""
            )
        );
    }

    @Test
    public void testValueWithNumber() {
        this.valueAndCheck(
            "=123.5",
            SpreadsheetFormulaParserToken.number(
                Lists.of(
                    SpreadsheetFormulaParserToken.digits("123", "123"),
                    SpreadsheetFormulaParserToken.decimalSeparatorSymbol(".", "."),
                    SpreadsheetFormulaParserToken.digits("5", "5")
                ),
                "123.5"
            )
        );
    }

    @Test
    public void testValueWithDate() {
        this.valueAndCheck(
            "=1999/12/31",
            SpreadsheetFormulaParserToken.date(
                Lists.of(
                    SpreadsheetFormulaParserToken.year(1999, "1999"),
                    SpreadsheetFormulaParserToken.textLiteral("/", "/"),
                    SpreadsheetFormulaParserToken.monthNumber(12, "12"),
                    SpreadsheetFormulaParserToken.textLiteral("/", "/"),
                    SpreadsheetFormulaParserToken.dayNumber(31, "31")
                ),
                "1999/12/31"
            )
        );
    }

    @Test
    public void testValueWithDateTime() {
        this.valueAndCheck(
            "=1999/12/31 12:58",
            SpreadsheetFormulaParserToken.dateTime(
                Lists.of(
                    SpreadsheetFormulaParserToken.year(1999, "1999"),
                    SpreadsheetFormulaParserToken.textLiteral("/", "/"),
                    SpreadsheetFormulaParserToken.monthNumber(12, "12"),
                    SpreadsheetFormulaParserToken.textLiteral("/", "/"),
                    SpreadsheetFormulaParserToken.dayNumber(31, "31"),
                    SpreadsheetFormulaParserToken.whitespace(" ", " "),
                    SpreadsheetFormulaParserToken.hour(12, "12"),
                    SpreadsheetFormulaParserToken.textLiteral(":", ":"),
                    SpreadsheetFormulaParserToken.minute(58, "58")
                ),
                "1999/12/31 12:58"
            )
        );
    }

    @Test
    public void testValueWithTime() {
        this.valueAndCheck(
            "=12:58:59",
            SpreadsheetFormulaParserToken.time(
                Lists.of(
                    SpreadsheetFormulaParserToken.hour(12, "12"),
                    SpreadsheetFormulaParserToken.textLiteral(":", ":"),
                    SpreadsheetFormulaParserToken.minute(58, "58"),
                    SpreadsheetFormulaParserToken.textLiteral(":", ":"),
                    SpreadsheetFormulaParserToken.seconds(59, "59")
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
            SpreadsheetFormulaParserToken.equalsSymbol("=", "=")
        );
        tokens.addAll(
            Lists.of(expected)
        );

        this.valueAndCheck(
            text,
            SpreadsheetFormulaParserToken.conditionRightEquals(
                tokens,
                text
            )
        );
    }

    private void valueAndCheck(final String text,
                               final ConditionRightEqualsSpreadsheetFormulaParserToken expected) {
        this.valueAndCheck(
            text,
            Optional.of(expected)
        );
    }

    private void valueAndCheck(final String text,
                               final Optional<ConditionRightSpreadsheetFormulaParserToken> expected) {
        final SpreadsheetFormulaParserComponent component = createComponent()
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
            this.createComponent(),
            "SpreadsheetFormulaParserComponent\n" +
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
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=1+2"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=1+2]\n"
        );
    }

    @Test
    public void testTreePrintWithConditionTest() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        ">3"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [>3]\n"
        );
    }

    @Test
    public void testTreePrintWithString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=\"Hello\""
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=\"Hello\"]\n"
        );
    }

    @Test
    public void testTreePrintWithNumber() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=123.0"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=123.0]\n"
        );
    }

    @Test
    public void testTreePrintWithDate() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=1999/12/31"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=1999/12/31]\n"
        );
    }

    @Test
    public void testTreePrintWithDateTime() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=1999/12/31 12:58"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=1999/12/31 12:58]\n"
        );
    }

    @Test
    public void testTreePrintWithTime() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=12:58:59"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=12:58:59]\n"
        );
    }

    @Test
    public void testTreePrintWithInvalidExpression() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "=1.25+"
                    )
                ),
            "SpreadsheetFormulaParserComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [=1.25+]\n" +
                "      Errors\n" +
                "        Invalid character '+' at 5\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetFormulaParserComponent createComponent() {
        return SpreadsheetFormulaParserComponent.empty(
            SPREADSHEET_PARSER,
            SPREADSHEET_PARSER_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormulaParserComponent> type() {
        return SpreadsheetFormulaParserComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
