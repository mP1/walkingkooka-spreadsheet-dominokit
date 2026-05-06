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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.validation.form.FormName;

import java.util.Objects;

/**
 * An action that deletes the named {@link walkingkooka.validation.form.Form}.
 * <pre>
 * #/1/SpreadsheetName/form/FormName/delete
 * </pre>
 */
public final class SpreadsheetFormDeleteHistoryToken extends SpreadsheetFormHistoryToken {

    static SpreadsheetFormDeleteHistoryToken with(final SpreadsheetId spreadsheetId,
                                                  final SpreadsheetName spreadsheetName,
                                                  final FormName formName) {
        return new SpreadsheetFormDeleteHistoryToken(
            spreadsheetId,
            spreadsheetName,
            formName
        );
    }

    private SpreadsheetFormDeleteHistoryToken(final SpreadsheetId spreadsheetId,
                                              final SpreadsheetName spreadsheetName,
                                              final FormName formName) {
        super(
            spreadsheetId,
            spreadsheetName
        );
        this.formName = Objects.requireNonNull(formName, "formName");
    }

    final FormName formName;

    // #/1/SpreadsheetName/form/FormName/delete
    @Override
    UrlFragment formUrlFragment() {
        return this.formName.urlFragment()
            .appendSlashThen(DELETE);
    }

    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return with(
            spreadsheetId,
            spreadsheetName,
            this.formName
        );
    }

    // /1/SpreadsheetName/form/FormName
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
        visitor.visitFormDelete(
            this.spreadsheetId,
            this.spreadsheetName,
            this.formName
        );
    }
}
