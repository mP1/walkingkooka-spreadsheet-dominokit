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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface SpreadsheetFormulaSelectAnchorComponentContextTesting<C extends SpreadsheetFormulaSelectAnchorComponentContext> extends HistoryContextTesting<C> {

    // formulaText......................................................................................................

    @Test
    default void testFormulaTextWithNullSpreadsheetExpressionReferenceFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().formulaText(null)
        );
    }

    default void formulaTextAndCheck(final C context,
                                     final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        this.formulaTextAndCheck(
            context,
            spreadsheetExpressionReference,
            Optional.empty()
        );
    }

    default void formulaTextAndCheck(final C context,
                                     final SpreadsheetExpressionReference spreadsheetExpressionReference,
                                     final String expected) {
        this.formulaTextAndCheck(
            context,
            spreadsheetExpressionReference,
            Optional.of(expected)
        );
    }

    default void formulaTextAndCheck(final C context,
                                     final SpreadsheetExpressionReference spreadsheetExpressionReference,
                                     final Optional<String> expected) {
        this.checkEquals(
            expected,
            context.formulaText(spreadsheetExpressionReference)
        );
    }
}
