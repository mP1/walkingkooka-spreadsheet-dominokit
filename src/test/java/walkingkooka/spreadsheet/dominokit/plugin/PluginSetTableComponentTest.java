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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.plugin.JarFileTesting;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;

public final class PluginSetTableComponentTest implements TableComponentTesting<HTMLDivElement, PluginSet, PluginSetTableComponent>,
    JarFileTesting {

    private final static String ID_PREFIX = "Table123-";

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            this.createComponent(),
            "PluginSetTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Filename\n" +
                "          User\n" +
                "          Timestamp\n" +
                "          Links\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=Table123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n"
        );
    }

    @Test
    public void testSeveralRows() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        PluginSet.with(
                            SortedSets.of(
                                this.plugin(
                                    "TestPlugin111",
                                    "filename111.jar",
                                    "user111@example.com",
                                    "1999-12-31T12:58:59"
                                ),
                                this.plugin(
                                    "TestPlugin222",
                                    "filename222.jar",
                                    "user222@example.com",
                                    "2000-01-01T12:58:59"
                                )
                            )
                        )
                    )
                ),
            "PluginSetTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Filename\n" +
                "          User\n" +
                "          Timestamp\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextComponent\n" +
                "              \"TestPlugin111\"\n" +
                "            TextComponent\n" +
                "              \"filename111.jar\"\n" +
                "            TextComponent\n" +
                "              \"user111@example.com\"\n" +
                "            TextComponent\n" +
                "              \"31/12/99, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Delete\" [#/plugin/TestPlugin111/delete] id=Table123-delete-Link\n" +
                "                \"Download\" [/api/plugin/TestPlugin111/download] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin111] id=Table123-view-Link\n" +
                "          ROW 1\n" +
                "            TextComponent\n" +
                "              \"TestPlugin222\"\n" +
                "            TextComponent\n" +
                "              \"filename222.jar\"\n" +
                "            TextComponent\n" +
                "              \"user222@example.com\"\n" +
                "            TextComponent\n" +
                "              \"1/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Delete\" [#/plugin/TestPlugin222/delete] id=Table123-delete-Link\n" +
                "                \"Download\" [/api/plugin/TestPlugin222/download] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin222] id=Table123-view-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=Table123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n"
        );
    }

    @Test
    public void testSeveralRowsWithRefreshPreviousNext() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        PluginSet.with(
                            SortedSets.of(
                                this.plugin(
                                    "TestPlugin111",
                                    "filename111.jar",
                                    "user111@example.com",
                                    "2000-01-01T12:58:59"
                                ),
                                this.plugin(
                                    "TestPlugin222",
                                    "filename222.jar",
                                    "user222@example.com",
                                    "2000-01-02T12:58:59"
                                ),
                                this.plugin(
                                    "TestPlugin333",
                                    "filename333.jar",
                                    "user333@example.com",
                                    "2000-03-03T12:58:59"
                                ),
                                this.plugin(
                                    "TestPlugin444",
                                    "filename444.jar",
                                    "user444@example.com",
                                    "2000-04-04T12:58:59"
                                )
                            )
                        )
                    )
                ).refresh(
                    HistoryToken.pluginListSelect(
                        HistoryTokenOffsetAndCount.with(
                            OptionalInt.of(10), // offset
                            OptionalInt.of(4) // count
                        )
                    )
                ),
            "PluginSetTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=Table123-Table\n" +
                "        COLUMN(S)\n" +
                "          Name\n" +
                "          Filename\n" +
                "          User\n" +
                "          Timestamp\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextComponent\n" +
                "              \"TestPlugin111\"\n" +
                "            TextComponent\n" +
                "              \"filename111.jar\"\n" +
                "            TextComponent\n" +
                "              \"user111@example.com\"\n" +
                "            TextComponent\n" +
                "              \"1/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Delete\" [#/plugin/TestPlugin111/delete] id=Table123-delete-Link\n" +
                "                \"Download\" [/api/plugin/TestPlugin111/download] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin111] id=Table123-view-Link\n" +
                "          ROW 1\n" +
                "            TextComponent\n" +
                "              \"TestPlugin222\"\n" +
                "            TextComponent\n" +
                "              \"filename222.jar\"\n" +
                "            TextComponent\n" +
                "              \"user222@example.com\"\n" +
                "            TextComponent\n" +
                "              \"2/1/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Delete\" [#/plugin/TestPlugin222/delete] id=Table123-delete-Link\n" +
                "                \"Download\" [/api/plugin/TestPlugin222/download] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin222] id=Table123-view-Link\n" +
                "          ROW 2\n" +
                "            TextComponent\n" +
                "              \"TestPlugin333\"\n" +
                "            TextComponent\n" +
                "              \"filename333.jar\"\n" +
                "            TextComponent\n" +
                "              \"user333@example.com\"\n" +
                "            TextComponent\n" +
                "              \"3/3/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Delete\" [#/plugin/TestPlugin333/delete] id=Table123-delete-Link\n" +
                "                \"Download\" [/api/plugin/TestPlugin333/download] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin333] id=Table123-view-Link\n" +
                "          ROW 3\n" +
                "            TextComponent\n" +
                "              \"TestPlugin444\"\n" +
                "            TextComponent\n" +
                "              \"filename444.jar\"\n" +
                "            TextComponent\n" +
                "              \"user444@example.com\"\n" +
                "            TextComponent\n" +
                "              \"4/4/00, 12:58 pm\"\n" +
                "            FlexLayoutComponent\n" +
                "              ROW\n" +
                "                \"Delete\" [#/plugin/TestPlugin444/delete] id=Table123-delete-Link\n" +
                "                \"Download\" [/api/plugin/TestPlugin444/download] id=Table123-download-Link\n" +
                "                \"View\" [#/plugin/TestPlugin444] id=Table123-view-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" [#/plugin/*/offset/7/count/4] id=Table123-previous-Link\n" +
                "              \"next\" [#/plugin/*/offset/13/count/4] mdi-arrow-right id=Table123-next-Link\n" +
                "        PLUGINS\n" +
                "          EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n"
        );
    }

    @Override
    public PluginSetTableComponent createComponent() {
        return PluginSetTableComponent.empty(
            ID_PREFIX,
            context()
        );
    }

    private static FakePluginSetTableComponentContext context() {
        return new FakePluginSetTableComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString("/plugin/TestPlugin123");
            }

            public PluginName pluginName() {
                return PluginName.with("TestPlugin123");
            }

            @Override
            public Locale locale() {
                return Locale.forLanguageTag("EN-AU");
            }
        };
    }

    private Plugin plugin(final String pluginName,
                          final String filename,
                          final String user,
                          final String timestamp) {
        return Plugin.with(
            PluginName.with(pluginName),
            filename,
            Binary.with(
                JarFileTesting.jarFile(
                    "ManifestVersion: 1.0\r\n",
                    Maps.empty()
                )
            ),
            EmailAddress.parse(user),
            LocalDateTime.parse(timestamp)
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginSetTableComponent> type() {
        return PluginSetTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
