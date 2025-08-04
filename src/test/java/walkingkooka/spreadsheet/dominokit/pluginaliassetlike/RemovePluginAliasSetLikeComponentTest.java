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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

public final class RemovePluginAliasSetLikeComponentTest implements HtmlComponentTesting<RemovePluginAliasSetLikeComponent<ExpressionFunctionName,
    ExpressionFunctionInfo,
    ExpressionFunctionInfoSet,
    ExpressionFunctionSelector,
    ExpressionFunctionAlias,
    ExpressionFunctionAliasSet>,
    HTMLDivElement> {

    private final static RemovePluginAliasSetLikeComponentContext CONTEXT = new FakeRemovePluginAliasSetLikeComponentContext() {
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
    public void testRefreshNoneRemovable() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.refresh(
            SpreadsheetExpressionFunctions.EMPTY_ALIAS_SET, // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // provider
            CONTEXT
        );

        // all disabled no need to create any remove links
        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshSomeRemovable() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3, name4"), // provider
            this.context()
        );

        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name2] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1] id=base-id-123-remove-2-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovable() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2,%20name3] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name2,%20name3] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name3] id=base-id-123-remove-2-Link\n" +
                "            \"Name3\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2] id=base-id-123-remove-3-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovableWithFilter() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.setFilter((t) -> t.toString().contains("name"));
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, missing3"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, missing3"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/missing3,%20name1,%20name2] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/missing3,%20name2] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/missing3,%20name1] id=base-id-123-remove-2-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovableIncludesAliases() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("alias1 name1"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias1%20name1] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/] id=base-id-123-remove-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovableIncludesAliases2() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("alias1 name1"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias1%20name1] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/] id=base-id-123-remove-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovableIncludesAliases3() {
        final RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = RemovePluginAliasSetLikeComponent.empty("base-id-123-remove-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, alias2 name2, alias3 name3 https://example.com/name3"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginAliasSetLikeComponent\n" +
                "  SpreadsheetCard\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias2%20name2,%20alias3%20name3%20https://example.com/name3%20,%20name1] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias2%20name2,%20alias3%20name3%20https://example.com/name3] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias3%20name3%20https://example.com/name3%20,%20name1] id=base-id-123-remove-2-Link\n" +
                "            \"Name3\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias2%20name2,%20name1] id=base-id-123-remove-3-Link\n"
        );
    }

    private RemovePluginAliasSetLikeComponentContext context() {
        return CONTEXT;
    }

    // class............................................................................................................

    @Override
    public Class<RemovePluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet>> type() {
        return Cast.to(RemovePluginAliasSetLikeComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
