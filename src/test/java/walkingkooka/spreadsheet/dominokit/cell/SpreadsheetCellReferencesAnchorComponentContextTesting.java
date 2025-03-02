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

package walkingkooka.spreadsheet.dominokit.cell;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface SpreadsheetCellReferencesAnchorComponentContextTesting<C extends SpreadsheetCellReferencesAnchorComponentContext> extends HistoryContextTesting<C> {

    @Test
    default void testCellReferencesWithNullSpreadsheetExpressionReferenceFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().cellReferences(null)
        );
    }

    default void cellReferencesAndCheck(final SpreadsheetCellReferencesAnchorComponentContext context,
                                        final SpreadsheetExpressionReference spreadsheetExpressionReference,
                                        final SpreadsheetExpressionReference... expected) {
        this.cellReferencesAndCheck(
            context,
            spreadsheetExpressionReference,
            Sets.of(expected)
        );
    }

    default void cellReferencesAndCheck(final SpreadsheetCellReferencesAnchorComponentContext context,
                                        final SpreadsheetExpressionReference spreadsheetExpressionReference,
                                        final Set<SpreadsheetExpressionReference> expected) {
        this.checkEquals(
            expected,
            context.cellReferences(spreadsheetExpressionReference),
            "cellReferences " + spreadsheetExpressionReference
        );
    }
}
