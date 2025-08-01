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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToLocaleMap;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveLocaleHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveLocaleHistoryToken> {

    private static final Locale EN_AU = Locale.forLanguageTag("en-AU");
    private static final Locale EN_NZ = Locale.forLanguageTag("en-NZ");

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(Locale.ENGLISH)
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
            () -> SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(Locale.ENGLISH),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(Locale.ENGLISH),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(Locale.ENGLISH)
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
            "/123/SpreadsheetName456/cell/A1/save/locale",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": "en-AU"
    // }
    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/locale/%7B%0A%20%20%22A1%22:%20%22en-AU%22%0A%7D",
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(EN_AU)
                )
            )
        );
    }

    // {
    //   "A1": "en-AU",
    //   "A2": "en-NZ"
    // }
    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/locale/%7B%0A%20%20%22A1%22:%20%22en-AU%22,%0A%20%20%22A2%22:%20%22en-NZ%22%0A%7D",
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        EN_AU
                    ),
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(
                        EN_NZ
                    )
                )
            )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseOneCellWithoutLocale() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/locale/%7B%0A%20%20%22A1%22:%20null%0A%7D",
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.empty()
                )
            )
        );
    }

    // {
    //   "A1": "en-AU
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<Locale>> cellToLocale = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                EN_AU
            )
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToLocale
            ),
            "/123/SpreadsheetName456/cell/A1/save/locale/" +
                marshallMap(cellToLocale)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<Locale>> cellToLocale = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(EN_AU)
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToLocale
            ),
            "/123/SpreadsheetName456/cell/A1/save/locale/" +
                marshallMap(cellToLocale)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<Locale>> cellToLocale = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(EN_AU),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(EN_NZ),
            SpreadsheetSelection.parseCell("A3"),
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToLocale
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/locale/" +
                marshallMap(cellToLocale)
        );
    }

    @Test
    public void testUrlFragmentWithNoLocale() {
        final Map<SpreadsheetCellReference, Optional<Locale>> cellToLocale = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToLocale
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/locale/" +
                marshallMap(cellToLocale)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<Locale>> map) {
        return marshall(
            SpreadsheetCellReferenceToLocaleMap.with(map)
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveValueWithDifferentLocale() {
        final Map<SpreadsheetCellReference, Optional<Locale>> value = Maps.of(
            CELL,
            Optional.of(Locale.FRANCE)
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveLocaleHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveLocaleHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveLocaleHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(EN_AU)
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveLocaleHistoryToken> type() {
        return SpreadsheetCellSaveLocaleHistoryToken.class;
    }
}
