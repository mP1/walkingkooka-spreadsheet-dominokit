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

package walkingkooka.spreadsheet.dominokit.pluginfosetlink;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.text.printer.TreePrintableTesting;

public final class EnablePluginInfoSetLikeComponentTest implements ClassTesting<EnablePluginInfoSetLikeComponent<SpreadsheetParserName,
        SpreadsheetParserInfo,
        SpreadsheetParserInfoSet,
        SpreadsheetParserSelector,
        SpreadsheetParserAlias,
        SpreadsheetParserAliasSet>>,
        TreePrintableTesting {

    @Test
    public void testRefreshAllDisabled() {
        final SpreadsheetParserInfo info1 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetParserName.with("parser1")
        );

        final SpreadsheetParserInfo info2 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetParserName.with("parser2")
        );


        final EnablePluginInfoSetLikeComponent<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> component = EnablePluginInfoSetLikeComponent.empty("base-id-123-enable-");
        component.refresh(
                SpreadsheetParserInfoSet.EMPTY, // enabled
                SpreadsheetParserInfoSet.with(
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
                "EnablePluginInfoSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Enable\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Parser1\" [#/1/SpreadsheetName123/metadata/parsers/save/https://example.com/info1%20parser1] id=base-id-123-enable-0-Link\n" +
                        "            \"Parser2\" [#/1/SpreadsheetName123/metadata/parsers/save/https://example.com/info2%20parser2] id=base-id-123-enable-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllEnabled() {
        final SpreadsheetParserInfo info1 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetParserName.with("parser1")
        );

        final SpreadsheetParserInfo info2 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetParserName.with("parser2")
        );


        final EnablePluginInfoSetLikeComponent<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> component = EnablePluginInfoSetLikeComponent.empty("base-id-123-enable-");
        component.refresh(
                SpreadsheetParserInfoSet.with(
                        Sets.of(
                                info1,
                                info2
                        )
                ), // enabled
                SpreadsheetParserInfoSet.with(
                        Sets.of(
                                info1,
                                info2
                        )
                ), // provider
                new FakeEnablePluginInfoSetLikeComponentContext()
        );

        // all infos are already enabled links list should be empty
        this.treePrintAndCheck(
                component,
                "EnablePluginInfoSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshSomeEnabled() {
        final SpreadsheetParserInfo info1 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info1"),
                SpreadsheetParserName.with("parser1")
        );

        final SpreadsheetParserInfo info2 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info2"),
                SpreadsheetParserName.with("parser2")
        );

        final SpreadsheetParserInfo info3 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info3"),
                SpreadsheetParserName.with("parser3")
        );

        final SpreadsheetParserInfo info4 = SpreadsheetParserInfo.with(
                Url.parseAbsolute("https://example.com/info4"),
                SpreadsheetParserName.with("parser4")
        );

        final EnablePluginInfoSetLikeComponent<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> component = EnablePluginInfoSetLikeComponent.empty("base-id-123-enable-");
        component.refresh(
                SpreadsheetParserInfoSet.with(
                        Sets.of(
                                info3,
                                info4
                        )
                ), // enabled
                SpreadsheetParserInfoSet.with(
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
                "EnablePluginInfoSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Enable\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Parser1\" [#/1/SpreadsheetName123/metadata/parsers/save/https://example.com/info1%20parser1,https://example.com/info3%20parser3,https://example.com/info4%20parser4] id=base-id-123-enable-0-Link\n" +
                        "            \"Parser2\" [#/1/SpreadsheetName123/metadata/parsers/save/https://example.com/info2%20parser2,https://example.com/info3%20parser3,https://example.com/info4%20parser4] id=base-id-123-enable-1-Link\n"
        );
    }

    private EnablePluginInfoSetLikeComponentContext context() {
        return new FakeEnablePluginInfoSetLikeComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.metadataPropertySelect(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName123"),
                        SpreadsheetMetadataPropertyName.PARSERS
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<EnablePluginInfoSetLikeComponent<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet>> type() {
        return Cast.to(EnablePluginInfoSetLikeComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
