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
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetCellClipboardPasteHistoryTokenTest extends SpreadsheetCellClipboardHistoryTokenTestCase<SpreadsheetCellClipboardPasteHistoryToken> {

    @Test
    public void testParseUnknownKind() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/paste/?unknown",
            HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/paste/cell",
            this.createHistoryToken()
        );
    }

    @Test
    public void testParseCell2() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:B2/paste/cell",
            this.createHistoryToken(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2").setDefaultAnchor(),
                SpreadsheetCellClipboardKind.CELL
            )
        );
    }

    @Test
    public void testParseFormula() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/paste/formula",
            this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetCellClipboardKind.FORMULA
            )
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(),
            "/123/SpreadsheetName456/cell/A1/paste/cell"
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetCellClipboardKind.FORMULA
            ),
            "/123/SpreadsheetName456/cell/A1/paste/formula"
        );
    }

    @Test
    public void testUrlFragmentFormatter() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetCellClipboardKind.FORMATTER
            ),
            "/123/SpreadsheetName456/cell/A1/paste/formatter"
        );
    }

    @Override
    SpreadsheetCellClipboardPasteHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final SpreadsheetCellClipboardKind kind) {
        return SpreadsheetCellClipboardPasteHistoryToken.with(
            id,
            name,
            anchoredSelection,
            kind
        );
    }

    @Override
    public Class<SpreadsheetCellClipboardPasteHistoryToken> type() {
        return SpreadsheetCellClipboardPasteHistoryToken.class;
    }
}
