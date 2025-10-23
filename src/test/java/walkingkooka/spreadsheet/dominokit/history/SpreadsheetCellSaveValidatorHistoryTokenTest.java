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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValidatorSelectorMap;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveValidatorHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveValidatorHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(
                        ValidatorSelector.parse("hello-validator-1")
                    )
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
            () -> SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        ValidatorSelector.parse("hello-validator-1")
                    ),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(
                        ValidatorSelector.parse("hello-validator-2")
                    ),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(
                        ValidatorSelector.parse("hello-validator-3")
                    )
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
            "/123/SpreadsheetName456/cell/A1/save/validator",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": "hello-validator-"
    // }
    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/validator/" + JsonNode.parse("{\"A1\":\"hello-validator-\"}"),
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        ValidatorSelector.parse("hello-validator-")
                    )
                )
            )
        );
    }

    // {
    //   "A1": "hello-validator-",
    //   "A2": null
    // }
    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/validator/" + JsonNode.parse("{\"A1\":\"hello-validator-1\",\"A2\":null}"),
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        ValidatorSelector.parse("hello-validator-1")
                    ),
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.empty()
                )
            )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseCellWithoutValidator() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/validator/%7B%22A1%22%3Anull%7D",
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.empty()
                )
            )
        );
    }

    // {
    //   "A1": "number #.##"
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> cellToValidator = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                ValidatorSelector.parse("hello-validator-")
            )
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToValidator
            ),
            "/123/SpreadsheetName456/cell/A1/save/validator/" +
                marshallMap(cellToValidator)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> cellToValidator = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                ValidatorSelector.parse("hello-validator-1")
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToValidator
            ),
            "/123/SpreadsheetName456/cell/A1/save/validator/" +
                marshallMap(cellToValidator)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> cellToValidator = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                ValidatorSelector.parse("hello-validator-1")
            ),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(
                ValidatorSelector.parse("hello-validator-2")
            ),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                ValidatorSelector.parse("hello-validator-3")
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToValidator
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/validator/" +
                marshallMap(cellToValidator)
        );
    }

    @Test
    public void testUrlFragmentWithMissingValidator() {
        final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> cellToValidator = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToValidator
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/validator/" +
                marshallMap(cellToValidator)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> map) {
        return marshall(
            SpreadsheetCellReferenceToValidatorSelectorMap.with(map)
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentValidator() {
        final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> value = Maps.of(
            CELL,
            Optional.of(
                ValidatorSelector.parse("different")
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveValidatorHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveValidatorHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveValidatorHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                    ValidatorSelector.parse("hello-validator-")
                )
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveValidatorHistoryToken> type() {
        return SpreadsheetCellSaveValidatorHistoryToken.class;
    }
}
