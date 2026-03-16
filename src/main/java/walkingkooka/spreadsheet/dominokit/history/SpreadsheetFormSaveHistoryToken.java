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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;
import walkingkooka.validation.form.Form;

import java.util.Objects;
import java.util.Optional;

/**
 * The save form action saves a new or replaces an existing {@link Form}.
 * <pre>
 * #/1/SpreadsheetName/form/FormName/save/Form
 * #/1/SpreadsheetName/form/FormName/field/A1/save/Form
 * #/1/SpreadsheetName/form/FormName/field/LABEL123/save/Form
 * </pre>
 */
public final class SpreadsheetFormSaveHistoryToken extends SpreadsheetFormHistoryToken
    implements Value<Form<SpreadsheetValidationReference>> {

    static SpreadsheetFormSaveHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final Form<SpreadsheetValidationReference> form,
                                                final Optional<SpreadsheetValidationReference> field) {
        return new SpreadsheetFormSaveHistoryToken(
            id,
            name,
            form,
            field
        );
    }

    private SpreadsheetFormSaveHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final Form<SpreadsheetValidationReference> form,
                                            final Optional<SpreadsheetValidationReference> field) {
        super(
            id,
            name
        );
        this.form = Objects.requireNonNull(form, "form");
        this.field = Objects.requireNonNull(field, "field");
    }

    final Optional<SpreadsheetValidationReference> field;

    @Override
    public Form<SpreadsheetValidationReference> value() {
        return this.form;
    }

    final Form<SpreadsheetValidationReference> form;

    // #/1/SpreadsheetName/form/FormName/save/Form
    // #/1/SpreadsheetName/form/FormName/field/A1/save/Form
    // #/1/SpreadsheetName/form/FormName/field/LABEL123/save/Form
    @Override
    UrlFragment formUrlFragment() {
        return this.form.name()
            .urlFragment()
            .appendSlashThen(
                this.field.map(
                        f -> FIELD.appendSlashThen(f.urlFragment()))
                    .orElse(UrlFragment.EMPTY)
            ).appendSlashThen(SAVE)
            .appendSlashThen(
                UrlFragment.with(
                    MARSHALL_UNMARSHALL_CONTEXT.marshall(this.form)
                        .toString()
                )
            );
    }

    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId id,
                                                        final SpreadsheetName name) {
        return with(
            id,
            name,
            this.form,
            this.field
        );
    }

    // /1/SpreadsheetName/form/FormName
    @Override
    public HistoryToken clearAction() {
        return formSelect(
            this.id,
            this.name,
            this.form.name(),
            NO_FIELD
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitFormSave(
            this.id,
            this.name,
            this.form,
            this.field
        );
    }
}
