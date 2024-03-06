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
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveStyleHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveStyleHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.parseCell("A2"),
                                TextStyle.EMPTY
                        )
                )
        );

        this.checkEquals(
                "Save value includes cells A2 outside A1",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testWithSaveFormulasOutsideRangeFails2() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                TextStyle.EMPTY,
                                SpreadsheetSelection.parseCell("A3"),
                                TextStyle.EMPTY,
                                SpreadsheetSelection.parseCell("A4"),
                                TextStyle.EMPTY
                        )
                )
        );

        this.checkEquals(
                "Save value includes cells A1, A4 outside A2:A3",
                thrown.getMessage(),
                "message"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseNoCellsFails() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/save/style",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/save/style/{\"A1\":{\"color\":\"#123456\"}}",
                SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                TextStyle.EMPTY.set(
                                        TextStylePropertyName.COLOR,
                                        Color.parse("#123456")
                                )
                        )
                )
        );
    }

    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:A2/save/style/{\"A1\":{\"color\":\"#111111\"},\"A2\":{\"color\":\"#222222\"}}",
                SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A2")
                                .setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                TextStyle.EMPTY.set(
                                        TextStylePropertyName.COLOR,
                                        Color.parse("#111111")
                                ),
                                SpreadsheetSelection.parseCell("A2"),
                                TextStyle.EMPTY.set(
                                        TextStylePropertyName.COLOR,
                                        Color.parse("#222222")
                                )
                        )
                )
        );
    }

    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, TextStyle> cellToStyle = Maps.of(
                SpreadsheetSelection.A1,
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#123456")
                )
        );
        this.urlFragmentAndCheck(
                SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        cellToStyle
                ),
                "/123/SpreadsheetName456/cell/A1/save/style/" + marshallMap(cellToStyle)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, TextStyle> cellToStyle = Maps.of(
                SpreadsheetSelection.A1,
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#123456")
                )
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        cellToStyle
                ),
                "/123/SpreadsheetName456/cell/A1/save/style/" + marshallMap(cellToStyle)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, TextStyle> cellToStyle = Maps.of(
                SpreadsheetSelection.A1,
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#111111")
                ),
                SpreadsheetSelection.parseCell("A2"),
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#222222")
                ),
                SpreadsheetSelection.parseCell("A3"),
                TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#333333")
                )
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveStyleHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A3")
                                .setDefaultAnchor(),
                        cellToStyle
                ),
                "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/style/" + marshallMap(cellToStyle)
        );
    }

    @Override
    SpreadsheetCellSaveStyleHistoryToken createHistoryToken(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveStyleHistoryToken.with(
                id,
                name,
                anchoredSelection,
                Maps.of(
                        SpreadsheetSelection.A1,
                        TextStyle.EMPTY.set(
                                TextStylePropertyName.TEXT_ALIGN,
                                TextAlign.CENTER
                        )
                )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveStyleHistoryToken> type() {
        return SpreadsheetCellSaveStyleHistoryToken.class;
    }
}
