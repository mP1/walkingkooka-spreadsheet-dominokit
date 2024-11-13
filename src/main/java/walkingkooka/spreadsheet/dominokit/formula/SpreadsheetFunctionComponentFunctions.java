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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SpreadsheetFunctionComponentFunctions implements PublicStaticHelper {

    /**
     * {@see SpreadsheetFunctionComponentExpressionParserFunction}
     */
    public static Function<String, SpreadsheetFormula> expressionParser(final Supplier<SpreadsheetParserContext> context) {
        return SpreadsheetFunctionComponentExpressionParserFunction.with(context);
    }

    private SpreadsheetFunctionComponentFunctions() {
        throw new UnsupportedOperationException();
    }
}
