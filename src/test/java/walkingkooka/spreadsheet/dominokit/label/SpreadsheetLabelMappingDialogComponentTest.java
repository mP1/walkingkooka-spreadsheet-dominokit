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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;

public final class SpreadsheetLabelMappingDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetLabelMappingDialogComponent> {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetLabelMappingCreateHistoryToken() {
        this.isMatchAndCheck2(
            HistoryToken.labelMappingCreate(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetLabelMappingDeleteHistoryToken() {
        this.isMatchAndCheck2(
            HistoryToken.labelMappingDelete(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.labelName("HelloLabel")
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetLabelMappingListHistoryToken() {
        this.isMatchAndCheck2(
            HistoryToken.labelMappingList(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                HistoryTokenOffsetAndCount.EMPTY
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellLabelSaveHistoryToken() {
        this.isMatchAndCheck2(
            HistoryToken.cellLabelSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                SpreadsheetSelection.labelName("HelloLabel")
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellLabelSelectHistoryToken() {
        this.isMatchAndCheck2(
            HistoryToken.cellLabelSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellSelectSelectHistoryToken() {
        this.isMatchAndCheck2(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    private void isMatchAndCheck2(final HistoryToken historyToken,
                                  final boolean expected) {
        this.isMatchAndCheck(
            SpreadsheetLabelMappingDialogComponent.with(
                this.context(
                    appContext(historyToken)
                )
            ),
            historyToken,
            expected
        );
    }

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChangeSpreadsheetLabelMappingCreateHistoryToken() {
        this.onHistoryTokenChangeAndCheck(
            "/1/SpreadsheetName111/create-label",
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                "          Errors\n" +
                "            Required\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [] id=labelMapping-reference-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );
    }

    // http://localhost:12345/index.html#/1/Untitled/cell/A1/label
    @Test
    public void testOnHistoryTokenChangeSpreadsheetLabelMappingSelectHistoryTokenWithCell() {
        this.onHistoryTokenChangeAndCheck(
            "/1/SpreadsheetName111/cell/A1/label",
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                "          Errors\n" +
                "            Required\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [A1] id=labelMapping-reference-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111/cell/A1] id=labelMapping-close-Link\n"
        );
    }

    // http://localhost:12345/index.html#/1/Untitled/cell/B2:C3/label
    @Test
    public void testOnHistoryTokenChangeSpreadsheetLabelMappingSelectHistoryTokenWIthCellRange() {
        this.onHistoryTokenChangeAndCheck(
            "/1/SpreadsheetName111/cell/B2:C3/label",
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                "          Errors\n" +
                "            Required\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [B2:C3] id=labelMapping-reference-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111/cell/B2:C3/bottom-right] id=labelMapping-close-Link\n"
        );
    }

    // http://localhost:12345/index.html#/1/Untitled/cell/Label123/label
    @Test
    public void testOnHistoryTokenChangeSpreadsheetLabelMappingSelectHistoryTokenWithLabel() {
        this.onHistoryTokenChangeAndCheck(
            "/1/SpreadsheetName111/cell/Label123/label",
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                "          Errors\n" +
                "            Required\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [Label123] id=labelMapping-reference-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111/cell/Label123] id=labelMapping-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeSpreadsheetLabelMappingCreateHistoryTokenAndHistoryTokenRefresh() {
        final AppContext context = appContext("/1/SpreadsheetName111/create-label");

        final SpreadsheetLabelMappingDialogComponent dialog = this.dialog(
            context(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                "          Errors\n" +
                "            Required\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [] id=labelMapping-reference-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );

        // refresh again ! for now label and expression reference are not updated from the SAVE response
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                "          Errors\n" +
                "            Required\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [] id=labelMapping-reference-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeSpreadsheetLabelMappingSelectHistoryTokenWithLabelNameAndSpreadsheetDeltaRefresh() {
        final AppContext context = appContext("/1/SpreadsheetName111/label/Label999");

        final SpreadsheetLabelMappingDialogComponent dialog = this.dialog(
            context(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [Label999] id=labelMapping-label-TextBox REQUIRED\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [] id=labelMapping-reference-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" DISABLED id=labelMapping-undo-Link\n" +
                "            \"Delete\" [#/1/SpreadsheetName111/label/Label999/delete] id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );

        dialog.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseAbsolute("https://example.com/api/spreadsheet/1/label"),
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    SpreadsheetSelection.labelName("LoadedLabel123")
                        .setLabelMappingReference(SpreadsheetSelection.parseCell("C3"))
                )
            ),
            context
        );

        // refresh again ! for now label and expression reference are not updated from the SAVE response
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "SpreadsheetLabelMappingDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Spreadsheet: Label\n" +
                "    id=labelMapping-Dialog includeClose=true\n" +
                "      SpreadsheetLabelComponent\n" +
                "        SpreadsheetSuggestBoxComponent\n" +
                "          Label [Label999] id=labelMapping-label-TextBox REQUIRED\n" +
                "      SpreadsheetExpressionReferenceComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            Cell, cell range or Label [] id=labelMapping-reference-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=labelMapping-save-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName111/label/LoadedLabel123/save/C3] id=labelMapping-undo-Link\n" +
                "            \"Delete\" [#/1/SpreadsheetName111/label/Label999/delete] id=labelMapping-delete-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndCheck(final String historyToken,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            appContext(historyToken),
            expected
        );
    }

    private static FakeAppContext appContext(final String historyToken) {
        return appContext(
            HistoryToken.parseString(historyToken)
        );
    }

    private static FakeAppContext appContext(final HistoryToken historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // nop
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public Locale locale() {
                return SpreadsheetMetadataTesting.LOCALE;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                    SpreadsheetId.with(1)
                ).set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                    SpreadsheetName.with("SpreadsheetName111")
                );
            }

            @Override
            public void error(final Object... values) {
                // load label is expected to fail and cause an error
            }
        };
    }

    private void onHistoryTokenChangeAndCheck(final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            this.dialog(
                this.context(context)
            ),
            context,
            expected
        );
    }

    private SpreadsheetLabelMappingDialogComponentContext context(final AppContext context) {
        return SpreadsheetLabelMappingDialogComponentContexts.appContext(context);
    }

    private SpreadsheetLabelMappingDialogComponent dialog(final SpreadsheetLabelMappingDialogComponentContext context) {
        return SpreadsheetLabelMappingDialogComponent.with(context);
    }

    @Override
    public SpreadsheetLabelMappingDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return this.dialog(
            context(
                appContext(historyToken)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<SpreadsheetLabelMappingDialogComponent> type() {
        return SpreadsheetLabelMappingDialogComponent.class;
    }
}
