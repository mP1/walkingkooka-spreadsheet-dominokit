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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.validation.form.Form;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFormSaveHistoryTokenTest extends SpreadsheetFormHistoryTokenTestCase<SpreadsheetFormSaveHistoryToken> {

    private final static Form<SpreadsheetExpressionReference> FORM = SpreadsheetForms.form(FORM_NAME).setFields(
        Lists.of(
            SpreadsheetForms.field(
                SpreadsheetSelection.A1
            ).setLabel("LabelA1")
        )
    );

    // with.............................................................................................................

    @Test
    public void testWithNullFormFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormSaveHistoryToken.with(
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

    // clear............................................................................................................

    @Test
    public void testClear() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.formSelect(
                ID,
                NAME,
                FORM_NAME
            )
        );
    }

    @Test
    public void testDelete() {
        this.deleteAndCheck(
            this.createHistoryToken()
        );
    }

    // UrlFragment......................................................................................................

    // {
    //  "name" : "FormName123",
    //  "fields" : [
    //    {
    //      "reference" : {
    //        "type" : "spreadsheet-cell-reference",
    //        "value" : "A1"
    //      },
    //      "label" : "LabelA1"
    //    }
    //  ]
    // }
    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            "/123/SpreadsheetName456/form/FormName123/save/" +
                JsonNodeMarshallContexts.basic()
                    .marshall(FORM)
        );
    }

    @Override
    SpreadsheetFormSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name) {
        return SpreadsheetFormSaveHistoryToken.with(
            id,
            name,
            FORM
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormSaveHistoryToken> type() {
        return SpreadsheetFormSaveHistoryToken.class;
    }
}
