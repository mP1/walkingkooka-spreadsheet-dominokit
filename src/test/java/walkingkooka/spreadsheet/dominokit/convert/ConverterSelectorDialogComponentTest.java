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

package walkingkooka.spreadsheet.dominokit.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public final class ConverterSelectorDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<ConverterSelectorDialogComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testOnHistoryTokenChange() {
        final AppContext context = this.appContext(
            "/1/Spreadsheet123/spreadsheet/findConverter"
        );

        final ConverterSelectorDialogComponent dialog = ConverterSelectorDialogComponent.with(
            new TestConverterSelectorDialogComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public Optional<ConverterSelector> undo() {
                    return Optional.of(
                        ConverterSelector.parse("hello-converter")
                    );
                }
            }
        );

        dialog.refresh(context);

        this.treePrintAndCheck(
            dialog,
            "ConverterSelectorDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    Converter Title123\n" +
                "    id=selector-Dialog includeClose=true CLOSED\n" +
                "      ConverterSelectorComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            [hello-converter] id=selector-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet123/spreadsheet/findConverter/save/hello-converter] id=selector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet123/spreadsheet/findConverter/save/] id=selector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet123/spreadsheet/findConverter/save/hello-converter] id=selector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet123/spreadsheet] id=selector-close-Link\n"
        );
    }

    private static class TestConverterSelectorDialogComponentContext extends FakeConverterSelectorDialogComponentContext {

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public Runnable addConverterFetcherWatcher(final ConverterFetcherWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public String dialogTitle() {
            return "Converter Title123";
        }
    }

    private AppContext appContext(final String historyToken) {
        return new FakeAppContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addConverterFetcherWatcher(final ConverterFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }
        };
    }

    @Override
    public ConverterSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return ConverterSelectorDialogComponent.with(
            ConverterSelectorDialogComponentContexts.appContext(
                SpreadsheetMetadataPropertyName.FIND_CONVERTER,
                this.appContext(historyToken.toString())
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<ConverterSelectorDialogComponent> type() {
        return ConverterSelectorDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
