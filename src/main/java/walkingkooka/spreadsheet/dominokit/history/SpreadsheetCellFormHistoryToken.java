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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.validation.form.FormName;

import java.util.Objects;

/**
 * Base class for various form functions for a cell selection.
 * <pre>
 * #/SpreadsheetId/SpreadsheetName/cell/form/
 * #/SpreadsheetId/SpreadsheetName/cell/form/save/XXX
 * </pre>
 */
public abstract class SpreadsheetCellFormHistoryToken extends SpreadsheetCellHistoryToken {

    SpreadsheetCellFormHistoryToken(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                    final FormName formName) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.formName = Objects.requireNonNull(formName, "formName");
    }

    public final FormName formName() {
        return this.formName;
    }

    final FormName formName;

    @Override //
    final UrlFragment cellUrlFragment() {
        return FORM.appendSlashThen(
            this.formName.urlFragment()
        ).appendSlashThen(this.cellFormUrlFragment());
    }

    abstract UrlFragment cellFormUrlFragment();
}
