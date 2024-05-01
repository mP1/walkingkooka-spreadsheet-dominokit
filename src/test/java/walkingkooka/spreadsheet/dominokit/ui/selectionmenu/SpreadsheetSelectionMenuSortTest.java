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
                "sort-", // id-prefix
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"SORT\"\n" +
                        "  sort-A-date-SubMenu \"Date\"\n" +
                        "    sort-A-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+UP]\n" +
                        "    sort-A-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+DOWN]\n" +
                        "  sort-A-date-time-SubMenu \"Date Time\"\n" +
                        "    sort-A-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+UP]\n" +
                        "    sort-A-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+DOWN]\n" +
                        "  sort-A-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "    sort-A-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+UP]\n" +
                        "    sort-A-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+DOWN]\n" +
                        "  sort-A-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "    sort-A-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+UP]\n" +
                        "    sort-A-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+DOWN]\n" +
                        "  sort-A-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "    sort-A-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+UP]\n" +
                        "    sort-A-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+DOWN]\n" +
                        "  sort-A-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "    sort-A-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+UP]\n" +
                        "    sort-A-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+DOWN]\n" +
                        "  sort-A-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "    sort-A-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+UP]\n" +
                        "    sort-A-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+DOWN]\n" +
                        "  sort-A-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "    sort-A-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+UP]\n" +
                        "    sort-A-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+DOWN]\n" +
                        "  sort-A-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "    sort-A-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+UP]\n" +
                        "    sort-A-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+DOWN]\n" +
                        "  sort-A-number-SubMenu \"Number\"\n" +
                        "    sort-A-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+UP]\n" +
                        "    sort-A-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+DOWN]\n" +
                        "  sort-A-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "    sort-A-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+UP]\n" +
                        "    sort-A-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+DOWN]\n" +
                        "  sort-A-text-SubMenu \"Text\"\n" +
                        "    sort-A-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+UP]\n" +
                        "    sort-A-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+DOWN]\n" +
                        "  sort-A-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "    sort-A-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+UP]\n" +
                        "    sort-A-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+DOWN]\n" +
                        "  sort-A-time-SubMenu \"Time\"\n" +
                        "    sort-A-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+UP]\n" +
                        "    sort-A-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+DOWN]\n" +
                        "  sort-A-year-SubMenu \"Year\"\n" +
                        "    sort-A-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+UP]\n" +
                        "    sort-A-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+DOWN]\n" +
                        "  -----\n" +
                        "  sort--edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A1/sort/edit/A%3D]\n"
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
                "sort-", // id-prefix
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"SORT\"\n" +
                        "  sort-12-date-SubMenu \"Date\"\n" +
                        "    sort-12-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+UP]\n" +
                        "    sort-12-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+DOWN]\n" +
                        "  sort-12-date-time-SubMenu \"Date Time\"\n" +
                        "    sort-12-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+UP]\n" +
                        "    sort-12-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+DOWN]\n" +
                        "  sort-12-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "    sort-12-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+UP]\n" +
                        "    sort-12-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+DOWN]\n" +
                        "  sort-12-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "    sort-12-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+UP]\n" +
                        "    sort-12-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+DOWN]\n" +
                        "  sort-12-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "    sort-12-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+UP]\n" +
                        "    sort-12-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+DOWN]\n" +
                        "  sort-12-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "    sort-12-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+UP]\n" +
                        "    sort-12-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+DOWN]\n" +
                        "  sort-12-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "    sort-12-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+UP]\n" +
                        "    sort-12-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+DOWN]\n" +
                        "  sort-12-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "    sort-12-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+UP]\n" +
                        "    sort-12-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+DOWN]\n" +
                        "  sort-12-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "    sort-12-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+UP]\n" +
                        "    sort-12-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+DOWN]\n" +
                        "  sort-12-number-SubMenu \"Number\"\n" +
                        "    sort-12-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+UP]\n" +
                        "    sort-12-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+DOWN]\n" +
                        "  sort-12-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "    sort-12-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+UP]\n" +
                        "    sort-12-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+DOWN]\n" +
                        "  sort-12-text-SubMenu \"Text\"\n" +
                        "    sort-12-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+UP]\n" +
                        "    sort-12-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+DOWN]\n" +
                        "  sort-12-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "    sort-12-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+UP]\n" +
                        "    sort-12-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+DOWN]\n" +
                        "  sort-12-time-SubMenu \"Time\"\n" +
                        "    sort-12-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+UP]\n" +
                        "    sort-12-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+DOWN]\n" +
                        "  sort-12-year-SubMenu \"Year\"\n" +
                        "    sort-12-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+UP]\n" +
                        "    sort-12-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+DOWN]\n" +
                        "  -----\n" +
                        "  sort--edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A12/sort/edit/12%3D]\n"
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
                "sort-", // id-prefix
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"SORT\"\n" +
                        "  sort-B-date-SubMenu \"Date\"\n" +
                        "    sort-B-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+UP]\n" +
                        "    sort-B-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+DOWN]\n" +
                        "  sort-B-date-time-SubMenu \"Date Time\"\n" +
                        "    sort-B-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+UP]\n" +
                        "    sort-B-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+DOWN]\n" +
                        "  sort-B-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "    sort-B-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+UP]\n" +
                        "    sort-B-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+DOWN]\n" +
                        "  sort-B-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "    sort-B-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+UP]\n" +
                        "    sort-B-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+DOWN]\n" +
                        "  sort-B-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "    sort-B-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+UP]\n" +
                        "    sort-B-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+DOWN]\n" +
                        "  sort-B-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "    sort-B-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+UP]\n" +
                        "    sort-B-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+DOWN]\n" +
                        "  sort-B-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "    sort-B-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+UP]\n" +
                        "    sort-B-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+DOWN]\n" +
                        "  sort-B-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "    sort-B-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+UP]\n" +
                        "    sort-B-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+DOWN]\n" +
                        "  sort-B-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "    sort-B-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+UP]\n" +
                        "    sort-B-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+DOWN]\n" +
                        "  sort-B-number-SubMenu \"Number\"\n" +
                        "    sort-B-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+UP]\n" +
                        "    sort-B-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+DOWN]\n" +
                        "  sort-B-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "    sort-B-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+UP]\n" +
                        "    sort-B-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+DOWN]\n" +
                        "  sort-B-text-SubMenu \"Text\"\n" +
                        "    sort-B-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+UP]\n" +
                        "    sort-B-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+DOWN]\n" +
                        "  sort-B-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "    sort-B-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+UP]\n" +
                        "    sort-B-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+DOWN]\n" +
                        "  sort-B-time-SubMenu \"Time\"\n" +
                        "    sort-B-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+UP]\n" +
                        "    sort-B-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+DOWN]\n" +
                        "  sort-B-year-SubMenu \"Year\"\n" +
                        "    sort-B-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+UP]\n" +
                        "    sort-B-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+DOWN]\n" +
                        "  -----\n" +
                        "  sort--edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B%3D]\n"
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
