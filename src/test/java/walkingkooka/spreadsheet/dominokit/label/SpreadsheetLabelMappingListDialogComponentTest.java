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

package walkingkooka.spreadsheet.dominokit.label;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLabelListHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingListHistoryToken;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

public final class SpreadsheetLabelMappingListDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetLabelMappingListDialogComponent>,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetLabelName A1LABEL = SpreadsheetSelection.labelName("A1LABEL");

    private final static SpreadsheetCell A1CELL = SpreadsheetSelection.A1.setFormula(
        SpreadsheetFormula.EMPTY.setText("=1+2")
    );

    private final static SpreadsheetMetadata METADATA = SpreadsheetMetadataTesting.METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetLabelMappingListHistoryTokenAndEmptyTable() {
        final SpreadsheetLabelMappingListHistoryToken historyToken = HistoryToken.labelMappingList(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            HistoryTokenOffsetAndCount.EMPTY
        );

        final TestAppContext context = this.appContext(historyToken);

        final SpreadsheetLabelMappingListDialogComponent dialog = SpreadsheetLabelMappingListDialogComponent.with(
            new TestSpreadsheetLabelMappingListDialogComponentContext(context)
        );

        context.metadataFetcherWatchers.onSpreadsheetMetadata(
            METADATA,
            context
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=labels-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetLabelMappingListHistoryTokenAndNotEmptyTable() {
        final SpreadsheetLabelMappingListHistoryToken historyToken = HistoryToken.labelMappingList(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            HistoryTokenOffsetAndCount.EMPTY
        );

        final TestAppContext context = this.appContext(historyToken);

        final SpreadsheetLabelMappingListDialogComponent dialog = SpreadsheetLabelMappingListDialogComponent.with(
            new TestSpreadsheetLabelMappingListDialogComponentContext(context)
        );

        context.metadataFetcherWatchers.onSpreadsheetMetadata(
            METADATA,
            context
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=labels-close-Link\n"
        );

        context.deltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/1/label/*/"),
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    A1LABEL.setLabelMappingReference(SpreadsheetSelection.A1),
                    SpreadsheetSelection.labelName("B2LABEL")
                        .setLabelMappingReference(SpreadsheetSelection.parseCell("B2"))
                )
            ).setCells(
                Sets.of(A1CELL)
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
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"A1LABEL\" [#/1/SpreadsheetName1/label/A1LABEL] id=labels-labels-A1LABEL-Link\n" +
                "                  \"=1+2\" [#/1/SpreadsheetName1/cell/A1LABEL/formula] id=labels-labels-A1LABEL-formula-Link\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetLabelLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"References\" [#/1/SpreadsheetName1/cell/A1LABEL/references] (2) id=labels-labels-A1LABEL-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName1/label/A1LABEL/delete] id=labels-labels-A1LABEL-delete-Link\n" +
                "                ROW 1\n" +
                "                  \"B2LABEL\" [#/1/SpreadsheetName1/label/B2LABEL] id=labels-labels-B2LABEL-Link\n" +
                "                  \"B2\" [#/1/SpreadsheetName1/cell/B2LABEL/formula] id=labels-labels-B2LABEL-formula-Link\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetLabelLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"References\" [#/1/SpreadsheetName1/cell/B2LABEL/references] (0) id=labels-labels-B2LABEL-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName1/label/B2LABEL/delete] id=labels-labels-B2LABEL-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=labels-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetLabelMappingListHistoryTokenWithOffset() {
        final SpreadsheetLabelMappingListHistoryToken historyToken = HistoryToken.labelMappingList(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            HistoryTokenOffsetAndCount.EMPTY.setOffset(OptionalInt.of(1))
        );

        final TestAppContext context = this.appContext(historyToken);

        final SpreadsheetLabelMappingListDialogComponent dialog = SpreadsheetLabelMappingListDialogComponent.with(
            new TestSpreadsheetLabelMappingListDialogComponentContext(context)
        );

        context.metadataFetcherWatchers.onSpreadsheetMetadata(
            METADATA,
            context
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" [#/1/SpreadsheetName1/label/*/offset/2] id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=labels-close-Link\n"
        );

        context.deltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/1/label/*/offset/1"),
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    //A1LABEL.setLabelMappingReference(SpreadsheetSelection.A1), offset=1
                    SpreadsheetSelection.labelName("B2LABEL")
                        .setLabelMappingReference(SpreadsheetSelection.parseCell("B2")),
                    SpreadsheetSelection.labelName("C3LABEL")
                        .setLabelMappingReference(SpreadsheetSelection.parseCell("C3"))
                )
            ).setCells(
                Sets.of(A1CELL)
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
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"B2LABEL\" [#/1/SpreadsheetName1/label/B2LABEL] id=labels-labels-B2LABEL-Link\n" +
                "                  \"B2\" [#/1/SpreadsheetName1/cell/B2LABEL/formula] id=labels-labels-B2LABEL-formula-Link\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetLabelLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"References\" [#/1/SpreadsheetName1/cell/B2LABEL/references] (0) id=labels-labels-B2LABEL-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName1/label/B2LABEL/delete] id=labels-labels-B2LABEL-delete-Link\n" +
                "                ROW 1\n" +
                "                  \"C3LABEL\" [#/1/SpreadsheetName1/label/C3LABEL] id=labels-labels-C3LABEL-Link\n" +
                "                  \"C3\" [#/1/SpreadsheetName1/cell/C3LABEL/formula] id=labels-labels-C3LABEL-formula-Link\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetLabelLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"References\" [#/1/SpreadsheetName1/cell/C3LABEL/references] (0) id=labels-labels-C3LABEL-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName1/label/C3LABEL/delete] id=labels-labels-C3LABEL-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" [#/1/SpreadsheetName1/label/*/offset/2] id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=labels-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellLabelListHistoryToken() {
        final SpreadsheetCellLabelListHistoryToken historyToken = HistoryToken.cellLabels(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            HistoryTokenOffsetAndCount.EMPTY
        );

        final TestAppContext context = this.appContext(historyToken);

        final SpreadsheetLabelMappingListDialogComponent dialog = SpreadsheetLabelMappingListDialogComponent.with(
            new TestSpreadsheetLabelMappingListDialogComponentContext(context)
        );

        context.metadataFetcherWatchers.onSpreadsheetMetadata(
            METADATA,
            context
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=labels-close-Link\n"
        );

        context.deltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/1/label/*/offset/1"),
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    //A1LABEL.setLabelMappingReference(SpreadsheetSelection.A1), offset=1
                    SpreadsheetSelection.labelName("B2LABEL")
                        .setLabelMappingReference(SpreadsheetSelection.parseCell("B2")),
                    SpreadsheetSelection.labelName("C3LABEL")
                        .setLabelMappingReference(SpreadsheetSelection.parseCell("C3"))
                )
            ).setCells(
                Sets.of(A1CELL)
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
            "SpreadsheetLabelMappingListDialogComponent\n" +
                "  DialogComponent\n" +
                "    Labels\n" +
                "    id=labels-Dialog includeClose=true\n" +
                "      SpreadsheetDeltaLabelsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=labels-labels-Table\n" +
                "              COLUMN(S)\n" +
                "                Label\n" +
                "                Cell\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"B2LABEL\" [#/1/SpreadsheetName1/label/B2LABEL] id=labels-labels-B2LABEL-Link\n" +
                "                  \"B2\" [#/1/SpreadsheetName1/cell/B2LABEL/formula] id=labels-labels-B2LABEL-formula-Link\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetLabelLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"References\" [#/1/SpreadsheetName1/cell/B2LABEL/references] (0) id=labels-labels-B2LABEL-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName1/label/B2LABEL/delete] id=labels-labels-B2LABEL-delete-Link\n" +
                "                ROW 1\n" +
                "                  \"C3LABEL\" [#/1/SpreadsheetName1/label/C3LABEL] id=labels-labels-C3LABEL-Link\n" +
                "                  \"C3\" [#/1/SpreadsheetName1/cell/C3LABEL/formula] id=labels-labels-C3LABEL-formula-Link\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetLabelLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"References\" [#/1/SpreadsheetName1/cell/C3LABEL/references] (0) id=labels-labels-C3LABEL-references-Link\n" +
                "                          \"Delete\" [#/1/SpreadsheetName1/label/C3LABEL/delete] id=labels-labels-C3LABEL-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=labels-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=labels-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Create\" [#/1/SpreadsheetName1/create-label] id=labels-create-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=labels-close-Link\n"
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
            return METADATA;
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.cache;
        }

        @Override
        public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
            return this.cache.resolveLabel(spreadsheetLabelName);
        }

        private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            System.out.println(Arrays.toString(values));
        }
    }

    static class TestSpreadsheetLabelMappingListDialogComponentContext extends FakeSpreadsheetLabelMappingListDialogComponentContext {

        TestSpreadsheetLabelMappingListDialogComponentContext(final AppContext context) {
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
        public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .cellReferences(spreadsheetExpressionReference);
        }

        @Override
        public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .formulaText(spreadsheetExpressionReference);
        }

        @Override
        public Optional<SpreadsheetCell> labelCell(final SpreadsheetLabelName label) {
            return Optional.ofNullable(
                A1LABEL.equals(label) ?
                    A1CELL :
                    null
            );
        }

        @Override
        public void loadLabelMappings(final SpreadsheetId id,
                                      final HistoryTokenOffsetAndCount offsetAndCount) {
            // NOP
        }

        @Override
        public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
            return this.context.resolveLabel(spreadsheetLabelName);
        }

        private final AppContext context;

        @Override
        public boolean shouldIgnore(final HistoryToken token) {
            return false;
        }

        @Override
        public boolean isMatch(final HistoryToken token) {
            return token instanceof SpreadsheetLabelMappingListHistoryToken ||
                token instanceof SpreadsheetCellLabelListHistoryToken;
        }
    }

    @Override
    public SpreadsheetLabelMappingListDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetLabelMappingListDialogComponent.with(
            new TestSpreadsheetLabelMappingListDialogComponentContext(
                new TestAppContext(historyToken)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetLabelMappingListDialogComponent> type() {
        return SpreadsheetLabelMappingListDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
