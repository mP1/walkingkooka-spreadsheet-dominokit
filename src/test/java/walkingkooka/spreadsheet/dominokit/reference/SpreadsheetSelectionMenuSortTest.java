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

package walkingkooka.spreadsheet.dominokit.reference;

import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpreadsheetSelectionMenuSortTest implements ClassTesting<SpreadsheetSelectionMenuSort>,
    SpreadsheetMetadataTesting,
    TreePrintableTesting {

    private final Collection<SpreadsheetComparatorName> SORT_COMPARATOR_NAMES = SPREADSHEET_COMPARATOR_PROVIDER.spreadsheetComparatorInfos()
        .stream()
        .map(SpreadsheetComparatorInfo::name)
        .collect(Collectors.toList());

    @Test
    public void testBuildColumn() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        this.buildAndCheck(
            token,
            SpreadsheetSelection.parseColumn("A"),
            "column-sort-", // id-prefix
            SpreadsheetIcons.columnSort(),
            SORT_COMPARATOR_NAMES,
            "\"Sort Column\" id=sort\n" +
                "  (mdi-sort) \"Sort Column\" id=column-sort-SubMenu\n" +
                "    \"Date\" id=column-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date%20UP] id=column-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date%20DOWN] id=column-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=column-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date-time%20UP] id=column-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date-time%20DOWN] id=column-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-month%20UP] id=column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-month%20DOWN] id=column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=column-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-week%20UP] id=column-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-week%20DOWN] id=column-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-am-pm%20UP] id=column-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-am-pm%20DOWN] id=column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=column-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-day%20UP] id=column-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-day%20DOWN] id=column-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=column-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=minute-of-hour%20UP] id=column-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=minute-of-hour%20DOWN] id=column-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=column-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=month-of-year%20UP] id=column-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=month-of-year%20DOWN] id=column-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=column-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=nano-of-second%20UP] id=column-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=nano-of-second%20DOWN] id=column-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=column-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=number%20UP] id=column-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=number%20DOWN] id=column-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=seconds-of-minute%20UP] id=column-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=seconds-of-minute%20DOWN] id=column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=column-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text%20UP] id=column-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text%20DOWN] id=column-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text-case-insensitive%20UP] id=column-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text-case-insensitive%20DOWN] id=column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=column-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=time%20UP] id=column-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=time%20DOWN] id=column-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=year%20UP] id=column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=year%20DOWN] id=column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/A1/sort/edit/A=] id=column-sort-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildRow() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.parseCell("A12").setDefaultAnchor()
        );

        this.buildAndCheck(
            token,
            SpreadsheetSelection.parseRow("12"),
            "row-sort-", // id-prefix
            SpreadsheetIcons.rowSort(),
            SORT_COMPARATOR_NAMES,
            "\"Sort Row\" id=sort\n" +
                "  (mdi-sort) \"Sort Row\" id=row-sort-SubMenu\n" +
                "    \"Date\" id=row-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date%20UP] id=row-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date%20DOWN] id=row-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=row-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date-time%20UP] id=row-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date-time%20DOWN] id=row-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=row-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-month%20UP] id=row-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-month%20DOWN] id=row-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=row-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-week%20UP] id=row-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-week%20DOWN] id=row-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-am-pm%20UP] id=row-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-am-pm%20DOWN] id=row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=row-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-day%20UP] id=row-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-day%20DOWN] id=row-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=row-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=minute-of-hour%20UP] id=row-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=minute-of-hour%20DOWN] id=row-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=row-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=month-of-year%20UP] id=row-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=month-of-year%20DOWN] id=row-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=row-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=nano-of-second%20UP] id=row-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=nano-of-second%20DOWN] id=row-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=row-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=number%20UP] id=row-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=number%20DOWN] id=row-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=seconds-of-minute%20UP] id=row-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=seconds-of-minute%20DOWN] id=row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=row-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text%20UP] id=row-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text%20DOWN] id=row-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text-case-insensitive%20UP] id=row-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text-case-insensitive%20DOWN] id=row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=row-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=time%20UP] id=row-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=time%20DOWN] id=row-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=row-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=year%20UP] id=row-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=year%20DOWN] id=row-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/A12/sort/edit/12=] id=row-sort-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildCellColumn() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.parseCell("B2").setDefaultAnchor()
        );

        this.buildAndCheck(
            token,
            SpreadsheetSelection.parseColumn("B"),
            "column-sort-", // id-prefix
            SpreadsheetIcons.columnSort(),
            SORT_COMPARATOR_NAMES,
            "\"Sort Column\" id=sort\n" +
                "  (mdi-sort) \"Sort Column\" id=column-sort-SubMenu\n" +
                "    \"Date\" id=column-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date%20UP] id=column-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date%20DOWN] id=column-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=column-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date-time%20UP] id=column-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date-time%20DOWN] id=column-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month%20UP] id=column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month%20DOWN] id=column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=column-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-week%20UP] id=column-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-week%20DOWN] id=column-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-am-pm%20UP] id=column-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-am-pm%20DOWN] id=column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=column-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-day%20UP] id=column-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-day%20DOWN] id=column-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=column-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=minute-of-hour%20UP] id=column-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=minute-of-hour%20DOWN] id=column-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=column-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=month-of-year%20UP] id=column-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=month-of-year%20DOWN] id=column-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=column-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=nano-of-second%20UP] id=column-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=nano-of-second%20DOWN] id=column-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=column-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=number%20UP] id=column-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=number%20DOWN] id=column-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=seconds-of-minute%20UP] id=column-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=seconds-of-minute%20DOWN] id=column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=column-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text%20UP] id=column-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text%20DOWN] id=column-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text-case-insensitive%20UP] id=column-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text-case-insensitive%20DOWN] id=column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=column-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=time%20UP] id=column-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=time%20DOWN] id=column-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year%20UP] id=column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year%20DOWN] id=column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B=] id=column-sort-edit-MenuItem\n"
        );
    }

    @Test
    public void testBuildCellColumnLimitedComparatorNames() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.parseCell("B2").setDefaultAnchor()
        );

        this.buildAndCheck(
            token,
            SpreadsheetSelection.parseColumn("B"),
            "column-sort-", // id-prefix
            SpreadsheetIcons.columnSort(),
            Lists.of(
                SpreadsheetComparatorName.DAY_OF_MONTH,
                SpreadsheetComparatorName.YEAR
            ),
            "\"Sort Column\" id=sort\n" +
                "  (mdi-sort) \"Sort Column\" id=column-sort-SubMenu\n" +
                "    \"Day Of Month\" id=column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month%20UP] id=column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month%20DOWN] id=column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Year\" id=column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year%20UP] id=column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year%20DOWN] id=column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/B2/sort/edit/B=] id=column-sort-edit-MenuItem\n"
        );
    }

    private void buildAndCheck(final HistoryToken historyToken,
                               final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                               final String idPrefix,
                               final Icon<?> icon,
                               final Collection<SpreadsheetComparatorName> sortComparatorNames,
                               final String expected) {
        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "sort",
                "Sort " + columnOrRow.textLabel(),
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            HistoryContexts.fake()
        );

        SpreadsheetSelectionMenuSort.build(
            historyToken,
            columnOrRow,
            idPrefix,
            icon,
            sortComparatorNames,
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
