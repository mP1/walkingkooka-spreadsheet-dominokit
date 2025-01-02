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

package walkingkooka.spreadsheet.dominokit.plugin;

import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.collect.map.Maps;
import walkingkooka.plugin.JarFileTesting;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginStore;
import walkingkooka.plugin.store.PluginStores;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.PluginUploadSelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

public final class PluginUploadDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<PluginUploadDialogComponent,
        SpreadsheetComparatorNameListDialogComponentContext>,
        SpreadsheetMetadataTesting {

    @Test
    public void testBeforeSelectingFile() {
        final TestAppContext context = new TestAppContext("/plugin-upload/");
        final PluginUploadDialogComponent dialog = PluginUploadDialogComponent.with(
                this.pluginDialogComponentContext(context)
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginUploadDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Upload Plugin\n" +
                        "    id=pluginUpload-Dialog includeClose=true\n" +
                        "      SpreadsheetUploadFileComponent\n" +
                        "        id=pluginUpload-UploadFile\n" +
                        "        label=Drop files here or click to upload.\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Upload\" DISABLED id=pluginUpload-upload-Link\n" +
                        "          \"Close\" [#/plugin] id=pluginUpload-close-Link\n"
        );
    }

    @Test
    public void testAfterSelectingFile() {
        final TestAppContext context = new TestAppContext("/plugin-upload/");
        context.savePlugins(4);

        final PluginUploadDialogComponent dialog = PluginUploadDialogComponent.with(
                this.pluginDialogComponentContext(context)
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginUploadDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Upload Plugin\n" +
                        "    id=pluginUpload-Dialog includeClose=true\n" +
                        "      SpreadsheetUploadFileComponent\n" +
                        "        id=pluginUpload-UploadFile\n" +
                        "        label=Drop files here or click to upload.\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Upload\" DISABLED id=pluginUpload-upload-Link\n" +
                        "          \"Close\" [#/plugin] id=pluginUpload-close-Link\n"
        );

        dialog.uploadFile.setValue(
                Optional.of(
                        BrowserFile.base64(
                                "TestPluginName" + 0,
                                Base64.getEncoder()
                                        .encodeToString(
                                                pluginBinary()
                                                        .value()
                                        )
                        )
                )
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginUploadDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Upload Plugin\n" +
                        "    id=pluginUpload-Dialog includeClose=true\n" +
                        "      SpreadsheetUploadFileComponent\n" +
                        "        id=pluginUpload-UploadFile\n" +
                        "        label=Drop files here or click to upload.\n" +
                        "          BrowserFileBase64\n" +
                        "            \"TestPluginName0\"\n" +
                        "              UEsDBBQACAgIAEB/IigAAAAAAAAAAAAAAAAUAA0ATUVUQS1JTkYvTUFOSUZFU1QuTUZVVAkABdjabjhYqGw44+UCAFBLBwishaIUBAAAAAIAAABQSwECFAAUAAgICABAfyIorIWiFAQAAAACAAAAFAAJAAAAAAAAAAAAAAAAAAAATUVUQS1JTkYvTUFOSUZFU1QuTUZVVAUABdjabjhQSwUGAAAAAAEAAQBLAAAAUwAAAAAA\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Upload\" [#/plugin-upload/save/base64/TestPluginName0/UEsDBBQACAgIAEB/IigAAAAAAAAAAAAAAAAUAA0ATUVUQS1JTkYvTUFOSUZFU1QuTUZVVAkABdjabjhYqGw44+UCAFBLBwishaIUBAAAAAIAAABQSwECFAAUAAgICABAfyIorIWiFAQAAAACAAAAFAAJAAAAAAAAAAAAAAAAAAAATUVUQS1JTkYvTUFOSUZFU1QuTUZVVAUABdjabjhQSwUGAAAAAAEAAQBLAAAAUwAAAAAA] id=pluginUpload-upload-Link\n" +
                        "          \"Close\" [#/plugin] id=pluginUpload-close-Link\n"
        );
    }

    final static class TestAppContext extends FakeAppContext {

        TestAppContext(final String historyToken) {
            this.historyToken = HistoryToken.parseString(historyToken)
                    .cast(PluginUploadSelectHistoryToken.class);
        }

        @Override
        public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
            return this.pluginFetcherWatchers.addPluginFetcherWatcher(watcher);
        }

        private final PluginFetcherWatchers pluginFetcherWatchers = PluginFetcherWatchers.empty();

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return null;
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        final HistoryToken historyToken;

        @Override
        public Locale locale() {
            return Locale.forLanguageTag("EN-AU");
        }

        @Override
        public PluginStore pluginStore() {
            return this.pluginStore;
        }

        void savePlugins(final int count) {
            for (int i = 0; i < count; i++) {
                this.pluginStore.save(
                        plugin(i+1)
                );
            }
        }

        private final PluginStore pluginStore = PluginStores.treeMap();
    }

    private static Plugin plugin(final int i) {
        return Plugin.with(
                PluginName.with("TestPluginName" + i),
                "filename" + i + ".jar",
                pluginBinary(),
                USER,
                NOW.now()
        );
    }

    private static Binary pluginBinary() {
        return Binary.with(
                JarFileTesting.jarFile(
                        "ManifestVersion: 1.0\r\n",
                        Maps.empty()
                )
        );
    }

    private PluginUploadDialogComponentContext pluginDialogComponentContext(final TestAppContext context) {
        return new FakePluginUploadDialogComponentContext() {

            @Override
            public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
                return context.addPluginFetcherWatcher(watcher);
            }

            @Override
            public HistoryToken historyToken() {
                return context.historyToken();
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return context.addHistoryTokenWatcher(watcher);
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PluginUploadDialogComponent> type() {
        return PluginUploadDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
