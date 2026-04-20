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

package walkingkooka.spreadsheet.dominokit.query;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellQueryHistoryToken;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQueryRequest;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.validation.ValueType;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetQueryDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetQueryDialogComponent>,
    SpreadsheetMetadataTesting {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellFindHistoryToken() {
        final SpreadsheetCellQueryHistoryToken historyToken = HistoryToken.cellQuery(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor(),
            SpreadsheetCellQueryRequest.empty()
        );

        this.isMatchAndCheck(
            SpreadsheetQueryDialogComponent.with(
                new TestSpreadsheetQueryDialogComponentContext(
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
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [BULR] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [date] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/offset/1234/count/5678/query/matchXyz()] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/matchXyz()] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeNoMatchesAndMetadataContainsHighlightingQuery() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final AppContext appContext = this.appContext(
            Optional.of(
                SpreadsheetCellQuery.parse("highlightQuery()")
            ), // WITH highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [BULR] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [date] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/offset/1234/count/5678/query/matchXyz()] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" [#/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/date/query/highlightQuery()] id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/matchXyz()] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithNoQuery() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/"
        );

        final AppContext appContext = this.appContext(
            Optional.of(
                SpreadsheetCellQuery.parse("highlightQuery()")
            ), // WITH highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" [#/123/SpreadsheetName456/cell/A1/query/query/highlightQuery()] id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" DISABLED id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeShowingMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
        );

        final TestAppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [BULR] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [date] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/offset/1234/count/5678/query/matchXyz()] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/matchXyz()] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );

        // required otherwise SpreadsheetViewportCache will "ignore delta because they belong to a different spreadsheet
        appContext.metadataFetcherWatchers.onSpreadsheetMetadata(
            appContext.spreadsheetMetadata()
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
            Url.parseRelative("/api/spreadsheet/123/cell/B1/query?cell-range-path=lrtd&query=%3Dtrue%28%29"),
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1")
                            .setValueType(
                                Optional.of(ValueType.TEXT)
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
            ) // delta
        );

        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [BULR] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [date] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [matchXyz()] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/offset/1234/count/5678/query/matchXyz()] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/matchXyz()] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  \"A1\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-cells-A1-Link\n" +
                "                  \"A1\" [#/123/SpreadsheetName456/cell/A1/formula] id=SpreadsheetCellQuery-cells-A1-formula-Link\n" +
                "                  TextComponent\n" +
                "                    \"\"\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetCellLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"Value\" [#/123/SpreadsheetName456/cell/A1/value/text] id=SpreadsheetCellQuery-cells-A1-value-Link\n" +
                "                          \"Create Label\" [#/123/SpreadsheetName456/cell/A1/label] id=SpreadsheetCellQuery-cells-A1-createLabel-Link\n" +
                "                          \"Labels\" [#/123/SpreadsheetName456/cell/A1/labels] (3) id=SpreadsheetCellQuery-cells-A1-label-Link\n" +
                "                          \"References\" [#/123/SpreadsheetName456/cell/A1/references] (1) id=SpreadsheetCellQuery-cells-A1-references-Link\n" +
                "                          \"Delete\" [#/123/SpreadsheetName456/cell/A1/delete] id=SpreadsheetCellQuery-cells-A1-delete-Link\n" +
                "                ROW 1\n" +
                "                  \"B2\" [#/123/SpreadsheetName456/cell/B2] id=SpreadsheetCellQuery-cells-B2-Link\n" +
                "                  \"=2\" [#/123/SpreadsheetName456/cell/B2/formula] id=SpreadsheetCellQuery-cells-B2-formula-Link\n" +
                "                  TextComponent\n" +
                "                    \"\"\n" +
                "                  TextNodeComponent\n" +
                "                  SpreadsheetCellLinksComponent\n" +
                "                    AnchorListComponent\n" +
                "                      FlexLayoutComponent\n" +
                "                        ROW\n" +
                "                          \"Value\" DISABLED id=SpreadsheetCellQuery-cells-B2-value-Link\n" +
                "                          \"Create Label\" [#/123/SpreadsheetName456/cell/B2/label] id=SpreadsheetCellQuery-cells-B2-createLabel-Link\n" +
                "                          \"Labels\" [#/123/SpreadsheetName456/cell/B2/labels] (0) id=SpreadsheetCellQuery-cells-B2-label-Link\n" +
                "                          \"References\" [#/123/SpreadsheetName456/cell/B2/references] (0) id=SpreadsheetCellQuery-cells-B2-references-Link\n" +
                "                          \"Delete\" [#/123/SpreadsheetName456/cell/B2/delete] id=SpreadsheetCellQuery-cells-B2-delete-Link\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" [#/123/SpreadsheetName456/cell/A1/query/path/BULR/offset/1234/count/5678/value-type/date/query/matchXyz()] mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/textMatch(\"*formula*\",cellFormula())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [*formula*] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(\"*formula*\",cellFormula())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*formula*%22,cellFormula())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*formula*%22,cellFormula())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/textMatch(%22*formula*%22,cellFormula())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetterAndExpression() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/1+textMatch(\"*formula*\",cellFormula())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [*formula*] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [1+textMatch(\"*formula*\",cellFormula())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/1+textMatch(%22*formula*%22,cellFormula())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/1+textMatch(%22*formula*%22,cellFormula())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/1+textMatch(%22*formula*%22,cellFormula())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetterOrFormatterGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter()))"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [*formula*] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [*formatter*] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*formula*\",cellFormula()),textMatch(\"*formatter*\",cellFormatter()))] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*formula*%22,cellFormula()),textMatch(%22*formatter*%22,cellFormatter()))] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*formula*%22,cellFormula()),textMatch(%22*formatter*%22,cellFormatter()))] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(%22*formula*%22,cellFormula()),textMatch(%22*formatter*%22,cellFormatter()))] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaGetterOrTrue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(\"*formula*\",cellFormula()),true())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [*formula*] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*formula*\",cellFormula()),true())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*formula*%22,cellFormula()),true())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*formula*%22,cellFormula()),true())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(%22*formula*%22,cellFormula()),true())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithCurrency() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(\"*AUD*\",cellCurrency()),1)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [*AUD*] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*AUD*\",cellCurrency()),1)] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*AUD*%22,cellCurrency()),1)] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*AUD*%22,cellCurrency()),1)] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(%22*AUD*%22,cellCurrency()),1)] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithDateTimeSymbols() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(\"*Hello*\",cellDateTimeSymbols()),1)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [*Hello*] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*Hello*\",cellDateTimeSymbols()),1)] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*Hello*%22,cellDateTimeSymbols()),1)] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*Hello*%22,cellDateTimeSymbols()),1)] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(%22*Hello*%22,cellDateTimeSymbols()),1)] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormulaAndValue() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(\"*formula*\",cellFormula()),cellValue()<10)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [*formula*] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [<10] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*formula*\",cellFormula()),cellValue()<10)] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*formula*%22,cellFormula()),cellValue()%3C10)] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*formula*%22,cellFormula()),cellValue()%3C10)] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(%22*formula*%22,cellFormula()),cellValue()%3C10)] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithDecimalNumberSymbols() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(cellDecimalNumberSymbols(),\"*Hello*\"),1)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [*Hello*] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*Hello*\",cellDecimalNumberSymbols()),1)] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(cellDecimalNumberSymbols(),%22*Hello*%22),1)] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(cellDecimalNumberSymbols(),%22*Hello*%22),1)] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(cellDecimalNumberSymbols(),%22*Hello*%22),1)] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormatterGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/textMatch(\"*formatter*\",cellFormatter())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [*formatter*] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(\"*formatter*\",cellFormatter())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*formatter*%22,cellFormatter())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*formatter*%22,cellFormatter())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/textMatch(%22*formatter*%22,cellFormatter())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithLocale() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(\"*en-AU*\",cellLocale()),1)"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [*en-AU*] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(textMatch(\"*en-AU*\",cellLocale()),1)] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*en-AU*%22,cellLocale()),1)] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(textMatch(%22*en-AU*%22,cellLocale()),1)] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(textMatch(%22*en-AU*%22,cellLocale()),1)] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithParserGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/textMatch(\"*parser*\",cellParser())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [*parser*] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(\"*parser*\",cellParser())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*parser*%22,cellParser())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*parser*%22,cellParser())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/textMatch(%22*parser*%22,cellParser())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithStyleGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/textMatch(\"*style*\",cellStyle())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [*style*] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(\"*style*\",cellStyle())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*style*%22,cellStyle())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*style*%22,cellStyle())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/textMatch(%22*style*%22,cellStyle())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithValueGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/cellValue()<999"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [<999] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [cellValue()<999] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/cellValue()%3C999] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/cellValue()%3C999] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/cellValue()%3C999] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithValidatorGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/textMatch(cellValidator(),\"*validator123*\")"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [*validator123*] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(\"*validator123*\",cellValidator())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(cellValidator(),%22*validator123*%22)] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(cellValidator(),%22*validator123*%22)] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/textMatch(cellValidator(),%22*validator123*%22)] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithFormattedValueGetter() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/textMatch(\"*formatted-value*\",cellFormattedValue())"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [*formatted-value*] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [textMatch(\"*formatted-value*\",cellFormattedValue())] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*formatted-value*%22,cellFormattedValue())] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/textMatch(%22*formatted-value*%22,cellFormattedValue())] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/textMatch(%22*formatted-value*%22,cellFormattedValue())] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
                "              PLUGINS\n" +
                "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeWithQueryWithAllWizardFields() {
        final HistoryToken historyToken = HistoryToken.parseString(
            "/123/SpreadsheetName456/cell/A1/query/query/OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),OR(textMatch(\"*formatted*\",cellFormatter()),OR(textMatch(\"*locale*\",cellLocale()),OR(textMatch(\"*parser*\",cellParser()),OR(textMatch(\"*style*\",cellStyle()),OR(cellValue()<10, OR(textMatch(\"*validator*\",cellValidator()),textMatch(\"*formattedValue*\",cellFormattedValue())))))))))"
        );

        final AppContext appContext = this.appContext(
            Optional.empty(), // no highlighting query
            historyToken
        );

        final SpreadsheetQueryDialogComponent dialog = SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(appContext)
        );
        this.onHistoryTokenChangeAndCheck(
            dialog,
            appContext,
            "SpreadsheetQueryDialogComponent\n" +
                "  DialogComponent\n" +
                "    Query\n" +
                "    id=SpreadsheetCellQuery-Dialog includeClose=true\n" +
                "      FourColumnComponent\n" +
                "        DIV\n" +
                "          style=\"display: grid; gap: 5px; grid-template-columns: calc(25% - 5px) calc(25% - 5px) calc(25% - 5px) calc(25% - 5px);\"\n" +
                "            SpreadsheetCellRangeReferenceComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Cell Range [A1] icons=mdi-close-circle id=SpreadsheetCellQuery-cellRange-TextBox REQUIRED\n" +
                "            SpreadsheetCellRangeReferencePathComponent\n" +
                "              SelectComponent\n" +
                "                Cell Range Path [] id=SpreadsheetCellQuery-cellRangePath-\n" +
                "                  \"Left-Right Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRTD-Option\n" +
                "                  \"Right-Left Top-Down\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLTD-Option\n" +
                "                  \"Left-Right Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-LRBU-Option\n" +
                "                  \"Right-Left Bottom-Up\" DISABLED id=SpreadsheetCellQuery-cellRangePath-RLBU-Option\n" +
                "                  \"Top-Down Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDLR-Option\n" +
                "                  \"Top-Down Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-TDRL-Option\n" +
                "                  \"Bottom-Up Left-Right\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BULR-Option\n" +
                "                  \"Bottom-Up Right-Left\" DISABLED id=SpreadsheetCellQuery-cellRangePath-BURL-Option\n" +
                "            ValueTypeEditComponent\n" +
                "              SelectComponent\n" +
                "                Value type [] id=SpreadsheetCellQuery-valueType-\n" +
                "                  \"Any\" DISABLED id=SpreadsheetCellQuery-valueType-*-Option\n" +
                "                  \"Boolean\" DISABLED id=SpreadsheetCellQuery-valueType-boolean-Option\n" +
                "                  \"Date\" DISABLED id=SpreadsheetCellQuery-valueType-date-Option\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetCellQuery-valueType-date-time-Option\n" +
                "                  \"Email\" DISABLED id=SpreadsheetCellQuery-valueType-email-Option\n" +
                "                  \"Error(spreadsheet)\" DISABLED id=SpreadsheetCellQuery-valueType-error(spreadsheet)-Option\n" +
                "                  \"Number\" DISABLED id=SpreadsheetCellQuery-valueType-number-Option\n" +
                "                  \"Text\" DISABLED id=SpreadsheetCellQuery-valueType-text-Option\n" +
                "                  \"Time\" DISABLED id=SpreadsheetCellQuery-valueType-time-Option\n" +
                "                  \"Url\" DISABLED id=SpreadsheetCellQuery-valueType-url-Option\n" +
                "                  \"Whole Number\" DISABLED id=SpreadsheetCellQuery-valueType-whole-number-Option\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formula [*formula*] icons=mdi-close-circle id=SpreadsheetCellQuery-formula-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Currency [] icons=mdi-close-circle id=SpreadsheetCellQuery-currency-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Date Time Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-dateTimeSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Decimal Number Symbols [] icons=mdi-close-circle id=SpreadsheetCellQuery-decimalNumberSymbols-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatter [*formatted*] icons=mdi-close-circle id=SpreadsheetCellQuery-formatter-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Locale [*locale*] icons=mdi-close-circle id=SpreadsheetCellQuery-locale-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Parser [*parser*] icons=mdi-close-circle id=SpreadsheetCellQuery-parser-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Style [*style*] icons=mdi-close-circle id=SpreadsheetCellQuery-style-TextBox\n" +
                "            ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Value [<10] icons=mdi-close-circle id=SpreadsheetCellQuery-value-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Validator [*validator*] icons=mdi-close-circle id=SpreadsheetCellQuery-validator-TextBox\n" +
                "            TextMatchComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  Formatted [*formattedValue*] icons=mdi-close-circle id=SpreadsheetCellQuery-formatted-TextBox\n" +
                "      SpreadsheetFormulaComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            Query [OR(OR(OR(OR(OR(OR(OR(oldQuery(),OR(textMatch(\"*formula*\",cellFormula()),cellValue()<10)),textMatch(\"*formatted*\",cellFormatter())),textMatch(\"*locale*\",cellLocale())),textMatch(\"*parser*\",cellParser())),textMatch(\"*style*\",cellStyle())),textMatch(\"*validator*\",cellValidator())),textMatch(\"*formattedValue*\",cellFormattedValue()))] icons=mdi-close-circle id=SpreadsheetCellQuery-query-TextBox REQUIRED\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Execute\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*locale*%22,cellLocale()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,%20OR(textMatch(%22*validator*%22,cellValidator()),textMatch(%22*formattedValue*%22,cellFormattedValue())))))))))] id=SpreadsheetCellQuery-execute-Link\n" +
                "            \"Reset\" [#/123/SpreadsheetName456/cell/A1/query/query/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*locale*%22,cellLocale()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,%20OR(textMatch(%22*validator*%22,cellValidator()),textMatch(%22*formattedValue*%22,cellFormattedValue())))))))))] id=SpreadsheetCellQuery-reset-Link\n" +
                "            \"Load Highlighting Query\" DISABLED id=SpreadsheetCellQuery-load-highlighting-query-Link\n" +
                "            \"Save as Highlighting Query\" [#/123/SpreadsheetName456/spreadsheet/query/save/OR(oldQuery(),OR(textMatch(%22*formula*%22,cellFormula()),OR(textMatch(%22*formatted*%22,cellFormatter()),OR(textMatch(%22*locale*%22,cellLocale()),OR(textMatch(%22*parser*%22,cellParser()),OR(textMatch(%22*style*%22,cellStyle()),OR(cellValue()%3C10,%20OR(textMatch(%22*validator*%22,cellValidator()),textMatch(%22*formattedValue*%22,cellFormattedValue())))))))))] id=SpreadsheetCellQuery-save-as-highlighting-query-Link\n" +
                "            \"Close\" [#/123/SpreadsheetName456/cell/A1] id=SpreadsheetCellQuery-close-Link\n" +
                "      SpreadsheetDeltaCellsTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetCellQuery-cells-Table\n" +
                "              COLUMN(S)\n" +
                "                Cell\n" +
                "                Formula\n" +
                "                Value\n" +
                "                Formatted\n" +
                "                Links\n" +
                "              CHILDREN\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    mdi-arrow-left \"previous\" DISABLED id=SpreadsheetCellQuery-previous-Link\n" +
                "                    \"next\" DISABLED mdi-arrow-right id=SpreadsheetCellQuery-next-Link\n" +
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
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public void pushHistoryToken(final HistoryToken token) {
            final HistoryToken previous = this.historyToken;
            this.historyToken = token;
            this.historyTokenWatchers.onHistoryTokenChange(
                previous,
                this
            );
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private HistoryToken historyToken;

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
                SpreadsheetMetadataPropertyName.QUERY,
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

    static class TestSpreadsheetQueryDialogComponentContext extends FakeSpreadsheetQueryDialogComponentContext {

        TestSpreadsheetQueryDialogComponentContext(final AppContext context) {
            this.context = context;
        }

        @Override
        public String dialogTitle() {
            return "Query";
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
        public void pushHistoryToken(final HistoryToken token) {
            this.context.pushHistoryToken(token);
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
                              final SpreadsheetCellQueryRequest find) {
            // nop
        }
    }

    @Override
    public SpreadsheetQueryDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetQueryDialogComponent.with(
            new TestSpreadsheetQueryDialogComponentContext(
                new TestAppContext(
                    Optional.empty(),
                    historyToken
                )
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetQueryDialogComponent> type() {
        return SpreadsheetQueryDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
