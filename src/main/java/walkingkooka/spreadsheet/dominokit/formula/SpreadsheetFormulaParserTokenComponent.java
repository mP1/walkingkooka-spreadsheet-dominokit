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

package walkingkooka.spreadsheet.dominokit.formula;


import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.Objects;

/**
 * A text box that supports entry of a condition with a value or expression. It returns a {@link ParserToken}.
 */
public final class SpreadsheetFormulaParserTokenComponent implements ValueTextBoxComponentDelegator<SpreadsheetFormulaParserTokenComponent, SpreadsheetFormulaParserToken> {

    public static SpreadsheetFormulaParserTokenComponent empty(final SpreadsheetParser parser,
                                                               final SpreadsheetParserContext context) {
        return new SpreadsheetFormulaParserTokenComponent(
            Objects.requireNonNull(parser, "parser"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetFormulaParserTokenComponent(final SpreadsheetParser parser,
                                                   final SpreadsheetParserContext context) {
        this.textBox = ValueTextBoxComponent.with(
            (text) -> parser.parseText(
                text,
                context
            ).cast(SpreadsheetFormulaParserToken.class),
            Object::toString
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<SpreadsheetFormulaParserToken> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<SpreadsheetFormulaParserToken> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
