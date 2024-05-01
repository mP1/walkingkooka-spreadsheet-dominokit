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
                "sort-", // id-prefix
                SpreadsheetIcons.columnSort(),
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"Sort Column\"\n" +
                        "  (mdi-sort) sort-column-SubMenu \"Sort Column\"\n" +
                        "    sort-column-date-SubMenu \"Date\"\n" +
                        "      sort-column-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+UP]\n" +
                        "      sort-column-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate+DOWN]\n" +
                        "    sort-column-date-time-SubMenu \"Date Time\"\n" +
                        "      sort-column-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+UP]\n" +
                        "      sort-column-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Ddate-time+DOWN]\n" +
                        "    sort-column-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      sort-column-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+UP]\n" +
                        "      sort-column-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-month+DOWN]\n" +
                        "    sort-column-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      sort-column-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+UP]\n" +
                        "      sort-column-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dday-of-week+DOWN]\n" +
                        "    sort-column-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      sort-column-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+UP]\n" +
                        "      sort-column-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-am-pm+DOWN]\n" +
                        "    sort-column-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      sort-column-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+UP]\n" +
                        "      sort-column-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dhour-of-day+DOWN]\n" +
                        "    sort-column-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      sort-column-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+UP]\n" +
                        "      sort-column-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dminute-of-hour+DOWN]\n" +
                        "    sort-column-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      sort-column-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+UP]\n" +
                        "      sort-column-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dmonth-of-year+DOWN]\n" +
                        "    sort-column-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      sort-column-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+UP]\n" +
                        "      sort-column-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnano-of-second+DOWN]\n" +
                        "    sort-column-number-SubMenu \"Number\"\n" +
                        "      sort-column-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+UP]\n" +
                        "      sort-column-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dnumber+DOWN]\n" +
                        "    sort-column-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      sort-column-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+UP]\n" +
                        "      sort-column-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dseconds-of-minute+DOWN]\n" +
                        "    sort-column-text-SubMenu \"Text\"\n" +
                        "      sort-column-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+UP]\n" +
                        "      sort-column-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext+DOWN]\n" +
                        "    sort-column-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      sort-column-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+UP]\n" +
                        "      sort-column-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtext-case-insensitive+DOWN]\n" +
                        "    sort-column-time-SubMenu \"Time\"\n" +
                        "      sort-column-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+UP]\n" +
                        "      sort-column-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dtime+DOWN]\n" +
                        "    sort-column-year-SubMenu \"Year\"\n" +
                        "      sort-column-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+UP]\n" +
                        "      sort-column-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A%3Dyear+DOWN]\n" +
                        "    -----\n" +
                        "    sort-column-edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A1/sort/edit/A%3D]\n"
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
                SpreadsheetIcons.rowSort(),
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"Sort Row\"\n" +
                        "  (mdi-sort) sort-row-SubMenu \"Sort Row\"\n" +
                        "    sort-row-date-SubMenu \"Date\"\n" +
                        "      sort-row-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+UP]\n" +
                        "      sort-row-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate+DOWN]\n" +
                        "    sort-row-date-time-SubMenu \"Date Time\"\n" +
                        "      sort-row-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+UP]\n" +
                        "      sort-row-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Ddate-time+DOWN]\n" +
                        "    sort-row-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      sort-row-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+UP]\n" +
                        "      sort-row-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-month+DOWN]\n" +
                        "    sort-row-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      sort-row-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+UP]\n" +
                        "      sort-row-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dday-of-week+DOWN]\n" +
                        "    sort-row-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      sort-row-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+UP]\n" +
                        "      sort-row-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-am-pm+DOWN]\n" +
                        "    sort-row-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      sort-row-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+UP]\n" +
                        "      sort-row-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dhour-of-day+DOWN]\n" +
                        "    sort-row-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      sort-row-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+UP]\n" +
                        "      sort-row-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dminute-of-hour+DOWN]\n" +
                        "    sort-row-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      sort-row-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+UP]\n" +
                        "      sort-row-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dmonth-of-year+DOWN]\n" +
                        "    sort-row-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      sort-row-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+UP]\n" +
                        "      sort-row-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnano-of-second+DOWN]\n" +
                        "    sort-row-number-SubMenu \"Number\"\n" +
                        "      sort-row-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+UP]\n" +
                        "      sort-row-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dnumber+DOWN]\n" +
                        "    sort-row-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      sort-row-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+UP]\n" +
                        "      sort-row-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dseconds-of-minute+DOWN]\n" +
                        "    sort-row-text-SubMenu \"Text\"\n" +
                        "      sort-row-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+UP]\n" +
                        "      sort-row-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext+DOWN]\n" +
                        "    sort-row-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      sort-row-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+UP]\n" +
                        "      sort-row-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtext-case-insensitive+DOWN]\n" +
                        "    sort-row-time-SubMenu \"Time\"\n" +
                        "      sort-row-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+UP]\n" +
                        "      sort-row-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dtime+DOWN]\n" +
                        "    sort-row-year-SubMenu \"Year\"\n" +
                        "      sort-row-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+UP]\n" +
                        "      sort-row-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12%3Dyear+DOWN]\n" +
                        "    -----\n" +
                        "    sort-row-edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/A12/sort/edit/12%3D]\n"
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
                SpreadsheetIcons.columnSort(),
                SpreadsheetComparatorProviders.builtIn()
                        .spreadsheetComparatorInfos(),
                "sort \"Sort Column\"\n" +
                        "  (mdi-sort) sort-column-SubMenu \"Sort Column\"\n" +
                        "    sort-column-date-SubMenu \"Date\"\n" +
                        "      sort-column-date-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+UP]\n" +
                        "      sort-column-date-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate+DOWN]\n" +
                        "    sort-column-date-time-SubMenu \"Date Time\"\n" +
                        "      sort-column-date-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+UP]\n" +
                        "      sort-column-date-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Ddate-time+DOWN]\n" +
                        "    sort-column-day-of-month-SubMenu \"Day Of Month\"\n" +
                        "      sort-column-day-of-month-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+UP]\n" +
                        "      sort-column-day-of-month-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-month+DOWN]\n" +
                        "    sort-column-day-of-week-SubMenu \"Day Of Week\"\n" +
                        "      sort-column-day-of-week-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+UP]\n" +
                        "      sort-column-day-of-week-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dday-of-week+DOWN]\n" +
                        "    sort-column-hour-of-am-pm-SubMenu \"Hour Of Am Pm\"\n" +
                        "      sort-column-hour-of-am-pm-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+UP]\n" +
                        "      sort-column-hour-of-am-pm-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-am-pm+DOWN]\n" +
                        "    sort-column-hour-of-day-SubMenu \"Hour Of Day\"\n" +
                        "      sort-column-hour-of-day-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+UP]\n" +
                        "      sort-column-hour-of-day-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dhour-of-day+DOWN]\n" +
                        "    sort-column-minute-of-hour-SubMenu \"Minute Of Hour\"\n" +
                        "      sort-column-minute-of-hour-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+UP]\n" +
                        "      sort-column-minute-of-hour-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dminute-of-hour+DOWN]\n" +
                        "    sort-column-month-of-year-SubMenu \"Month Of Year\"\n" +
                        "      sort-column-month-of-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+UP]\n" +
                        "      sort-column-month-of-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dmonth-of-year+DOWN]\n" +
                        "    sort-column-nano-of-second-SubMenu \"Nano Of Second\"\n" +
                        "      sort-column-nano-of-second-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+UP]\n" +
                        "      sort-column-nano-of-second-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnano-of-second+DOWN]\n" +
                        "    sort-column-number-SubMenu \"Number\"\n" +
                        "      sort-column-number-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+UP]\n" +
                        "      sort-column-number-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dnumber+DOWN]\n" +
                        "    sort-column-seconds-of-minute-SubMenu \"Seconds Of Minute\"\n" +
                        "      sort-column-seconds-of-minute-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+UP]\n" +
                        "      sort-column-seconds-of-minute-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dseconds-of-minute+DOWN]\n" +
                        "    sort-column-text-SubMenu \"Text\"\n" +
                        "      sort-column-text-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+UP]\n" +
                        "      sort-column-text-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext+DOWN]\n" +
                        "    sort-column-text-case-insensitive-SubMenu \"Text Case Insensitive\"\n" +
                        "      sort-column-text-case-insensitive-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+UP]\n" +
                        "      sort-column-text-case-insensitive-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtext-case-insensitive+DOWN]\n" +
                        "    sort-column-time-SubMenu \"Time\"\n" +
                        "      sort-column-time-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+UP]\n" +
                        "      sort-column-time-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dtime+DOWN]\n" +
                        "    sort-column-year-SubMenu \"Year\"\n" +
                        "      sort-column-year-UP-MenuItem \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+UP]\n" +
                        "      sort-column-year-DOWN-MenuItem \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B%3Dyear+DOWN]\n" +
                        "    -----\n" +
                        "    sort-column-edit-MenuItem \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B%3D]\n"
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
