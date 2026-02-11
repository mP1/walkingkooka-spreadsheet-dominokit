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
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
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
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date] id=column-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date-reversed] id=column-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=column-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date-time] id=column-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=date-time-reversed] id=column-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=column-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-month] id=column-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-month-reversed] id=column-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=column-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-week] id=column-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=day-of-week-reversed] id=column-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-am-pm] id=column-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-am-pm-reversed] id=column-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=column-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-day] id=column-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=hour-of-day-reversed] id=column-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=column-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=minute-of-hour] id=column-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=minute-of-hour-reversed] id=column-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=column-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=month-of-year] id=column-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=month-of-year-reversed] id=column-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=column-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=nano-of-second] id=column-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=nano-of-second-reversed] id=column-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=column-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=number] id=column-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=number-reversed] id=column-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=seconds-of-minute] id=column-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=seconds-of-minute-reversed] id=column-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=column-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text] id=column-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text-reversed] id=column-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text-case-insensitive] id=column-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=text-case-insensitive-reversed] id=column-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=column-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=time] id=column-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=time-reversed] id=column-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=column-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=year] id=column-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A1/sort/save/A=year-reversed] id=column-sort-year-reverse-MenuItem\n" +
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
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date] id=row-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date-reversed] id=row-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=row-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date-time] id=row-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=date-time-reversed] id=row-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=row-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-month] id=row-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-month-reversed] id=row-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=row-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-week] id=row-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=day-of-week-reversed] id=row-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-am-pm] id=row-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-am-pm-reversed] id=row-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=row-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-day] id=row-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=hour-of-day-reversed] id=row-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=row-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=minute-of-hour] id=row-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=minute-of-hour-reversed] id=row-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=row-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=month-of-year] id=row-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=month-of-year-reversed] id=row-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=row-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=nano-of-second] id=row-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=nano-of-second-reversed] id=row-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=row-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=number] id=row-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=number-reversed] id=row-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=seconds-of-minute] id=row-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=seconds-of-minute-reversed] id=row-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=row-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text] id=row-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text-reversed] id=row-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text-case-insensitive] id=row-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=text-case-insensitive-reversed] id=row-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=row-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=time] id=row-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=time-reversed] id=row-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=row-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=year] id=row-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/A12/sort/save/12=year-reversed] id=row-sort-year-reverse-MenuItem\n" +
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
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date] id=column-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date-reversed] id=column-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=column-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date-time] id=column-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=date-time-reversed] id=column-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=column-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month] id=column-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month-reversed] id=column-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=column-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-week] id=column-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-week-reversed] id=column-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-am-pm] id=column-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-am-pm-reversed] id=column-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=column-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-day] id=column-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=hour-of-day-reversed] id=column-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=column-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=minute-of-hour] id=column-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=minute-of-hour-reversed] id=column-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=column-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=month-of-year] id=column-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=month-of-year-reversed] id=column-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=column-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=nano-of-second] id=column-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=nano-of-second-reversed] id=column-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=column-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=number] id=column-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=number-reversed] id=column-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=seconds-of-minute] id=column-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=seconds-of-minute-reversed] id=column-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=column-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text] id=column-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text-reversed] id=column-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text-case-insensitive] id=column-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=text-case-insensitive-reversed] id=column-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=column-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=time] id=column-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=time-reversed] id=column-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=column-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year] id=column-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year-reversed] id=column-sort-year-reverse-MenuItem\n" +
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
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month] id=column-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=day-of-month-reversed] id=column-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Year\" id=column-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year] id=column-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2/sort/save/B=year-reversed] id=column-sort-year-reverse-MenuItem\n" +
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
