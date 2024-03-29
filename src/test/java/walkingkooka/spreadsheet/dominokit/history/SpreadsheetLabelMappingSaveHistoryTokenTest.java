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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLabelMappingSaveHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase<SpreadsheetLabelMappingSaveHistoryToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/label/Label123/save/A1"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                SpreadsheetLabelMappingSaveHistoryToken.with(
                        ID,
                        NAME,
                        LABEL.mapping(SpreadsheetSelection.parseCellRange("B2:C3"))
                ),
                "/123/SpreadsheetName456/label/Label123/save/B2:C3"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.labelMapping(
                        ID,
                        NAME,
                        Optional.of(LABEL)
                )
        );
    }

    @Override
    SpreadsheetLabelMappingSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSaveHistoryToken.with(
                id,
                name,
                label.mapping(CELL)
        );
    }

    @Override
    public Class<SpreadsheetLabelMappingSaveHistoryToken> type() {
        return SpreadsheetLabelMappingSaveHistoryToken.class;
    }
}
