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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.time.LocalDateTime;

/**
 * A mostly complete {@link SpreadsheetPatternDialogComponentContext}.
 */
abstract class SpreadsheetPatternDialogComponentContextBasic implements SpreadsheetPatternDialogComponentContext {

    SpreadsheetPatternDialogComponentContextBasic(final AppContext context) {
        this.context = context;
    }

    // SpreadsheetPatternDialogComponentContext.........................................................................

    @Override
    public final SpreadsheetPatternKind patternKind() {
        return this.historyToken()
                .patternKind()
                .get();
    }

    @Override
    public final Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public final Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public final HistoryToken historyToken() {
        return this.context.historyToken();
    }

    @Override
    public final void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public final void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    @Override
    public final SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.context.spreadsheetMetadata()
                .formatterContext(
                        LocalDateTime::now,
                        (s) -> {
                            throw new UnsupportedOperationException();
                        }
                );
    }

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public final Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataWatcher(watcher);
    }

    @Override
    public final void debug(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public final void error(final Object... values) {
        this.context.error(values);
    }

    final AppContext context;

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
