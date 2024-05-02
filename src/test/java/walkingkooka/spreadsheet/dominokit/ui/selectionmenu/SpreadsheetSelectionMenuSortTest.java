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
                        "      column-sort-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+UP]\n" +
                        "      column-sort-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+DOWN]\n" +
                        "    column-sort-date-time-SubMenu \"Date Time\"\n" +
                        "      column-sort-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+UP]\n" +
                        "      column-sort-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+DOWN]\n" +
                        "    column-sort-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      column-sort-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+UP]\n" +
                        "      column-sort-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+DOWN]\n" +
                        "    column-sort-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      column-sort-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+UP]\n" +
                        "      column-sort-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+DOWN]\n" +
                        "    column-sort-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      column-sort-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+UP]\n" +
                        "      column-sort-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+DOWN]\n" +
                        "    column-sort-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      column-sort-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+UP]\n" +
                        "      column-sort-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+DOWN]\n" +
                        "    column-sort-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      column-sort-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+UP]\n" +
                        "      column-sort-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+DOWN]\n" +
                        "    column-sort-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      column-sort-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+UP]\n" +
                        "      column-sort-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+DOWN]\n" +
                        "    column-sort-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      column-sort-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+UP]\n" +
                        "      column-sort-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+DOWN]\n" +
                        "    column-sort-number-SubMenu \"Number\"\n" +
                        "      column-sort-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+UP]\n" +
                        "      column-sort-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+DOWN]\n" +
                        "    column-sort-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      column-sort-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+UP]\n" +
                        "      column-sort-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+DOWN]\n" +
                        "    column-sort-text-SubMenu \"Text\"\n" +
                        "      column-sort-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+UP]\n" +
                        "      column-sort-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+DOWN]\n" +
                        "    column-sort-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      column-sort-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+UP]\n" +
                        "      column-sort-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+DOWN]\n" +
                        "    column-sort-time-SubMenu \"Time\"\n" +
                        "      column-sort-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+UP]\n" +
                        "      column-sort-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+DOWN]\n" +
                        "    column-sort-year-SubMenu \"Year\"\n" +
                        "      column-sort-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+UP]\n" +
                        "      column-sort-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+DOWN]\n" +
                        "    -----\n" +
                        "    column-sort-edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A1/sort/edit/A%3D]\n"
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
                        "      row-sort-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+UP]\n" +
                        "      row-sort-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+DOWN]\n" +
                        "    row-sort-date-time-SubMenu \"Date Time\"\n" +
                        "      row-sort-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+UP]\n" +
                        "      row-sort-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+DOWN]\n" +
                        "    row-sort-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      row-sort-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+UP]\n" +
                        "      row-sort-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+DOWN]\n" +
                        "    row-sort-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      row-sort-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+UP]\n" +
                        "      row-sort-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+DOWN]\n" +
                        "    row-sort-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      row-sort-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+UP]\n" +
                        "      row-sort-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+DOWN]\n" +
                        "    row-sort-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      row-sort-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+UP]\n" +
                        "      row-sort-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+DOWN]\n" +
                        "    row-sort-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      row-sort-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+UP]\n" +
                        "      row-sort-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+DOWN]\n" +
                        "    row-sort-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      row-sort-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+UP]\n" +
                        "      row-sort-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+DOWN]\n" +
                        "    row-sort-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      row-sort-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+UP]\n" +
                        "      row-sort-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+DOWN]\n" +
                        "    row-sort-number-SubMenu \"Number\"\n" +
                        "      row-sort-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+UP]\n" +
                        "      row-sort-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+DOWN]\n" +
                        "    row-sort-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      row-sort-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+UP]\n" +
                        "      row-sort-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+DOWN]\n" +
                        "    row-sort-text-SubMenu \"Text\"\n" +
                        "      row-sort-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+UP]\n" +
                        "      row-sort-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+DOWN]\n" +
                        "    row-sort-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      row-sort-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+UP]\n" +
                        "      row-sort-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+DOWN]\n" +
                        "    row-sort-time-SubMenu \"Time\"\n" +
                        "      row-sort-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+UP]\n" +
                        "      row-sort-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+DOWN]\n" +
                        "    row-sort-year-SubMenu \"Year\"\n" +
                        "      row-sort-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+UP]\n" +
                        "      row-sort-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+DOWN]\n" +
                        "    -----\n" +
                        "    row-sort-edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A12/sort/edit/12%3D]\n"
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
                        "      column-sort-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+UP]\n" +
                        "      column-sort-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+DOWN]\n" +
                        "    column-sort-date-time-SubMenu \"Date Time\"\n" +
                        "      column-sort-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+UP]\n" +
                        "      column-sort-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+DOWN]\n" +
                        "    column-sort-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      column-sort-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+UP]\n" +
                        "      column-sort-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+DOWN]\n" +
                        "    column-sort-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      column-sort-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+UP]\n" +
                        "      column-sort-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+DOWN]\n" +
                        "    column-sort-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      column-sort-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+UP]\n" +
                        "      column-sort-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+DOWN]\n" +
                        "    column-sort-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      column-sort-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+UP]\n" +
                        "      column-sort-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+DOWN]\n" +
                        "    column-sort-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      column-sort-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+UP]\n" +
                        "      column-sort-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+DOWN]\n" +
                        "    column-sort-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      column-sort-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+UP]\n" +
                        "      column-sort-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+DOWN]\n" +
                        "    column-sort-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      column-sort-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+UP]\n" +
                        "      column-sort-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+DOWN]\n" +
                        "    column-sort-number-SubMenu \"Number\"\n" +
                        "      column-sort-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+UP]\n" +
                        "      column-sort-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+DOWN]\n" +
                        "    column-sort-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      column-sort-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+UP]\n" +
                        "      column-sort-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+DOWN]\n" +
                        "    column-sort-text-SubMenu \"Text\"\n" +
                        "      column-sort-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+UP]\n" +
                        "      column-sort-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+DOWN]\n" +
                        "    column-sort-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      column-sort-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+UP]\n" +
                        "      column-sort-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+DOWN]\n" +
                        "    column-sort-time-SubMenu \"Time\"\n" +
                        "      column-sort-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+UP]\n" +
                        "      column-sort-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+DOWN]\n" +
                        "    column-sort-year-SubMenu \"Year\"\n" +
                        "      column-sort-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+UP]\n" +
                        "      column-sort-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+DOWN]\n" +
                        "    -----\n" +
                        "    column-sort-edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B%3D]\n"
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
