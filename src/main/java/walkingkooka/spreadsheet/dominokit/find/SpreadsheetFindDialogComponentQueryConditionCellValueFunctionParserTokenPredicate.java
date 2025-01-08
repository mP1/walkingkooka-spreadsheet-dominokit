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

import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetNamedFunctionParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.function.Predicate;

final class SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate implements Predicate<ParserToken> {

    final static SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate INSTANCE = new SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate();

    private SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate() {
    }

    // SpreadsheetGreaterThan "cellValue() > 1"
    //  SpreadsheetNamedFunction "cellValue()"
    //    SpreadsheetFunctionName "cellValue" cellValue (walkingkooka.spreadsheet.expression.SpreadsheetFunctionName)
    //    SpreadsheetFunctionParameters "()"
    //      SpreadsheetParenthesisOpenSymbol "(" "("
    //      SpreadsheetParenthesisCloseSymbol ")" ")"
    //  SpreadsheetWhitespace " " " "
    //  SpreadsheetGreaterThanSymbol ">" ">"
    //  SpreadsheetWhitespace " " " "
    //  SpreadsheetNumber "1"
    //    SpreadsheetDigits "1" "1"
    @Override
    public boolean test(final ParserToken token) {
        boolean test = false;

        if (token instanceof SpreadsheetParserToken) {
            final SpreadsheetParserToken spreadsheetParserToken = token.cast(SpreadsheetParserToken.class);

            if (spreadsheetParserToken.isCondition()) {
                final SpreadsheetConditionParserToken condition = (SpreadsheetConditionParserToken) spreadsheetParserToken;

                test = this.isCellValueFunction(
                    condition.left()
                );
            }
        }

        return test;
    }

    private boolean isCellValueFunction(final SpreadsheetParserToken token) {
        boolean test = false;

        if (token.isNamedFunction()) {
            final SpreadsheetNamedFunctionParserToken namedFunction = (SpreadsheetNamedFunctionParserToken) token;
            final ExpressionFunctionName functionName = namedFunction.functionName()
                .toExpressionFunctionName();

            if (SpreadsheetExpressionFunctions.CELL_VALUE.equals(functionName)) {
                final List<ParserToken> parameters = namedFunction.parameters()
                    .parameters();

                switch (parameters.size()) {
                    case 0:
                        test = true;
                        break;
                    default:
                        break;
                }
            }
        }

        return test;
    }

    @Override
    public String toString() {
        return "cellValue()";
    }
}