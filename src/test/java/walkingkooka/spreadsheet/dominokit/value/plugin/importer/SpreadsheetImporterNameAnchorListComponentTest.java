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

package walkingkooka.spreadsheet.dominokit.value.plugin.importer;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetImporterNameAnchorListComponentTest implements ValueComponentTesting<HTMLDivElement, SpreadsheetImporterName, SpreadsheetImporterNameAnchorListComponent>,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "SpreadsheetImporterNameAnchorListComponent\n" +
                "  PluginNameAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=TestId123-links\n" +
                "            \"Apple Tree\" [#/123/SpreadsheetName456/spreadsheet/importers/save/apple-tree] id=TestId123-apple-tree-Link\n" +
                "            \"Banana\" [#/123/SpreadsheetName456/spreadsheet/importers/save/banana] id=TestId123-banana-Link\n" +
                "            \"Carrot\" [#/123/SpreadsheetName456/spreadsheet/importers/save/carrot] id=TestId123-carrot-Link\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetImporterName.with("carrot")
                    )
                ),
            "SpreadsheetImporterNameAnchorListComponent\n" +
                "  PluginNameAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=TestId123-links\n" +
                "            \"Apple Tree\" [#/123/SpreadsheetName456/spreadsheet/importers/save/apple-tree] id=TestId123-apple-tree-Link\n" +
                "            \"Banana\" [#/123/SpreadsheetName456/spreadsheet/importers/save/banana] id=TestId123-banana-Link\n" +
                "            \"Carrot\" [#/123/SpreadsheetName456/spreadsheet/importers/save/carrot] id=TestId123-carrot-Link\n"
        );
    }

    @Override
    public SpreadsheetImporterNameAnchorListComponent createComponent() {
        return SpreadsheetImporterNameAnchorListComponent.with(
            "TestId123-",
            new FakeSpreadsheetImporterNameAnchorListComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.metadataPropertySelect(
                        SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetMetadataPropertyName.IMPORTERS
                    );
                }

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return METADATA_EN_AU.set(
                        SpreadsheetMetadataPropertyName.IMPORTERS,
                        SpreadsheetImporterAliasSet.parse("apple-tree, banana, carrot")
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetImporterNameAnchorListComponent> type() {
        return SpreadsheetImporterNameAnchorListComponent.class;
    }
}
