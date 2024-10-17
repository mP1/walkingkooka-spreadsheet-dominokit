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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAlias;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Arrays;
import java.util.function.Consumer;

public final class PluginInfoSetDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<PluginInfoSetDialogComponent<SpreadsheetFormatterName,
        SpreadsheetFormatterInfo,
        SpreadsheetFormatterInfoSet,
        SpreadsheetFormatterSelector,
        SpreadsheetFormatterAlias,
        SpreadsheetFormatterAliasSet>>,
        HistoryTokenTesting,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetFormatterInfo INFO1 = SpreadsheetFormatterInfo.with(
            Url.parseAbsolute("https://example.com/Text111"),
            SpreadsheetFormatterName.with("Text111")
    );

    private final static SpreadsheetFormatterInfo INFO2 = SpreadsheetFormatterInfo.with(
            Url.parseAbsolute("https://example.com/Text222"),
            SpreadsheetFormatterName.with("Text222")
    );

    private final static SpreadsheetFormatterInfo INFO3 = SpreadsheetFormatterInfo.with(
            Url.parseAbsolute("https://example.com/Text333"),
            SpreadsheetFormatterName.with("Text333")
    );

    @Test
    public void testEmptyTextRefreshAndTreePrint() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/formatters"),
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                INFO1,
                                INFO2
                        )
                )
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                PluginInfoSetDialogComponent.with(
                        this.dialogContext(context)
                ),
                "",
                context,
                "PluginInfoSetDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    SpreadsheetFormatters123\n" +
                        "    id=PluginInfoSetDialogComponent includeClose=true\n" +
                        "      EnablePluginInfoSetComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Enable\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Text111\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111] id=PluginInfoSetDialogComponent-enable-enable-0-Link\n" +
                        "                  \"Text222\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text222%20Text222] id=PluginInfoSetDialogComponent-enable-enable-1-Link\n" +
                        "                  \"Text333\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text333%20Text333] id=PluginInfoSetDialogComponent-enable-enable-2-Link\n" +
                        "      DisablePluginInfoSetComponent\n" +
                        "      SpreadsheetFormatterInfoSetComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [] id=PluginInfoSetDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/formatters/save/] id=PluginInfoSetDialogComponent-save-Link\n" +
                        "          \"Reset\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetDialogComponent-reset-Link\n" +
                        "          \"Disable All\" [#/1/Spreadsheet123/metadata/formatters/save/] id=PluginInfoSetDialogComponent-disable all-Link\n" +
                        "          \"Enable All\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetDialogComponent-enable all-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=PluginInfoSetDialogComponent-close-Link\n"
        );
    }

    @Test
    public void testRefreshAndTreePrint() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/formatters"),
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                INFO1,
                                INFO2
                        )
                )
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                PluginInfoSetDialogComponent.with(
                        this.dialogContext(context)
                ),
                SpreadsheetFormatterInfoSet.with(
                        Sets.of(
                                INFO1
                        )
                ).text(),
                context,
                "PluginInfoSetDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    SpreadsheetFormatters123\n" +
                        "    id=PluginInfoSetDialogComponent includeClose=true\n" +
                        "      EnablePluginInfoSetComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Enable\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Text222\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222] id=PluginInfoSetDialogComponent-enable-enable-0-Link\n" +
                        "                  \"Text333\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111,https://example.com/Text333%20Text333] id=PluginInfoSetDialogComponent-enable-enable-1-Link\n" +
                        "      DisablePluginInfoSetComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Disable\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Text111\" [#/1/Spreadsheet123/metadata/formatters/save/] id=PluginInfoSetDialogComponent-disable-disable-0-Link\n" +
                        "      SpreadsheetFormatterInfoSetComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [https://example.com/Text111 Text111] id=PluginInfoSetDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111] id=PluginInfoSetDialogComponent-save-Link\n" +
                        "          \"Reset\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetDialogComponent-reset-Link\n" +
                        "          \"Disable All\" [#/1/Spreadsheet123/metadata/formatters/save/] id=PluginInfoSetDialogComponent-disable all-Link\n" +
                        "          \"Enable All\" [#/1/Spreadsheet123/metadata/formatters/save/https://example.com/Text111%20Text111,https://example.com/Text222%20Text222,https://example.com/Text333%20Text333] id=PluginInfoSetDialogComponent-enable all-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=PluginInfoSetDialogComponent-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndSetTextAndCheck(final PluginInfoSetDialogComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet, SpreadsheetFormatterSelector, SpreadsheetFormatterAlias, SpreadsheetFormatterAliasSet> dialog,
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

    private PluginInfoSetDialogComponentContext<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet, SpreadsheetFormatterSelector, SpreadsheetFormatterAlias, SpreadsheetFormatterAliasSet> dialogContext(final AppContext context) {
        return new FakePluginInfoSetDialogComponentContext<>() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return context.addHistoryTokenWatcher(watcher);
            }

            @Override
            public HistoryToken historyToken() {
                return context.historyToken();
            }

            // PluginInfoSetDialogComponentContext......................................................

            @Override
            public String dialogTitle() {
                return "SpreadsheetFormatters123";
            }

            @Override
            public ValueSpreadsheetTextBoxWrapper textBox() {
                return SpreadsheetFormatterInfoSetComponent.empty();
            }

            @Override
            public boolean isMatch(final HistoryToken token) {
                return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                        token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                                .propertyName()
                                .equals(SpreadsheetMetadataPropertyName.FORMATTERS);
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return () -> {
                };
            }

            @Override
            public Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetFormatterInfoSet> infos) {
                return () -> {
                };
            }

            @Override
            public void loadProviderInfoSet() {
                // dummy server call
            }

            @Override
            public SpreadsheetFormatterInfoSet parse(final String text) {
                return SpreadsheetFormatterInfoSet.parse(text);
            }

            @Override
            public SpreadsheetFormatterInfoSet emptyInfoSet() {
                return SpreadsheetFormatterInfoSet.EMPTY;
            }

            @Override
            public SpreadsheetFormatterInfoSet metadataInfoSet() {
                return context.spreadsheetMetadata()
                        .getOrFail(SpreadsheetMetadataPropertyName.FORMATTERS);
            }

            @Override
            public SpreadsheetFormatterInfoSet providerInfoSet() {
                return SpreadsheetFormatterInfoSet.with(
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
                                  final SpreadsheetFormatterInfoSet infos) {
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
                        SpreadsheetMetadataPropertyName.FORMATTERS,
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
    public Class<PluginInfoSetDialogComponent<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet, SpreadsheetFormatterSelector, SpreadsheetFormatterAlias, SpreadsheetFormatterAliasSet>> type() {
        return Cast.to(PluginInfoSetDialogComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
