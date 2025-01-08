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

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.CaseKind;
import walkingkooka.text.HasText;

import java.util.Objects;

final class SpreadsheetComparatorNameListDialogComponentContextSortComparators implements SpreadsheetComparatorNameListDialogComponentContext,
    SpreadsheetDialogComponentContextDelegator {

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
            token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                .propertyName()
                .equals(PROPERTY_NAME);
    }

    @Override
    public String dialogTitle() {
        return CaseKind.kebabToTitle(
            PROPERTY_NAME.text()
        );
    }

    @Override
    public String undo() {
        return this.context.spreadsheetMetadata()
            .get(PROPERTY_NAME)
            .map(HasText::text)
            .orElse("");
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return this.context;
    }

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
