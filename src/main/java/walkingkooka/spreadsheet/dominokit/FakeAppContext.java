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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;
import java.util.Set;

public class FakeAppContext implements AppContext {
    @Override
    public void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fireSpreadsheetDelta(final SpreadsheetDelta delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addHistoryWatcher(final HistoryWatcher watcher) {
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
    public Set<SpreadsheetCellRange> viewportWindow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportAll(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportCell(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportColumnHeader(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyle viewportRowHeader(final boolean selected) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveViewportFocus(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Element> findViewportElement(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(final Object message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object message) {
        throw new UnsupportedOperationException();
    }
}
