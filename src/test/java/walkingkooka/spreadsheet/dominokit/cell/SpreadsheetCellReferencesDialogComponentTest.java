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

package walkingkooka.spreadsheet.dominokit.cell;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetCellReferencesDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetCellReferencesDialogComponent,
    SpreadsheetCellReferencesDialogComponentContext>,
    SpreadsheetMetadataTesting {

    // refresh..........................................................................................................

    @Test
    public void testRefreshEmptyTable() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/references"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellReferencesDialogComponent dialog = SpreadsheetCellReferencesDialogComponent.with(
            new TestSpreadsheetCellReferencesDialogComponentContext(context)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellReferencesDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Cell References\n" +
                "    id=references-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        SpreadsheetCard\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=references-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=references-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=references-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=references-close-Link\n"
        );
    }

    @Test
    public void testRefreshNonEmptyTable() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/references"
        );

        final TestAppContext context = this.appContext(historyToken);

        final SpreadsheetCellReferencesDialogComponent dialog = SpreadsheetCellReferencesDialogComponent.with(
            new TestSpreadsheetCellReferencesDialogComponentContext(context)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellReferencesDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Cell References\n" +
                "    id=references-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        SpreadsheetCard\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=references-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=references-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=references-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=references-close-Link\n"
        );

        context.deltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/123/cell/A1/"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1+2")
                    )
                )
            ).setReferences(
                Maps.of(
                    SpreadsheetSelection.A1,
                    Sets.of(
                        SpreadsheetSelection.parseCell("B2"),
                        SpreadsheetSelection.parseCell("C3")
                    )
                )
            ),
            context
        );

        this.treePrintAndCheck(
            dialog,
            "SpreadsheetCellReferencesDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Cell References\n" +
                "    id=references-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        SpreadsheetCard\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=references-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"A1\" [#/123/SpreadsheetName456/cell/A1] id=references-cells-A1-Link\n" +
                "                  \"A1\" [#/123/SpreadsheetName456/cell/A1/formula] id=references-cells-A1-formula-Link\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"\"\n" +
                "                  SpreadsheetTextNodeComponent\n" +
                "                  SpreadsheetCellLinksComponent\n" +
                "                    SpreadsheetLinkListComponent\n" +
                "                      SpreadsheetFlexLayout\n" +
                "                        ROW\n" +
                "                          \"Create Label\" [#/123/SpreadsheetName456/cell/A1/label] id=references-cells-A1-createLabel-Link\n" +
                "                          \"Labels\" [#/123/SpreadsheetName456/cell/A1/labels] (0) id=references-cells-A1-label-Link\n" +
                "                          \"References\" [#/123/SpreadsheetName456/cell/A1/references] (0) id=references-cells-A1-references-Link\n" +
                "                          \"Delete\" [#/123/SpreadsheetName456/cell/A1/delete] id=references-cells-A1-delete-Link\n" +
                "              CHILDREN\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=references-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=references-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=references-close-Link\n"
        );
    }

    private TestAppContext appContext(final HistoryToken historyToken) {
        return new TestAppContext(historyToken);
    }

    class TestAppContext extends FakeAppContext {

        TestAppContext(final HistoryToken historyToken) {
            this.historyToken = historyToken;
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.deltaFetcherWatchers.add(watcher);
        }

        // necessary so some tests can fire a SpreadsheetDeltaFetcherWatcher#onSpreadsheetDelta
        final SpreadsheetDeltaFetcherWatchers deltaFetcherWatchers = SpreadsheetDeltaFetcherWatchers.empty();

        @Override
        public void giveFocus(final Runnable focus) {
            // ignore
        }

        @Override
        public HistoryToken historyToken() {
            return historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return this.metadataFetcherWatchers.add(watcher);
        }

        final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers = SpreadsheetMetadataFetcherWatchers.empty();

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetId.parse("123")
            );
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.cache;
        }

        private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            System.out.println(Arrays.toString(values));
        }
    }

    static class TestSpreadsheetCellReferencesDialogComponentContext extends FakeSpreadsheetCellReferencesDialogComponentContext {

        TestSpreadsheetCellReferencesDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
        }

        @Override
        public HistoryToken historyToken() {
            return this.context.historyToken();
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.context.spreadsheetMetadata();
        }

        @Override
        public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .cellLabels(spreadsheetExpressionReference);
        }

        @Override
        public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .cellReferences(spreadsheetExpressionReference);
        }

        @Override
        public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .formulaText(spreadsheetExpressionReference);
        }

        private final AppContext context;

        @Override
        public void loadCellReferences(final SpreadsheetId id,
                                       final SpreadsheetCellRangeReference cells,
                                       final HistoryTokenOffsetAndCount offsetAndCount) {
            // NOP
        }
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellReferencesDialogComponent> type() {
        return SpreadsheetCellReferencesDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
