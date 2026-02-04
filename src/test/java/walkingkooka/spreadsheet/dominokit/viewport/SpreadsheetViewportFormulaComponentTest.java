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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.text.LineEnding;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetViewportFormulaComponentTest implements HtmlComponentTesting<SpreadsheetViewportFormulaComponent, HTMLFieldSetElement>,
    ComponentLifecycleMatcherTesting,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);
    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("Name222");

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        this.isMatchAndCheck(
            SpreadsheetViewportFormulaComponent.with(
                new TestSpreadsheetViewportFormulaComponentContext()
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellFormulaSelectHistoryToken() {
        this.isMatchAndCheck(
            SpreadsheetViewportFormulaComponent.with(
                new TestSpreadsheetViewportFormulaComponentContext()
            ),
            HistoryToken.cellFormula(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellFormulaSaveHistoryToken() {
        this.isMatchAndCheck(
            SpreadsheetViewportFormulaComponent.with(
                new TestSpreadsheetViewportFormulaComponentContext()
            ),
            HistoryToken.cellFormulaSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                "=1"
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetColumnSelectHistoryToken() {
        this.isMatchAndCheck(
            SpreadsheetViewportFormulaComponent.with(
                new TestSpreadsheetViewportFormulaComponentContext()
            ),
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRowSelectHistoryToken() {
        this.isMatchAndCheck(
            SpreadsheetViewportFormulaComponent.with(
                new TestSpreadsheetViewportFormulaComponentContext()
            ),
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testRefresh() {

        final TestSpreadsheetViewportFormulaComponentContext context = new TestSpreadsheetViewportFormulaComponentContext();

        final SpreadsheetViewportFormulaComponent component = SpreadsheetViewportFormulaComponent.with(context);

        context.spreadsheetMetadataFetcherWatchers.onSpreadsheetMetadata(
            context.spreadsheetMetadata()
        );

        context.historyTokenWatchers.onHistoryTokenChange(
            context.historyToken(),
            context
        );

        context.spreadsheetDeltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/1/cell/"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1+2")
                    )
                )
            )
        );

        component.refresh(context);

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportFormulaComponent\n" +
                "  SpreadsheetFormulaComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [=1+2]\n"
        );
    }

    static class TestSpreadsheetViewportFormulaComponentContext extends FakeAppContext implements SpreadsheetViewportFormulaComponentContext {

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.spreadsheetDeltaFetcherWatchers.add(watcher);
        }

        private final SpreadsheetDeltaFetcherWatchers spreadsheetDeltaFetcherWatchers = SpreadsheetDeltaFetcherWatchers.empty();

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return this.spreadsheetMetadataFetcherWatchers.add(watcher);
        }

        private final SpreadsheetMetadataFetcherWatchers spreadsheetMetadataFetcherWatchers = SpreadsheetMetadataFetcherWatchers.empty();

        @Override
        public HistoryToken historyToken() {
            return HistoryToken.cellFormula(
                SpreadsheetViewportFormulaComponentTest.SPREADSHEET_ID,
                SPREADSHEET_NAME,
                AnchoredSpreadsheetSelection.with(
                    SpreadsheetSelection.A1,
                    SpreadsheetViewportAnchor.NONE
                )
            );
        }

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetViewportFormulaComponentTest.SPREADSHEET_ID
            ).set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                SPREADSHEET_NAME
            );
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.cache;
        }

        private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

        @Override
        public void giveFocus(final Runnable focus) {
            // nop
        }

        @Override
        public void debug(final Object... values) {
            System.out.println(Arrays.toString(values));
        }

        @Override
        public void warn(final Object... values) {
            System.out.println(Arrays.toString(values));
        }

        @Override
        public TestSpreadsheetViewportFormulaComponentContext cloneEnvironment() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TestSpreadsheetViewportFormulaComponentContext setEnvironmentContext(final EnvironmentContext environmentContext) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                            final T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLineEnding(final LineEnding lineEnding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLocale(final Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setUser(final Optional<EmailAddress> user) {
            throw new UnsupportedOperationException();
        }
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportFormulaComponent> type() {
        return SpreadsheetViewportFormulaComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
