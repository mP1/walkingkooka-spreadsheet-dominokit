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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.validation.form.FormName;

import java.util.Objects;
import java.util.Optional;

/**
 * Selects a Form for viewing or editing.
 * <pre>
 * #/1/SpreadsheetName/form/FormName
 * </pre>
 */
public final class SpreadsheetFormSelectHistoryToken extends SpreadsheetFormHistoryToken {

    static SpreadsheetFormSelectHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final FormName formName) {
        return new SpreadsheetFormSelectHistoryToken(
            id,
            name,
            formName
        );
    }

    private SpreadsheetFormSelectHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final FormName formName) {
        super(
            id,
            name
        );
        this.formName = Objects.requireNonNull(formName, "formName");
    }

    @Override
    public Optional<FormName> formName() {
        return Optional.of(this.formName);
    }

    final FormName formName;

    // #/1/SpreadsheetName/form/FormName
    @Override
    UrlFragment formUrlFragment() {
        return this.formName.urlFragment();
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
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
        visitor.visitFormSelect(
            this.id,
            this.name,
            this.formName
        );
    }
}
