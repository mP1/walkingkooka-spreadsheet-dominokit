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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;

/**
 * A {@link HistoryTokenAwareComponentLifecycle} specialization where {@link #isMatch(HistoryToken)} logic performs the same
 * tests as required by a {@link SpreadsheetViewportComponent}.
 */
public interface SpreadsheetViewportComponentLifecycle extends HistoryTokenAwareComponentLifecycle,
    HasSpreadsheetMetadata,
    HasSpreadsheetViewportCache {

    @Override
    default boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override default boolean isMatch(final HistoryToken token) {
        boolean match = token instanceof SpreadsheetNameHistoryToken;

        if (match) {
            final SpreadsheetMetadata metadata = this.spreadsheetMetadata();
            final SpreadsheetViewportCache viewportCache = this.spreadsheetViewportCache();
            final SpreadsheetViewportWindows windows = viewportCache.windows();

            match = metadata.isNotEmpty() && windows.isNotEmpty();
        }
        return match;
    }
}
