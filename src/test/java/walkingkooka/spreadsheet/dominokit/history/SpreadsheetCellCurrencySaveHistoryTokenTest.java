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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellCurrencySaveHistoryTokenTest extends SpreadsheetCellCurrencyHistoryTokenTestCase<SpreadsheetCellCurrencySaveHistoryToken> {

    @Test
    public void testWithNullSpreadsheetCurrencySelectorFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellCurrencySaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/currency/save/" + CURRENCY.getCurrencyCode());
    }

    @Test
    public void testUrlFragmentCellEmptySave() {
        this.urlFragmentAndCheck(
            SpreadsheetCellCurrencySaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            ),
            "/123/SpreadsheetName456/cell/A1/currency/save/"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/currency/save/" + CURRENCY.getCurrencyCode()
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/currency/save/" + CURRENCY.getCurrencyCode()
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/currency/save/" + CURRENCY.getCurrencyCode()
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellCurrencySelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    @Override
    SpreadsheetCellCurrencySaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellCurrencySaveHistoryToken.with(
            id,
            name,
            selection,
            Optional.of(CURRENCY)
        );
    }

    @Override
    public Class<SpreadsheetCellCurrencySaveHistoryToken> type() {
        return SpreadsheetCellCurrencySaveHistoryToken.class;
    }
}
