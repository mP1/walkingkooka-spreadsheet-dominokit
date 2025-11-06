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

package walkingkooka.spreadsheet.dominokit.comparator;

import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetComparatorNameListDialogComponentContextSortComparators implements SpreadsheetComparatorNameListDialogComponentContext,
    DialogComponentContextDelegator {

    static SpreadsheetComparatorNameListDialogComponentContextSortComparators with(final AppContext context) {
        return new SpreadsheetComparatorNameListDialogComponentContextSortComparators(
            Objects.requireNonNull(context, "context")
        );
    }

    private final static SpreadsheetMetadataPropertyName<SpreadsheetComparatorNameList> PROPERTY_NAME = SpreadsheetMetadataPropertyName.SORT_COMPARATORS;

    private SpreadsheetComparatorNameListDialogComponentContextSortComparators(final AppContext context) {
        this.context = context;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            PROPERTY_NAME.equals(
                token.metadataPropertyName()
                    .orElse(null)
            );
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(PROPERTY_NAME);
    }

    @Override
    public Optional<SpreadsheetComparatorNameList> undo() {
        return this.context.spreadsheetMetadata()
            .get(PROPERTY_NAME);
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // DialogComponentContext...........................................................................................

    @Override
    public DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(this.context);
    }

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
