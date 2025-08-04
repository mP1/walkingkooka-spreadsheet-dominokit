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

import walkingkooka.spreadsheet.dominokit.textmatch.TextMatchComponent;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.formula.parser.ConditionSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.EqualsSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.GreaterThanEqualsSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.GreaterThanSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.LessThanEqualsSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.LessThanSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.NamedFunctionSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.NotEqualsSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserTokenVisitor;
import walkingkooka.spreadsheet.formula.parser.TextSpreadsheetFormulaParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Optional;

/**
 * A {@link SpreadsheetFormulaParserTokenVisitor} that visits a token and attempts to find functions composed of a textMatch
 * with a cellXXX getter and string literal and a cellValue() within a comparison.
 */
final class SpreadsheetCellFindDialogComponentSpreadsheetFormulaParserTokenVisitor extends SpreadsheetFormulaParserTokenVisitor {

    static void refresh(final SpreadsheetFormulaParserToken query,
                        final SpreadsheetCellFindDialogComponent wizard) {
        new SpreadsheetCellFindDialogComponentSpreadsheetFormulaParserTokenVisitor(wizard)
            .accept(query);
    }

    SpreadsheetCellFindDialogComponentSpreadsheetFormulaParserTokenVisitor(final SpreadsheetCellFindDialogComponent wizard) {
        super();
        this.wizard = wizard;
    }

    // function.........................................................................................................

    // if textMatch with 2 parameters and either LHS or RHS is a method call to cellXXX getter
    // and the other parameter is a String literal,
    // extract the string literal.
    @Override
    protected Visiting startVisit(final NamedFunctionSpreadsheetFormulaParserToken token) {
        this.tryRefreshTextMatch(token);

        return Visiting.CONTINUE;
    }

    private void tryRefreshTextMatch(final NamedFunctionSpreadsheetFormulaParserToken token) {
        final ExpressionFunctionName functionName = token.functionName()
            .toExpressionFunctionName();

        if (SpreadsheetExpressionFunctions.TEXT_MATCH.equals(functionName)) {
            final List<ParserToken> parameters = token.parameters()
                .parameters();

            switch (parameters.size()) {
                case 2:
                    this.textMatchFunctionParameters(
                        parameters.get(0)
                            .cast(SpreadsheetFormulaParserToken.class),
                        parameters.get(1)
                            .cast(SpreadsheetFormulaParserToken.class)
                    );
                    break;
                default:
                    break;
            }
        }
    }

    // textMatch( cellXXX(), StringLiteral)
    private void textMatchFunctionParameters(final SpreadsheetFormulaParserToken left,
                                             final SpreadsheetFormulaParserToken right) {
        for (; ; ) {
            final SpreadsheetCellFindDialogComponent wizard = this.wizard;

            if (this.isCellGetterAndTextMatch(left, right, SpreadsheetExpressionFunctions.CELL_FORMULA, wizard.formula)) {
                break;
            }
            if (this.isCellGetterAndTextMatch(left, right, SpreadsheetExpressionFunctions.CELL_DATE_TIME_SYMBOLS, wizard.dateTimeSymbols)) {
                break;
            }
            if (this.isCellGetterAndTextMatch(left, right, SpreadsheetExpressionFunctions.CELL_FORMATTER, wizard.formatter)) {
                break;
            }
            if (this.isCellGetterAndTextMatch(left, right, SpreadsheetExpressionFunctions.CELL_PARSER, wizard.parser)) {
                break;
            }
            if (this.isCellGetterAndTextMatch(left, right, SpreadsheetExpressionFunctions.CELL_STYLE, wizard.style)) {
                break;
            }
            if (this.isCellGetterAndTextMatch(left, right, SpreadsheetExpressionFunctions.CELL_FORMATTED_VALUE, wizard.formattedValue)) {
                break;
            }

            break;
        }
    }

    private boolean isCellGetterAndTextMatch(final SpreadsheetFormulaParserToken left,
                                             final SpreadsheetFormulaParserToken right,
                                             final ExpressionFunctionName functionName,
                                             final TextMatchComponent component) {
        return this.isCellGetterAndTextMatch0(
            left,
            right,
            functionName,
            component
        ) || this.isCellGetterAndTextMatch0(
            right,
            left,
            functionName,
            component
        );
    }

    private boolean isCellGetterAndTextMatch0(final SpreadsheetFormulaParserToken left,
                                              final SpreadsheetFormulaParserToken right,
                                              final ExpressionFunctionName functionName,
                                              final TextMatchComponent component) {
        boolean updated = false;

        if (this.isCellGetterFunction(left, functionName)) {
            final Optional<String> stringLiteral = this.stringLiteral(right);
            if (stringLiteral.isPresent()) {
                component.setStringValue(
                    stringLiteral
                );

                updated = true;
            }
        }

        return updated;
    }

    private Optional<String> stringLiteral(final SpreadsheetFormulaParserToken token) {
        String string = null;

        if (token.isText()) {
            string = token.cast(TextSpreadsheetFormulaParserToken.class)
                .textValue();
        }

        return Optional.ofNullable(string);
    }

    // condition........................................................................................................

    @Override
    protected Visiting startVisit(final EqualsSpreadsheetFormulaParserToken token) {
        return this.condition(token);
    }

    @Override
    protected Visiting startVisit(final GreaterThanSpreadsheetFormulaParserToken token) {
        return this.condition(token);
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsSpreadsheetFormulaParserToken token) {
        return this.condition(token);
    }

    @Override
    protected Visiting startVisit(final LessThanSpreadsheetFormulaParserToken token) {
        return this.condition(token);
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsSpreadsheetFormulaParserToken token) {
        return this.condition(token);
    }

    @Override
    protected Visiting startVisit(final NotEqualsSpreadsheetFormulaParserToken token) {
        return this.condition(token);
    }

    private Visiting condition(final ConditionSpreadsheetFormulaParserToken token) {
        if (this.isCellGetterFunction(token.left(), SpreadsheetExpressionFunctions.CELL_VALUE)) {
            this.wizard.value.setValue(
                Optional.of(
                    token.toConditionRightSpreadsheetFormulaParserToken()
                )
            );
        }
        return Visiting.SKIP; // no need to visit child tokens within ConditionSpreadsheetFormulaParserToken
    }

    /**
     * Tests if the given token is a function call to the given getter.
     */
    private boolean isCellGetterFunction(final SpreadsheetFormulaParserToken token,
                                         final ExpressionFunctionName getter) {
        boolean test = false;

        if (token.isNamedFunction()) {
            final NamedFunctionSpreadsheetFormulaParserToken namedFunction = (NamedFunctionSpreadsheetFormulaParserToken) token;
            final ExpressionFunctionName functionName = namedFunction.functionName()
                .toExpressionFunctionName();

            // callFormula()
            // callFormatter()
            // callParser()
            // callStyle()
            // callFormattedValue()
            if (getter.equals(functionName)) {
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

    private final SpreadsheetCellFindDialogComponent wizard;
}
