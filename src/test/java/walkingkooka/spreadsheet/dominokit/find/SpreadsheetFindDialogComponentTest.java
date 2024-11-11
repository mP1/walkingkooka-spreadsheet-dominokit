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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import java.util.Arrays;

public final class SpreadsheetFindDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetFindDialogComponent>,
        SpreadsheetMetadataTesting {

    @Test
    public void testRefreshNoMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
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
                        "    id=find includeClose=true CLOSED\n" +
                        "      SpreadsheetFindDialogComponentGridLayout\n" +
                        "        Left\n" +
                        "          SpreadsheetCellRangeReferenceComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Cell Range [] id=find--cell-range\n" +
                        "                Errors\n" +
                        "                  Empty \"text\"\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [] id=find--cell-range-path-Select\n" +
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
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [] id=query-TextBox\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" id=find-find-Link\n" +
                        "              \"Reset\" id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" id=find-load highlighting query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRefreshNoMatchesAndMetadataContainsHighlightingQuery() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
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
                        "                Cell Range [A1] id=find--cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find--cell-range-path-Select\n" +
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
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/max/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRefreshShowingMatches() {
        final HistoryToken historyToken = HistoryToken.parseString(
                "/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/" + SpreadsheetValueType.DATE + "/query/matchXyz()"
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
                        "                Cell Range [A1] id=find--cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find--cell-range-path-Select\n" +
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
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/max/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Find\" id=find-find-Link\n" +
                        "              \"Reset\" id=find-reset-Link\n" +
                        "              \"Load Highlighting Query\" id=find-load highlighting query-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n" +
                        "        Content\n" +
                        "          SpreadsheetDeltaMatchedCellsTableComponent\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              COLUMN(S)\n" +
                        "                Cell\n" +
                        "                Formula\n" +
                        "                Formatted\n" +
                        "                Value\n" +
                        "              PLUGINS\n" +
                        "                BodyScrollPlugin\n"
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
                        "                Cell Range [A1] id=find--cell-range\n" +
                        "          SpreadsheetCellRangeReferencePathComponent\n" +
                        "            SpreadsheetSelectComponent\n" +
                        "              Cell Range Path [BULR] id=find--cell-range-path-Select\n" +
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
                        "          SpreadsheetFormulaComponent\n" +
                        "            ValueSpreadsheetTextBox\n" +
                        "              SpreadsheetTextBox\n" +
                        "                Query [matchXyz()] id=query-TextBox\n" +
                        "          SpreadsheetFlexLayout\n" +
                        "            ROW\n" +
                        "              \"Find\" [#/123/SpreadsheetName456/cell/A1/find/path/BULR/offset/1234/max/5678/value-type/date/query/matchXyz()] id=find-find-Link\n" +
                        "              \"Reset\" [#/123/SpreadsheetName456/cell/A1/find/offset/1234/max/5678/query/matchXyz()] id=find-reset-Link\n" +
                        "              \"Close\" [#/123/SpreadsheetName456/cell/A1] id=find-close-Link\n" +
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
                        "                BodyScrollPlugin\n"
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
                );
            }

            @Override
            public void debug(final Object... values) {
                System.out.println(Arrays.toString(values));
            }
        };
    }

    private SpreadsheetFindDialogComponent dialog(final AppContext context) {
        return SpreadsheetFindDialogComponent.with(
                new FakeSpreadsheetFindDialogComponentContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return context.addHistoryTokenWatcher(watcher);
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return context.historyToken();
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
