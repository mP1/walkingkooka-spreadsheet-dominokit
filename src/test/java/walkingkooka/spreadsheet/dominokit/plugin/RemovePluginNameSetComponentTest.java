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
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class RemovePluginNameSetComponentTest implements HtmlComponentTesting<RemovePluginNameSetComponent,
    HTMLDivElement> {

    private final static RemovePluginNameSetComponentContext CONTEXT = new FakeRemovePluginNameSetComponentContext() {
        @Override
        public HistoryToken historyToken() {
            return HistoryToken.metadataPropertySelect(
                SpreadsheetId.with(1),
                SpreadsheetName.with("SpreadsheetName123"),
                SpreadsheetMetadataPropertyName.PLUGINS
            );
        }
    };

    @Test
    public void testRefreshNoneRemovable() {
        final RemovePluginNameSetComponent component = RemovePluginNameSetComponent.empty("base-id-123-remove-");
        component.refresh(
            PluginNameSet.EMPTY, // present
            PluginNameSet.parse("name1, name2, name3"), // provider
            CONTEXT
        );

        // all disabled no need to create any remove links
        this.treePrintAndCheck(
            component,
            "RemovePluginNameSetComponent\n"
        );
    }

    @Test
    public void testRefreshSomeRemovable() {
        final RemovePluginNameSetComponent component = RemovePluginNameSetComponent.empty("base-id-123-remove-");
        component.refresh(
            PluginNameSet.parse("name1, name2"), // present
            PluginNameSet.parse("name1, name2, name3, name4"), // provider
            this.context()
        );

        this.treePrintAndCheck(
            component,
            "RemovePluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name1,name2] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name2] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name1] id=base-id-123-remove-2-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovable() {
        final RemovePluginNameSetComponent component = RemovePluginNameSetComponent.empty("base-id-123-remove-");
        component.refresh(
            PluginNameSet.parse("name1, name2, name3"), // present
            PluginNameSet.parse("name1, name2, name3"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name1,name2,name3] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name2,name3] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name1,name3] id=base-id-123-remove-2-Link\n" +
                "            \"Name3\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/name1,name2] id=base-id-123-remove-3-Link\n"
        );
    }

    @Test
    public void testRefreshAllRemovableWithFilter() {
        final RemovePluginNameSetComponent component = RemovePluginNameSetComponent.empty("base-id-123-remove-");
        component.setFilter((t) -> t.toString().contains("name"));
        component.refresh(
            PluginNameSet.parse("name1, name2, missing3"), // present
            PluginNameSet.parse("name1, name2, missing3"), // provider
            CONTEXT
        );

        // all disabled no need to create any enable links
        this.treePrintAndCheck(
            component,
            "RemovePluginNameSetComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      Remove\n" +
                "        SpreadsheetFlexLayout\n" +
                "          ROW\n" +
                "            \"*\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/missing3,name1,name2] id=base-id-123-remove-0-Link\n" +
                "            \"Name1\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/missing3,name2] id=base-id-123-remove-1-Link\n" +
                "            \"Name2\" [#/1/SpreadsheetName123/spreadsheet/plugins/save/missing3,name1] id=base-id-123-remove-2-Link\n"
        );
    }

    private RemovePluginNameSetComponentContext context() {
        return CONTEXT;
    }

    // class............................................................................................................

    @Override
    public Class<RemovePluginNameSetComponent> type() {
        return Cast.to(RemovePluginNameSetComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
