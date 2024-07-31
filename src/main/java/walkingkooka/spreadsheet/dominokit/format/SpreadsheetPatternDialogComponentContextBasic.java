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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;

/**
 * A mostly complete {@link SpreadsheetPatternDialogComponentContext}.
 */
abstract class SpreadsheetPatternDialogComponentContextBasic implements SpreadsheetPatternDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator {

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
    public final SpreadsheetFormatterContext spreadsheetFormatterContext() {
        final AppContext context = this.context;

        return context.spreadsheetMetadata()
                .formatterContext(
                        context, // implements SpreadsheetFormatterProvider
                        context, // context as SpreadsheetParserProvider
                        context::now,
                        SpreadsheetLabelNameResolvers.fake()
                );
    }

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public final HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    final AppContext context;

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
