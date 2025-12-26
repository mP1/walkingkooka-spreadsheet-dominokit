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
import walkingkooka.Cast;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

public final class AddPluginNameSetComponentTest implements HtmlComponentTesting<AddPluginNameSetComponent,
    HTMLDivElement> {

    private final static AddPluginNameSetComponentContext CONTEXT = new FakeAddPluginNameSetComponentContext() {
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
    public void testRefreshWhenEmpty() {
        final AddPluginNameSetComponent component = AddPluginNameSetComponent.empty("base-id-123-add-");
        component.refresh(
            PluginNameSet.parse("plugin1, plugin2, plugin3"), // present
            PluginNameSet.parse("plugin1, plugin2, plugin3"), // provider
            CONTEXT
        );

        // card will still be visible with filter and "*" link.
        this.treePrintAndCheck(
            component,
            "AddPluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2,%20plugin3] id=base-id-123-add-0-Link\n"
        );
    }

    @Test
    public void testRefreshSomeAdded() {
        final AddPluginNameSetComponent component = AddPluginNameSetComponent.empty("base-id-123-add-");
        component.refresh(
            PluginNameSet.parse("plugin1, plugin2"), // present
            PluginNameSet.parse("plugin1, plugin2, plugin3, plugin4"), // provider
            this.context()
        );

        // links to add plugin3 and plugin4 are needed
        this.treePrintAndCheck(
            component,
            "AddPluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2,%20plugin3,%20plugin4] id=base-id-123-add-0-Link\n" +
                "            \"Plugin3\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2,%20plugin3] id=base-id-123-add-1-Link\n" +
                "            \"Plugin4\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2,%20plugin4] id=base-id-123-add-2-Link\n"
        );
    }

    @Test
    public void testRefreshAllAdded() {
        final AddPluginNameSetComponent component = AddPluginNameSetComponent.empty("base-id-123-add-");
        component.refresh(
            PluginNameSet.parse(""), // present
            PluginNameSet.parse("plugin1"), // provider
            CONTEXT
        );

        // plugin1 add link needed
        this.treePrintAndCheck(
            component,
            "AddPluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1] id=base-id-123-add-0-Link\n" +
                "            \"Plugin1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1] id=base-id-123-add-1-Link\n"
        );
    }

    @Test
    public void testRefreshAllAdded2() {
        final AddPluginNameSetComponent component = AddPluginNameSetComponent.empty("base-id-123-add-");
        component.refresh(
            PluginNameSet.parse(""), // present
            PluginNameSet.parse("plugin1, plugin2, plugin3"), // provider
            CONTEXT
        );

        // need to create links for all plugins: plugin1, plugin2, plugin3
        this.treePrintAndCheck(
            component,
            "AddPluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2,%20plugin3] id=base-id-123-add-0-Link\n" +
                "            \"Plugin1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1] id=base-id-123-add-1-Link\n" +
                "            \"Plugin2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin2] id=base-id-123-add-2-Link\n" +
                "            \"Plugin3\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin3] id=base-id-123-add-3-Link\n"
        );
    }

    @Test
    public void testRefreshAllAddedWithFilter() {
        final AddPluginNameSetComponent component = AddPluginNameSetComponent.empty("base-id-123-add-");
        component.setFilter((t) -> t.toString().contains("plug"));
        component.refresh(
            PluginNameSet.parse(""), // present
            PluginNameSet.parse("plugin1, plugin2, hidden3"), // provider
            CONTEXT
        );

        // plugin1 add link needed
        this.treePrintAndCheck(
            component,
            "AddPluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2] id=base-id-123-add-0-Link\n" +
                "            \"Plugin1\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1] id=base-id-123-add-1-Link\n" +
                "            \"Plugin2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin2] id=base-id-123-add-2-Link\n"
        );
    }

    @Test
    public void testRefreshSomeAddedWithFilter() {
        final AddPluginNameSetComponent component = AddPluginNameSetComponent.empty("base-id-123-add-");
        component.setFilter((t) -> t.toString().contains("plug"));
        component.refresh(
            PluginNameSet.parse("plugin1"), // present
            PluginNameSet.parse("plugin1, plugin2, hidden3"), // provider
            CONTEXT
        );

        // plugin1 add link needed
        this.treePrintAndCheck(
            component,
            "AddPluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Add\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2] id=base-id-123-add-0-Link\n" +
                "            \"Plugin2\" [#/1/SpreadsheetName123/spreadsheet/formulaFunctions/save/plugin1,%20plugin2] id=base-id-123-add-1-Link\n"
        );
    }

    private AddPluginNameSetComponentContext context() {
        return CONTEXT;
    }

    // class............................................................................................................

    @Override
    public Class<AddPluginNameSetComponent> type() {
        return Cast.to(AddPluginNameSetComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
