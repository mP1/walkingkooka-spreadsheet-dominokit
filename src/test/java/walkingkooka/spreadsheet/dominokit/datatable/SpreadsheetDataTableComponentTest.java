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
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;
import java.util.OptionalInt;

public final class SpreadsheetDataTableComponentTest implements ClassTesting<SpreadsheetDataTableComponent<String>>,
    TreePrintableTesting {

    private final static String ID_PREFIX = "tableId123-";

    @Test
    public void testEmptyValue() {
        this.treePrintAndCheck(
            walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
                "  id=tableId123-Table\n" +
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
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
                "  id=tableId123-Table\n" +
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
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
            "SpreadsheetDataTableComponent\n" +
                "  id=tableId123-Table\n" +
                "  COLUMN(S)\n" +
                "    column-1A-title\n" +
                "    column-2B-title\n" +
                "    column-3C-title\n" +
                "  ROW(S)\n" +
                "    ROW 0\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"ABC\"\n" +
                "      [#/1/SpreadsheetName123/cell/A1]\n" +
                "      TextBoxComponent\n" +
                "        []\n" +
                "    ROW 1\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"DEF\"\n" +
                "      [#/1/SpreadsheetName123/cell/A1]\n" +
                "      TextBoxComponent\n" +
                "        []\n" +
                "    ROW 2\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"GHI\"\n" +
                "      [#/1/SpreadsheetName123/cell/A1]\n" +
                "      TextBoxComponent\n" +
                "        []\n"
        );
    }

    @Test
    public void testBodyScrollPlugin() {
        this.treePrintAndCheck(
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
            "SpreadsheetDataTableComponent\n" +
                "  id=tableId123-Table\n"
        );
    }

    @Test
    public void testThreeColumnsThreeRowsHeadersHidden() {
        this.treePrintAndCheck(
            SpreadsheetDataTableComponent.<String>with(
                ID_PREFIX, // id-prefix
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
                "  id=tableId123-Table\n" +
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
    public void testThreeColumnsIncludesDisabledNextPrev() {
        this.treePrintAndCheck(
            SpreadsheetDataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
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
            "SpreadsheetDataTableComponent\n" +
                "  id=tableId123-Table\n" +
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
            SpreadsheetDataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
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
            "SpreadsheetDataTableComponent\n" +
                "  id=tableId123-Table\n" +
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
                "        \"III\"\n" +
                "    ROW 3\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"JJJ\"\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"KKK\"\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"LLL\"\n" +
                "    ROW 4\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"MMM\"\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"NNN\"\n" +
                "      SpreadsheetTextComponent\n" +
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
            SpreadsheetDataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
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
            "SpreadsheetDataTableComponent\n" +
                "  id=tableId123-Table\n" +
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
            SpreadsheetDataTableComponent.<String>with(
                    ID_PREFIX, // id-prefix
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
            "SpreadsheetDataTableComponent\n" +
                "  id=tableId123-Table\n" +
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
                "        \"III\"\n" +
                "    ROW 3\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"JJJ\"\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"KKK\"\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"LLL\"\n" +
                "    ROW 4\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"MMM\"\n" +
                "      SpreadsheetTextComponent\n" +
                "        \"NNN\"\n" +
                "      SpreadsheetTextComponent\n" +
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
    public Class<SpreadsheetDataTableComponent<String>> type() {
        return Cast.to(SpreadsheetDataTableComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
