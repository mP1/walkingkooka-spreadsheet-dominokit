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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;

import java.util.Optional;

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
                null,
                Optional.empty()
            )
        );
    }

    @Test
    public void testWithNullFieldFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormSelectHistoryToken.with(
                ID,
                NAME,
                FORM_NAME,
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
                FORM_NAME,
                Optional.empty() // SpreadsheetValidationReference
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

    @Test
    public void testFieldNameWhenMissing() {
        this.fieldAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public void testFieldNameWhenCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        this.fieldAndCheck(
            this.createHistoryToken(
                Optional.of(cell)
            ),
            cell
        );
    }

    @Test
    public void testFieldNameWhenLabel() {
        final SpreadsheetLabelName label = SpreadsheetSelection.labelName("Label123");

        this.fieldAndCheck(
            this.createHistoryToken(
                Optional.of(label)
            ),
            label
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            "/123/SpreadsheetName456/form/FormName123"
        );
    }

    @Test
    public void testUrlFragmentWithFieldSpreadsheetCellReference() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                Optional.of(
                    SpreadsheetSelection.A1
                )
            ),
            "/123/SpreadsheetName456/form/FormName123/field/A1"
        );
    }

    @Test
    public void testUrlFragmentWithFieldSpreadsheetLabelName() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                Optional.of(
                    SpreadsheetSelection.labelName("Label123")
                )
            ),
            "/123/SpreadsheetName456/form/FormName123/field/Label123"
        );
    }

    @Override
    SpreadsheetFormSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name) {
        return SpreadsheetFormSelectHistoryToken.with(
            id,
            name,
            FORM_NAME,
            Optional.empty()
        );
    }

    private SpreadsheetFormSelectHistoryToken createHistoryToken(final Optional<SpreadsheetValidationReference> field) {
        return SpreadsheetFormSelectHistoryToken.with(
            ID,
            NAME,
            FORM_NAME,
            field
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormSelectHistoryToken> type() {
        return SpreadsheetFormSelectHistoryToken.class;
    }
}
