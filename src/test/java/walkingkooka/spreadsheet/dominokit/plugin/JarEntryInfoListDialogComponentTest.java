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
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfo;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public final class JarEntryInfoListDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<JarEntryInfoListDialogComponent> {

    private final static PluginName PLUGIN_NAME = PluginName.with("TestPluginName111");

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithPluginSelectHistoryToken() {
        this.isMatchAndCheck(
            this.dialog(
                this.pluginDialogComponentContext(
                    new TestAppContext("/plugin/" + PLUGIN_NAME)
                )
            ),
            HistoryToken.pluginSelect(PLUGIN_NAME),
            true
        );
    }

    @Test
    public void testIsMatchWithPluginListSelectHistoryToken() {
        this.isMatchAndCheck(
            this.dialog(
                this.pluginDialogComponentContext(
                    new TestAppContext("/plugin/")
                )
            ),
            HistoryToken.pluginListSelect(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            false
        );
    }

    // render...........................................................................................................

    @Test
    public void testEmptyBeforeLoadingJarEntryInfoList() {
        this.onHistoryTokenChangeAndCheck(
            "/plugin/" + PLUGIN_NAME,
            "JarEntryInfoListDialogComponent\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" DISABLED id=plugin-delete-Link\n" +
                "            \"Download\" DISABLED id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
        );
    }

    @Test
    public void testLoadReturnsEmptyJar() {
        final TestAppContext context = new TestAppContext("/plugin/" + PLUGIN_NAME);

        final JarEntryInfoListDialogComponent dialog = this.dialog(
            this.pluginDialogComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "JarEntryInfoListDialogComponent\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" DISABLED id=plugin-delete-Link\n" +
                "            \"Download\" DISABLED id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
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
            "JarEntryInfoListDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    TestPluginName111\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" [#/plugin/TestPluginName111/delete] id=plugin-delete-Link\n" +
                "            \"Download\" [/api/plugin/TestPluginName111/download] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
        );
    }


    @Test
    public void testLoadReturnsNonEmptyJar() {
        final TestAppContext context = new TestAppContext("/plugin/" + PLUGIN_NAME);

        final JarEntryInfoListDialogComponent dialog = this.dialog(
            this.pluginDialogComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "JarEntryInfoListDialogComponent\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" DISABLED id=plugin-delete-Link\n" +
                "            \"Download\" DISABLED id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
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
            "JarEntryInfoListDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    TestPluginName111\n" +
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
                "                Last modified timestamp\n" +
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
                "                      \"Download\" [/api/plugin/TestPluginName111/download/META-INF/MANIFEST.MF] id=plugin-download-Link\n" +
                "                      \"View\" [#/plugin/TestPluginName111/file/META-INF/MANIFEST.MF] id=plugin-view-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" [#/plugin/TestPluginName111/delete] id=plugin-delete-Link\n" +
                "            \"Download\" [/api/plugin/TestPluginName111/download] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
        );
    }

    @Test
    public void testLoadDifferentPlugin() {
        final TestAppContext context = new TestAppContext("/plugin/" + PLUGIN_NAME);

        final JarEntryInfoListDialogComponent dialog = this.dialog(
            this.pluginDialogComponentContext(context)
        );

        // initially empty
        this.onHistoryTokenChangeAndCheck(
            dialog,
            context,
            "JarEntryInfoListDialogComponent\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" DISABLED id=plugin-delete-Link\n" +
                "            \"Download\" DISABLED id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
        );

        dialog.onJarEntryInfoList(
            PLUGIN_NAME,
            Optional.of(
                JarEntryInfoList.with(
                    Lists.of(
                        JarEntryInfo.with(
                            JarEntryInfoName.with("/META-INF/MANIFEST.MF"), // filename
                            OptionalLong.of(111), // size
                            OptionalLong.of(11), // compressedSize
                            OptionalInt.of(1), // method
                            OptionalLong.of(0x1111), // crc
                            Optional.of(
                                LocalDateTime.of(1999, 1, 1, 1, 1)
                            ), // created
                            Optional.of(
                                LocalDateTime.of(2000, 1, 1, 1, 1)
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
            "JarEntryInfoListDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    TestPluginName111\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"/META-INF/MANIFEST.MF\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"111 (11)\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"1\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"1111\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"1/1/99, 1:01 am\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"1/1/00, 1:01 am\"\n" +
                "                  SpreadsheetFlexLayout\n" +
                "                    ROW\n" +
                "                      \"Download\" [/api/plugin/TestPluginName111/download/META-INF/MANIFEST.MF] id=plugin-download-Link\n" +
                "                      \"View\" [#/plugin/TestPluginName111/file/META-INF/MANIFEST.MF] id=plugin-view-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" [#/plugin/TestPluginName111/delete] id=plugin-delete-Link\n" +
                "            \"Download\" [/api/plugin/TestPluginName111/download] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
        );

        final PluginName differentPluginName = PluginName.with("differentPlugin222");
        context.historyToken = "/plugin/" + differentPluginName;

        dialog.onJarEntryInfoList(
            differentPluginName,
            Optional.of(
                JarEntryInfoList.with(
                    Lists.of(
                        JarEntryInfo.with(
                            JarEntryInfoName.with("/META-INF/MANIFEST.MF"), // filename
                            OptionalLong.of(222), // size
                            OptionalLong.of(222), // compressedSize
                            OptionalInt.of(2), // method
                            OptionalLong.of(0x222222), // crc
                            Optional.of(
                                LocalDateTime.of(1999, 2, 2, 2, 2)
                            ), // created
                            Optional.of(
                                LocalDateTime.of(2000, 2, 2, 2, 2)
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
            "JarEntryInfoListDialogComponent\n" +
                "  SpreadsheetDialogComponent\n" +
                "    differentPlugin222\n" +
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
                "                Last modified timestamp\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"/META-INF/MANIFEST.MF\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"222 (222)\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"2\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"222222\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"2/2/99, 2:02 am\"\n" +
                "                  SpreadsheetTextComponent\n" +
                "                    \"2/2/00, 2:02 am\"\n" +
                "                  SpreadsheetFlexLayout\n" +
                "                    ROW\n" +
                "                      \"Download\" [/api/plugin/TestPluginName111/download/META-INF/MANIFEST.MF] id=plugin-download-Link\n" +
                "                      \"View\" [#/plugin/TestPluginName111/file/META-INF/MANIFEST.MF] id=plugin-view-Link\n" +
                "              PLUGINS\n" +
                "                EmptyStatePlugin (mdi-gauge-empty) \"empty JAR file\"\n" +
                "      SpreadsheetLinkListComponent\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"Delete\" [#/plugin/differentPlugin222/delete] id=plugin-delete-Link\n" +
                "            \"Download\" [/api/plugin/differentPlugin222/download] id=plugin-download-Link\n" +
                "            \"Close\" [#/plugin] id=plugin-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndCheck(final String historyToken,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            new TestAppContext(historyToken),
            expected
        );
    }

    @Override
    public JarEntryInfoListDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return JarEntryInfoListDialogComponent.with(
            this.pluginDialogComponentContext(
                new TestAppContext(historyToken.toString())
            )
        );
    }

    static class TestAppContext extends FakeAppContext {

        TestAppContext(final String historyToken) {
            this.historyToken = historyToken;
        }

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
            return HistoryToken.parseString(this.historyToken);
        }

        String historyToken;

        @Override
        public Locale locale() {
            return Locale.forLanguageTag("EN-AU");
        }
    }

    private void onHistoryTokenChangeAndCheck(final TestAppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            this.dialog(
                this.pluginDialogComponentContext(context)
            ),
            context,
            expected
        );
    }

    private JarEntryInfoListDialogComponentContext pluginDialogComponentContext(final TestAppContext context) {
        return new FakeJarEntryInfoListDialogComponentContext() {

            @Override
            public String dialogTitle() {
                return "Plugin";
            }

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

    private JarEntryInfoListDialogComponent dialog(final JarEntryInfoListDialogComponentContext context) {
        return JarEntryInfoListDialogComponent.with(context);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<JarEntryInfoListDialogComponent> type() {
        return JarEntryInfoListDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
