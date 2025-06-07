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
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetDateComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.validation.ValidationValueTypeName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class SpreadsheetCellValueDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetCellValueDialogComponent<LocalDate>>,
    SpreadsheetMetadataTesting {

    private static final String DATE_COMPONENT_ID = "Test123date-Date";

    private static final Supplier<LocalDate> CLEAR_VALUE = () -> LocalDate.of(
        1999,
        12,
        31
    );

    // isMatch..........................................................................................................

    @Test
    public void testShouldIgnoreSpreadsheetCellValueSaveHistoryToken() {
        final SpreadsheetCellValueHistoryToken historyToken = HistoryToken.cellValueSave(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            ValidationValueTypeName.DATE,
            ""
        );

        this.shouldIgnoreAndCheck(
            this.createSpreadsheetDialogComponentLifecycle(historyToken),
            historyToken,
            true
        );
    }

    @Test
    public void testShouldIgnoreSpreadsheetCellSelectHistoryToken() {
        final SpreadsheetCellSelectHistoryToken historyToken = HistoryToken.cellSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        this.shouldIgnoreAndCheck(
            this.createSpreadsheetDialogComponentLifecycle(historyToken),
            historyToken,
            false
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellValueHistoryToken() {
        final SpreadsheetCellValueHistoryToken historyToken = HistoryToken.cellValueSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            ValidationValueTypeName.DATE
        );

        this.isMatchAndCheck(
            this.createSpreadsheetDialogComponentLifecycle(historyToken),
            historyToken,
            true
        );
    }

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndNoValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/date"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<LocalDate> dialog = SpreadsheetCellValueDialogComponent.with(
            SpreadsheetDateComponent.empty(
                DATE_COMPONENT_ID,
                CLEAR_VALUE
            ),
            new TestSpreadsheetCellValueDialogComponentContext(
                Optional.empty(),
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      SpreadsheetDateComponent\n" +
                "        [1999-12-31] id=Test123date-Date\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/date/save/] id=Test123-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/date/save/] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/date"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<LocalDate> dialog = SpreadsheetCellValueDialogComponent.with(
            SpreadsheetDateComponent.empty(
                DATE_COMPONENT_ID,
                CLEAR_VALUE
            ),
            new TestSpreadsheetCellValueDialogComponentContext(
                Optional.of(
                    LocalDate.of(
                        2025,
                        06,
                        06
                    )
                ),
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      SpreadsheetDateComponent\n" +
                "        [1999-12-31] id=Test123date-Date\n" + // TODO SpreadsheetDelta refresh
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/date/save/] id=Test123-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/date/save/%222025-06-06%22] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
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
                SPREADSHEET_ID
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

    static class TestSpreadsheetCellValueDialogComponentContext extends FakeSpreadsheetCellValueDialogComponentContext<LocalDate> {

        TestSpreadsheetCellValueDialogComponentContext(final AppContext context) {
            this(
                Optional.empty(),
                context
            );
        }

        TestSpreadsheetCellValueDialogComponentContext(final Optional<LocalDate> value,
                                                       final AppContext context) {
            this.value = Objects.requireNonNull(value, "value");
            this.context = context;
        }

        @Override
        public String id() {
            return "Test123";
        }

        @Override
        public String dialogTitle() {
            return "HelloDialogTitle";
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
        public boolean isMatch(final ValidationValueTypeName valueType) {
            return ValidationValueTypeName.DATE.equals(valueType);
        }

        @Override
        public Optional<LocalDate> value() {
            return this.value;
        }

        private final Optional<LocalDate> value;

        @Override
        public String prepareSaveValue(final Optional<LocalDate> value) {
            Objects.requireNonNull(value, "value");

            final JsonNode json = JsonNodeMarshallContexts.basic()
                .marshallOptional(value);
            return json.isNull() ?
                "" :
                json.toString();
        }

        private final AppContext context;
    }

    @Override
    public SpreadsheetCellValueDialogComponent<LocalDate> createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetCellValueDialogComponent.with(
            SpreadsheetDateComponent.empty(
                DATE_COMPONENT_ID,
                CLEAR_VALUE
            ),
            new TestSpreadsheetCellValueDialogComponentContext(
                this.appContext(historyToken)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellValueDialogComponent<LocalDate>> type() {
        return Cast.to(SpreadsheetCellValueDialogComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
