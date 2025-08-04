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
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.text.TextComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;
import java.util.OptionalInt;

public final class DataTableComponentTest implements ClassTesting<DataTableComponent<String>>,
    TreePrintableTesting {

    private final static String ID_PREFIX = "tableId123-";

    @Test
    public void testEmptyValue() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
                    Optional.of(
                        CharSequences.repeating(
                            data.charAt(column),
                            3
                        ).toString()
                    )
                )
            ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n"
        );
    }

    @Test
    public void testThreeColumnsNotValue() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
                    Optional.of(
                        CharSequences.repeating(
                            data.charAt(column),
                            3
                        ).toString()
                    )
                )
            ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n"
        );
    }

    @Test
    public void testThreeColumnsNotValueWithChildren() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
                    Optional.of(
                        CharSequences.repeating(
                            data.charAt(column),
                            3
                        ).toString()
                    )
                )
            ).appendChild(
                TextComponent.with(
                    Optional.of("child-1A")
                )
            ).appendChild(
                TextComponent.with(
                    Optional.of("child-2B")
                )
            ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  CHILDREN\n" +
                "    TextComponent\n" +
                "      \"child-1A\"\n" +
                "    TextComponent\n" +
                "      \"child-2B\"\n"
        );
    }

    @Test
    public void testThreeColumnsThreeRows() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
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
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"AAA\"\n" +
                "      TextComponent\n" +
                "        \"BBB\"\n" +
                "      TextComponent\n" +
                "        \"CCC\"\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DDD\"\n" +
                "      TextComponent\n" +
                "        \"EEE\"\n" +
                "      TextComponent\n" +
                "        \"FFF\"\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GGG\"\n" +
                "      TextComponent\n" +
                "        \"HHH\"\n" +
                "      TextComponent\n" +
                "        \"III\"\n"
        );
    }

    @Test
    public void testThreeColumnsMixedComponentTypes() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> {
                    switch (column) {
                        case 0:
                            return TextComponent.with(
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
                            return TextBoxComponent.empty();
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
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"ABC\"\n" +
                "      [#/1/SpreadsheetName123/cell/A1]\n" +
                "      TextBoxComponent\n" +
                "        []\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DEF\"\n" +
                "      [#/1/SpreadsheetName123/cell/A1]\n" +
                "      TextBoxComponent\n" +
                "        []\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GHI\"\n" +
                "      [#/1/SpreadsheetName123/cell/A1]\n" +
                "      TextBoxComponent\n" +
                "        []\n"
        );
    }

    @Test
    public void testBodyScrollPlugin() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
                    Optional.of(
                        CharSequences.repeating(
                            data.charAt(column),
                            3
                        ).toString()
                    )
                )
            ).bodyScrollPlugin(),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
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
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
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
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
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
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
                    Optional.of(
                        CharSequences.repeating(
                            data.charAt(column),
                            3
                        ).toString()
                    )
                )
            ).hideHeaders(),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n"
        );
    }

    @Test
    public void testThreeColumnsThreeRowsHeadersHidden() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
                Lists.of(
                    ColumnConfig.create("column-1A-name", "column-1A-title"),
                    ColumnConfig.create("column-2B-name", "column-2B-title"),
                    ColumnConfig.create("column-3C-name", "column-3C-title")
                ),
                (column, data) -> TextComponent.with(
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
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"AAA\"\n" +
                "      TextComponent\n" +
                "        \"BBB\"\n" +
                "      TextComponent\n" +
                "        \"CCC\"\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DDD\"\n" +
                "      TextComponent\n" +
                "        \"EEE\"\n" +
                "      TextComponent\n" +
                "        \"FFF\"\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GGG\"\n" +
                "      TextComponent\n" +
                "        \"HHH\"\n" +
                "      TextComponent\n" +
                "        \"III\"\n"
        );
    }

    @Test
    public void testThreeColumnsIncludesDisabledNextPrev() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
                    Lists.of(
                        ColumnConfig.create("column-1A-name", "column-1A-title"),
                        ColumnConfig.create("column-2B-name", "column-2B-title"),
                        ColumnConfig.create("column-3C-name", "column-3C-title")
                    ),
                    (column, data) -> TextComponent.with(
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
                ).previousNextLinks(ID_PREFIX)
                .setPrevious(
                    Optional.of(
                        HistoryToken.parseString("/*/offset/11/count/5")
                    )
                ).setNext(
                    Optional.of(
                        HistoryToken.parseString("/*/offset/22/count/5")
                    )
                ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"AAA\"\n" +
                "      TextComponent\n" +
                "        \"BBB\"\n" +
                "      TextComponent\n" +
                "        \"CCC\"\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DDD\"\n" +
                "      TextComponent\n" +
                "        \"EEE\"\n" +
                "      TextComponent\n" +
                "        \"FFF\"\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GGG\"\n" +
                "      TextComponent\n" +
                "        \"HHH\"\n" +
                "      TextComponent\n" +
                "        \"III\"\n" +
                "  CHILDREN\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        mdi-arrow-left \"previous\" [#/*/offset/11/count/5] id=tableId123-previous-Link\n" +
                "        \"next\" [#/*/offset/22/count/5] mdi-arrow-right id=tableId123-next-Link\n"
        );
    }

    @Test
    public void testThreeColumnsIncludesRefreshedNextPrevNoPrevious() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
                    Lists.of(
                        ColumnConfig.create("column-1A-name", "column-1A-title"),
                        ColumnConfig.create("column-2B-name", "column-2B-title"),
                        ColumnConfig.create("column-3C-name", "column-3C-title")
                    ),
                    (column, data) -> TextComponent.with(
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
                            "GHI",
                            "JKL",
                            "MNO"
                        )
                    )
                ).previousNextLinks(ID_PREFIX)
                .refreshPreviousNextLinks(
                    HistoryToken.spreadsheetListSelect(
                        HistoryTokenOffsetAndCount.with(
                            OptionalInt.of(0), // offset
                            OptionalInt.of(5) // count
                        )
                    ),
                    100
                ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"AAA\"\n" +
                "      TextComponent\n" +
                "        \"BBB\"\n" +
                "      TextComponent\n" +
                "        \"CCC\"\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DDD\"\n" +
                "      TextComponent\n" +
                "        \"EEE\"\n" +
                "      TextComponent\n" +
                "        \"FFF\"\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GGG\"\n" +
                "      TextComponent\n" +
                "        \"HHH\"\n" +
                "      TextComponent\n" +
                "        \"III\"\n" +
                "    ROW 3\n" +
                "      TextComponent\n" +
                "        \"JJJ\"\n" +
                "      TextComponent\n" +
                "        \"KKK\"\n" +
                "      TextComponent\n" +
                "        \"LLL\"\n" +
                "    ROW 4\n" +
                "      TextComponent\n" +
                "        \"MMM\"\n" +
                "      TextComponent\n" +
                "        \"NNN\"\n" +
                "      TextComponent\n" +
                "        \"OOO\"\n" +
                "  CHILDREN\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        mdi-arrow-left \"previous\" DISABLED id=tableId123-previous-Link\n" +
                "        \"next\" [#/*/offset/4/count/5] mdi-arrow-right id=tableId123-next-Link\n"
        );
    }

    @Test
    public void testThreeColumnsIncludesRefreshedNextPrevNoNext() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
                    Lists.of(
                        ColumnConfig.create("column-1A-name", "column-1A-title"),
                        ColumnConfig.create("column-2B-name", "column-2B-title"),
                        ColumnConfig.create("column-3C-name", "column-3C-title")
                    ),
                    (column, data) -> TextComponent.with(
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
                ).previousNextLinks(ID_PREFIX)
                .refreshPreviousNextLinks(
                    HistoryToken.spreadsheetListSelect(
                        HistoryTokenOffsetAndCount.with(
                            OptionalInt.of(16), // offset
                            OptionalInt.of(5) // count
                        )
                    ),
                    100
                ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"AAA\"\n" +
                "      TextComponent\n" +
                "        \"BBB\"\n" +
                "      TextComponent\n" +
                "        \"CCC\"\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DDD\"\n" +
                "      TextComponent\n" +
                "        \"EEE\"\n" +
                "      TextComponent\n" +
                "        \"FFF\"\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GGG\"\n" +
                "      TextComponent\n" +
                "        \"HHH\"\n" +
                "      TextComponent\n" +
                "        \"III\"\n" +
                "  CHILDREN\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        mdi-arrow-left \"previous\" [#/*/offset/12/count/5] id=tableId123-previous-Link\n" +
                "        \"next\" DISABLED mdi-arrow-right id=tableId123-next-Link\n"
        );
    }

    @Test
    public void testThreeColumnsIncludesRefreshedNextPrevWithBothPrevAndNext() {
        this.treePrintAndCheck(
            DataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
                    Lists.of(
                        ColumnConfig.create("column-1A-name", "column-1A-title"),
                        ColumnConfig.create("column-2B-name", "column-2B-title"),
                        ColumnConfig.create("column-3C-name", "column-3C-title")
                    ),
                    (column, data) -> TextComponent.with(
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
                            "GHI",
                            "JKL",
                            "MNO"
                        )
                    )
                ).previousNextLinks(ID_PREFIX)
                .refreshPreviousNextLinks(
                    HistoryToken.spreadsheetListSelect(
                        HistoryTokenOffsetAndCount.with(
                            OptionalInt.of(16), // offset
                            OptionalInt.of(5) // count
                        )
                    ),
                    100
                ),
            "DataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      TextComponent\n" +
                "        \"AAA\"\n" +
                "      TextComponent\n" +
                "        \"BBB\"\n" +
                "      TextComponent\n" +
                "        \"CCC\"\n" +
                "    ROW 1\n" +
                "      TextComponent\n" +
                "        \"DDD\"\n" +
                "      TextComponent\n" +
                "        \"EEE\"\n" +
                "      TextComponent\n" +
                "        \"FFF\"\n" +
                "    ROW 2\n" +
                "      TextComponent\n" +
                "        \"GGG\"\n" +
                "      TextComponent\n" +
                "        \"HHH\"\n" +
                "      TextComponent\n" +
                "        \"III\"\n" +
                "    ROW 3\n" +
                "      TextComponent\n" +
                "        \"JJJ\"\n" +
                "      TextComponent\n" +
                "        \"KKK\"\n" +
                "      TextComponent\n" +
                "        \"LLL\"\n" +
                "    ROW 4\n" +
                "      TextComponent\n" +
                "        \"MMM\"\n" +
                "      TextComponent\n" +
                "        \"NNN\"\n" +
                "      TextComponent\n" +
                "        \"OOO\"\n" +
                "  CHILDREN\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        mdi-arrow-left \"previous\" [#/*/offset/12/count/5] id=tableId123-previous-Link\n" +
                "        \"next\" [#/*/offset/20/count/5] mdi-arrow-right id=tableId123-next-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<DataTableComponent<String>> type() {
        return Cast.to(DataTableComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
