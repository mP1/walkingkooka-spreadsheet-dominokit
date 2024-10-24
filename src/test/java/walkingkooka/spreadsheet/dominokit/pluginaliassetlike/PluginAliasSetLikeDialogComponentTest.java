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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.function.ExpressionFunctionAliasSetComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

import java.util.Arrays;
import java.util.function.Consumer;

public final class PluginAliasSetLikeDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<PluginAliasSetLikeDialogComponent<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliasSet>>,
        HistoryTokenTesting,
        SpreadsheetMetadataTesting {

    private final static ExpressionFunctionAlias ALIAS1 = ExpressionFunctionAlias.parse("name1");

    private final static ExpressionFunctionAlias ALIAS2 = ExpressionFunctionAlias.parse("name2");

    private final static ExpressionFunctionAlias ALIAS3 = ExpressionFunctionAlias.parse("name3");

    @Test
    public void testEmptyTextRefreshAndTreePrint() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/formula-functions"),
                ExpressionFunctionAliasSet.with(
                        SortedSets.of(
                                ALIAS1,
                                ALIAS2
                        )
                )
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                PluginAliasSetLikeDialogComponent.with(
                        this.dialogContext(context)
                ),
                "",
                context,
                "PluginAliasSetLikeDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    ExpressionFunctions123\n" +
                        "    id=PluginAliasSetLikeDialogComponent includeClose=true\n" +
                        "      AddPluginAliasSetLikeComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Add\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Name1\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1] id=PluginAliasSetLikeDialogComponent-add-0-Link\n" +
                        "                  \"Name2\" [#/1/Spreadsheet123/metadata/formula-functions/save/name2] id=PluginAliasSetLikeDialogComponent-add-1-Link\n" +
                        "                  \"Name3\" [#/1/Spreadsheet123/metadata/formula-functions/save/name3] id=PluginAliasSetLikeDialogComponent-add-2-Link\n" +
                        "      RemovePluginAliasSetLikeComponent\n" +
                        "      ExpressionFunctionAliasSetComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [] id=PluginAliasSetLikeDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/formula-functions/save/] id=PluginAliasSetLikeDialogComponent-save-Link\n" +
                        "          \"Reset\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1,%20name2,%20name3] id=PluginAliasSetLikeDialogComponent-reset-Link\n" +
                        "          \"Remove All\" [#/1/Spreadsheet123/metadata/formula-functions/save/] id=PluginAliasSetLikeDialogComponent-remove all-Link\n" +
                        "          \"Add All\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1,%20name2,%20name3] id=PluginAliasSetLikeDialogComponent-add all-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=PluginAliasSetLikeDialogComponent-close-Link\n"
        );
    }

    @Test
    public void testRefreshAndTreePrint() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/formula-functions/"),
                ExpressionFunctionAliasSet.with(
                        SortedSets.of(
                                ALIAS1,
                                ALIAS2
                        )
                )
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                PluginAliasSetLikeDialogComponent.with(
                        this.dialogContext(context)
                ),
                ExpressionFunctionAliasSet.with(
                        SortedSets.of(
                                ALIAS1
                        )
                ).text(),
                context,
                "PluginAliasSetLikeDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    ExpressionFunctions123\n" +
                        "    id=PluginAliasSetLikeDialogComponent includeClose=true\n" +
                        "      AddPluginAliasSetLikeComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Add\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Name2\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1,%20name2] id=PluginAliasSetLikeDialogComponent-add-0-Link\n" +
                        "                  \"Name3\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1,%20name3] id=PluginAliasSetLikeDialogComponent-add-1-Link\n" +
                        "      RemovePluginAliasSetLikeComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Name1\" [#/1/Spreadsheet123/metadata/formula-functions/save/] id=PluginAliasSetLikeDialogComponent-remove-0-Link\n" +
                        "      ExpressionFunctionAliasSetComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [name1] id=PluginAliasSetLikeDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1] id=PluginAliasSetLikeDialogComponent-save-Link\n" +
                        "          \"Reset\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1,%20name2,%20name3] id=PluginAliasSetLikeDialogComponent-reset-Link\n" +
                        "          \"Remove All\" [#/1/Spreadsheet123/metadata/formula-functions/save/] id=PluginAliasSetLikeDialogComponent-remove all-Link\n" +
                        "          \"Add All\" [#/1/Spreadsheet123/metadata/formula-functions/save/name1,%20name2,%20name3] id=PluginAliasSetLikeDialogComponent-add all-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=PluginAliasSetLikeDialogComponent-close-Link\n"
        );
    }

    private void onHistoryTokenChangeAndSetTextAndCheck(final PluginAliasSetLikeDialogComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> dialog,
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

    private PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> dialogContext(final AppContext context) {
        return new FakePluginAliasSetLikeDialogComponentContext<>() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return context.addHistoryTokenWatcher(watcher);
            }

            @Override
            public HistoryToken historyToken() {
                return context.historyToken();
            }

            // PluginAliasSetLikeDialogComponentContext.................................................................

            @Override
            public String dialogTitle() {
                return "ExpressionFunctions123";
            }

            @Override
            public ExpressionFunctionAliasSetComponent textBox() {
                return ExpressionFunctionAliasSetComponent.empty();
            }

            @Override
            public boolean isMatch(final HistoryToken token) {
                return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                        token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                                .propertyName()
                                .equals(SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS);
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return () -> {
                };
            }

            @Override
            public Runnable addProviderFetcherWatcher(final Consumer<ExpressionFunctionAliasSet> aliases) {
                return () -> {
                };
            }

            @Override
            public void loadProviderInfoSetLike() {
                // dummy server call
            }

            @Override
            public ExpressionFunctionAliasSet parseAliasSetLike(final String text) {
                return ExpressionFunctionAliasSet.parse(text);
            }

            @Override
            public ExpressionFunctionAliasSet emptyAliasSetLike() {
                return ExpressionFunctionAliasSet.EMPTY;
            }

            @Override
            public ExpressionFunctionAliasSet metadataAliasSetLike() {
                return context.spreadsheetMetadata()
                        .getOrFail(SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS);
            }

            @Override
            public ExpressionFunctionAliasSet providerAliasSetLike() {
                return ExpressionFunctionAliasSet.with(
                        SortedSets.of(
                                ALIAS1,
                                ALIAS2,
                                ALIAS3
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
                                  final ExpressionFunctionAliasSet infos) {
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
                        SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS,
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
    public Class<PluginAliasSetLikeDialogComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet>> type() {
        return Cast.to(PluginAliasSetLikeDialogComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}