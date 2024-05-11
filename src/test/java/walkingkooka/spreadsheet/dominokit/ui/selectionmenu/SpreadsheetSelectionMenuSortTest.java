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

package walkingkooka.spreadsheet.dominokit.ui.selectionmenu;

import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;
import java.util.Set;

public class SpreadsheetSelectionMenuSortTest implements ClassTesting<SpreadsheetSelectionMenuSort>,
        TreePrintableTesting {

    @Test
    public void testBuildColumn() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.A1.setDefaultAnchor()
        );

        this.buildAndCheck(
                token,
                SpreadsheetSelection.parseColumn("A"),
                "column-sort-", // id-prefix
                SpreadsheetIcons.columnSort(),
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"Sort Column\"\n" +
                        "  (mdi-sort) column-sort-SubMenu \"Sort Column\"\n" +
                        "    column-sort-date-SubMenu \"Date\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+UP] id=column-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+DOWN] id=column-sort-date-DOWN-MenuItem\n" +
                        "    column-sort-date-time-SubMenu \"Date Time\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+UP] id=column-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+DOWN] id=column-sort-date-time-DOWN-MenuItem\n" +
                        "    column-sort-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+UP] id=column-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+DOWN] id=column-sort-day-of-month-DOWN-MenuItem\n" +
                        "    column-sort-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+UP] id=column-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+DOWN] id=column-sort-day-of-week-DOWN-MenuItem\n" +
                        "    column-sort-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+UP] id=column-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+DOWN] id=column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    column-sort-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+UP] id=column-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+DOWN] id=column-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    column-sort-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+UP] id=column-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+DOWN] id=column-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    column-sort-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+UP] id=column-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+DOWN] id=column-sort-month-of-year-DOWN-MenuItem\n" +
                        "    column-sort-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+UP] id=column-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+DOWN] id=column-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    column-sort-number-SubMenu \"Number\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+UP] id=column-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+DOWN] id=column-sort-number-DOWN-MenuItem\n" +
                        "    column-sort-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+UP] id=column-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+DOWN] id=column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    column-sort-text-SubMenu \"Text\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+UP] id=column-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+DOWN] id=column-sort-text-DOWN-MenuItem\n" +
                        "    column-sort-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+UP] id=column-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+DOWN] id=column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    column-sort-time-SubMenu \"Time\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+UP] id=column-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+DOWN] id=column-sort-time-DOWN-MenuItem\n" +
                        "    column-sort-year-SubMenu \"Year\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+UP] id=column-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+DOWN] id=column-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/cell/A1/sort/edit/A%3D] id=column-sort-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildRow() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.parseCell("A12").setDefaultAnchor()
        );

        this.buildAndCheck(
                token,
                SpreadsheetSelection.parseRow("12"),
                "row-sort-", // id-prefix
                SpreadsheetIcons.rowSort(),
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"Sort Row\"\n" +
                        "  (mdi-sort) row-sort-SubMenu \"Sort Row\"\n" +
                        "    row-sort-date-SubMenu \"Date\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+UP] id=row-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+DOWN] id=row-sort-date-DOWN-MenuItem\n" +
                        "    row-sort-date-time-SubMenu \"Date Time\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+UP] id=row-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+DOWN] id=row-sort-date-time-DOWN-MenuItem\n" +
                        "    row-sort-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+UP] id=row-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+DOWN] id=row-sort-day-of-month-DOWN-MenuItem\n" +
                        "    row-sort-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+UP] id=row-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+DOWN] id=row-sort-day-of-week-DOWN-MenuItem\n" +
                        "    row-sort-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+UP] id=row-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+DOWN] id=row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    row-sort-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+UP] id=row-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+DOWN] id=row-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    row-sort-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+UP] id=row-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+DOWN] id=row-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    row-sort-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+UP] id=row-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+DOWN] id=row-sort-month-of-year-DOWN-MenuItem\n" +
                        "    row-sort-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+UP] id=row-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+DOWN] id=row-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    row-sort-number-SubMenu \"Number\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+UP] id=row-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+DOWN] id=row-sort-number-DOWN-MenuItem\n" +
                        "    row-sort-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+UP] id=row-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+DOWN] id=row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    row-sort-text-SubMenu \"Text\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+UP] id=row-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+DOWN] id=row-sort-text-DOWN-MenuItem\n" +
                        "    row-sort-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+UP] id=row-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+DOWN] id=row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    row-sort-time-SubMenu \"Time\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+UP] id=row-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+DOWN] id=row-sort-time-DOWN-MenuItem\n" +
                        "    row-sort-year-SubMenu \"Year\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+UP] id=row-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+DOWN] id=row-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/cell/A12/sort/edit/12%3D] id=row-sort-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildCellColumn() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.parseCell("B2").setDefaultAnchor()
        );

        this.buildAndCheck(
                token,
                SpreadsheetSelection.parseColumn("B"),
                "column-sort-", // id-prefix
                SpreadsheetIcons.columnSort(),
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"Sort Column\"\n" +
                        "  (mdi-sort) column-sort-SubMenu \"Sort Column\"\n" +
                        "    column-sort-date-SubMenu \"Date\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+UP] id=column-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+DOWN] id=column-sort-date-DOWN-MenuItem\n" +
                        "    column-sort-date-time-SubMenu \"Date Time\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+UP] id=column-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+DOWN] id=column-sort-date-time-DOWN-MenuItem\n" +
                        "    column-sort-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+UP] id=column-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+DOWN] id=column-sort-day-of-month-DOWN-MenuItem\n" +
                        "    column-sort-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+UP] id=column-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+DOWN] id=column-sort-day-of-week-DOWN-MenuItem\n" +
                        "    column-sort-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+UP] id=column-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+DOWN] id=column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    column-sort-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+UP] id=column-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+DOWN] id=column-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    column-sort-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+UP] id=column-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+DOWN] id=column-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    column-sort-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+UP] id=column-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+DOWN] id=column-sort-month-of-year-DOWN-MenuItem\n" +
                        "    column-sort-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+UP] id=column-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+DOWN] id=column-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    column-sort-number-SubMenu \"Number\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+UP] id=column-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+DOWN] id=column-sort-number-DOWN-MenuItem\n" +
                        "    column-sort-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+UP] id=column-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+DOWN] id=column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    column-sort-text-SubMenu \"Text\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+UP] id=column-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+DOWN] id=column-sort-text-DOWN-MenuItem\n" +
                        "    column-sort-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+UP] id=column-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+DOWN] id=column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    column-sort-time-SubMenu \"Time\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+UP] id=column-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+DOWN] id=column-sort-time-DOWN-MenuItem\n" +
                        "    column-sort-year-SubMenu \"Year\"\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+UP] id=column-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+DOWN] id=column-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B%3D] id=column-sort-edit-MenuItem\n"
        );
    }

    private void buildAndCheck(final HistoryToken historyToken,
                               final SpreadsheetColumnOrRowReference columnOrRow,
                               final String idPrefix,
                               final Icon<?> icon,
                               final Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos,
                               final String expected) {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "sort",
                        "Sort " + columnOrRow.textLabel(),
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                HistoryTokenContexts.fake()
        );

        SpreadsheetSelectionMenuSort.build(
                historyToken,
                columnOrRow,
                idPrefix,
                icon,
                spreadsheetComparatorInfos,
                menu
        );

        this.treePrintAndCheck(
                menu,
                expected
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuSort> type() {
        return SpreadsheetSelectionMenuSort.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
