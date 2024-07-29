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

package walkingkooka.spreadsheet.dominokit.datatable;

import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetDataTableComponentTest implements ClassTesting<walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent<String>>,
        TreePrintableTesting {

    @Test
    public void testThreeColumnsNotValue() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ),
                "SpreadsheetDataTableComponent\n" +
                        "  COLUMN(S)\n" +
                        "    column-1A-title\n" +
                        "    column-2B-title\n" +
                        "    column-3C-title\n"
        );
    }

    @Test
    public void testThreeColumnsNotValueWithChildren() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ).appendChild(
                        SpreadsheetTextComponent.with(
                                Optional.of("child-1A")
                        )
                ).appendChild(
                        SpreadsheetTextComponent.with(
                                Optional.of("child-2B")
                        )
                ),
                "SpreadsheetDataTableComponent\n" +
                        "  COLUMN(S)\n" +
                        "    column-1A-title\n" +
                        "    column-2B-title\n" +
                        "    column-3C-title\n" +
                        "  CHILDREN\n" +
                        "    SpreadsheetTextComponent\n" +
                        "      \"child-1A\"\n" +
                        "    SpreadsheetTextComponent\n" +
                        "      \"child-2B\"\n"
        );
    }

    @Test
    public void testThreeColumnsThreeRows() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ).setValue(
                        Optional.of(
                                Lists.of(
                                        "ABC",
                                        "DEF",
                                        "GHI"
                                )
                        )
                ),
                "SpreadsheetDataTableComponent\n" +
                        "  COLUMN(S)\n" +
                        "    column-1A-title\n" +
                        "    column-2B-title\n" +
                        "    column-3C-title\n" +
                        "  ROW(S)\n" +
                        "    ROW 0\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"AAA\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"BBB\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"CCC\"\n" +
                        "    ROW 1\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"DDD\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"EEE\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"FFF\"\n" +
                        "    ROW 2\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"GGG\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"HHH\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"III\"\n"
        );
    }

    @Test
    public void testThreeColumnsMixedComponentTypes() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> {
                            switch (column) {
                                case 0:
                                    return SpreadsheetTextComponent.with(
                                            Optional.of(
                                                    data
                                            )
                                    );
                                case 1:
                                    return HistoryTokenAnchorComponent.empty()
                                            .setHistoryToken(
                                                    Optional.of(
                                                            HistoryToken.parseString("/1/SpreadsheetName123/cell/A1")
                                                    )
                                            );
                                case 2:
                                    return SpreadsheetTextBox.empty();
                                default:
                                    throw new IllegalArgumentException("Invalid column " + column);
                            }
                        }
                ).setValue(
                        Optional.of(
                                Lists.of(
                                        "ABC",
                                        "DEF",
                                        "GHI"
                                )
                        )
                ),
                "SpreadsheetDataTableComponent\n" +
                        "  COLUMN(S)\n" +
                        "    column-1A-title\n" +
                        "    column-2B-title\n" +
                        "    column-3C-title\n" +
                        "  ROW(S)\n" +
                        "    ROW 0\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"ABC\"\n" +
                        "      [#/1/SpreadsheetName123/cell/A1]\n" +
                        "      SpreadsheetTextBox\n" +
                        "        []\n" +
                        "    ROW 1\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"DEF\"\n" +
                        "      [#/1/SpreadsheetName123/cell/A1]\n" +
                        "      SpreadsheetTextBox\n" +
                        "        []\n" +
                        "    ROW 2\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"GHI\"\n" +
                        "      [#/1/SpreadsheetName123/cell/A1]\n" +
                        "      SpreadsheetTextBox\n" +
                        "        []\n"
        );
    }

    @Test
    public void testBodyScrollPlugin() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ).bodyScrollPlugin(),
                "SpreadsheetDataTableComponent\n" +
                        "  COLUMN(S)\n" +
                        "    column-1A-title\n" +
                        "    column-2B-title\n" +
                        "    column-3C-title\n" +
                        "  PLUGINS\n" +
                        "    BodyScrollPlugin\n"
        );
    }

    @Test
    public void testEmptyStatePlugin() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ).emptyStatePlugin(
                        SpreadsheetIcons.alignLeft(),
                        "Empty456"
                ),
                "SpreadsheetDataTableComponent\n" +
                        "  COLUMN(S)\n" +
                        "    column-1A-title\n" +
                        "    column-2B-title\n" +
                        "    column-3C-title\n" +
                        "  PLUGINS\n" +
                        "    EmptyStatePlugin (mdi-format-align-left) \"Empty456\"\n"
        );
    }

    @Test
    public void testThreeColumnsNoValueHeadersHidden() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ).hideHeaders(),
                "SpreadsheetDataTableComponent\n"
        );
    }

    @Test
    public void testThreeColumnsThreeRowsHeadersHidden() {
        this.treePrintAndCheck(
                walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                        "tableId123", // id
                        Lists.of(
                                ColumnConfig.create("column-1A-name", "column-1A-title"),
                                ColumnConfig.create("column-2B-name", "column-2B-title"),
                                ColumnConfig.create("column-3C-name", "column-3C-title")
                        ),
                        (column, data) -> SpreadsheetTextComponent.with(
                                Optional.of(
                                        CharSequences.repeating(
                                                data.charAt(column),
                                                3
                                        ).toString()
                                )
                        )
                ).setValue(
                        Optional.of(
                                Lists.of(
                                        "ABC",
                                        "DEF",
                                        "GHI"
                                )
                        )
                ).hideHeaders(),
                "SpreadsheetDataTableComponent\n" +
                        "  ROW(S)\n" +
                        "    ROW 0\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"AAA\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"BBB\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"CCC\"\n" +
                        "    ROW 1\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"DDD\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"EEE\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"FFF\"\n" +
                        "    ROW 2\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"GGG\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"HHH\"\n" +
                        "      SpreadsheetTextComponent\n" +
                        "        \"III\"\n"
        );
    }

    @Override
    public Class<walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent<String>> type() {
        return Cast.to(SpreadsheetDataTableComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
