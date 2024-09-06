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

public final class DisablePluginInfoSetComponentTest implements ClassTesting<DisablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet>>,
        TreePrintableTesting {

    @Test
    public void testRefreshNoneEnabled() {
        final SpreadsheetFormatterInfo info1 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetFormatterName.with("formatter1")
        );

        final SpreadsheetFormatterInfo info2 = SpreadsheetFormatterInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetFormatterName.with("formatter2")
        );


        final DisablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet> component = DisablePluginInfoSetComponent.empty("base-id-123-");
        component.refresh(
                SpreadsheetFormatterInfoSet.with(
                        Sets.empty()
                ), // enabled
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                info1,
                                info2
                        )
                ), // provider
                new FakeDisablePluginInfoSetComponentContext()
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
                component,
                "DisablePluginInfoSetComponent\n"
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

        final DisablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet> component = DisablePluginInfoSetComponent.empty("base-id-123-");
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
                "DisablePluginInfoSetComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Disable\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Formatter3\" [#/1/SpreadsheetName123/metadata/spreadsheet-formatters/save/https://example.com/info4%20formatter4] id=base-id-123-disable-0-Link\n" +
                        "            \"Formatter4\" [#/1/SpreadsheetName123/metadata/spreadsheet-formatters/save/https://example.com/info3%20formatter3] id=base-id-123-disable-1-Link\n"
        );
    }

    private DisablePluginInfoSetComponentContext context() {
        return new FakeDisablePluginInfoSetComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.metadataPropertySelect(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName123"),
                        SpreadsheetMetadataPropertyName.SPREADSHEET_FORMATTERS
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<DisablePluginInfoSetComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet>> type() {
        return Cast.to(DisablePluginInfoSetComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
