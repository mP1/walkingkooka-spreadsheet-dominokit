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

import elemental2.dom.HTMLDivElement;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;

import java.util.Arrays;
import java.util.Objects;

public abstract class SpreadsheetViewportScrollbarComponentTestCase<R extends SpreadsheetColumnOrRowReference>
    implements ValueComponentTesting<HTMLDivElement, R, SpreadsheetViewportScrollbarComponent<R>>,
    TypeNameTesting<SpreadsheetViewportScrollbarComponent<R>>,
    SpreadsheetMetadataTesting {

    final static SpreadsheetCellReference HOME = SpreadsheetSelection.A1;

    final static SpreadsheetId ID = SpreadsheetId.with(1);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName222");

    final static HistoryToken SPREADSHEET_LIST = HistoryToken.spreadsheetListSelect(
        HistoryTokenOffsetAndCount.EMPTY
    );

    final static HistoryToken SPREADSHEET_SELECT = HistoryToken.spreadsheetSelect(
        ID,
        NAME
    );

    final static HistoryToken CELL_SELECT = HistoryToken.cellSelect(
        ID,
        NAME,
        SpreadsheetSelection.A1.setDefaultAnchor()
    );

    SpreadsheetViewportScrollbarComponentTestCase() {
        super();
    }

    final static int VIEWPORT_WIDTH = 1000;

    final static int VIEWPORT_HEIGHT = 500;

    static class TestAppContext extends FakeAppContext {
        TestAppContext(final SpreadsheetCellReference home,
                       final HistoryToken historyToken) {
            this.spreadsheetMetadata = SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                ID
            ).set(
                SpreadsheetMetadataPropertyName.VIEWPORT_HOME,
                home
            ).remove(
                SpreadsheetMetadataPropertyName.VIEWPORT_SELECTION
            );
            this.historyToken = historyToken;

            this.spreadsheetViewportCache.onSpreadsheetMetadata(
                this.spreadsheetMetadata,
                this
            );

            this.spreadsheetViewportCache.setWindows(
                SpreadsheetViewportWindows.parse("A1:C3")
            );
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            this.historyTokenWatchers.add(watcher);
            return null;
        }

        private HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return null;
        }

        @Override
        public void pushHistoryToken(final HistoryToken token) {
            final HistoryToken previous = this.historyToken;
            this.historyToken = token;

            this.historyTokenWatchers.onHistoryTokenChange(
                previous,
                this
            );
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private HistoryToken historyToken;

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.spreadsheetMetadata;
        }

        private final SpreadsheetMetadata spreadsheetMetadata;

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.spreadsheetViewportCache;
        }

        private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            System.out.println(
                Arrays.toString(values)
            );
        }
    }

    static class TestSpreadsheetViewportScrollbarComponentContext extends FakeSpreadsheetViewportScrollbarComponentContext {
        TestSpreadsheetViewportScrollbarComponentContext(final TestAppContext context) {
            super();
            this.context = Objects.requireNonNull(context, "context");
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.context.addHistoryTokenWatcher(watcher);
        }

        @Override
        public HistoryToken historyToken() {
            return this.context.historyToken();
        }

        @Override
        public int viewportGridWidth() {
            return VIEWPORT_WIDTH;
        }

        @Override
        public int viewportGridHeight() {
            return VIEWPORT_HEIGHT;
        }

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.context.spreadsheetMetadata();
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.context.spreadsheetViewportCache();
        }

        private final AppContext context;
    }

    @Override
    public final String typeNamePrefix() {
        return SpreadsheetViewportScrollbarComponent.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
