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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.printer.TreePrintableTesting;

public final class EnablePluginInfoSetComponentTest implements ClassTesting<EnablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet>>,
        TreePrintableTesting {

    @Test
    public void testRefreshAllDisabled() {
        final SpreadsheetFormatterInfo info1 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetFormatterName.with("formatter1")
        );

        final SpreadsheetFormatterInfo info2 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetFormatterName.with("formatter2")
        );


        final EnablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet> component = EnablePluginInfoSetComponent.empty("base-id-123-");
        component.refresh(
                SpreadsheetFormatterInfoSet.EMPTY, // enabled
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                info1,
                                info2
                        )
                ), // provider
                this.context()
        );

        // all infos should appear in the link list
        this.treePrintAndCheck(
                component,
                "EnablePluginInfoSetComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Enable\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Formatter1\" [#/1/SpreadsheetName123/metadata/formatters/save/https://example.com/info1%20formatter1] id=base-id-123-enable-0-Link\n" +
                        "            \"Formatter2\" [#/1/SpreadsheetName123/metadata/formatters/save/https://example.com/info2%20formatter2] id=base-id-123-enable-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllEnabled() {
        final SpreadsheetFormatterInfo info1 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetFormatterName.with("formatter1")
        );

        final SpreadsheetFormatterInfo info2 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetFormatterName.with("formatter2")
        );


        final EnablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet> component = EnablePluginInfoSetComponent.empty("base-id-123-");
        component.refresh(
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                info1,
                                info2
                        )
                ), // enabled
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                info1,
                                info2
                        )
                ), // provider
                new FakeEnablePluginInfoSetComponentContext()
        );

        // all infos are already enabled links list should be empty
        this.treePrintAndCheck(
                component,
                "EnablePluginInfoSetComponent\n"
        );
    }

    @Test
    public void testRefreshSomeEnabled() {
        final SpreadsheetFormatterInfo info1 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetFormatterName.with("formatter1")
        );

        final SpreadsheetFormatterInfo info2 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetFormatterName.with("formatter2")
        );

        final SpreadsheetFormatterInfo info3 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info3"),
                SpreadsheetFormatterName.with("formatter3")
        );

        final SpreadsheetFormatterInfo info4 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info4"),
                SpreadsheetFormatterName.with("formatter4")
        );

        final EnablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet> component = EnablePluginInfoSetComponent.empty("base-id-123-");
        component.refresh(
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                info3,
                                info4
                        )
                ), // enabled
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                info1,
                                info2,
                                info3,
                                info4
                        )
                ), // provider
                this.context()
        );

        this.treePrintAndCheck(
                component,
                "EnablePluginInfoSetComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Enable\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Formatter1\" [#/1/SpreadsheetName123/metadata/formatters/save/https://example.com/info1%20formatter1,https://example.com/info3%20formatter3,https://example.com/info4%20formatter4] id=base-id-123-enable-0-Link\n" +
                        "            \"Formatter2\" [#/1/SpreadsheetName123/metadata/formatters/save/https://example.com/info2%20formatter2,https://example.com/info3%20formatter3,https://example.com/info4%20formatter4] id=base-id-123-enable-1-Link\n"
        );
    }

    private EnablePluginInfoSetComponentContext context() {
        return new FakeEnablePluginInfoSetComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.metadataPropertySelect(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName123"),
                        SpreadsheetMetadataPropertyName.FORMATTERS
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<EnablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet>> type() {
        return Cast.to(EnablePluginInfoSetComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
