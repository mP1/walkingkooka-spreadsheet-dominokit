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
import walkingkooka.spreadsheet.formula.NamedFunctionSpreadsheetParserToken;
import walkingkooka.spreadsheet.formula.SpreadsheetParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that matches a textMatch function call that also contains a cellXXX getter function.
 */
final class SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate implements Predicate<ParserToken> {

    static SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate with(final ExpressionFunctionName getter) {
        return new SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate(getter);
    }

    private SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate(final ExpressionFunctionName getter) {
        this.getter = getter;
    }

    // SpreadsheetNamedFunction "textMatch(\"hello\", cellFormula())"
    //  SpreadsheetFunctionName "textMatch" textMatch (walkingkooka.spreadsheet.expression.SpreadsheetFunctionName)
    //  SpreadsheetFunctionParameters "(\"hello\", cellFormula())"
    //    SpreadsheetParenthesisOpenSymbol "(" "("
    //    SpreadsheetText "hello"
    //      SpreadsheetDoubleQuoteSymbol "\"" "\""
    //      SpreadsheetTextLiteral "hello" "hello"
    //      SpreadsheetDoubleQuoteSymbol "\"" "\""
    //    SpreadsheetValueSeparatorSymbol "," ","
    //    SpreadsheetWhitespace " " " "
    //    SpreadsheetNamedFunction "cellFormula()"
    //      SpreadsheetFunctionName "cellFormula" cellFormula (walkingkooka.spreadsheet.expression.SpreadsheetFunctionName)
    //      SpreadsheetFunctionParameters "()"
    //        SpreadsheetParenthesisOpenSymbol "(" "("
    //        SpreadsheetParenthesisCloseSymbol ")" ")"
    //    SpreadsheetParenthesisCloseSymbol ")" ")"
    @Override
    public boolean test(final ParserToken token) {
        boolean test = false;

        if (token instanceof SpreadsheetParserToken) {
            final SpreadsheetParserToken spreadsheetParserToken = token.cast(SpreadsheetParserToken.class);

            if (spreadsheetParserToken.isNamedFunction()) {
                final NamedFunctionSpreadsheetParserToken namedFunction = (NamedFunctionSpreadsheetParserToken) spreadsheetParserToken;
                final ExpressionFunctionName functionName = namedFunction.functionName()
                    .toExpressionFunctionName();

                if (SpreadsheetExpressionFunctions.TEXT_MATCH.equals(functionName)) {
                    final List<ParserToken> parameters = namedFunction.parameters()
                        .parameters();

                    switch (parameters.size()) {
                        case 2:
                            test = isCellGetterFunction(
                                parameters.get(0)
                                    .cast(SpreadsheetParserToken.class)
                            ) ||
                                isCellGetterFunction(
                                    parameters.get(1)
                                        .cast(SpreadsheetParserToken.class)
                                );
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return test;
    }

    private boolean isCellGetterFunction(final SpreadsheetParserToken token) {
        boolean test = false;

        if (token.isNamedFunction()) {
            final NamedFunctionSpreadsheetParserToken namedFunction = (NamedFunctionSpreadsheetParserToken) token;
            final ExpressionFunctionName functionName = namedFunction.functionName()
                .toExpressionFunctionName();

            // callFormula()
            // callFormatter()
            // callParser()
            // callStyle()
            // callFormattedValue()
            if (this.getter.equals(functionName)) {
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

    private final ExpressionFunctionName getter;

    @Override
    public String toString() {
        return "textMatch(\"\", " + this.getter + "())";
    }
}
