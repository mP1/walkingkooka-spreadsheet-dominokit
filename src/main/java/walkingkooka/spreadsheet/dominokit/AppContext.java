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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.Element;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;
import java.util.Set;

public interface AppContext extends Context {

    void addHistoryWatcher(final HistoryWatcher watcher);

    void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher);

    SpreadsheetDeltaFetcher spreadsheetDeltaFetcher();

    void fireSpreadsheetDelta(final SpreadsheetDelta delta);

    void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher);

    SpreadsheetMetadataFetcher spreadsheetMetadataFetcher();

    void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata);

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}.
     */
    SpreadsheetMetadata spreadsheetMetadata();

    /**
     * Returns the current history token.
     */
    HistoryToken historyToken();

    void pushHistoryToken(final HistoryToken token);

    /**
     * Getter that returns the ranges of the viewport window.
     */
    Set<SpreadsheetCellRange> viewportWindow();

    TextStyle viewportAll(final boolean selected);

    TextStyle viewportCell(final boolean selected);

    TextStyle viewportColumnHeader(final boolean selected);

    TextStyle viewportRowHeader(final boolean selected);

    /**
     * If the {@link SpreadsheetSelection} is present, the element will be given focus.
     */
    void giveViewportFocus(final SpreadsheetSelection selection);

    /**
     * Finds an existing {@link Element} for the given {@link SpreadsheetSelection}.
     */
    Optional<Element> findViewportElement(final SpreadsheetSelection selection);

    void debug(final Object message);

    void error(final Object message);
}
