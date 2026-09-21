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

package walkingkooka.spreadsheet.dominokit.value.plugin.export;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetExporterNameAnchorListComponentTest implements ValueComponentTesting<HTMLDivElement, SpreadsheetExporterName, SpreadsheetExporterNameAnchorListComponent>,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "SpreadsheetExporterNameAnchorListComponent\n" +
                "  PluginNameAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=TestId123-links\n" +
                "            \"Apple Tree\" [#/123/SpreadsheetName456/spreadsheet/exporters/save/apple-tree] id=TestId123-apple-tree-Link\n" +
                "            \"Banana\" [#/123/SpreadsheetName456/spreadsheet/exporters/save/banana] id=TestId123-banana-Link\n" +
                "            \"Carrot\" [#/123/SpreadsheetName456/spreadsheet/exporters/save/carrot] id=TestId123-carrot-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetExporterName.with("carrot")
                    )
                ),
            "SpreadsheetExporterNameAnchorListComponent\n" +
                "  PluginNameAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=TestId123-links\n" +
                "            \"Apple Tree\" [#/123/SpreadsheetName456/spreadsheet/exporters/save/apple-tree] id=TestId123-apple-tree-Link\n" +
                "            \"Banana\" [#/123/SpreadsheetName456/spreadsheet/exporters/save/banana] id=TestId123-banana-Link\n" +
                "            \"Carrot\" [#/123/SpreadsheetName456/spreadsheet/exporters/save/carrot] id=TestId123-carrot-Link\n"
        );
    }

    @Override
    public SpreadsheetExporterNameAnchorListComponent createComponent() {
        return SpreadsheetExporterNameAnchorListComponent.with(
            "TestId123-",
            new FakeSpreadsheetExporterNameAnchorListComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.metadataPropertySelect(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetMetadataPropertyName.EXPORTERS
                    );
                }

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return METADATA_EN_AU.set(
                        SpreadsheetMetadataPropertyName.EXPORTERS,
                        SpreadsheetExporterAliasSet.parse("apple-tree, banana, carrot")
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExporterNameAnchorListComponent> type() {
        return SpreadsheetExporterNameAnchorListComponent.class;
    }
}
