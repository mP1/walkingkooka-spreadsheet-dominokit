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
import walkingkooka.spreadsheet.dominokit.HtmlElementComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

import java.time.LocalDateTime;
import java.util.Locale;

public final class PluginSetTableComponentTest implements HtmlElementComponentTesting<PluginSetTableComponent, HTMLDivElement>,
        JarFileTesting {

    private final static String ID_PREFIX = "Table123-";

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
                this.createComponent(),
                "PluginSetTableComponent\n"
        );
    }

    @Test
    public void testSeveralRows() {
        this.treePrintAndCheck(
                this.createComponent()
                        .setSet(
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
                        ),
                "PluginSetTableComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        id=Table123-Table\n" +
                        "        COLUMN(S)\n" +
                        "          Name\n" +
                        "          Filename\n" +
                        "          User\n" +
                        "          Timestamp\n" +
                        "          Links\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"TestPlugin111\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"filename111.jar\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user111@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"31/12/99, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Download\" [/api/plugin/TestPlugin111/download] id=Table123-download-Link\n" +
                        "                \"View\" [#/plugin/TestPlugin111] id=Table123-view-Link\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"TestPlugin222\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"filename222.jar\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"user222@example.com\"\n" +
                        "            SpreadsheetTextComponent\n" +
                        "              \"1/1/00, 12:58 pm\"\n" +
                        "            SpreadsheetFlexLayout\n" +
                        "              ROW\n" +
                        "                \"Download\" [/api/plugin/TestPlugin222/download] id=Table123-download-Link\n" +
                        "                \"View\" [#/plugin/TestPlugin222] id=Table123-view-Link\n" +
                        "        PLUGINS\n" +
                        "          EmptyStatePlugin (mdi-gauge-empty) \"No plugins available\"\n"
        );
    }

    private PluginSetTableComponent createComponent() {
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
