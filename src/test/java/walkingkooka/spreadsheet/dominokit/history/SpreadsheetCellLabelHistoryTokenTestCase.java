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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;

public abstract class SpreadsheetCellLabelHistoryTokenTestCase<T extends SpreadsheetCellLabelHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    SpreadsheetCellLabelHistoryTokenTestCase() {
        super();
    }

    // delete...........................................................................................................

    @Test
    public final void testDelete() {
        this.deleteAndCheck(
            this.createHistoryToken()
        );
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // labelMappingTarget...............................................................................................

    @Test
    public final void testLabelMappingTargetWhenCell() {
        this.labelMappingTargetAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            ),
            CELL
        );
    }

    @Test
    public final void testLabelMappingTargetWhenCellRange() {
        this.labelMappingTargetAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                RANGE.setDefaultAnchor()
            ),
            RANGE
        );
    }

    @Test
    public final void testLabelMappingTargetWhenLabel() {
        this.labelMappingTargetAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                LABEL.setDefaultAnchor()
            ),
            LABEL
        );
    }
}
