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
import walkingkooka.spreadsheet.formula.parser.NamedFunctionSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that matches OR function with a TEXTMATCH in the first parameter ignoring the second.
 */
final class SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate implements Predicate<ParserToken> {

    static SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate with(final int parameterIndex,
                                                                                               final ExpressionFunctionName getter) {
        return new SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate(
            parameterIndex,
            getter
        );
    }

    private SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate(final int parameterIndex,
                                                                                           final ExpressionFunctionName getter) {
        this.parameterIndex = parameterIndex;
        this.textMatch = SpreadsheetCellFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(getter);
    }

    @Override
    public boolean test(final ParserToken token) {
        boolean test = false;

        if (token instanceof SpreadsheetFormulaParserToken) {
            final SpreadsheetFormulaParserToken spreadsheetParserToken = token.cast(SpreadsheetFormulaParserToken.class);

            if (spreadsheetParserToken.isNamedFunction()) {
                final NamedFunctionSpreadsheetFormulaParserToken namedFunction = (NamedFunctionSpreadsheetFormulaParserToken) spreadsheetParserToken;
                final ExpressionFunctionName functionName = namedFunction.functionName()
                    .toExpressionFunctionName();

                if (SpreadsheetExpressionFunctions.OR.equals(functionName)) {
                    final List<ParserToken> parameters = namedFunction.parameters()
                        .parameters();

                    switch (parameters.size()) {
                        case 2:
                            test = this.textMatch.test(
                                parameters.get(this.parameterIndex)
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

    private final int parameterIndex;

    private final Predicate<ParserToken> textMatch;

    @Override
    public String toString() {
        return 0 == this.parameterIndex ?
            "or(textMatch(\"*\",cellXXX()),1)" :
            "or(0,textMatch(\"*\",cellXXX()))";
    }
}
