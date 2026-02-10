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

package walkingkooka.spreadsheet.dominokit.sort;

import org.junit.jupiter.api.Test;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorSelector;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;

public final class SpreadsheetCellSortDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetCellSortDialogComponent>,
    SpreadsheetMetadataTesting {

    // should have one *EMPTY* SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent
    @Test
    public void testOnHistoryTokenChangeWhenCellEmpty() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                ""
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // The first column is invalid so should include an ERROR MESSAGE
    @Test
    public void testOnHistoryTokenChangeWhenCellInvalidColumn() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "Z=text"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [Z=text] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Invalid column(s) Z are not within B2:C3\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [Z=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                          Errors\n" +
                "                            Invalid Column Z is not within B2:C3\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,time] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/Z=text,year] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // The first column is invalid so should include an ERROR MESSAGE
    @Test
    public void testOnHistoryTokenChangeWhenCellInvalidRow() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "99=text"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [99=text] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Invalid row(s) 99 are not within B2:C3\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [99=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                          Errors\n" +
                "                            Invalid Row 99 is not within B2:C3\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,time] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/99=text,year] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // should have two SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent
    // the first with "B" and an empty second.
    @Test
    public void testOnHistoryTokenChangeWhenCellColumn() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "B"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Missing '='\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                          Errors\n" +
                "                            Missing '='\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=time] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=year] id=SpreadsheetCellSort-comparatorNames-0-append-14-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWhenCellColumnEqualsSign() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "B="
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Missing comparator name\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                          Errors\n" +
                "                            Missing comparator name\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=time] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=year] id=SpreadsheetCellSort-comparatorNames-0-append-14-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // should only have 2x SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent because there are only
    // 2 columns in the range.
    @Test
    public void testOnHistoryTokenChangeWhenCellColumnComparatorName() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "B=text"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,time] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,year] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/save/B=text] id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWhenCellColumnComparatorNameComparatorName() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "B=text,text2"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text,text2] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text,text2] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,time] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text2,year] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text2] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "                        \"text2\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-0-remove-1-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/save/B=text,text2] id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWhenCellColumnComparatorNameColumnComparatorName() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "B=text;C=text2"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text;C=text2] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/C=text2;B=text] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,date;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,date-time;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,day-of-month;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,day-of-week;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,hour-of-am-pm;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,hour-of-day;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,minute-of-hour;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,month-of-year;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,nano-of-second;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,number;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,seconds-of-minute;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text-case-insensitive;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,time;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,year;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/C=text2] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [C=text2] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/C=text2;B=text] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,date] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,date-time] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,day-of-month] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,day-of-week] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,hour-of-day] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,month-of-year] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,nano-of-second] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,number] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,text] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,time] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;C=text2,year] id=SpreadsheetCellSort-comparatorNames-1-append-14-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text2\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/save/B=text;C=text2] id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // contains a duplicate COLUMN in the expression so the 2nd SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent
    // should have an error
    @Test
    public void testOnHistoryTokenChangeWhenCellWithDuplicateColumn() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:C3",
                "B=text;B=text2"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text;B=text2] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Duplicate column B\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text2;B=text] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,date;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,date-time;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,day-of-month;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,day-of-week;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,hour-of-am-pm;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,hour-of-day;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,minute-of-hour;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,month-of-year;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,nano-of-second;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,number;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,seconds-of-minute;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,text-case-insensitive;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,time;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text,year;B=text2] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text2] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text2] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Duplicate Column B\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text2;B=text] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,date] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,date-time] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,day-of-month] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,day-of-week] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,hour-of-day] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,month-of-year] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,nano-of-second] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,number] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,text] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,time] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text;B=text2,year] id=SpreadsheetCellSort-comparatorNames-1-append-14-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text2\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:C3/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // Must have 3 SpreadsheetColumnOrRowSpreadsheetComparatorName: B, C, *EMPTY*
    @Test
    public void testOnHistoryTokenChangeWhenCellWith2ColumnsAndColumnComparatorNameColumnComparatorName() {
        this.onHistoryTokenChangeAndCheck(
            this.cellAppContext(
                "B2:D4",
                "B=text;C=text2"
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text;C=text2] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/C=text2;B=text] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,date;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,date-time;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,day-of-month;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,day-of-week;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,hour-of-am-pm;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,hour-of-day;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,minute-of-hour;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,month-of-year;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,nano-of-second;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,number;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,seconds-of-minute;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,text-case-insensitive;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,time;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text,year;C=text2] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/C=text2] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [C=text2] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/C=text2;B=text] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,date] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,date-time] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,day-of-month] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,day-of-week] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,hour-of-day] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,month-of-year] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,nano-of-second] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,number] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,text] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,time] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text;C=text2,year] id=SpreadsheetCellSort-comparatorNames-1-append-14-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text2\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-2-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-2-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-2-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right/sort/save/B=text;C=text2] id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/B2:D4/bottom-right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testColumn() {
        this.onHistoryTokenChangeAndCheck(
            this.appContext(
                HistoryToken.columnSortEdit(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.parseColumnRange("B:C")
                        .setDefaultAnchor(),
                    "B=text"
                )
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,time] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,year] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" [#/1/SpreadsheetName1/column/B:C/right/sort/save/B=text] id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/column/B:C/right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // only the 2nd SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent should have the duplicate Column error
    @Test
    public void testColumnDuplicateColumn() {
        this.onHistoryTokenChangeAndCheck(
            this.appContext(
                HistoryToken.columnSortEdit(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.parseColumnRange("B:C")
                        .setDefaultAnchor(),
                    "B=text;B=text-case-insensitive"
                )
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text;B=text-case-insensitive] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Duplicate column B\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text-case-insensitive;B=text] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,date;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,date-time;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,day-of-month;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,day-of-week;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,hour-of-am-pm;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,hour-of-day;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,minute-of-hour;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,month-of-year;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,nano-of-second;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,number;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,seconds-of-minute;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,text-case-insensitive;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,time;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,year;B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Duplicate Column B\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text-case-insensitive;B=text] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,date] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,date-time] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,day-of-month] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,day-of-week] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,hour-of-day] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,month-of-year] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,nano-of-second] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,number] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,text] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,time] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;B=text-case-insensitive,year] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text-case-insensitive\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/column/B:C/right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testColumnGotRow() {
        this.onHistoryTokenChangeAndCheck(
            this.appContext(
                HistoryToken.columnSortEdit(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.parseColumnRange("B:C")
                        .setDefaultAnchor(),
                    "B=text;2=text-case-insensitive"
                )
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [B=text;2=text-case-insensitive] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Got Row 2 expected Column\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [B=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/2=text-case-insensitive;B=text] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,date;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,date-time;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,day-of-month;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,day-of-week;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,hour-of-am-pm;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,hour-of-day;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,minute-of-hour;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,month-of-year;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,nano-of-second;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,number;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,seconds-of-minute;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,text-case-insensitive;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,time;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text,year;2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [2=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Got Row 2 expected Column\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/2=text-case-insensitive;B=text] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,date] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,date-time] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,day-of-month] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,day-of-week] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,hour-of-day] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,month-of-year] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,nano-of-second] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,number] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,text] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,time] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text;2=text-case-insensitive,year] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text-case-insensitive\" [#/1/SpreadsheetName1/column/B:C/right/sort/edit/B=text] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/column/B:C/right] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testRow() {
        this.onHistoryTokenChangeAndCheck(
            this.appContext(
                HistoryToken.rowSortEdit(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.parseRowRange("3:4")
                        .setDefaultAnchor(),
                    "3=text"
                )
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [3=text] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [3=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,date] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,date-time] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,day-of-month] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,day-of-week] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,hour-of-day] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,month-of-year] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,nano-of-second] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,number] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,time] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,year] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Empty \"text\"\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/save/3=text] id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/row/3:4/bottom] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    // only the 3rd SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent should have the duplicate Row error message.
    @Test
    public void testRowDuplicateRow() {
        this.onHistoryTokenChangeAndCheck(
            this.appContext(
                HistoryToken.rowSortEdit(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.parseRowRange("3:5")
                        .setDefaultAnchor(),
                    "3=text;4=text;3=text-case-insensitive"
                )
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [3=text;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Duplicate row 3\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [3=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/4=text;3=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,date;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,date-time;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,day-of-month;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,day-of-week;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,hour-of-am-pm;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,hour-of-day;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,minute-of-hour;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,month-of-year;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,nano-of-second;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,number;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,seconds-of-minute;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,text-case-insensitive;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,time;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text,year;4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/4=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [4=text] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/4=text;3=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;3=text-case-insensitive;4=text] id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,date;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,date-time;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,day-of-month;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,day-of-week;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,hour-of-am-pm;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,hour-of-day;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,minute-of-hour;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,month-of-year;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,nano-of-second;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,number;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,seconds-of-minute;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,text-case-insensitive;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,time;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text,year;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [3=text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-2-TextBox\n" +
                "                          Errors\n" +
                "                            Duplicate Row 3\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;3=text-case-insensitive;4=text] id=SpreadsheetCellSort-comparatorNames-2-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-2-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,date] id=SpreadsheetCellSort-comparatorNames-2-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,date-time] id=SpreadsheetCellSort-comparatorNames-2-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,day-of-month] id=SpreadsheetCellSort-comparatorNames-2-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,day-of-week] id=SpreadsheetCellSort-comparatorNames-2-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-2-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,hour-of-day] id=SpreadsheetCellSort-comparatorNames-2-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-2-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,month-of-year] id=SpreadsheetCellSort-comparatorNames-2-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,nano-of-second] id=SpreadsheetCellSort-comparatorNames-2-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,number] id=SpreadsheetCellSort-comparatorNames-2-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-2-append-10-Link\n" +
                "                            \"text\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,text] id=SpreadsheetCellSort-comparatorNames-2-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,time] id=SpreadsheetCellSort-comparatorNames-2-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text;3=text-case-insensitive,year] id=SpreadsheetCellSort-comparatorNames-2-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text-case-insensitive\" [#/1/SpreadsheetName1/row/3:5/bottom/sort/edit/3=text;4=text] id=SpreadsheetCellSort-comparatorNames-2-remove-0-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/row/3:5/bottom] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    @Test
    public void testRowGotColumn() {
        this.onHistoryTokenChangeAndCheck(
            this.appContext(
                HistoryToken.rowSortEdit(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.parseRowRange("3:4")
                        .setDefaultAnchor(),
                    "3=text;A=text"
                )
            ),
            "SpreadsheetCellSortDialogComponent\n" +
                "  DialogComponent\n" +
                "    Sort\n" +
                "    id=SpreadsheetCellSort-Dialog includeClose=true\n" +
                "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [3=text;A=text] id=SpreadsheetCellSort-columnOrRowComparatorNamesList-TextBox\n" +
                "            Errors\n" +
                "              Got Column A expected Row\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [3=text] id=SpreadsheetCellSort-comparatorNames-0-TextBox\n" +
                "                    \"Move Up\" DISABLED id=SpreadsheetCellSort-comparatorNames-0-moveUp-Link\n" +
                "                    \"Move Down\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/A=text;3=text] id=SpreadsheetCellSort-comparatorNames-0-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,date;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,date-time;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,day-of-month;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,day-of-week;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,hour-of-am-pm;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,hour-of-day;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,minute-of-hour;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,month-of-year;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,nano-of-second;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,number;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,seconds-of-minute;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,text-case-insensitive;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,time;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text,year;A=text] id=SpreadsheetCellSort-comparatorNames-0-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/A=text] id=SpreadsheetCellSort-comparatorNames-0-remove-0-Link\n" +
                "          SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "            FlexLayoutComponent\n" +
                "              COLUMN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                "                      ValueTextBoxComponent\n" +
                "                        TextBoxComponent\n" +
                "                          [A=text] id=SpreadsheetCellSort-comparatorNames-1-TextBox\n" +
                "                          Errors\n" +
                "                            Got Column A expected Row\n" +
                "                    \"Move Up\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/A=text;3=text] id=SpreadsheetCellSort-comparatorNames-1-moveUp-Link\n" +
                "                    \"Move Down\" DISABLED id=SpreadsheetCellSort-comparatorNames-1-moveDown-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAppenderComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Append comparator(s)\n" +
                "                        FlexLayoutComponent\n" +
                "                          ROW\n" +
                "                            \"date\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,date] id=SpreadsheetCellSort-comparatorNames-1-append-0-Link\n" +
                "                            \"date-time\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,date-time] id=SpreadsheetCellSort-comparatorNames-1-append-1-Link\n" +
                "                            \"day-of-month\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,day-of-month] id=SpreadsheetCellSort-comparatorNames-1-append-2-Link\n" +
                "                            \"day-of-week\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,day-of-week] id=SpreadsheetCellSort-comparatorNames-1-append-3-Link\n" +
                "                            \"hour-of-am-pm\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,hour-of-am-pm] id=SpreadsheetCellSort-comparatorNames-1-append-4-Link\n" +
                "                            \"hour-of-day\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,hour-of-day] id=SpreadsheetCellSort-comparatorNames-1-append-5-Link\n" +
                "                            \"minute-of-hour\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,minute-of-hour] id=SpreadsheetCellSort-comparatorNames-1-append-6-Link\n" +
                "                            \"month-of-year\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,month-of-year] id=SpreadsheetCellSort-comparatorNames-1-append-7-Link\n" +
                "                            \"nano-of-second\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,nano-of-second] id=SpreadsheetCellSort-comparatorNames-1-append-8-Link\n" +
                "                            \"number\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,number] id=SpreadsheetCellSort-comparatorNames-1-append-9-Link\n" +
                "                            \"seconds-of-minute\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,seconds-of-minute] id=SpreadsheetCellSort-comparatorNames-1-append-10-Link\n" +
                "                            \"text-case-insensitive\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,text-case-insensitive] id=SpreadsheetCellSort-comparatorNames-1-append-11-Link\n" +
                "                            \"time\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,time] id=SpreadsheetCellSort-comparatorNames-1-append-12-Link\n" +
                "                            \"year\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text;A=text,year] id=SpreadsheetCellSort-comparatorNames-1-append-13-Link\n" +
                "                SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameRemoverComponent\n" +
                "                  CardComponent\n" +
                "                    Card\n" +
                "                      Remove comparator(s)\n" +
                "                        \"text\" [#/1/SpreadsheetName1/row/3:4/bottom/sort/edit/3=text] id=SpreadsheetCellSort-comparatorNames-1-remove-0-Link\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Sort\" DISABLED id=SpreadsheetCellSort-sort-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/row/3:4/bottom] id=SpreadsheetCellSort-close-Link\n"
        );
    }

    private AppContext cellAppContext(final String cellRange,
                                      final String edit) {
        return this.appContext(
            HistoryToken.cellSortEdit(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCellRange(cellRange)
                    .setDefaultAnchor(),
                edit
            )
        );
    }

    private AppContext appContext(final HistoryToken historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return SpreadsheetViewportCache.empty(this);
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorSelector selector,
                                                                  final ProviderContext context) {
                return SPREADSHEET_COMPARATOR_PROVIDER.spreadsheetComparator(
                    selector,
                    context
                );
            }

            @Override
            public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                                  final List<?> values,
                                                                  final ProviderContext context) {
                return SPREADSHEET_COMPARATOR_PROVIDER.spreadsheetComparator(
                    name,
                    values,
                    context
                );
            }

            @Override
            public SpreadsheetComparatorInfoSet spreadsheetComparatorInfos() {
                return SPREADSHEET_COMPARATOR_PROVIDER.spreadsheetComparatorInfos();
            }
        };
    }

    private SpreadsheetCellSortDialogComponent dialog(final AppContext context) {
        return SpreadsheetCellSortDialogComponent.with(
            new FakeSpreadsheetCellSortDialogComponentContext() {

                @Override
                public String dialogTitle() {
                    return "Sort";
                }

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return context.addHistoryTokenWatcher(watcher);
                }

                @Override
                public HistoryToken historyToken() {
                    return context.historyToken();
                }

                @Override
                public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorSelector selector,
                                                                      final ProviderContext c) {
                    return context.spreadsheetComparator(
                        selector,
                        context
                    );
                }

                @Override
                public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                                      final List<?> values,
                                                                      final ProviderContext c) {
                    return context.spreadsheetComparator(
                        name,
                        values,
                        context
                    );
                }

                @Override
                public SpreadsheetComparatorInfoSet spreadsheetComparatorInfos() {
                    return context.spreadsheetComparatorInfos();
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return context.spreadsheetViewportCache();
                }
            }
        );
    }

    private void onHistoryTokenChangeAndCheck(final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
            this.dialog(context),
            context,
            expected
        );
    }

    @Override
    public SpreadsheetCellSortDialogComponent createSpreadsheetDialogComponentLifecycle(HistoryToken historyToken) {
        return SpreadsheetCellSortDialogComponent.with(
            new FakeSpreadsheetCellSortDialogComponentContext() {

                @Override
                public String dialogTitle() {
                    return "Sort";
                }

                @Override
                public HistoryToken historyToken() {
                    return historyToken;
                }

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return null;
                }
            }
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCellSortDialogComponent> type() {
        return SpreadsheetCellSortDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
