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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueMap;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.validation.form.FormName;

/**
 * An active form for the given cell selection.
 * <pre>
 * #/1/SpreadsheetName/cell/A1/form/FormName
 * </pre>
 */
public final class SpreadsheetCellFormSelectHistoryToken extends SpreadsheetCellFormHistoryToken {

    static {
        SpreadsheetCellReferenceToValueMap.with(Maps.empty());
    }

    static SpreadsheetCellFormSelectHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final FormName formName) {
        return new SpreadsheetCellFormSelectHistoryToken(
            id,
            name,
            anchoredSelection,
            formName
        );
    }

    private SpreadsheetCellFormSelectHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final FormName formName) {
        super(
            id,
            name,
            anchoredSelection,
            formName
        );
    }

    // #/1/SpreadsheetName/cell/A1/form/FormName
    @Override
    UrlFragment cellFormUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return with(
            id,
            name,
            anchoredSelection,
            this.formName
        );
    }

    // /1/SpreadsheetName/cell/A1/form/FormName
    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellFormSelect(
            this.id,
            this.name,
            this.anchoredSelection,
            this.formName
        );
    }
}
