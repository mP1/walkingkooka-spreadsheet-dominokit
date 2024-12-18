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
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfo;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public final class PluginDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<PluginDialogComponent,
        SpreadsheetComparatorNameListDialogComponentContext> {

    private final static PluginName PLUGIN_NAME = PluginName.with("TestPluginName123");

    @Test
    public void testEmpty() {
        this.onHistoryTokenChangeAndCheck(
                "/plugin/" + PLUGIN_NAME,
                "PluginDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Loading...\n" +
                        "    id=plugin-Dialog includeClose=true\n" +
                        "      JarEntryInfoListTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              id=plugin-Table\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Size(Compressed)\n" +
                        "                Method\n" +
                        "                CRC\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Links\n" +
                        "              ROW(S)\n" +
                        "              PLUGINS\n" +
                        "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Close\" [#/plugin] id=plugin-close-Link\n"
        );
    }

    @Test
    public void testLoadReturnsEmptyJar() {
        final AppContext context = appContext("/plugin/" + PLUGIN_NAME);

        final PluginDialogComponent dialog = this.dialog(
                this.pluginDialogComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Loading...\n" +
                        "    id=plugin-Dialog includeClose=true\n" +
                        "      JarEntryInfoListTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              id=plugin-Table\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Size(Compressed)\n" +
                        "                Method\n" +
                        "                CRC\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Links\n" +
                        "              ROW(S)\n" +
                        "              PLUGINS\n" +
                        "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Close\" [#/plugin] id=plugin-close-Link\n"
        );

        dialog.onJarEntryInfoList(
                PLUGIN_NAME,
                Optional.of(
                        JarEntryInfoList.EMPTY
                ),
                context
        );

        // refresh again !
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    TestPluginName123\n" +
                        "    id=plugin-Dialog includeClose=true\n" +
                        "      JarEntryInfoListTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              id=plugin-Table\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Size(Compressed)\n" +
                        "                Method\n" +
                        "                CRC\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Links\n" +
                        "              ROW(S)\n" +
                        "              PLUGINS\n" +
                        "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Close\" [#/plugin] id=plugin-close-Link\n"
        );
    }


    @Test
    public void testLoadReturnsNonEmptyJar() {
        final AppContext context = appContext("/plugin/" + PLUGIN_NAME);

        final PluginDialogComponent dialog = this.dialog(
                this.pluginDialogComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Loading...\n" +
                        "    id=plugin-Dialog includeClose=true\n" +
                        "      JarEntryInfoListTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              id=plugin-Table\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Size(Compressed)\n" +
                        "                Method\n" +
                        "                CRC\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Links\n" +
                        "              ROW(S)\n" +
                        "              PLUGINS\n" +
                        "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Close\" [#/plugin] id=plugin-close-Link\n"
        );

        dialog.onJarEntryInfoList(
                PLUGIN_NAME,
                Optional.of(
                        JarEntryInfoList.with(
                                Lists.of(
                                        JarEntryInfo.with(
                                                JarEntryInfoName.with("/META-INF/MANIFEST.MF"), // filename
                                                OptionalLong.of(111), // size
                                                OptionalLong.of(22), // compressedSize
                                                OptionalInt.of(1), // method
                                                OptionalLong.of(0x12345678), // crc
                                                Optional.of(
                                                        LocalDateTime.of(1999, 12, 31, 12, 58)
                                                ), // created
                                                Optional.of(
                                                        LocalDateTime.of(2000, 1, 2, 12, 58)
                                                ) // last mod
                                        )
                                )
                        )
                ),
                context
        );

        // refresh again !
        this.onHistoryTokenChangeAndCheck(
                dialog,
                context,
                "PluginDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    TestPluginName123\n" +
                        "    id=plugin-Dialog includeClose=true\n" +
                        "      JarEntryInfoListTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              id=plugin-Table\n" +
                        "              COLUMN(S)\n" +
                        "                Name\n" +
                        "                Size(Compressed)\n" +
                        "                Method\n" +
                        "                CRC\n" +
                        "                Created\n" +
                        "                Last modified by\n" +
                        "                Links\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"/META-INF/MANIFEST.MF\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"111 (22)\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"1\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"12345678\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"31/12/99, 12:58 pm\"\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"2/1/00, 12:58 pm\"\n" +
                        "                  SpreadsheetFlexLayout\n" +
                        "                    ROW\n" +
                        "                      \"view\" [#/plugin/TestPluginName123/file/META-INF/MANIFEST.MF] id=plugin-view-Link\n" +
                        "              PLUGINS\n" +
                        "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Close\" [#/plugin] id=plugin-close-Link\n"
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
            public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(historyToken);
            }

            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
            }
        };
    }

    private void onHistoryTokenChangeAndCheck(final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
                this.dialog(
                        this.pluginDialogComponentContext(context)
                ),
                context,
                expected
        );
    }

    private PluginDialogComponentContext pluginDialogComponentContext(final AppContext context) {
        return new FakePluginDialogComponentContext() {
            @Override
            public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
                return this.watchers.addPluginFetcherWatcher(watcher);
            }

            @Override
            public void listJarEntries(final PluginName pluginName) {
                // nop
            }

            private final PluginFetcherWatchers watchers = PluginFetcherWatchers.empty();

            @Override
            public HistoryToken historyToken() {
                return context.historyToken();
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return context.addHistoryTokenWatcher(watcher);
            }
            @Override
            public Locale locale() {
                return context.locale();
            }

            @Override
            public PluginName pluginName() {
                return PLUGIN_NAME;
            }
        };
    }

    private PluginDialogComponent dialog(final PluginDialogComponentContext context) {
        return PluginDialogComponent.with(context);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PluginDialogComponent> type() {
        return PluginDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
