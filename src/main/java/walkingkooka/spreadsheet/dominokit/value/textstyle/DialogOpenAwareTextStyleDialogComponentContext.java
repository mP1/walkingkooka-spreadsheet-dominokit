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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;
import java.util.function.BooleanSupplier;

final class DialogOpenAwareTextStyleDialogComponentContext implements TextStyleDialogComponentContext,
    DialogComponentContextDelegator,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    HasSpreadsheetMetadataFetcherWatchersDelegator {

    static DialogOpenAwareTextStyleDialogComponentContext with(final BooleanSupplier dialogIsOpen,
                                                               final TextStyleDialogComponentContext context) {
        return new DialogOpenAwareTextStyleDialogComponentContext(
            dialogIsOpen,
            context
        );
    }

    private DialogOpenAwareTextStyleDialogComponentContext(final BooleanSupplier dialogIsOpen,
                                                           final TextStyleDialogComponentContext context) {
        super();

        this.historyContext = HistoryContexts.dialogComponentOpenAware(
            dialogIsOpen,
            context
        );
        this.context = context;
    }

    // TextStyleDialogComponentContext..................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public Optional<TextStyle> undo() {
        return this.context.undo();
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return this.context.shouldIgnore(token);
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return this.context.isMatch(token);
    }

    // DialogComponentContextDelegator..................................................................................

    @Override
    public String dialogTitle() {
        return this.context.dialogTitle();
    }

    @Override
    public DialogComponentContext dialogComponentContext() {
        return this.context;
    }

    private final TextStyleDialogComponentContext context;

    // HistoryContextDelegator..........................................................................................

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.historyContext.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.historyContext.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // HasSpreadsheetDeltaFetcherWatchersDelegator......................................................................

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    // HasSpreadsheetMetadataFetcherWatchersDelegator...................................................................

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcherOnce(watcher);
    }

    @Override
    public HasSpreadsheetMetadataFetcherWatchers hasSpreadsheetMetadataFetcherWatchers() {
        return this.context;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
