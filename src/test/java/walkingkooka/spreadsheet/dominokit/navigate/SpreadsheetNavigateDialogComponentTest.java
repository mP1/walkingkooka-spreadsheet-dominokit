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

package walkingkooka.spreadsheet.dominokit.navigate;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetNavigateDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetNavigateDialogComponent> {

    // with.............................................................................................................

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetNavigateDialogComponent.with(null)
        );
    }

    // navigate.........................................................................................................

    @Test
    public void testOnHistoryTokenWithSpreadsheetSelect() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );

        final SpreadsheetNavigateDialogComponent dialog = SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.navigate(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetNavigateDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Navigate\n" +
                "    id=navigate-Dialog includeClose=true CLOSED\n" +
                "      SpreadsheetCellReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [] id=navigate-home-TextBox\n" +
                "            Errors\n" +
                "              End of text, expected CELL\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=navigate-save-Link\n" +
                "            \"Undo\" DISABLED id=navigate-undo-Link\n" +
                "            \"Close\" DISABLED id=navigate-close-Link\n"
        );
    }

    private final static Optional<SpreadsheetViewportHomeNavigationList> MISSING_NAVIGATION_LIST = Optional.empty();

    @Test
    public void testOnHistoryTokenWithSpreadsheetNavigate() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.navigate(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                MISSING_NAVIGATION_LIST
            )
        );

        final SpreadsheetNavigateDialogComponent dialog = SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.navigate(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetNavigateDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Navigate\n" +
                "    id=navigate-Dialog includeClose=true\n" +
                "      SpreadsheetCellReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [] id=navigate-home-TextBox\n" +
                "            Errors\n" +
                "              End of text, expected CELL\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=navigate-save-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/navigate/A1] id=navigate-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1] id=navigate-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellNavigate() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.cellNavigate(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor(),
                MISSING_NAVIGATION_LIST
            )
        );

        final SpreadsheetNavigateDialogComponent dialog = SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.cellNavigate(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetNavigateDialogComponent\n" +
                "  DialogComponent\n" +
                "    B2: Navigate\n" +
                "    id=navigate-Dialog includeClose=true\n" +
                "      SpreadsheetCellReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [] id=navigate-home-TextBox\n" +
                "            Errors\n" +
                "              End of text, expected CELL\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=navigate-save-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/B2/navigate/A1] id=navigate-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2] id=navigate-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetColumnNavigate() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.columnNavigate(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseColumn("C")
                    .setDefaultAnchor(),
                MISSING_NAVIGATION_LIST
            )
        );

        final SpreadsheetNavigateDialogComponent dialog = SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.columnNavigate(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetNavigateDialogComponent\n" +
                "  DialogComponent\n" +
                "    C: Navigate\n" +
                "    id=navigate-Dialog includeClose=true\n" +
                "      SpreadsheetCellReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [] id=navigate-home-TextBox\n" +
                "            Errors\n" +
                "              End of text, expected CELL\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=navigate-save-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/column/C/navigate/A1] id=navigate-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/column/C] id=navigate-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetRowNavigate() {
        final TestAppContext context = new TestAppContext(
            HistoryToken.rowNavigate(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseRow("99")
                    .setDefaultAnchor(),
                MISSING_NAVIGATION_LIST
            )
        );

        final SpreadsheetNavigateDialogComponent dialog = SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.rowNavigate(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetNavigateDialogComponent\n" +
                "  DialogComponent\n" +
                "    99: Navigate\n" +
                "    id=navigate-Dialog includeClose=true\n" +
                "      SpreadsheetCellReferenceComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [] id=navigate-home-TextBox\n" +
                "            Errors\n" +
                "              End of text, expected CELL\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=navigate-save-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/row/99/navigate/A1] id=navigate-undo-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/row/99] id=navigate-close-Link\n"
        );
    }

    final static class TestAppContext extends FakeAppContext {

        TestAppContext(final HistoryToken historyToken) {
            this.historyToken = historyToken;
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return null;
        }

        @Override
        public HistoryToken historyToken() {
            return historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SPREADSHEET_ID
            ).set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                SPREADSHEET_NAME
            ).set(
                SpreadsheetMetadataPropertyName.VIEWPORT_HOME,
                SpreadsheetSelection.A1
            ).remove(SpreadsheetMetadataPropertyName.VIEWPORT_SELECTION);
        }

        @Override
        public void giveFocus(final Runnable focus) {
            // NOP
        }

        @Override
        public String toString() {
            return this.historyToken.toString();
        }
    }

    @Override
    public SpreadsheetNavigateDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetNavigateDialogComponent.with(
            new FakeSpreadsheetNavigateDialogComponentContext() {
                @Override
                public String dialogTitle() {
                    return "Title123";
                }

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public HistoryToken historyToken() {
                    return historyToken;
                }
            }
        );
    }

    @Override
    public void testShouldIgnoreWithSpreadsheetCellSelectHistoryToken() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        throw new UnsupportedOperationException();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetNavigateDialogComponent> type() {
        return SpreadsheetNavigateDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
