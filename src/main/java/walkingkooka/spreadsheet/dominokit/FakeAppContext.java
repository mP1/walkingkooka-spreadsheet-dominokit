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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public class FakeAppContext implements AppContext {

    @Override
    public Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetLabelMappingWatcher(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetLabelMappingWatcherOnce(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetLabelMappingFetcher spreadsheetLabelMappingFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetMetadataWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    // json.............................................................................................................

    @Override
    public JsonNodeMarshallContext marshallContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNodeUnmarshallContext unmarshallContext() {
        throw new UnsupportedOperationException();
    }

    // HistoryToken.....................................................................................................

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryToken historyToken() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fireCurrentHistoryToken() {
        throw new UnsupportedOperationException();
    }

    // UI...............................................................................................................

    @Override
    public TextStyle selectedIconStyle() {
        throw new UnsupportedOperationException();
    }

    // viewport.........................................................................................................

    @Override
    public SpreadsheetViewportCache viewportCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetCell> viewportCell(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportAllStyle(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportCellStyle(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportColumnHeaderStyle(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportRowHeaderStyle(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(final Runnable focus) {
        throw new UnsupportedOperationException();
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }
}
