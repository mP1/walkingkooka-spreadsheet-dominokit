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
                "Sort-", // id-prefix
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"SORT\"\n" +
                        "  Sort-A-date-SubMenu \"Date\"\n" +
                        "    Sort-A-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+UP]\n" +
                        "    Sort-A-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+DOWN]\n" +
                        "  Sort-A-date-time-SubMenu \"Date Time\"\n" +
                        "    Sort-A-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+UP]\n" +
                        "    Sort-A-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+DOWN]\n" +
                        "  Sort-A-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "    Sort-A-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+UP]\n" +
                        "    Sort-A-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+DOWN]\n" +
                        "  Sort-A-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "    Sort-A-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+UP]\n" +
                        "    Sort-A-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+DOWN]\n" +
                        "  Sort-A-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "    Sort-A-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+UP]\n" +
                        "    Sort-A-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+DOWN]\n" +
                        "  Sort-A-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "    Sort-A-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+UP]\n" +
                        "    Sort-A-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+DOWN]\n" +
                        "  Sort-A-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "    Sort-A-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+UP]\n" +
                        "    Sort-A-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+DOWN]\n" +
                        "  Sort-A-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "    Sort-A-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+UP]\n" +
                        "    Sort-A-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+DOWN]\n" +
                        "  Sort-A-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "    Sort-A-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+UP]\n" +
                        "    Sort-A-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+DOWN]\n" +
                        "  Sort-A-number-SubMenu \"Number\"\n" +
                        "    Sort-A-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+UP]\n" +
                        "    Sort-A-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+DOWN]\n" +
                        "  Sort-A-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "    Sort-A-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+UP]\n" +
                        "    Sort-A-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+DOWN]\n" +
                        "  Sort-A-text-SubMenu \"Text\"\n" +
                        "    Sort-A-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+UP]\n" +
                        "    Sort-A-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+DOWN]\n" +
                        "  Sort-A-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "    Sort-A-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+UP]\n" +
                        "    Sort-A-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+DOWN]\n" +
                        "  Sort-A-time-SubMenu \"Time\"\n" +
                        "    Sort-A-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+UP]\n" +
                        "    Sort-A-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+DOWN]\n" +
                        "  Sort-A-year-SubMenu \"Year\"\n" +
                        "    Sort-A-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+UP]\n" +
                        "    Sort-A-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+DOWN]\n" +
                        "  -----\n" +
                        "  Sort--edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A1/sort/edit/A%3D]\n"
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
                "Sort-", // id-prefix
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"SORT\"\n" +
                        "  Sort-12-date-SubMenu \"Date\"\n" +
                        "    Sort-12-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+UP]\n" +
                        "    Sort-12-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+DOWN]\n" +
                        "  Sort-12-date-time-SubMenu \"Date Time\"\n" +
                        "    Sort-12-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+UP]\n" +
                        "    Sort-12-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+DOWN]\n" +
                        "  Sort-12-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "    Sort-12-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+UP]\n" +
                        "    Sort-12-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+DOWN]\n" +
                        "  Sort-12-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "    Sort-12-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+UP]\n" +
                        "    Sort-12-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+DOWN]\n" +
                        "  Sort-12-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "    Sort-12-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+UP]\n" +
                        "    Sort-12-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+DOWN]\n" +
                        "  Sort-12-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "    Sort-12-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+UP]\n" +
                        "    Sort-12-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+DOWN]\n" +
                        "  Sort-12-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "    Sort-12-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+UP]\n" +
                        "    Sort-12-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+DOWN]\n" +
                        "  Sort-12-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "    Sort-12-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+UP]\n" +
                        "    Sort-12-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+DOWN]\n" +
                        "  Sort-12-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "    Sort-12-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+UP]\n" +
                        "    Sort-12-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+DOWN]\n" +
                        "  Sort-12-number-SubMenu \"Number\"\n" +
                        "    Sort-12-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+UP]\n" +
                        "    Sort-12-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+DOWN]\n" +
                        "  Sort-12-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "    Sort-12-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+UP]\n" +
                        "    Sort-12-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+DOWN]\n" +
                        "  Sort-12-text-SubMenu \"Text\"\n" +
                        "    Sort-12-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+UP]\n" +
                        "    Sort-12-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+DOWN]\n" +
                        "  Sort-12-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "    Sort-12-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+UP]\n" +
                        "    Sort-12-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+DOWN]\n" +
                        "  Sort-12-time-SubMenu \"Time\"\n" +
                        "    Sort-12-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+UP]\n" +
                        "    Sort-12-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+DOWN]\n" +
                        "  Sort-12-year-SubMenu \"Year\"\n" +
                        "    Sort-12-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+UP]\n" +
                        "    Sort-12-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+DOWN]\n" +
                        "  -----\n" +
                        "  Sort--edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A12/sort/edit/12%3D]\n"
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
                "Sort-", // id-prefix
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"SORT\"\n" +
                        "  Sort-B-date-SubMenu \"Date\"\n" +
                        "    Sort-B-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+UP]\n" +
                        "    Sort-B-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+DOWN]\n" +
                        "  Sort-B-date-time-SubMenu \"Date Time\"\n" +
                        "    Sort-B-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+UP]\n" +
                        "    Sort-B-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+DOWN]\n" +
                        "  Sort-B-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "    Sort-B-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+UP]\n" +
                        "    Sort-B-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+DOWN]\n" +
                        "  Sort-B-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "    Sort-B-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+UP]\n" +
                        "    Sort-B-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+DOWN]\n" +
                        "  Sort-B-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "    Sort-B-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+UP]\n" +
                        "    Sort-B-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+DOWN]\n" +
                        "  Sort-B-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "    Sort-B-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+UP]\n" +
                        "    Sort-B-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+DOWN]\n" +
                        "  Sort-B-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "    Sort-B-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+UP]\n" +
                        "    Sort-B-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+DOWN]\n" +
                        "  Sort-B-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "    Sort-B-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+UP]\n" +
                        "    Sort-B-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+DOWN]\n" +
                        "  Sort-B-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "    Sort-B-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+UP]\n" +
                        "    Sort-B-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+DOWN]\n" +
                        "  Sort-B-number-SubMenu \"Number\"\n" +
                        "    Sort-B-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+UP]\n" +
                        "    Sort-B-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+DOWN]\n" +
                        "  Sort-B-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "    Sort-B-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+UP]\n" +
                        "    Sort-B-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+DOWN]\n" +
                        "  Sort-B-text-SubMenu \"Text\"\n" +
                        "    Sort-B-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+UP]\n" +
                        "    Sort-B-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+DOWN]\n" +
                        "  Sort-B-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "    Sort-B-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+UP]\n" +
                        "    Sort-B-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+DOWN]\n" +
                        "  Sort-B-time-SubMenu \"Time\"\n" +
                        "    Sort-B-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+UP]\n" +
                        "    Sort-B-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+DOWN]\n" +
                        "  Sort-B-year-SubMenu \"Year\"\n" +
                        "    Sort-B-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+UP]\n" +
                        "    Sort-B-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+DOWN]\n" +
                        "  -----\n" +
                        "  Sort--edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B%3D]\n"
        );
    }

    private void buildAndCheck(final HistoryToken historyToken,
                               final SpreadsheetColumnOrRowReference columnOrRow,
                               final String idPrefix,
                               final Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos,
                               final String expected) {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "sort",
                        "SORT",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                HistoryTokenContexts.fake()
        );

        SpreadsheetSelectionMenuSort.build(
                historyToken,
                columnOrRow,
                idPrefix,
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
