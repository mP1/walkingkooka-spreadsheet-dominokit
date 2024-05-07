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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;

/**
 * A watcher that receives all {@link SpreadsheetDelta} response events.
 */
public interface SpreadsheetDeltaFetcherWatcher extends FetcherWatcher {

    /**
     * Contains the result of a request. Note the path only contains the path after the {@link SpreadsheetId} and may be
     * used to identify the actual target end point.
     */
    void onSpreadsheetDelta(final HttpMethod method,
                            final AbsoluteOrRelativeUrl url,
                            final SpreadsheetDelta delta,
                            final AppContext context);
}
