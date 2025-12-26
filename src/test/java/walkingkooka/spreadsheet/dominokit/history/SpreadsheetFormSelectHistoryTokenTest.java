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

import static org.junit.Assert.assertThrows;

public final class SpreadsheetFormSelectHistoryTokenTest extends SpreadsheetFormHistoryTokenTestCase<SpreadsheetFormSelectHistoryToken> {

    // with.............................................................................................................

    @Test
    public void testWithNullFormNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormSelectHistoryToken.with(
                ID,
                NAME,
                null
            )
        );
    }

    // formName.........................................................................................................

    @Test
    public void testFormName() {
        this.formNameAndCheck(
            this.createHistoryToken(),
            FORM_NAME
        );
    }

    // clear...........................................................................................................

    @Test
    public void testClear() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            SpreadsheetFormSelectHistoryToken.with(
                ID,
                NAME,
                FORM_NAME
            )
        );
    }

    @Test
    public void testDelete() {
        this.deleteAndCheck(
            this.createHistoryToken(),
            HistoryToken.formDelete(
                ID,
                NAME,
                FORM_NAME
            )
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            "/123/SpreadsheetName456/form/FormName123"
        );
    }

    @Override
    SpreadsheetFormSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name) {
        return SpreadsheetFormSelectHistoryToken.with(
            id,
            name,
            FORM_NAME
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormSelectHistoryToken> type() {
        return SpreadsheetFormSelectHistoryToken.class;
    }
}
