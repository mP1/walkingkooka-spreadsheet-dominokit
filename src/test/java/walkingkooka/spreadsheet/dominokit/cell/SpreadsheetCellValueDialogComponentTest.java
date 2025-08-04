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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.datetime.DateComponent;
import walkingkooka.spreadsheet.dominokit.datetime.SpreadsheetDateTimeComponent;
import walkingkooka.spreadsheet.dominokit.datetime.SpreadsheetTimeComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueHistoryToken;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.validation.ValidationValueTypeName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class SpreadsheetCellValueDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetCellValueDialogComponent<LocalDate>>,
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
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndNoDateValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/date"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<LocalDate> dialog = SpreadsheetCellValueDialogComponent.with(
            DateComponent.empty(
                DATE_COMPONENT_ID,
                CLEAR_VALUE
            ),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.DATE,
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  DialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      DateComponent\n" +
                "        [1999-12-31] id=Test123date-Date\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=Test123-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/date/save/] id=Test123-clear-Link\n" +
                "            \"Today\" [#/1/SpreadsheetName456/cell/A1/value/date/save/today] id=Test123-today-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/date/save/] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndDateValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/date"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<LocalDate> dialog = SpreadsheetCellValueDialogComponent.with(
            DateComponent.empty(
                DATE_COMPONENT_ID,
                CLEAR_VALUE
            ),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.DATE,
                Optional.of(
                    LocalDate.of(
                        2025,
                        6,
                        6
                    )
                ),
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  DialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      DateComponent\n" +
                "        [2025-06-06] id=Test123date-Date\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName456/cell/A1/value/date/save/%222025-06-06%22] id=Test123-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/date/save/] id=Test123-clear-Link\n" +
                "            \"Today\" [#/1/SpreadsheetName456/cell/A1/value/date/save/today] id=Test123-today-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/date/save/%222025-06-06%22] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndDateTimeValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/dateTime"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<LocalDateTime> dialog = SpreadsheetCellValueDialogComponent.with(
            SpreadsheetDateTimeComponent.empty(
                DATE_COMPONENT_ID,
                () -> LocalDateTime.of(
                    CLEAR_VALUE.get(),
                    LocalTime.MIN
                )
            ),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.DATE_TIME,
                Optional.of(
                    LocalDateTime.of(
                        LocalDate.of(
                            2025,
                            6,
                            6
                        ),
                        LocalTime.of(
                            12,
                            58,
                            59
                        )
                    )
                ),
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  DialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      SpreadsheetDateTimeComponent\n" +
                "        [2025-06-06T12:58:59] id=Test123date-Date\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName456/cell/A1/value/dateTime/save/%222025-06-06T12:58:59%22] id=Test123-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/dateTime/save/] id=Test123-clear-Link\n" +
                "            \"Now\" [#/1/SpreadsheetName456/cell/A1/value/dateTime/save/now] id=Test123-now-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/dateTime/save/%222025-06-06T12:58:59%22] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndTextValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/text"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<String> dialog = SpreadsheetCellValueDialogComponent.with(
            TextBoxComponent.empty()
                .setId("TextBox-Text"),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.TEXT,
                Optional.of("HelloTextValue"),
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  DialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      TextBoxComponent\n" +
                "        [HelloTextValue] id=TextBox-Text\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName456/cell/A1/value/text/save/%22HelloTextValue%22] id=Test123-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/text/save/] id=Test123-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/text/save/%22HelloTextValue%22] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndTextValueEmpty() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/text"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<String> dialog = SpreadsheetCellValueDialogComponent.with(
            TextBoxComponent.empty()
                .setId("TextBox-Text"),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.TEXT,
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  DialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      TextBoxComponent\n" +
                "        [] id=TextBox-Text\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=Test123-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/text/save/] id=Test123-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/text/save/] id=Test123-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName456/cell/A1] id=Test123-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithSpreadsheetCellValueHistoryTokenAndTimeValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/1/SpreadsheetName456/cell/A1/value/time"
        );

        final AppContext context = this.appContext(historyToken);

        final SpreadsheetCellValueDialogComponent<LocalTime> dialog = SpreadsheetCellValueDialogComponent.with(
            SpreadsheetTimeComponent.empty(
                "TestTime123",
                () -> LocalTime.MIN
            ),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.TIME,
                Optional.of(
                    LocalTime.of(
                        12,
                        58,
                        59
                    )
                ),
                context
            )
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetCellValueDialogComponent\n" +
                "  DialogComponent\n" +
                "    HelloDialogTitle\n" +
                "    id=Test123-Dialog includeClose=true\n" +
                "      SpreadsheetTimeComponent\n" +
                "        [12:58:59] id=TestTime123\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName456/cell/A1/value/time/save/%2212:58:59%22] id=Test123-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName456/cell/A1/value/time/save/] id=Test123-clear-Link\n" +
                "            \"Now\" [#/1/SpreadsheetName456/cell/A1/value/time/save/now] id=Test123-now-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName456/cell/A1/value/time/save/%2212:58:59%22] id=Test123-undo-Link\n" +
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

    static class TestSpreadsheetCellValueDialogComponentContext<T> extends FakeSpreadsheetCellValueDialogComponentContext<T> {

        TestSpreadsheetCellValueDialogComponentContext(final ValidationValueTypeName valueType,
                                                       final AppContext context) {
            this(
                valueType,
                Optional.empty(),
                context
            );
        }

        TestSpreadsheetCellValueDialogComponentContext(final ValidationValueTypeName valueType,
                                                       final Optional<T> value,
                                                       final AppContext context) {
            this.valueType = valueType;
            this.cell = Objects.requireNonNull(value, "value")
                .map(v ->
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setValue(
                            Cast.to(
                                Optional.of(v)
                            )
                        )
                    )
                );
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
        public ValidationValueTypeName valueType() {
            return this.valueType;
        }

        private final ValidationValueTypeName valueType;

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.watchers.add(watcher);
        }

        private final SpreadsheetDeltaFetcherWatchers watchers = SpreadsheetDeltaFetcherWatchers.empty();

        @Override
        public Optional<SpreadsheetCell> cell() {
            return this.cell;
        }

        private final Optional<SpreadsheetCell> cell;

        @Override
        public String toHistoryTokenSaveStringValue(final Optional<T> value) {
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
            DateComponent.empty(
                DATE_COMPONENT_ID,
                CLEAR_VALUE
            ),
            new TestSpreadsheetCellValueDialogComponentContext<>(
                ValidationValueTypeName.DATE,
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
