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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public abstract class SpreadsheetLabelMappingHistoryTokenTestCase<T extends SpreadsheetLabelMappingHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.parseCell("A1");

    final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    SpreadsheetLabelMappingHistoryTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final SpreadsheetLabelName label,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createSpreadsheetHistoryHashToken(
                        label
                ),
                expected
        );
    }

    final T createHistoryHashToken() {
        return this.createSpreadsheetHistoryHashToken(
                ID,
                NAME,
                LABEL
        );
    }

    final T createSpreadsheetHistoryHashToken(final SpreadsheetLabelName label) {
        return this.createSpreadsheetHistoryHashToken(
                ID,
                NAME,
                label
        );
    }

    abstract T createSpreadsheetHistoryHashToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetLabelName label);
}
