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
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

public final class AddPluginAliasSetLikeComponentTest implements HtmlComponentTesting<AddPluginAliasSetLikeComponent<ExpressionFunctionName,
    ExpressionFunctionInfo,
    ExpressionFunctionInfoSet,
    ExpressionFunctionSelector,
    ExpressionFunctionAlias,
    ExpressionFunctionAliasSet>,
    HTMLDivElement> {

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
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // provider
            CONTEXT
        );

        // all aliases(name1, name2, name3) already present no links will be added
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshNoneAddedWithAliases() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("alias1 name1"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1"), // provider
            CONTEXT
        );

        // all aliases(alias1) already present no links will be added
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshNoneAddedWithAliases2() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("alias1 name1, name2, name3"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // provider
            CONTEXT
        );

        // all aliases(name1, name2, name3) no need to create any add links
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n"
        );
    }

    @Test
    public void testRefreshSomeAdded() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3, name4"), // provider
            this.context()
        );

        // links to add name3 and name4 are needed
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2,%20name3,%20name4] id=base-id-123-add-0-Link\n" +
                "            \"Name3\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2,%20name3] id=base-id-123-add-1-Link\n" +
                "            \"Name4\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2,%20name4] id=base-id-123-add-2-Link\n"
        );
    }

    @Test
    public void testRefreshAllAdded() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet(""), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1"), // provider
            CONTEXT
        );

        // name1 add link needed
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1] id=base-id-123-add-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1] id=base-id-123-add-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllAdded2() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet(""), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3"), // provider
            CONTEXT
        );

        // need to create links for all aliases: name1, name2, name3
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2,%20name3] id=base-id-123-add-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1] id=base-id-123-add-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name2] id=base-id-123-add-2-Link\n" +
                "            \"Name3\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name3] id=base-id-123-add-3-Link\n"
        );
    }

    @Test
    public void testRefreshAllAddedWithFilter() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.setFilter((t) -> t.toString().contains("name"));
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet(""), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, hidden3"), // provider
            CONTEXT
        );

        // name1 add link needed
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2] id=base-id-123-add-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1] id=base-id-123-add-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name2] id=base-id-123-add-2-Link\n"
        );
    }

    @Test
    public void testRefreshSomeAddedIncludesAliases1() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("alias1 name1"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2"), // provider
            CONTEXT
        );

        // add links needed for name2
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2] id=base-id-123-add-0-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias1%20name1,%20name2] id=base-id-123-add-1-Link\n"
        );
    }

    @Test
    public void testRefreshSomeAddedIncludesAliases2() {
        final AddPluginAliasSetLikeComponent<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> component = AddPluginAliasSetLikeComponent.empty("base-id-123-add-");
        component.refresh(
            SpreadsheetExpressionFunctions.parseAliasSet("name1, alias2 name2, alias3 name3 https://example.com/name3"), // present
            SpreadsheetExpressionFunctions.parseAliasSet("name1, name2, name3, name4"), // provider
            CONTEXT
        );

        // need to add link for name4
        this.treePrintAndCheck(
            component,
            "AddPluginAliasSetLikeComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/name1,%20name2,%20name3,%20name4] id=base-id-123-add-0-Link\n" +
                "            \"Name4\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/alias2%20name2,%20alias3%20name3%20https://example.com/name3%20,%20name1,%20name4] id=base-id-123-add-1-Link\n"
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
