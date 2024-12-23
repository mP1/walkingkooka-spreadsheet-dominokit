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


import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.Objects;

/**
 * A text box that supports entry of a condition with a value or expression. It returns a {@link ParserToken}.
 */
public final class SpreadsheetParserComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetParserComponent, SpreadsheetParserToken> {

    public static SpreadsheetParserComponent empty(final SpreadsheetParser parser,
                                                   final SpreadsheetParserContext context) {
        return new SpreadsheetParserComponent(
                Objects.requireNonNull(parser, "parser"),
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetParserComponent(final SpreadsheetParser parser,
                                       final SpreadsheetParserContext context) {
        this.textBox = ValueSpreadsheetTextBox.with(
                (text) -> parser.parseText(
                                text,
                        context
                ).cast(SpreadsheetParserToken.class),
                Object::toString
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetParserToken> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetParserToken> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
