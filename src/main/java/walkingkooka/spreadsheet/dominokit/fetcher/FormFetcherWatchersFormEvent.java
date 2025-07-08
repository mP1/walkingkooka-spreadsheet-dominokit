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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.validation.form.Form;

/**
 * The event payload used by {@link FormFetcherWatchers}.
 */
final class FormFetcherWatchersFormEvent extends FetcherWatchersEvent<FormFetcherWatcher> {

    private FormFetcherWatchersFormEvent(final SpreadsheetId id,
                                         final Form<SpreadsheetExpressionReference> form,
                                         final AppContext context) {
        super(context);
        this.id = id;
        this.form = form;
    }

    static FormFetcherWatchersFormEvent with(final SpreadsheetId id,
                                             final Form<SpreadsheetExpressionReference> form,
                                             final AppContext context) {
        return new FormFetcherWatchersFormEvent(
            id,
            form,
            context
        );
    }

    @Override
    void fire(final FormFetcherWatcher watcher) {
        watcher.onForm(
            this.id,
            this.form,
            this.context
        );
    }

    private final SpreadsheetId id;

    private final Form<SpreadsheetExpressionReference> form;

    @Override
    public String toString() {
        return this.id + " " + this.form;
    }
}
