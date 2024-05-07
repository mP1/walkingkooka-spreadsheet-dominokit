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

package walkingkooka.spreadsheet.dominokit.net;

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;

/**
 * The event payload used by {@link SpreadsheetDeltaFetcherWatchers}.
 */
final class SpreadsheetDeltaFetcherWatchersEvent extends FetcherWatchersEvent<SpreadsheetDeltaFetcherWatcher> {

    static SpreadsheetDeltaFetcherWatchersEvent with(final HttpMethod method,
                                                     final AbsoluteOrRelativeUrl url,
                                                     final SpreadsheetDelta delta,
                                                     final AppContext context) {
        return new SpreadsheetDeltaFetcherWatchersEvent(
                method,
                url,
                delta,
                context
        );
    }

    private SpreadsheetDeltaFetcherWatchersEvent(final HttpMethod method,
                                                 final AbsoluteOrRelativeUrl url,
                                                 final SpreadsheetDelta delta,
                                                 final AppContext context) {
        super(context);
        this.method = method;
        this.url = url;
        this.delta = delta;
    }

    @Override
    public void accept(final SpreadsheetDeltaFetcherWatcher watcher) {
        try {
            watcher.onSpreadsheetDelta(
                    this.method,
                    this.url,
                    this.delta,
                    this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                    "SpreadsheetDeltaFetcherWatchersEvent.accept exception: " + cause.getMessage(),
                    cause
            );
        }
    }

    private final HttpMethod method;

    private final AbsoluteOrRelativeUrl url;

    private final SpreadsheetDelta delta;

    @Override
    public String toString() {
        return this.method + " " + this.url + " " + this.delta + " " + this.context;
    }
}
