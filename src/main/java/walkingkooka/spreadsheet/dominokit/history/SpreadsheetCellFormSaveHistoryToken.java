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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueMap;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.validation.form.FormName;

import java.util.Map;
import java.util.Optional;

/**
 * Saves which includes validation of a form.
 * <pre>
 * #/1/SpreadsheetName/cell/A1/form/FormName/save/VALUES
 * </pre>
 */
public final class SpreadsheetCellFormSaveHistoryToken extends SpreadsheetCellFormHistoryToken implements Value<Map<SpreadsheetCellReference, Optional<Object>>> {

    static SpreadsheetCellFormSaveHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final FormName formName,
                                                    final Map<SpreadsheetCellReference, Optional<Object>> value) {
        return new SpreadsheetCellFormSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            formName,
            value
        );
    }

    private SpreadsheetCellFormSaveHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final FormName formName,
                                                final Map<SpreadsheetCellReference, Optional<Object>> values) {
        super(
            id,
            name,
            anchoredSelection,
            formName
        );
        this.values = SpreadsheetCellReferenceToValueMap.with(values);
    }

    @Override
    public Map<SpreadsheetCellReference, Optional<Object>> value() {
        return this.values;
    }

    private final SpreadsheetCellReferenceToValueMap values;

    // #/1/SpreadsheetName/cell/A1/form/FormName/save/SpreadsheetCellReferenceToValueMap
    @Override
    UrlFragment cellFormUrlFragment() {
        return saveUrlFragment(this.values);
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return with(
            id,
            name,
            anchoredSelection,
            this.formName,
            this.values
        );
    }

    // /1/SpreadsheetName/cell/A1/form/FormName
    @Override
    public HistoryToken clearAction() {
        return cellFormSelect(
            this.id,
            this.name(),
            this.anchoredSelection(),
            this.formName()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }
}
