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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.plugin.JarFileTesting;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.plugin.store.PluginStore;
import walkingkooka.plugin.store.PluginStores;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.TreeSet;

public final class PluginNameSetDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<PluginNameSetDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithPluginSelectHistoryToken() {
        final HistoryToken historyToken = HistoryToken.metadataPropertySelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetMetadataPropertyName.PLUGINS
        );

        final AppContext context = new FakeAppContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }
        };

        this.isMatchAndCheck(
            PluginNameSetDialogComponent.with(
                this.dialogContext(context)
            ),
            context.historyToken(),
            true
        );
    }

    @Test
    public void testIsMatchWithPluginListSelectHistoryToken() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME
        );

        final AppContext context = new FakeAppContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }
        };

        this.isMatchAndCheck(
            PluginNameSetDialogComponent.with(
                this.dialogContext(context)
            ),
            context.historyToken(),
            false
        );
    }

    // render...........................................................................................................

    @Test
    public void testEmptyTextRefresh() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet123/spreadsheet/plugins"),
            "plugin111, plugin222", // metadata.PLUGINS
            "plugin111, plugin222" // PluginStore plugins
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            PluginNameSetDialogComponent.with(
                this.dialogContext(context)
            ),
            "",
            context,
            "PluginNameSetDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    PluginsTitle999\n" +
                "    id=pluginNameSet-Dialog includeClose=true\n" +
                "      AddPluginNameSetComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Add\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"*\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-add-0-Link\n" +
                "                  \"Plugin111\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111] id=pluginNameSet-add-1-Link\n" +
                "                  \"Plugin222\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin222] id=pluginNameSet-add-2-Link\n" +
                "      RemovePluginNameSetComponent\n" +
                "      PluginNameSetComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            [] id=pluginNameSet-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet123/spreadsheet/plugins/save/] id=pluginNameSet-save-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet123/spreadsheet] id=pluginNameSet-close-Link\n"
        );
    }

    @Test
    public void testNonEmptyTextRefresh() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet123/spreadsheet/plugins/"),
            "plugin111, plugin222", // metadata.PLUGINS
            "plugin111, plugin222" // PluginStore plugins
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            PluginNameSetDialogComponent.with(
                this.dialogContext(context)
            ),
            "plugin111",
            context,
            "PluginNameSetDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    PluginsTitle999\n" +
                "    id=pluginNameSet-Dialog includeClose=true\n" +
                "      AddPluginNameSetComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Add\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"*\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-add-0-Link\n" +
                "                  \"Plugin222\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-add-1-Link\n" +
                "      RemovePluginNameSetComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"*\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111] id=pluginNameSet-remove-0-Link\n" +
                "                  \"Plugin111\" [#/1/Spreadsheet123/spreadsheet/plugins/save/] id=pluginNameSet-remove-1-Link\n" +
                "      PluginNameSetComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            [plugin111] id=pluginNameSet-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111] id=pluginNameSet-save-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet123/spreadsheet] id=pluginNameSet-close-Link\n"
        );
    }

    @Test
    public void testNonEmptyTextDifferentPluginsAndRefresh() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet123/spreadsheet/plugins/"),
            "plugin111, plugin222", // metadata.PLUGINS
            "plugin111, plugin222, plugin333" // PluginStore plugins
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            PluginNameSetDialogComponent.with(
                this.dialogContext(context)
            ),
            "plugin111",
            context,
            "PluginNameSetDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    PluginsTitle999\n" +
                "    id=pluginNameSet-Dialog includeClose=true\n" +
                "      AddPluginNameSetComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Add\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"*\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222,plugin333] id=pluginNameSet-add-0-Link\n" +
                "                  \"Plugin222\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-add-1-Link\n" +
                "                  \"Plugin333\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin333] id=pluginNameSet-add-2-Link\n" +
                "      RemovePluginNameSetComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"*\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111] id=pluginNameSet-remove-0-Link\n" +
                "                  \"Plugin111\" [#/1/Spreadsheet123/spreadsheet/plugins/save/] id=pluginNameSet-remove-1-Link\n" +
                "      PluginNameSetComponent\n" +
                "        ValueSpreadsheetTextBox\n" +
                "          SpreadsheetTextBox\n" +
                "            [plugin111] id=pluginNameSet-TextBox\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111] id=pluginNameSet-save-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet123/spreadsheet/plugins/save/plugin111,plugin222] id=pluginNameSet-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet123/spreadsheet] id=pluginNameSet-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndSetTextAndCheck(final PluginNameSetDialogComponent dialog,
                                                        final String text,
                                                        final AppContext context,
                                                        final String expected) {
        this.checkEquals(
            false,
            dialog.isMatch(NOT_MATCHED),
            () -> "should not be matched " + NOT_MATCHED
        );

        dialog.onHistoryTokenChange(
            NOT_MATCHED,
            context
        );

        dialog.setText(text);

        this.treePrintAndCheck(
            dialog,
            expected
        );
    }

    private PluginNameSetDialogComponentContext dialogContext(final AppContext context) {
        return new TestPluginNameSetDialogComponentContext(context);
    }

    static class TestPluginNameSetDialogComponentContext extends FakePluginNameSetDialogComponentContext {

        TestPluginNameSetDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
            return this.pluginFetcherWatchers.add(watcher);
        }

        @Override
        public void pluginFilter(final String query,
                                 final OptionalInt offset,
                                 final OptionalInt count) {
            final List<Plugin> plugin = this.context.pluginStore()
                .filter(
                    query,
                    offset.orElse(0),
                    count.orElse(1)
                );
            this.pluginFetcherWatchers.onPluginSet(
                PluginSet.with(
                    new TreeSet<>(plugin)
                ),
                this.context
            );
        }

        private final PluginFetcherWatchers pluginFetcherWatchers = PluginFetcherWatchers.empty();

        // SpreadsheetMetadata......................................................................................

        @Override
        public void loadSpreadsheetMetadata(final SpreadsheetId id) {

        }

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return new Runnable() {
                @Override
                public void run() {

                }
            };
        }

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.context.spreadsheetMetadata();
        }

        // HistoryContext..........................................................................................

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.context.addHistoryTokenWatcher(watcher);
        }

        @Override
        public HistoryToken historyToken() {
            return this.context.historyToken();
        }

        // PluginNameSetDialogComponentContext..........................................................................

        @Override
        public String dialogTitle() {
            return "PluginsTitle999";
        }

        // LoggingContext...............................................................................................

        @Override
        public void debug(final Object... values) {
            this.context.debug(values);
        }

        @Override
        public void error(final Object... values) {
            this.context.error(values);
        }

        private final AppContext context;
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final String metadataPlugins,
                                  final String pluginStorePlugins) {
        final PluginStore pluginStore = PluginStores.treeMap();

        for (final PluginName pluginStoreName : PluginNameSet.parse(pluginStorePlugins)) {
            pluginStore.save(
                Plugin.with(
                    pluginStoreName,
                    pluginStoreName.value() + ".jar",
                    Binary.with(
                        JarFileTesting.jarFile(
                            "Manifest-Version: 1.0\r\n",
                            Maps.empty() // contents filename to file content - bytes
                        )
                    ),
                    USER,
                    NOW.now()
                )
            );
        }

        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                    SpreadsheetId.with(1)
                ).setOrRemove(
                    SpreadsheetMetadataPropertyName.PLUGINS,
                    PluginNameSet.parse(metadataPlugins)
                );
            }

            @Override
            public PluginStore pluginStore() {
                return pluginStore;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // nop
            }

            @Override
            public void debug(final Object... values) {
                System.out.println(Arrays.toString(values));
            }

            @Override
            public void error(final Object... values) {
                System.err.println(Arrays.toString(values));
            }
        };
    }

    @Override
    public PluginNameSetDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return PluginNameSetDialogComponent.with(
            this.dialogContext(
                this.appContext(
                    HistoryToken.parseString("/1/Spreadsheet123/spreadsheet/plugins/"),
                    "plugin111, plugin222", // metadata.PLUGINS
                    "plugin111, plugin222" // PluginStore plugins
                )
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginNameSetDialogComponent> type() {
        return Cast.to(PluginNameSetDialogComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
