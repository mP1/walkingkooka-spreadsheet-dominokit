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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.validation.form.FormName;

import static org.junit.Assert.assertThrows;

public abstract class SpreadsheetCellFormHistoryTokenTestCase<T extends SpreadsheetCellFormHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    final static FormName FORM_NAME = FormName.with("FormName123");

    SpreadsheetCellFormHistoryTokenTestCase() {
        super();
    }

    // with.............................................................................................................

    @Test
    public final void testWithNullFormNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // formName.........................................................................................................

    @Test
    public final void testFormName() {
        this.formNameAndCheck(
            this.createHistoryToken(),
            FORM_NAME
        );
    }

    final void formNameAndCheck(final SpreadsheetCellFormHistoryToken token,
                                final FormName expected) {
        this.checkEquals(
            expected,
            token.formName(),
            token.urlFragment()::toString
        );
    }

    // delete...........................................................................................................

    @Test
    public final void testDelete() {
        this.deleteAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellFormSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                FORM_NAME
            )
        );
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // HistoryToken.....................................................................................................

    @Override //
    final T createHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
            id,
            name,
            anchoredSelection,
            FORM_NAME
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                  final FormName formName);
}
