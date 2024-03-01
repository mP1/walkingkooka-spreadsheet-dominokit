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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetCellClipboardCopyHistoryTokenTest extends SpreadsheetCellClipboardHistoryTokenTestCase<SpreadsheetCellClipboardCopyHistoryToken> {

    @Test
    public void testParseUnknownSpreadsheetCellClipboardValueSelector() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/copy/?unknown",
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
                "/123/SpreadsheetName456/cell/A1/copy/cell",
                this.createHistoryToken()
        );
    }

    @Test
    public void testParseCell2() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:B2/copy/cell",
                this.createHistoryToken(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:B2").setDefaultAnchor(),
                        SpreadsheetCellClipboardValueSelector.CELL
                )
        );
    }

    @Test
    public void testParseFormula() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/copy/formula",
                this.createHistoryToken(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellClipboardValueSelector.FORMULA
                )
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/cell/A1/copy/cell"
        );
    }

    @Test
    public void testUrlFragmentFormula() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellClipboardValueSelector.FORMULA
                ),
                "/123/SpreadsheetName456/cell/A1/copy/formula"
        );
    }

    @Test
    public void testUrlFragmentFormatPattern() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellClipboardValueSelector.FORMAT_PATTERN
                ),
                "/123/SpreadsheetName456/cell/A1/copy/format-pattern"
        );
    }

    @Override
    SpreadsheetCellClipboardCopyHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                                final SpreadsheetCellClipboardValueSelector valueSelector) {
        return SpreadsheetCellClipboardCopyHistoryToken.with(
                id,
                name,
                anchoredSelection,
                valueSelector
        );
    }

    @Override
    public Class<SpreadsheetCellClipboardCopyHistoryToken> type() {
        return SpreadsheetCellClipboardCopyHistoryToken.class;
    }
}
