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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

public final class AddPluginAliasSetLikeComponentTest implements ClassTesting<AddPluginAliasSetLikeComponent<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliasSet>>,
        TreePrintableTesting {

    private final static AddPluginAliasSetLikeComponentContext CONTEXT = new FakeAddPluginAliasSetLikeComponentContext() {
        @Override
        public HistoryToken historyToken() {
            return HistoryToken.metadataPropertySelect(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName123"),
                    SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS
            );
        }
    };

    @Test
    public void testRefreshNoneAdded() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse("name1, name2, name3"), // present
                ExpressionFunctionAliasSet.parse("name1, name2, name3"), // provider
                CONTEXT
        );

        // all disabled no need to create any add links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshNoneAddedWithAliases() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse("alias1 name1"), // present
                ExpressionFunctionAliasSet.parse("name1"), // provider
                CONTEXT
        );

        // all disabled no need to create any add links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshNoneAddedWithAliases2() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse("alias1 name1, name2, name3"), // present
                ExpressionFunctionAliasSet.parse("name1, name2, name3"), // provider
                CONTEXT
        );

        // all disabled no need to create any add links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshSomeAdded() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse("name1, name2"), // present
                ExpressionFunctionAliasSet.parse("name1, name2, name3, name4"), // provider
                this.context()
        );

        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Add\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Name3\" [#/1/SpreadsheetName123/metadata/formula-functions/save/name1,%20name2,%20name3] id=base-id-123-add-0-Link\n" +
                        "            \"Name4\" [#/1/SpreadsheetName123/metadata/formula-functions/save/name1,%20name2,%20name4] id=base-id-123-add-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllAdded() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse(""), // present
                ExpressionFunctionAliasSet.parse("name1"), // provider
                CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Add\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Name1\" [#/1/SpreadsheetName123/metadata/formula-functions/save/name1] id=base-id-123-add-0-Link\n"
        );
    }

    @Test
    public void testRefreshAllAdded2() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse(""), // present
                ExpressionFunctionAliasSet.parse("name1, name2, name3"), // provider
                CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Add\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Name1\" [#/1/SpreadsheetName123/metadata/formula-functions/save/name1] id=base-id-123-add-0-Link\n" +
                        "            \"Name2\" [#/1/SpreadsheetName123/metadata/formula-functions/save/name2] id=base-id-123-add-1-Link\n" +
                        "            \"Name3\" [#/1/SpreadsheetName123/metadata/formula-functions/save/name3] id=base-id-123-add-2-Link\n"
        );
    }

    @Test
    public void testRefreshSomeAddedIncludesAliases1() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse("alias1 name1"), // present
                ExpressionFunctionAliasSet.parse("name1, name2"), // provider
                CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Add\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Name2\" [#/1/SpreadsheetName123/metadata/formula-functions/save/alias1%20name1,%20name2] id=base-id-123-add-0-Link\n"
        );
    }

    @Test
    public void testRefreshSomeAddedIncludesAliases2() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-");
        component.refresh(
                ExpressionFunctionAliasSet.parse("name1, alias2 name2, alias3 name3 https://example.com/name3"), // present
                ExpressionFunctionAliasSet.parse("name1, name2, name3, name4"), // provider
                CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
                component,
                "AddPluginAliasSetLikeComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Add\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"Name4\" [#/1/SpreadsheetName123/metadata/formula-functions/save/alias2%20name2,%20alias3%20name3%20https://example.com/name3%20,%20name1,%20name4] id=base-id-123-add-0-Link\n"
        );
    }

    private AddPluginAliasSetLikeComponentContext context() {
        return CONTEXT;
    }

    // class............................................................................................................

    @Override
    public Class<AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet>> type() {
        return Cast.to(AddPluginAliasSetLikeComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
