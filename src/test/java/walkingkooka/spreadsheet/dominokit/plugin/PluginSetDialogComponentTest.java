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
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.plugin.store.PluginStore;
import walkingkooka.plugin.store.PluginStores;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.PluginListSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.PluginSelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Locale;
import java.util.TreeSet;

public final class PluginSetDialogComponentTest implements DialogComponentLifecycleTesting<PluginSetDialogComponent>,
    SpreadsheetMetadataTesting {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithPluginListSelectHistoryToken() {
        final PluginListSelectHistoryToken historyToken = HistoryToken.pluginListSelect(
            HistoryTokenOffsetAndCount.EMPTY
        );

        final PluginSetDialogComponent dialog = PluginSetDialogComponent.with(
            PluginSetDialogComponentContexts.appContext(
                new TestAppContext(historyToken)
            )
        );

        this.isMatchAndCheck(
            dialog,
            historyToken,
            true
        );
    }

    @Test
    public void testIsMatchWithPluginNameHistoryToken() {
        final PluginSelectHistoryToken historyToken = HistoryToken.pluginSelect(
            PluginName.with("hello")
        );

        final PluginSetDialogComponent dialog = PluginSetDialogComponent.with(
            PluginSetDialogComponentContexts.appContext(
                new TestAppContext(historyToken)
            )
        );

        this.isMatchAndCheck(
            dialog,
            historyToken,
            false
        );
    }

    // onHistoryToken...................................................................................................

    @Test
    public void testOnHistoryTokenChangeWithoutPlugins() {
        final TestAppContext appContext = new TestAppContext("/plugin/*");

        final PluginSetDialogComponentContext context = PluginSetDialogComponentContexts.appContext(appContext);
        final PluginSetDialogComponent dialog = PluginSetDialogComponent.with(context);

        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "PluginSetDialogComponent\n" +
                "  DialogComponent\n" +
                "    Plugin\n" +
                "    id=PluginSet-Dialog includeClose=true\n" +
                "      PluginSetTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=PluginSet-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Filename\n" +
                "                User\n" +
                "                Timestamp\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=PluginSet-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=PluginSet-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Upload\" [#/plugin-upload] id=PluginSet-upload-Link\n" +
                "            \"Close\" [#/] id=PluginSet-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeTableFullOfPlugins() {
        final TestAppContext appContext = new TestAppContext("/plugin/*/count/3");
        appContext.savePlugins(4);

        final PluginSetDialogComponent dialog = PluginSetDialogComponent.with(
            PluginSetDialogComponentContexts.appContext(appContext)
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "PluginSetDialogComponent\n" +
                "  DialogComponent\n" +
                "    Plugin\n" +
                "    id=PluginSet-Dialog includeClose=true\n" +
                "      PluginSetTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=PluginSet-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Filename\n" +
                "                User\n" +
                "                Timestamp\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=PluginSet-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=PluginSet-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Upload\" [#/plugin-upload] id=PluginSet-upload-Link\n" +
                "            \"Close\" [#/] id=PluginSet-close-Link\n"
        );

        appContext.loadPlugins(
            0,
            3
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "PluginSetDialogComponent\n" +
                "  DialogComponent\n" +
                "    Plugin\n" +
                "    id=PluginSet-Dialog includeClose=true\n" +
                "      PluginSetTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=PluginSet-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Filename\n" +
                "                User\n" +
                "                Timestamp\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextComponent\n" +
                "                    \"test-plugin-name-1\"\n" +
                "                  TextComponent\n" +
                "                    \"filename1.jar\"\n" +
                "                  TextComponent\n" +
                "                    \"user@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/12/99, 12:58 pm\"\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Delete\" [#/plugin/test-plugin-name-1/delete] id=PluginSet-delete-Link\n" +
                "                      \"Download\" [/api/plugin/test-plugin-name-1/download] id=PluginSet-download-Link\n" +
                "                      \"View\" [#/plugin/test-plugin-name-1] id=PluginSet-view-Link\n" +
                "                ROW 1\n" +
                "                  TextComponent\n" +
                "                    \"test-plugin-name-2\"\n" +
                "                  TextComponent\n" +
                "                    \"filename2.jar\"\n" +
                "                  TextComponent\n" +
                "                    \"user@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/12/99, 12:58 pm\"\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Delete\" [#/plugin/test-plugin-name-2/delete] id=PluginSet-delete-Link\n" +
                "                      \"Download\" [/api/plugin/test-plugin-name-2/download] id=PluginSet-download-Link\n" +
                "                      \"View\" [#/plugin/test-plugin-name-2] id=PluginSet-view-Link\n" +
                "                ROW 2\n" +
                "                  TextComponent\n" +
                "                    \"test-plugin-name-3\"\n" +
                "                  TextComponent\n" +
                "                    \"filename3.jar\"\n" +
                "                  TextComponent\n" +
                "                    \"user@example.com\"\n" +
                "                  TextComponent\n" +
                "                    \"31/12/99, 12:58 pm\"\n" +
                "                  FlexLayoutComponent\n" +
                "                    ROW\n" +
                "                      \"Delete\" [#/plugin/test-plugin-name-3/delete] id=PluginSet-delete-Link\n" +
                "                      \"Download\" [/api/plugin/test-plugin-name-3/download] id=PluginSet-download-Link\n" +
                "                      \"View\" [#/plugin/test-plugin-name-3] id=PluginSet-view-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=PluginSet-previous-Link\n" +
                "                    \"next\" [#/plugin/*/offset/2/count/3] mdi-arrow-right id=PluginSet-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Upload\" [#/plugin-upload] id=PluginSet-upload-Link\n" +
                "            \"Close\" [#/] id=PluginSet-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeTableFullOfPluginsOffset1() {
        final TestAppContext appContext = new TestAppContext("/plugin/*/offset/1/count/3");
        appContext.savePlugins(1 + 3 + 1);

        final PluginSetDialogComponent dialog = PluginSetDialogComponent.with(
            PluginSetDialogComponentContexts.appContext(appContext)
        );

        appContext.loadPlugins(
            1,
            3
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "PluginSetDialogComponent\n" +
                "  DialogComponent\n" +
                "    Plugin\n" +
                "    id=PluginSet-Dialog includeClose=true\n" +
                "      PluginSetTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=PluginSet-Table\n" +
                "              COLUMN(S)\n" +
                "                Name\n" +
                "                Filename\n" +
                "                User\n" +
                "                Timestamp\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" [#/plugin/*/offset/0/count/3] id=PluginSet-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=PluginSet-next-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Upload\" [#/plugin-upload] id=PluginSet-upload-Link\n" +
                "            \"Close\" [#/] id=PluginSet-close-Link\n"
        );
    }

    final static class TestAppContext extends FakeAppContext {

        TestAppContext(final String historyToken) {
            this(HistoryToken.parseString(historyToken));
        }

        TestAppContext(final HistoryToken historyToken) {
            this.historyToken = historyToken;
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

        @Override public PluginStore pluginStore() {
            return this.pluginStore;
        }

        void savePlugins(final int count) {
            for (int i = 0; i < count; i++) {
                final int j = i + 1;

                this.pluginStore.save(
                    Plugin.with(
                        PluginName.with("test-plugin-name-" + j),
                        "filename" + j + ".jar",
                        Binary.with(
                            JarFileTesting.jarFile(
                                "ManifestVersion: 1.0\r\n",
                                Maps.empty()
                            )
                        ),
                        PluginSetDialogComponentTest.USER,
                        NOW.now()
                    )
                );
            }
        }

        void loadPlugins(final int offset,
                         final int count) {
            this.pluginFetcherWatchers.onPluginSet(
                PluginSet.with(
                    new TreeSet<>(
                        this.pluginStore()
                            .filter(
                                "*", // query
                                offset,
                                count
                            )
                    )
                )
            );
        }

        private final PluginStore pluginStore = PluginStores.treeMap();
    }

    @Override
    public PluginSetDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return PluginSetDialogComponent.with(
            PluginSetDialogComponentContexts.appContext(
                new TestAppContext(historyToken)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PluginSetDialogComponent> type() {
        return PluginSetDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
