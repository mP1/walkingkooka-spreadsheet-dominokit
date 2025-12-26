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

public final class SpreadsheetCellFormSelectHistoryTokenTest extends SpreadsheetCellFormHistoryTokenTestCase<SpreadsheetCellFormSelectHistoryToken> {

    // clear...........................................................................................................

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
            "/123/SpreadsheetName456/cell/A1/form/FormName123"
        );
    }

    @Override
    SpreadsheetCellFormSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection,
                                                             final FormName formName) {
        return SpreadsheetCellFormSelectHistoryToken.with(
            id,
            name,
            anchoredSelection,
            formName
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellFormSelectHistoryToken> type() {
        return SpreadsheetCellFormSelectHistoryToken.class;
    }
}
