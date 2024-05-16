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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponentTest implements ClassTesting<SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent>,
        TreePrintableTesting {

    private final static String ID = SpreadsheetSortDialogComponent.ID_PREFIX + "comparator-1-";

    private final static Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> MOVE_UP = (names) -> {
        throw new UnsupportedOperationException();
    };

    private final static Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> MOVE_DOWN = MOVE_UP;

    private final static Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> SETTER = (names) -> {
        throw new UnsupportedOperationException();
    };

    private final static String HISTORY_TOKEN = "/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/";

    @Test
    public void testWithNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                        null,
                        MOVE_UP,
                        MOVE_DOWN,
                        SETTER,
                        HistoryTokenContexts.fake()
                )
        );
    }

    @Test
    public void testWithEmptyIdFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                        "",
                        MOVE_UP,
                        MOVE_DOWN,
                        SETTER,
                        HistoryTokenContexts.fake()
                )
        );
    }

    @Test
    public void testWithNullMoveUpFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                        ID,
                        null,
                        MOVE_DOWN,
                        SETTER,
                        HistoryTokenContexts.fake()
                )
        );
    }

    @Test
    public void testWithNullMoveDownFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                        ID,
                        MOVE_UP,
                        null,
                        SETTER,
                        HistoryTokenContexts.fake()
                )
        );
    }

    @Test
    public void testWithNullSetterFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                        ID,
                        MOVE_UP,
                        MOVE_DOWN,
                        null,
                        HistoryTokenContexts.fake()
                )
        );
    }

    @Test
    public void testCellEmptyColumnOrRowSpreadsheetComparatorNames() {
        this.refreshAndCheck(
                "",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [] id=sort-comparator-1-TextBox\n" +
                        "                Errors\n" +
                        "                  text is empty\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n"
        );
    }

    @Test
    public void testCellOnlyColumn() {
        this.refreshAndCheck(
                "A",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [A] id=sort-comparator-1-TextBox\n" +
                        "                Errors\n" +
                        "                  Missing '='\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-2] id=sort-comparator-1-append-1-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-3] id=sort-comparator-1-append-2-Link\n"
        );
    }

    @Test
    public void testCellOnlyColumnEqualsSign() {
        this.refreshAndCheck(
                "A=",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [A=] id=sort-comparator-1-TextBox\n" +
                        "                Errors\n" +
                        "                  Missing comparator name\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-2] id=sort-comparator-1-append-1-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-3] id=sort-comparator-1-append-2-Link\n"
        );
    }

    @Test
    public void testCellOnlyRowEqualsSign() {
        this.refreshAndCheck(
                "12=",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [12=] id=sort-comparator-1-TextBox\n" +
                        "                Errors\n" +
                        "                  Missing comparator name\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/12=comparator-1] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/12=comparator-2] id=sort-comparator-1-append-1-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/12=comparator-3] id=sort-comparator-1-append-2-Link\n"
        );
    }

    @Test
    public void testCellComparatorName() {
        this.refreshAndCheck(
                "A=comparator-1",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [A=comparator-1] id=sort-comparator-1-TextBox\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-2] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-3] id=sort-comparator-1-append-1-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit] id=sort-comparator-1-remove-0-Link\n"
        );
    }

    @Test
    public void testCellSeveralComparatorName() {
        this.refreshAndCheck(
                "A=comparator-1,comparator-2",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [A=comparator-1,comparator-2] id=sort-comparator-1-TextBox\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-2,comparator-3] id=sort-comparator-1-append-0-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-2] id=sort-comparator-1-remove-0-Link\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1] id=sort-comparator-1-remove-1-Link\n"
        );
    }

    @Test
    public void testCellAllComparatorNames() {
        this.refreshAndCheck(
                "A=comparator-1,comparator-2,comparator-3",
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [A=comparator-1,comparator-2,comparator-3] id=sort-comparator-1-TextBox\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-2,comparator-3] id=sort-comparator-1-remove-0-Link\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-3] id=sort-comparator-1-remove-1-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-2] id=sort-comparator-1-remove-2-Link\n"
        );
    }

    @Test
    public void testCellAllComparatorNamesFirst() {
        this.refreshAndCheck(
                "A=comparator-1",
                (names) -> Optional.empty(), // first CANT move-up
                (names) -> Optional.of(
                        HistoryToken.parseString(
                                HISTORY_TOKEN +
                                        concat(
                                                "B=comparator-2",
                                                names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                        .orElse(""),
                                                "C=comparator-3"
                                        )
                        )
                ), // move-down
                (names) -> HistoryToken.parseString(
                        HISTORY_TOKEN +
                                concat(
                                        names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                .orElse(""),
                                        "B=comparator-2",
                                        "C=comparator-3"
                                )
                ), // setter
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [A=comparator-1] id=sort-comparator-1-TextBox\n" +
                        "          \"Move Up\" DISABLED id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/B=comparator-2;A=comparator-1;C=comparator-3] id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-2;B=comparator-2;C=comparator-3] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1,comparator-3;B=comparator-2;C=comparator-3] id=sort-comparator-1-append-1-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/B=comparator-2;C=comparator-3] id=sort-comparator-1-remove-0-Link\n"
        );
    }

    @Test
    public void testCellAllComparatorNamesMiddle() {
        this.refreshAndCheck(
                "B=comparator-2",
                (names) -> Optional.of(
                        HistoryToken.parseString(
                                HISTORY_TOKEN +
                                        concat(
                                                names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                        .orElse(""),
                                                "A=comparator-1",
                                                "C=comparator-3"
                                        )
                        )
                ), // move-up
                (names) -> Optional.of(
                        HistoryToken.parseString(
                                HISTORY_TOKEN +
                                        concat(
                                                "A=comparator-1",
                                                "C=comparator-3",
                                                names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                        .orElse("")
                                        )
                        )
                ), // move-down
                (names) -> HistoryToken.parseString(
                        HISTORY_TOKEN +
                                concat(
                                        "A=comparator-1",
                                        names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                .orElse(""),
                                        "C=comparator-3"
                                )
                ), // setter
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [B=comparator-2] id=sort-comparator-1-TextBox\n" +
                        "          \"Move Up\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/B=comparator-2;A=comparator-1;C=comparator-3] id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;C=comparator-3;B=comparator-2] id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;B=comparator-2,comparator-1;C=comparator-3] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;B=comparator-2,comparator-3;C=comparator-3] id=sort-comparator-1-append-1-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove comparator(s)\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;C=comparator-3] id=sort-comparator-1-remove-0-Link\n"
        );
    }

    @Test
    public void testCellAllComparatorNamesLast() {
        this.refreshAndCheck(
                "C=comparator-3",
                (names) -> Optional.of(
                        HistoryToken.parseString(
                                HISTORY_TOKEN +
                                        concat(
                                                "A=comparator-1",
                                                names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                        .orElse(""),
                                                "B=comparator-2"
                                        )
                        )
                ), // move-up
                (names) -> Optional.empty(), // LAST CANT move-down
                (names) -> HistoryToken.parseString(
                        HISTORY_TOKEN +
                                concat(
                                        "A=comparator-1",
                                        "B=comparator-2",
                                        names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                .orElse("")
                                )
                ), // setter
                "SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  SpreadsheetFlexLayout\n" +
                        "    COLUMN\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "            ParserSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                [C=comparator-3] id=sort-comparator-1-TextBox\n" +
                        "          \"Move Up\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;C=comparator-3;B=comparator-2] id=sort-comparator-1-moveUp-Link\n" +
                        "          \"Move Down\" DISABLED id=sort-comparator-1-moveDown-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append comparator(s)\n" +
                        "              \"comparator-1\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;B=comparator-2;C=comparator-3,comparator-1] id=sort-comparator-1-append-0-Link\n" +
                        "              \"comparator-2\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;B=comparator-2;C=comparator-3,comparator-2] id=sort-comparator-1-append-1-Link\n" +
                        "      SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove comparator(s)\n" +
                        "              \"comparator-3\" [#/1/SpreadsheetName123/cell/A1:C3/top-right/sort/edit/A=comparator-1;B=comparator-2] id=sort-comparator-1-remove-0-Link\n"
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final String expected) {
        this.refreshAndCheck(
                columnOrRowSpreadsheetComparatorNames,
                (names) -> Optional.empty(),
                (names) -> Optional.empty(),
                (names) -> HistoryToken.parseString(
                        HISTORY_TOKEN +
                                concat(
                                        names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                                                .orElse("")
                                )
                ),
                expected
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                         final String expected) {
        this.refreshAndCheck(
                columnOrRowSpreadsheetComparatorNames,
                HISTORY_TOKEN,
                moveUp,
                moveDown,
                setter,
                expected
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final String historyToken,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                         final String expected) {
        this.refreshAndCheck(
                columnOrRowSpreadsheetComparatorNames,
                moveUp,
                moveDown,
                setter,
                new FakeSpreadsheetSortDialogComponentContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString(historyToken);
                    }

                    @Override
                    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
                        return Sets.of(
                                SpreadsheetComparatorInfo.with(
                                        Url.parseAbsolute("https://example.com/comparator-1"),
                                        SpreadsheetComparatorName.with("comparator-1")
                                ),
                                SpreadsheetComparatorInfo.with(
                                        Url.parseAbsolute("https://example.com/comparator-2"),
                                        SpreadsheetComparatorName.with("comparator-2")
                                ),
                                SpreadsheetComparatorInfo.with(
                                        Url.parseAbsolute("https://example.com/comparator-3"),
                                        SpreadsheetComparatorName.with("comparator-3")
                                )
                        );
                    }
                },
                expected
        );
    }

    void refreshAndCheck(final String columnOrRowSpreadsheetComparatorNames,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown,
                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter,
                         final SpreadsheetSortDialogComponentContext context,
                         final String expected) {
        final SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent component = SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                ID,
                moveUp,
                moveDown,
                setter,
                context
        );
        component.refresh(
                columnOrRowSpreadsheetComparatorNames,
                context
        );
        this.treePrintAndCheck(
                component,
                expected
        );
    }

    private static String concat(final String... names) {
        return Arrays.stream(names)
                .filter(s -> false == s.isEmpty())
                .collect(Collectors.joining(";"));
    }

    @Test
    @Override
    public void testAllMethodsVisibility() {
        this.allMethodsVisibilityCheck(
                "setStringValue",
                "stringValue"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> type() {
        return SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
