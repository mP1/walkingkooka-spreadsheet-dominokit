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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Arrays;
import java.util.Optional;

public final class SpreadsheetFindDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetFindDialogComponent>,
        SpreadsheetMetadataTesting {

    // refresh..........................................................................................................

    @Test
    public void testRefreshNoMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [date] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/matchXyz()] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshNoMatchesAndMetadataContainsHighlightingQuery() {
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
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [date] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/highlightQuery()] id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/matchXyz()] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithNoQuery() {
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
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" [#/123/SpreadsheetName456/cell/A1/find/query/highlightQuery()] id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" DISABLED id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshShowingMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [date] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/matchXyz()] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );

        dialog.table.onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/2/cell/B1/find?cell-range-path=lrtd&query=%3Dtrue%28%29"),
                SpreadsheetDelta.EMPTY.setCells(
                        Sets.of(
                                SpreadsheetSelection.A1.setFormula(
                                        SpreadsheetFormula.EMPTY.setText("=1")
                                ),
                                SpreadsheetSelection.parseCell("B2")
                                        .setFormula(
                                                SpreadsheetFormula.EMPTY.setText("=2")
                                        )
                        )
                ), // delta
                appContext
        );

        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [date] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  \"A1\" [#/123/SpreadsheetName456/cell/A1]\n" +
                        "                  \"=1\" [#/123/SpreadsheetName456/cell/A1/formula]\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"\"\n" +
                        "                ROW 1\n" +
                        "                  \"B2\" [#/123/SpreadsheetName456/cell/B2]\n" +
                        "                  \"=2\" [#/123/SpreadsheetName456/cell/B2/formula]\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                  SpreadsheetTextComponent\n" +
                        "                    \"\"\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/count/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/matchXyz()] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithFormulaGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormula(),\"*formula*\")"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [*formula*] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [textMatch(cellFormula(),\"*formula*\")] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormula(),%22*formula*%22)] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormula(),%22*formula*%22)] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/textMatch(cellFormula(),%22*formula*%22)] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithFormulaGetterAndExpression() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/1+textMatch(cellFormula(),\"*formula*\")"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [*formula*] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [1+textMatch(cellFormula(),\"*formula*\")] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/1+textMatch(cellFormula(),%22*formula*%22)] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/1+textMatch(cellFormula(),%22*formula*%22)] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/1+textMatch(cellFormula(),%22*formula*%22)] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithFormulaGetterOrFormatterGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),\"*formula*\"),textMatch(cellFormatter(),\"*formatter*\"))"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [*formula*] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [*formatter*] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [or(textMatch(cellFormula(),\"*formula*\"),textMatch(cellFormatter(),\"*formatter*\"))] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),textMatch(cellFormatter(),%22*formatter*%22))] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),textMatch(cellFormatter(),%22*formatter*%22))] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/or(textMatch(cellFormula(),%22*formula*%22),textMatch(cellFormatter(),%22*formatter*%22))] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithFormulaGetterOrTrue() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),\"*formula*\"),true())"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [*formula*] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [or(textMatch(cellFormula(),\"*formula*\"),true())] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),true())] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/or(textMatch(cellFormula(),%22*formula*%22),true())] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/or(textMatch(cellFormula(),%22*formula*%22),true())] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithFormatterGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormatter(),\"*formatter*\")"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [*formatter*] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [textMatch(cellFormatter(),\"*formatter*\")] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormatter(),%22*formatter*%22)] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormatter(),%22*formatter*%22)] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/textMatch(cellFormatter(),%22*formatter*%22)] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithParserGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellParser(),\"*parser*\")"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [*parser*] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [textMatch(cellParser(),\"*parser*\")] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellParser(),%22*parser*%22)] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellParser(),%22*parser*%22)] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/textMatch(cellParser(),%22*parser*%22)] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithStyleGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellStyle(),\"*style*\")"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [*style*] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [textMatch(cellStyle(),\"*style*\")] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellStyle(),%22*style*%22)] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellStyle(),%22*style*%22)] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/textMatch(cellStyle(),%22*style*%22)] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithValueGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/cellValue()<999"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [<999] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [cellValue()<999] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/cellValue()%3C999] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/cellValue()%3C999] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/cellValue()%3C999] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithFormattedValueGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormattedValue(),\"*formatted-value*\")"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [*formatted-value*] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [textMatch(cellFormattedValue(),\"*formatted-value*\")] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormattedValue(),%22*formatted-value*%22)] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/textMatch(cellFormattedValue(),%22*formatted-value*%22)] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/textMatch(cellFormattedValue(),%22*formatted-value*%22)] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    @Test
    public void testRefreshWithQueryWithAllWizardFields() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/query/OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue())))))))"
        );

        final AppContext appContext = this.appContext(
                Optional.empty(), // no highlighting query
                historyToken
        );

        final SpreadsheetFindDialogComponent dialog = SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
                dialog,
                appContext,
                "SpreadsheetFindDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Find\n" +
                        "    id=find includeClose=true\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [A1] id=find-cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find-cell-range-path-Select\n" +
                        "                left-right top-down=LRTD\n" +
                        "                right-left top-down=RLTD\n" +
                        "                left-right bottom-up=LRBU\n" +
                        "                right-left bottom-up=RLBU\n" +
                        "                top-down left-right=TDLR\n" +
                        "                top-down right-left=TDRL\n" +
                        "                bottom-up left-right=BULR\n" +
                        "                bottom-up right-left=BURL\n" +
                        "          SpreadsheetValueTypeComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Value type [] id=find-value-type-Select\n" +
                        "                Any=*\n" +
                        "                Boolean=boolean\n" +
                        "                Date=date\n" +
                        "                Error=error\n" +
                        "                DateTime=date-time\n" +
                        "                Number=number\n" +
                        "                Text=text\n" +
                        "                Time=time\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formula [*formula*] id=find-formula-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatter [*formatted*] id=find-formatter-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Parser [*parser*] id=find-parser-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Style [*style*] id=find-style-TextBox\n" +
                        "          SpreadsheetConditionRightParserTokenComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Value [<10] id=find-value-TextBox\n" +
                        "          TextMatchComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Formatted [*formattedValue*] id=find-formatted-TextBox\n" +
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10,textMatch(\"*formattedValue*\",cellFormattedValue())))))))] id=query-TextBox\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n" +
                        "        Footer\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/query/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,textMatch(%22*formattedValue*%22,cellFormattedValue())))))))] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/query/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,textMatch(%22*formattedValue*%22,cellFormattedValue())))))))] id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" DISABLED id=find-load-highlighting-query-Link\n" +
                        "              \"Save as Highlighting Query\" [#/123/SpreadsheetName456/metadata/find-query/save/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,textMatch(%22*formattedValue*%22,cellFormattedValue())))))))] id=find-save-as-highlighting-query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n"
        );
    }

    private AppContext appContext(final Optional<SpreadsheetCellQuery> highlightingQuery,
                                  final HistoryToken historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
                return this.deltaFetcher;
            }

            private final SpreadsheetDeltaFetcher deltaFetcher = SpreadsheetDeltaFetcher.with(
                    SpreadsheetDeltaFetcherWatchers.empty(),
                    this
            );

            @Override
            public void giveFocus(final Runnable focus) {
                // ignore
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

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

            @Override
            public void debug(final Object... values) {
                System.out.println(Arrays.toString(values));
            }
        };
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
