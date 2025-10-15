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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueTypeNameMap;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.validation.ValueTypeName;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveValueTypeHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveValueTypeHistoryToken>
    implements SpreadsheetMetadataTesting {

    private final static ValueTypeName VALUE_TYPE = ValueTypeName.with("hello-value-type");

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(VALUE_TYPE)
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
            () -> SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(VALUE_TYPE),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(VALUE_TYPE),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(VALUE_TYPE)
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
            "/123/SpreadsheetName456/cell/A1/save/valueType",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": VALUE_TYPE
    // }
    @Test
    public void testParseOneCell() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(VALUE_TYPE)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/valueType/" + marshallMap(map),
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": VALUE_TYPE
    //   "A2": VALUE_TYPE
    // }
    @Test
    public void testParseSeveralCells() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(VALUE_TYPE),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(VALUE_TYPE)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/valueType/" + marshallMap(map),
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseOneCellWithoutSymbols() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/valueType/" + marshallMap(map),
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": ValueTypeName
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> cellToValueTypeName = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(VALUE_TYPE)
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToValueTypeName
            ),
            "/123/SpreadsheetName456/cell/A1/save/valueType/" +
                marshallMap(cellToValueTypeName)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> cellToValueTypeName = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(VALUE_TYPE)
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToValueTypeName
            ),
            "/123/SpreadsheetName456/cell/A1/save/valueType/" +
                marshallMap(cellToValueTypeName)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> cellToValueType = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(VALUE_TYPE),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(VALUE_TYPE),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                ValueTypeName.with("different-value-type")
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToValueType
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/valueType/" +
                marshallMap(cellToValueType)
        );
    }

    @Test
    public void testUrlFragmentWithNoValueTypeName() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> cellToValueType = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToValueType
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/valueType/" +
                marshallMap(cellToValueType)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<ValueTypeName>> map) {
        return marshall(
            SpreadsheetCellReferenceToValueTypeNameMap.with(map)
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentValueTypeName() {
        final Map<SpreadsheetCellReference, Optional<ValueTypeName>> value = Maps.of(
            CELL,
            Optional.of(
                ValueTypeName.with("different")
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveValueTypeHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveValueTypeHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveValueTypeHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(VALUE_TYPE)
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveValueTypeHistoryToken> type() {
        return SpreadsheetCellSaveValueTypeHistoryToken.class;
    }
}
