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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueMap;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.validation.form.FormName;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertThrows;

public final class SpreadsheetCellFormSaveHistoryTokenTest extends SpreadsheetCellFormHistoryTokenTestCase<SpreadsheetCellFormSaveHistoryToken> {

    private final static Map<SpreadsheetCellReference, Optional<Object>> CELL_TO_VALUE = Maps.of(
        SpreadsheetSelection.A1,
        Optional.of("String1"),
        SpreadsheetSelection.parseCell("A2"),
        Optional.of(
            ExpressionNumberKind.BIG_DECIMAL.create(12.5)
        ),
        SpreadsheetSelection.parseCell("A3"),
        Optional.of(
            LocalDate.of(1999, 12, 31)
        )
    );

    // with.............................................................................................................

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellFormSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                FORM_NAME,
                null
            )
        );
    }

    // clear............................................................................................................

    @Test
    public void testClear() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellFormSelect(
                ID,
                NAME,
                SELECTION,
                FORM_NAME
            )
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            "/123/SpreadsheetName456/cell/A1/form/FormName123/save/" + SpreadsheetCellReferenceToValueMap.with(CELL_TO_VALUE).urlFragment()
        );
    }

    @Override
    SpreadsheetCellFormSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final FormName formName) {
        return SpreadsheetCellFormSaveHistoryToken.with(
            id,
            name,
            anchoredSelection,
            formName,
            CELL_TO_VALUE
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellFormSaveHistoryToken> type() {
        return SpreadsheetCellFormSaveHistoryToken.class;
    }
}
