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
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormName;

import java.util.Objects;
import java.util.Optional;

/**
 * The save form action saves a new or replaces an existing {@link Form}.
 * <pre>
 * #/1/SpreadsheetName/form/FormName/save/Form
 * </pre>
 */
public final class SpreadsheetFormSaveHistoryToken extends SpreadsheetFormHistoryToken
    implements Value<Form<SpreadsheetExpressionReference>> {

    static SpreadsheetFormSaveHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final Form<SpreadsheetExpressionReference> form) {
        return new SpreadsheetFormSaveHistoryToken(
            id,
            name,
            form
        );
    }

    private SpreadsheetFormSaveHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final Form<SpreadsheetExpressionReference> form) {
        super(
            id,
            name
        );
        this.form = Objects.requireNonNull(form, "form");
    }

    @Override
    public Optional<FormName> formName() {
        return Optional.of(
            this.form.name()
        );
    }

    @Override
    public Form<SpreadsheetExpressionReference> value() {
        return this.form;
    }

    private final Form<SpreadsheetExpressionReference> form;

    // #/1/SpreadsheetName/form/FormName/save/Form
    @Override
    UrlFragment formUrlFragment() {
        return this.form.name().urlFragment()
            .appendSlashThen(
                SAVE).appendSlashThen(
                UrlFragment.with(
                    MARSHALL_UNMARSHALL_CONTEXT.marshall(this.form)
                        .toString()
                )
            );
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return with(
            id,
            name,
            this.form
        );
    }

    // /1/SpreadsheetName/form/FormName
    @Override
    public HistoryToken clearAction() {
        return formSelect(
            this.id,
            this.name,
            this.form.name()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }
}
