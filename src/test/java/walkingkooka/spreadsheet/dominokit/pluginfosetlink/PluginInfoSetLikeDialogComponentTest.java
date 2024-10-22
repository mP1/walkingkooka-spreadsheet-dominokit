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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;

import java.util.Arrays;
import java.util.function.Consumer;

public final class PluginInfoSetLikeDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<PluginInfoSetLikeDialogComponent<SpreadsheetParserName,
        SpreadsheetParserInfo,
        SpreadsheetParserInfoSet,
        SpreadsheetParserSelector,
        SpreadsheetParserAlias,
        SpreadsheetParserAliasSet>>,
        HistoryTokenTesting,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetParserInfo INFO1 = SpreadsheetParserInfo.with(
            Url.parseAbsolute("https://example.com/Text111"),
            SpreadsheetParserName.with("Text111")
    );

    private final static SpreadsheetParserInfo INFO2 = SpreadsheetParserInfo.with(
            Url.parseAbsolute("https://example.com/Text222"),
            SpreadsheetParserName.with("Text222")
    );

    private final static SpreadsheetParserInfo INFO3 = SpreadsheetParserInfo.with(
            Url.parseAbsolute("https://example.com/Text333"),
            SpreadsheetParserName.with("Text333")
    );

    @Test
    public void testEmptyTextRefreshAndTreePrint() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/parsers"),
                SpreadsheetParserInfoSet.with(
                        Sets.of(
                                INFO1,
                                INFO2
                        )
                )
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                PluginInfoSetLikeDialogComponent.with(
                        this.dialogContext(context)
                ),
                "",
                context,
                "PluginInfoSetLikeDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    SpreadsheetParsers123\n" +
                        "    id=PluginInfoSetLikeDialogComponent includeClose=true\n" +
                        "      EnablePluginInfoSetLikeComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Enable\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Text111\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111] id=PluginInfoSetLikeDialogComponent-enable-0-Link\n" +
                        "                  \"Text222\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text222%20Text222] id=PluginInfoSetLikeDialogComponent-enable-1-Link\n" +
                        "                  \"Text333\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text333%20Text333] id=PluginInfoSetLikeDialogComponent-enable-2-Link\n" +
                        "      DisablePluginInfoSetLikeComponent\n" +
                        "      SpreadsheetParserInfoSetComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [] id=PluginInfoSetLikeDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/parsers/save/] id=PluginInfoSetLikeDialogComponent-save-Link\n" +
                        "          \"Reset\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetLikeDialogComponent-reset-Link\n" +
                        "          \"Disable All\" [#/1/Spreadsheet123/metadata/parsers/save/] id=PluginInfoSetLikeDialogComponent-disable all-Link\n" +
                        "          \"Enable All\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetLikeDialogComponent-enable all-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=PluginInfoSetLikeDialogComponent-close-Link\n"
        );
    }

    @Test
    public void testRefreshAndTreePrint() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/parsers"),
                SpreadsheetParserInfoSet.with(
                        Sets.of(
                                INFO1,
                                INFO2
                        )
                )
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                PluginInfoSetLikeDialogComponent.with(
                        this.dialogContext(context)
                ),
                SpreadsheetParserInfoSet.with(
                        Sets.of(
                                INFO1
                        )
                ).text(),
                context,
                "PluginInfoSetLikeDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    SpreadsheetParsers123\n" +
                        "    id=PluginInfoSetLikeDialogComponent includeClose=true\n" +
                        "      EnablePluginInfoSetLikeComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Enable\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Text222\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222] id=PluginInfoSetLikeDialogComponent-enable-0-Link\n" +
                        "                  \"Text333\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111,https://example.com/Text333%20Text333] id=PluginInfoSetLikeDialogComponent-enable-1-Link\n" +
                        "      DisablePluginInfoSetLikeComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Disable\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Text111\" [#/1/Spreadsheet123/metadata/parsers/save/] id=PluginInfoSetLikeDialogComponent-disable-0-Link\n" +
                        "      SpreadsheetParserInfoSetComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [https://example.com/Text111 Text111] id=PluginInfoSetLikeDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111] id=PluginInfoSetLikeDialogComponent-save-Link\n" +
                        "          \"Reset\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetLikeDialogComponent-reset-Link\n" +
                        "          \"Disable All\" [#/1/Spreadsheet123/metadata/parsers/save/] id=PluginInfoSetLikeDialogComponent-disable all-Link\n" +
                        "          \"Enable All\" [#/1/Spreadsheet123/metadata/parsers/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetLikeDialogComponent-enable all-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=PluginInfoSetLikeDialogComponent-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndSetTextAndCheck(final PluginInfoSetLikeDialogComponent<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> dialog,
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

    private PluginInfoSetLikeDialogComponentContext<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> dialogContext(final AppContext context) {
        return new FakePluginInfoSetLikeDialogComponentContext<>() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return context.addHistoryTokenWatcher(watcher);
            }

            @Override
            public HistoryToken historyToken() {
                return context.historyToken();
            }

            // PluginInfoSetLikeDialogComponentContext......................................................

            @Override
            public String dialogTitle() {
                return "SpreadsheetParsers123";
            }

            @Override
            public ValueSpreadsheetTextBoxWrapper textBox() {
                return SpreadsheetParserInfoSetComponent.empty();
            }

            @Override
            public boolean isMatch(final HistoryToken token) {
                return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                        token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                                .propertyName()
                                .equals(SpreadsheetMetadataPropertyName.PARSERS);
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return () -> {
                };
            }

            @Override
            public Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetParserInfoSet> infos) {
                return () -> {
                };
            }

            @Override
            public void loadProviderInfoSetLike() {
                // dummy server call
            }

            @Override
            public SpreadsheetParserInfoSet parseInfoSetLike(final String text) {
                return SpreadsheetParserInfoSet.parse(text);
            }

            @Override
            public SpreadsheetParserInfoSet emptyInfoSetLike() {
                return SpreadsheetParserInfoSet.EMPTY;
            }

            @Override
            public SpreadsheetParserInfoSet metadataInfoSetLike() {
                return context.spreadsheetMetadata()
                        .getOrFail(SpreadsheetMetadataPropertyName.PARSERS);
            }

            @Override
            public SpreadsheetParserInfoSet providerInfoSetLike() {
                return SpreadsheetParserInfoSet.with(
                        Sets.of(
                                INFO1,
                                INFO2,
                                INFO3
                        )
                );
            }

            @Override
            public void debug(final Object... values) {
                context.debug(values);
            }

            @Override
            public void error(final Object... values) {
                context.error(values);
            }
        };
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final SpreadsheetParserInfoSet infos) {
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
                        SpreadsheetMetadataPropertyName.PARSERS,
                        infos
                );
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

    // class............................................................................................................

    @Override
    public Class<PluginInfoSetLikeDialogComponent<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet>> type() {
        return Cast.to(PluginInfoSetLikeDialogComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
