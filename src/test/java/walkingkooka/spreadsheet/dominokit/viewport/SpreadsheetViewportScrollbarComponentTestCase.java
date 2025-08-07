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
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportRectangle;

import java.util.Objects;

public abstract class SpreadsheetViewportScrollbarComponentTestCase<R extends SpreadsheetColumnOrRowReference>
    implements ValueComponentTesting<HTMLDivElement, R, SpreadsheetViewportScrollbarComponent<R>>,
    TypeNameTesting<SpreadsheetViewportScrollbarComponent<R>>,
    SpreadsheetMetadataTesting {

    final static SpreadsheetId ID = SpreadsheetId.with(1);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName222");

    final static HistoryToken CELL_SELECT = HistoryToken.cellSelect(
        ID,
        NAME,
        SpreadsheetSelection.A1.setDefaultAnchor()
    );

    SpreadsheetViewportScrollbarComponentTestCase() {
        super();
    }

    final static double VIEWPORT_WIDTH = 1000;

    final static double VIEWPORT_HEIGHT = 500;

    static class TestAppContext extends FakeAppContext {
        TestAppContext(final SpreadsheetCellReference home,
                       final HistoryToken historyToken) {
            this.spreadsheetMetadata = SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.VIEWPORT,
                SpreadsheetViewportRectangle.with(
                    home,
                    VIEWPORT_WIDTH,
                    VIEWPORT_HEIGHT
                ).viewport()
            );
            this.historyToken = historyToken;
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.spreadsheetMetadata;
        }

        private final SpreadsheetMetadata spreadsheetMetadata;
    }

    static class TestSpreadsheetViewportScrollbarComponentContext extends FakeSpreadsheetViewportScrollbarComponentContext {
        TestSpreadsheetViewportScrollbarComponentContext(final HistoryToken token) {
            super();
            this.historyToken = Objects.requireNonNull(token, "token");
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return null;
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public double viewportWidth() {
            return VIEWPORT_WIDTH;
        }

        @Override
        public double viewportHeight() {
            return VIEWPORT_HEIGHT;
        }
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
