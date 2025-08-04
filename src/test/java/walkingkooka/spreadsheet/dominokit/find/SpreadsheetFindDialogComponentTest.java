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

package walkingkooka.spreadsheet.dominokit.find;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFindHistoryToken;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetFindDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetFindDialogComponent>,
    SpreadsheetMetadataTesting {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellFindHistoryToken() {
        final SpreadsheetCellFindHistoryToken historyToken = HistoryToken.cellFind(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            SpreadsheetCellFindQuery.empty()
        );

        this.isMatchAndCheck(
            SpreadsheetFindDialogComponent.with(
                new TestSpreadsheetFindDialogComponentContext(
                    this.appContext(
                        Optional.empty(), // no highlighting query
                        historyToken
                    )
                )
            ),
            historyToken,
            true
        );
    }

    // onHistoryTokenChange.............................................................................................

    @Test
    public void testOnHistoryTokenChangeNoMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [BULR] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [date] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/matchXyz()] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeNoMatchesAndMetadataContainsHighlightingQuery() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final AppContext appContext = this.appContext(
            Optional.of(
                SpreadsheetCellQuery.parse("highlightQuery()")
            ), // WITH highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [BULR] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [date] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/highlightQuery()] id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/matchXyz()] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithNoQuery() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/"
        );

        final AppContext appContext = this.appContext(
            Optional.of(
                SpreadsheetCellQuery.parse("highlightQuery()")
            ), // WITH highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" [#/123/SpreadsheetName456/cell/A1/find/query/highlightQuery()] id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" DISABLED id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeShowingMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final TestAppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [BULR] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [date] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/matchXyz()] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );

        // required otherwise SpreadsheetViewportCache will "ignore delta because they belong to a different spreadsheet
        appContext.metadataFetcherWatchers.onSpreadsheetMetadata(
            appContext.spreadsheetMetadata(),
            appContext
        );

        // SpreadsheetId=123
        this.checkEquals(
            SpreadsheetId.with(0x123),
            appContext.spreadsheetMetadata()
                .id()
                .orElse(null),
            "active spreadsheet id = 123"
        );

        appContext.deltaFetcherWatchers.onSpreadsheetDelta(
            HttpMethod.GET,
            Url.parseRelative("/api/spreadsheet/123/cell/B1/find?cell-range-path=lrtd&query=%3Dtrue%28%29"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1")
                            .setValueType(
                                Optional.of(ValidationValueTypeName.TEXT)
                            )
                    ),
                    SpreadsheetSelection.parseCell("B2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY.setText("=2")
                        )
                )
            ).setLabels(
                Sets.of(
                    SpreadsheetSelection.labelName("Label1")
                        .setLabelMappingReference(SpreadsheetSelection.A1),
                    SpreadsheetSelection.labelName("Label2")
                        .setLabelMappingReference(SpreadsheetSelection.A1),
                    SpreadsheetSelection.labelName("Label3")
                        .setLabelMappingReference(SpreadsheetSelection.A1)
                )
            ).setReferences(
                Maps.of(
                    SpreadsheetSelection.A1,
                    Sets.of(
                        SpreadsheetSelection.parseCell("Z1")
                    )
                )
            ), // delta
            appContext
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [BULR] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [date] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/matchXyz()] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"A1\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-cells-A1-Link\n" +
                "                  \"A1\" [#/123/SpreadsheetName456/cell/A1/formula] id=cellFind-cells-A1-formula-Link\n" +
                "                  TextComponent\n" +
                "                    \"\"\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetCellLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"Value\" [#/123/SpreadsheetName456/cell/A1/value/text] id=cellFind-cells-A1-value-Link\n" +
                "                          \"Create Label\" [#/123/SpreadsheetName456/cell/A1/label] id=cellFind-cells-A1-createLabel-Link\n" +
                "                          \"Labels\" [#/123/SpreadsheetName456/cell/A1/labels] (3) id=cellFind-cells-A1-label-Link\n" +
                "                          \"References\" [#/123/SpreadsheetName456/cell/A1/references] (1) id=cellFind-cells-A1-references-Link\n" +
                "                          \"Delete\" [#/123/SpreadsheetName456/cell/A1/delete] id=cellFind-cells-A1-delete-Link\n" +
                "                ROW 1\n" +
                "                  \"B2\" [#/123/SpreadsheetName456/cell/B2] id=cellFind-cells-B2-Link\n" +
                "                  \"=2\" [#/123/SpreadsheetName456/cell/B2/formula] id=cellFind-cells-B2-formula-Link\n" +
                "                  TextComponent\n" +
                "                    \"\"\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetCellLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"Value\" DISABLED id=cellFind-cells-B2-value-Link\n" +
                "                          \"Create Label\" [#/123/SpreadsheetName456/cell/B2/label] id=cellFind-cells-B2-createLabel-Link\n" +
                "                          \"Labels\" [#/123/SpreadsheetName456/cell/B2/labels] (0) id=cellFind-cells-B2-label-Link\n" +
                "                          \"References\" [#/123/SpreadsheetName456/cell/B2/references] (0) id=cellFind-cells-B2-references-Link\n" +
                "                          \"Delete\" [#/123/SpreadsheetName456/cell/B2/delete] id=cellFind-cells-B2-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormula(),\"*formula*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [*formula*] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(cellFormula(),\"*formula*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormula(),%22*formula*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormula(),%22*formula*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/textMatch(cellFormula(),%22*formula*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetterAndExpression() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/1+textMatch(cellFormula(),\"*formula*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [*formula*] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [1+textMatch(cellFormula(),\"*formula*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/1+textMatch(cellFormula(),%22*formula*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/1+textMatch(cellFormula(),%22*formula*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/1+textMatch(cellFormula(),%22*formula*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetterOrFormatterGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),\"*formula*\"),textMatch(cellFormatter(),\"*formatter*\"))"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [*formula*] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [*formatter*] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [or(textMatch(cellFormula(),\"*formula*\"),textMatch(cellFormatter(),\"*formatter*\"))] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),textMatch(cellFormatter(),%22*formatter*%22))] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),textMatch(cellFormatter(),%22*formatter*%22))] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/or(textMatch(cellFormula(),%22*formula*%22),textMatch(cellFormatter(),%22*formatter*%22))] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetterOrTrue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),\"*formula*\"),true())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [*formula*] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [or(textMatch(cellFormula(),\"*formula*\"),true())] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),true())] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),true())] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/or(textMatch(cellFormula(),%22*formula*%22),true())] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithDateTimeSymbols() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellDateTimeSymbols(),\"*Hello*\"),1)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [*Hello*] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [or(textMatch(cellDateTimeSymbols(),\"*Hello*\"),1)] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellDateTimeSymbols(),%22*Hello*%22),1)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellDateTimeSymbols(),%22*Hello*%22),1)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/or(textMatch(cellDateTimeSymbols(),%22*Hello*%22),1)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithDecimalNumberSymbols() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellDecimalNumberSymbols(),\"*Hello*\"),1)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [or(textMatch(cellDecimalNumberSymbols(),\"*Hello*\"),1)] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellDecimalNumberSymbols(),%22*Hello*%22),1)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellDecimalNumberSymbols(),%22*Hello*%22),1)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/or(textMatch(cellDecimalNumberSymbols(),%22*Hello*%22),1)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormatterGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormatter(),\"*formatter*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [*formatter*] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(cellFormatter(),\"*formatter*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormatter(),%22*formatter*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormatter(),%22*formatter*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/textMatch(cellFormatter(),%22*formatter*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithParserGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellParser(),\"*parser*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [*parser*] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(cellParser(),\"*parser*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellParser(),%22*parser*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellParser(),%22*parser*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/textMatch(cellParser(),%22*parser*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithStyleGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellStyle(),\"*style*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [*style*] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(cellStyle(),\"*style*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellStyle(),%22*style*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellStyle(),%22*style*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/textMatch(cellStyle(),%22*style*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithValueGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/cellValue()<999"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [<999] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [cellValue()<999] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/cellValue()%3C999] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/cellValue()%3C999] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/cellValue()%3C999] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithValidatorGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellValidator(),\"*validator123*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(cellValidator(),\"*validator123*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellValidator(),%22*validator123*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellValidator(),%22*validator123*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/textMatch(cellValidator(),%22*validator123*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormattedValueGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormattedValue(),\"*formatted-value*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [*formatted-value*] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(cellFormattedValue(),\"*formatted-value*\")] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormattedValue(),%22*formatted-value*%22)] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormattedValue(),%22*formatted-value*%22)] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/textMatch(cellFormattedValue(),%22*formatted-value*%22)] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithAllWizardFields() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/find/query/OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10, OR(textMatch(\"*validator*\",cellValidator()),textMatch(\"*formattedValue*\",cellFormattedValue()))))))))"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetFindDialogComponent\n" +
                "  DialogComponent\n" +
                "    Find\n" +
                "    id=cellFind-Dialog includeClose=true\n" +
                "      RowComponent\n" +
                "        SpreadsheetCellRangeReferenceComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Cell Range [A1] id=cellFind-cell-range-TextBox\n" +
                "        SpreadsheetCellRangeReferencePathComponent\n" +
                "          SelectComponent\n" +
                "            Cell Range Path [] id=cellFind-cell-range-path-Select\n" +
                "              left-right top-down=LRTD\n" +
                "              right-left top-down=RLTD\n" +
                "              left-right bottom-up=LRBU\n" +
                "              right-left bottom-up=RLBU\n" +
                "              top-down left-right=TDLR\n" +
                "              top-down right-left=TDRL\n" +
                "              bottom-up left-right=BULR\n" +
                "              bottom-up right-left=BURL\n" +
                "        SpreadsheetValueTypeComponent\n" +
                "          SelectComponent\n" +
                "            Value type [] id=cellFind-value-type-Select\n" +
                "              Any=*\n" +
                "              Boolean=boolean\n" +
                "              Date=date\n" +
                "              Date Time=dateTime\n" +
                "              Error=error\n" +
                "              Number=number\n" +
                "              Text=text\n" +
                "              Time=time\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formula [*formula*] id=cellFind-formula-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              DateTimeSymbols [] id=cellFind-datetimesymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              decimalNumberSymbols [] id=cellFind-decimalnumbersymbols-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatter [*formatted*] id=cellFind-formatter-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Parser [*parser*] id=cellFind-parser-TextBox\n" +
                "      RowComponent\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Style [*style*] id=cellFind-style-TextBox\n" +
                "        ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [<10] id=cellFind-value-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Validator [] id=cellFind-validator-TextBox\n" +
                "        TextMatchComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Formatted [*formattedValue*] id=cellFind-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10, OR(textMatch(\"*validator*\",cellValidator()),textMatch(\"*formattedValue*\",cellFormattedValue()))))))))] id=query-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,%20OR(textMatch(%22*validator*%22,cellValidator()),textMatch(%22*formattedValue*%22,cellFormattedValue()))))))))] id=cellFind-find-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,%20OR(textMatch(%22*validator*%22,cellValidator()),textMatch(%22*formattedValue*%22,cellFormattedValue()))))))))] id=cellFind-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=cellFind-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/findQuery/save/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,%20OR(textMatch(%22*validator*%22,cellValidator()),textMatch(%22*formattedValue*%22,cellFormattedValue()))))))))] id=cellFind-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=cellFind-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            SpreadsheetDataTableComponent\n" +
                "              id=cellFind-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=cellFind-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=cellFind-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    private TestAppContext appContext(final Optional<SpreadsheetCellQuery> highlightingQuery,
                                      final HistoryToken historyToken) {
        return new TestAppContext(highlightingQuery, historyToken);
    }

    static class TestAppContext extends FakeAppContext {

        TestAppContext(final Optional<SpreadsheetCellQuery> highlightingQuery,
                       final HistoryToken historyToken) {
            this.highlightingQuery = highlightingQuery;
            this.historyToken = historyToken;
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.deltaFetcherWatchers.add(watcher);
        }

        // necessary so some tests can fire a SpreadsheetDeltaFetcherWatcher#onSpreadsheetDelta
        final SpreadsheetDeltaFetcherWatchers deltaFetcherWatchers = SpreadsheetDeltaFetcherWatchers.empty();

        @Override
        public void giveFocus(final Runnable focus) {
            // ignore
        }

        @Override
        public HistoryToken historyToken() {
            return historyToken;
        }

        private final HistoryToken historyToken;

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return this.metadataFetcherWatchers.add(watcher);
        }

        final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers = SpreadsheetMetadataFetcherWatchers.empty();

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                SpreadsheetId.parse("123")
            ).setOrRemove(
                SpreadsheetMetadataPropertyName.FIND_QUERY,
                highlightingQuery.orElse(null)
            );
        }

        private final Optional<SpreadsheetCellQuery> highlightingQuery;

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.cache;
        }

        private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            System.out.println(Arrays.toString(values));
        }
    }

    static class TestSpreadsheetFindDialogComponentContext extends FakeSpreadsheetFindDialogComponentContext {

        TestSpreadsheetFindDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public String dialogTitle() {
            return "Find";
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
        }

        @Override
        public HistoryToken historyToken() {
            return this.context.historyToken();
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public SpreadsheetMetadata spreadsheetMetadata() {
            return this.context.spreadsheetMetadata();
        }

        @Override
        public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
            return this.context.spreadsheetViewportCache()
                .cell(selection);
        }

        @Override
        public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .cellLabels(spreadsheetExpressionReference);
        }

        @Override
        public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .cellReferences(spreadsheetExpressionReference);
        }

        // SpreadsheetFormulaSelectAnchorComponentContext...................................................................

        @Override
        public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
            return this.context.spreadsheetViewportCache()
                .formulaText(spreadsheetExpressionReference);
        }

        private final AppContext context;

        @Override
        public void findCells(final SpreadsheetId id,
                              final SpreadsheetCellRangeReference cells,
                              final SpreadsheetCellFindQuery find) {
            // nop
        }
    }

    @Override
    public SpreadsheetFindDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetFindDialogComponent.with(
            new TestSpreadsheetFindDialogComponentContext(
                new TestAppContext(
                    Optional.empty(),
                    historyToken
                )
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetFindDialogComponent> type() {
        return SpreadsheetFindDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
