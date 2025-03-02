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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface SpreadsheetCellLabelsAnchorComponentContextTesting<C extends SpreadsheetCellLabelsAnchorComponentContext> extends HistoryTokenContextTesting<C> {

    @Test
    default void testCellLabelsWithNullCellFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().cellLabels(null)
        );
    }

    default void cellLabelsAndCheck(final SpreadsheetCellLabelsAnchorComponentContext context,
                                    final SpreadsheetCellReference cell,
                                    final SpreadsheetLabelName... expected) {
        this.cellLabelsAndCheck(
            context,
            cell,
            Sets.of(expected)
        );
    }

    default void cellLabelsAndCheck(final SpreadsheetCellLabelsAnchorComponentContext context,
                                    final SpreadsheetCellReference cell,
                                    final Set<SpreadsheetLabelName> expected) {
        this.checkEquals(
            expected,
            context.cellLabels(cell),
            "cellLabels " + cell
        );
    }
}
