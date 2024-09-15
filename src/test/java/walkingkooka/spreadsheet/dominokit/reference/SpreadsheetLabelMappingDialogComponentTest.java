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

package walkingkooka.spreadsheet.dominokit.reference;

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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;

public final class SpreadsheetLabelMappingDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetLabelMappingDialogComponent> {

    // http://localhost:12345/index.html#/1/Untitled/label
    @Test
    public void testEmpty() {
        this.onHistoryTokenChangeAndCheck(
                "/1/SpreadsheetName111/label",
                "SpreadsheetLabelMappingDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Label\n" +
                        "    id=labelMapping includeClose=true\n" +
                        "      SpreadsheetLabelComponent\n" +
                        "        SpreadsheetSuggestBoxComponent\n" +
                        "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                        "          Errors\n" +
                        "            Label is empty\n" +
                        "      SpreadsheetExpressionReferenceComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            Cell, cell range or Label [] id=labelMapping-target-TextBox\n" +
                        "            Errors\n" +
                        "              text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" DISABLED id=labelMapping-save-Link\n" +
                        "          \"Undo\" [#/1/SpreadsheetName111/label] id=labelMapping-undo-Link\n" +
                        "          \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                        "          \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );
    }

    @Test
    public void testCreateWithoutLabelAndSaved() {
        final AppContext context = appContext("/1/SpreadsheetName111/label");

        final SpreadsheetLabelMappingDialogComponent dialog = this.dialog(
                spreadsheetListComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetLabelMappingDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Label\n" +
                        "    id=labelMapping includeClose=true\n" +
                        "      SpreadsheetLabelComponent\n" +
                        "        SpreadsheetSuggestBoxComponent\n" +
                        "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                        "          Errors\n" +
                        "            Label is empty\n" +
                        "      SpreadsheetExpressionReferenceComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            Cell, cell range or Label [] id=labelMapping-target-TextBox\n" +
                        "            Errors\n" +
                        "              text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" DISABLED id=labelMapping-save-Link\n" +
                        "          \"Undo\" [#/1/SpreadsheetName111/label] id=labelMapping-undo-Link\n" +
                        "          \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                        "          \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );

        dialog.onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseAbsolute("https://example.com/api/spreadsheet/1/label"),
                SpreadsheetDelta.EMPTY.setLabels(
                        Sets.of(
                                SpreadsheetSelection.labelName("SavedLabel123")
                                        .mapping(SpreadsheetSelection.parseCell("C3"))
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
                        "    Label\n" +
                        "    id=labelMapping includeClose=true\n" +
                        "      SpreadsheetLabelComponent\n" +
                        "        SpreadsheetSuggestBoxComponent\n" +
                        "          Label [] id=labelMapping-label-TextBox REQUIRED\n" +
                        "          Errors\n" +
                        "            Label is empty\n" +
                        "      SpreadsheetExpressionReferenceComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            Cell, cell range or Label [] id=labelMapping-target-TextBox\n" +
                        "            Errors\n" +
                        "              text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" DISABLED id=labelMapping-save-Link\n" +
                        "          \"Undo\" [#/1/SpreadsheetName111/label/SavedLabel123/save/C3] id=labelMapping-undo-Link\n" +
                        "          \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                        "          \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );
    }

    @Test
    public void testCreateWithLabelAndSaved() {
        final AppContext context = appContext("/1/SpreadsheetName111/label/Label999");

        final SpreadsheetLabelMappingDialogComponent dialog = this.dialog(
                spreadsheetListComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "SpreadsheetLabelMappingDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Label\n" +
                        "    id=labelMapping includeClose=true\n" +
                        "      SpreadsheetLabelComponent\n" +
                        "        SpreadsheetSuggestBoxComponent\n" +
                        "          Label [Label999] id=labelMapping-label-TextBox REQUIRED\n" +
                        "      SpreadsheetExpressionReferenceComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            Cell, cell range or Label [] id=labelMapping-target-TextBox\n" +
                        "            Errors\n" +
                        "              text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" DISABLED id=labelMapping-save-Link\n" +
                        "          \"Undo\" [#/1/SpreadsheetName111/label] id=labelMapping-undo-Link\n" +
                        "          \"Delete\" DISABLED id=labelMapping-delete-Link\n" +
                        "          \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
        );

        dialog.onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseAbsolute("https://example.com/api/spreadsheet/1/label"),
                SpreadsheetDelta.EMPTY.setLabels(
                        Sets.of(
                                SpreadsheetSelection.labelName("SavedLabel123")
                                        .mapping(SpreadsheetSelection.parseCell("C3"))
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
                        "    Label\n" +
                        "    id=labelMapping includeClose=true\n" +
                        "      SpreadsheetLabelComponent\n" +
                        "        SpreadsheetSuggestBoxComponent\n" +
                        "          Label [Label999] id=labelMapping-label-TextBox REQUIRED\n" +
                        "      SpreadsheetExpressionReferenceComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            Cell, cell range or Label [] id=labelMapping-target-TextBox\n" +
                        "            Errors\n" +
                        "              text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" DISABLED id=labelMapping-save-Link\n" +
                        "          \"Undo\" [#/1/SpreadsheetName111/label/SavedLabel123/save/C3] id=labelMapping-undo-Link\n" +
                        "          \"Delete\" [#/1/SpreadsheetName111/label/Label999/delete] id=labelMapping-delete-Link\n" +
                        "          \"Close\" [#/1/SpreadsheetName111] id=labelMapping-close-Link\n"
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
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
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
        };
    }

    private void onHistoryTokenChangeAndCheck(final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
                this.dialog(
                        this.spreadsheetListComponentContext(context)
                ),
                context,
                expected
        );
    }

    private SpreadsheetLabelMappingDialogComponentContext spreadsheetListComponentContext(final AppContext context) {
        return SpreadsheetLabelMappingDialogComponentContexts.appContext(context);
    }

    private SpreadsheetLabelMappingDialogComponent dialog(final SpreadsheetLabelMappingDialogComponentContext context) {
        return SpreadsheetLabelMappingDialogComponent.with(context);
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
