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

import walkingkooka.spreadsheet.dominokit.history.CrudHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetPatternComponent} provided various inputs.
 */
public interface SpreadsheetPatternComponentContext extends CanGiveFocus,
        CrudHistoryTokenContext<SpreadsheetPattern>,
        HistoryTokenContext,
        ComponentLifecycleMatcher,
        LoggingContext {

    /**
     * The {@link SpreadsheetPatternKind} being edited.
     */
    SpreadsheetPatternKind patternKind();

    /**
     * Switches the editor to the given {@link SpreadsheetPatternKind}.
     */
    void setPatternKind(final SpreadsheetPatternKind patternKind);

    /**
     * Provides the UNDO or loaded text.
     */
    String loaded();

    /**
     * Adds a {@link HistoryTokenWatcher}.
     */
    Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher);

    /**
     * A {@link SpreadsheetFormatterContext} which will be used to format values for the samples table.
     */
    SpreadsheetFormatterContext spreadsheetFormatterContext();

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
