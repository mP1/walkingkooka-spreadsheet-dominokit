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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellReferenceListHistoryToken;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.ValueType;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetCellReferencesDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetCellReferencesDialogComponent>,
    SpreadsheetMetadataTesting {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellReferencesHistoryToken() {
        final SpreadsheetCellReferenceListHistoryToken historyToken = HistoryToken.cellReferences(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            HistoryTokenOffsetAndCount.EMPTY
        );

        this.isMatchAndCheck(
            SpreadsheetCellReferencesDialogComponent.with(
                new TestSpreadsheetCellReferencesDialogComponentContext(
                    this.appContext(historyToken)
                )
            ),
            historyToken,
            true
        );
    }

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChangeRefreshEmptyTable() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/references"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellReferencesDialogComponent dialog = SpreadsheetCellReferencesDialogComponent.with(
            new TestSpreadsheetCellReferencesDialogComponentContext(context)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellReferencesDialogComponent\n" +
                "  DialogComponent\n" +
                "    Cell References\n" +
                "    id=SpreadsheetCellReferences-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellReferences-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellReferences-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellReferences-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=SpreadsheetCellReferences-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeRefreshNonEmptyTable() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/references"
        );

        final TestAppContext context = this.appContext(historyToken);

        final SpreadsheetCellReferencesDialogComponent dialog = SpreadsheetCellReferencesDialogComponent.with(
            new TestSpreadsheetCellReferencesDialogComponentContext(context)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellReferencesDialogComponent\n" +
                "  DialogComponent\n" +
                "    Cell References\n" +
                "    id=SpreadsheetCellReferences-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellReferences-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellReferences-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellReferences-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=SpreadsheetCellReferences-close-Link\n"
        );

        context.metadataFetcherWatchers.onSpreadsheetMetadata(
            context.spreadsheetMetadata()
        );

        context.deltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/1/cell/A1/"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1+2")
                            .setValueType(
                                Optional.of(ValueType.TEXT)
                            )
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
            )
        );

        this.treePrintAndCheck(
            dialog,
            "SpreadsheetCellReferencesDialogComponent\n" +
                "  DialogComponent\n" +
                "    Cell References\n" +
                "    id=SpreadsheetCellReferences-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellReferences-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"A1\" [#/1/SpreadsheetName456/cell/A1] id=SpreadsheetCellReferences-cells-A1-Link\n" +
                "                  \"A1\" [#/1/SpreadsheetName456/cell/A1/formula] id=SpreadsheetCellReferences-cells-A1-formula-Link\n" +
                "                  TextComponent\n" +
                "                    \"\"\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetCellLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"Value\" [#/1/SpreadsheetName456/cell/A1/value/text] id=SpreadsheetCellReferences-cells-A1-value-Link\n" +
                "                          \"Create Label\" [#/1/SpreadsheetName456/cell/A1/label] id=SpreadsheetCellReferences-cells-A1-createLabel-Link\n" +
                "                          \"Labels\" [#/1/SpreadsheetName456/cell/A1/labels] (0) id=SpreadsheetCellReferences-cells-A1-label-Link\n" +
                "                          \"References\" [#/1/SpreadsheetName456/cell/A1/references] (2) id=SpreadsheetCellReferences-cells-A1-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName456/cell/A1/delete] id=SpreadsheetCellReferences-cells-A1-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellReferences-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellReferences-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=SpreadsheetCellReferences-close-Link\n"
        );
    }

    private TestAppContext appContext(final HistoryToken historyToken) {
        return new TestAppContext(historyToken);
    }

    static class TestAppContext extends FakeAppContext {

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
            return METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetCellReferencesDialogComponentTest.SPREADSHEET_ID
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
        public void debug(final Object... values) {
            System.out.println(Arrays.toString(values));
        }
    }

    static class TestSpreadsheetCellReferencesDialogComponentContext extends FakeSpreadsheetCellReferencesDialogComponentContext {

        TestSpreadsheetCellReferencesDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public String dialogTitle() {
            return "Cell References";
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
        public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
            return this.context.spreadsheetViewportCache()
                .cell(selection);
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

    @Override
    public SpreadsheetCellReferencesDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetCellReferencesDialogComponent.with(
            new TestSpreadsheetCellReferencesDialogComponentContext(
                this.appContext(historyToken)
            )
        );
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
