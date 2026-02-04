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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import org.junit.jupiter.api.Test;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.DateTimeSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class DateTimeSymbolsDialogComponentTest implements DialogComponentLifecycleTesting<DateTimeSymbolsDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    // onHistoryToken...................................................................................................

    @Test
    public void testOnHistoryTokenWithSpreadsheetCellDateTimeSymbolsSelectHistoryToken() {
        final AppContext context = this.appContext(
            HistoryToken.cellDateTimeSymbolsSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            DATE_TIME_SYMBOLS
        );

        this.onHistoryTokenChangeAndCheck2(
            DateTimeSymbolsDialogComponent.with(
                DateTimeSymbolsDialogComponentContexts.cell(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "DateTimeSymbolsDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Date Time Symbols\n" +
                "    id=DateTimeSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                AM/PM [] id=DateTimeSymbols-ampms-TextBox\n" +
                "                Errors\n" +
                "                  Require 2\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Month names [] id=DateTimeSymbols-monthNames-TextBox\n" +
                "                Errors\n" +
                "                  Require 12 or more\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Month name abbreviations [] id=DateTimeSymbols-monthNameAbbreviations-TextBox\n" +
                "                Errors\n" +
                "                  Require 12 or more\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Week day names [] id=DateTimeSymbols-weekDayNames-TextBox\n" +
                "                Errors\n" +
                "                  Require 7\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Week day names Abbreviations [] id=DateTimeSymbols-weekDayNameAbbreviations-TextBox\n" +
                "                Errors\n" +
                "                  Require 7\n" +
                "          DateTimeSymbolsComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Date Time Symbols []\n" +
                "                Errors\n" +
                "                  Expected 5 tokens but got 0\n" +
                "      LocaleComponent\n" +
                "        SuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=DateTimeSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/cell/A1/dateTimeSymbols/save/] id=DateTimeSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/cell/A1/dateTimeSymbols/save/] id=DateTimeSymbols-undo-Link\n" +
                "            \"Copy Defaults\" [#/1/SpreadsheetName1/cell/A1/dateTimeSymbols/save/%22am,pm%22,%22January,February,March,April,May,June,July,August,September,October,November,December%22,%22Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.%22,%22Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday%22,%22Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.%22] id=DateTimeSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/cell/A1] id=DateTimeSymbols-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetMetadataPropertySelectHistoryTokenDateTimeSymbols() {
        final AppContext context = this.appContext(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS
            ),
            DATE_TIME_SYMBOLS
        );

        this.onHistoryTokenChangeAndCheck2(
            DateTimeSymbolsDialogComponent.with(
                DateTimeSymbolsDialogComponentContexts.metadata(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "DateTimeSymbolsDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Date Time Symbols (dateTimeSymbols)\n" +
                "    id=DateTimeSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                AM/PM [am,pm] id=DateTimeSymbols-ampms-TextBox\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Month names [January,February,March,April,May,June,July,August,September,October,November,December] id=DateTimeSymbols-monthNames-TextBox\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Month name abbreviations [Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.] id=DateTimeSymbols-monthNameAbbreviations-TextBox\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Week day names [Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday] id=DateTimeSymbols-weekDayNames-TextBox\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Week day names Abbreviations [Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.] id=DateTimeSymbols-weekDayNameAbbreviations-TextBox\n" +
                "          DateTimeSymbolsComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Date Time Symbols [\"am,pm\",\"January,February,March,April,May,June,July,August,September,October,November,December\",\"Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.\",\"Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday\",\"Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.\"]\n" +
                "      LocaleComponent\n" +
                "        SuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/SpreadsheetName1/spreadsheet/dateTimeSymbols/save/%22am,pm%22,%22January,February,March,April,May,June,July,August,September,October,November,December%22,%22Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.%22,%22Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday%22,%22Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.%22] id=DateTimeSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/dateTimeSymbols/save/] id=DateTimeSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/spreadsheet/dateTimeSymbols/save/%22am,pm%22,%22January,February,March,April,May,June,July,August,September,October,November,December%22,%22Jan.,Feb.,Mar.,Apr.,May,Jun.,Jul.,Aug.,Sep.,Oct.,Nov.,Dec.%22,%22Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday%22,%22Sun.,Mon.,Tue.,Wed.,Thu.,Fri.,Sat.%22] id=DateTimeSymbols-undo-Link\n" +
                "            \"Copy Defaults\" DISABLED id=DateTimeSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/spreadsheet] id=DateTimeSymbols-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenWithSpreadsheetMetadataPropertySelectHistoryTokenMissingDateTimeSymbols() {
        final AppContext context = this.appContext(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS
            ),
            null // missing DateTimeSymbols
        );

        this.onHistoryTokenChangeAndCheck2(
            DateTimeSymbolsDialogComponent.with(
                DateTimeSymbolsDialogComponentContexts.metadata(context)
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            context,
            "DateTimeSymbolsDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Date Time Symbols (dateTimeSymbols)\n" +
                "    id=DateTimeSymbols-Dialog includeClose=true\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                AM/PM [] id=DateTimeSymbols-ampms-TextBox\n" +
                "                Errors\n" +
                "                  Require 2\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Month names [] id=DateTimeSymbols-monthNames-TextBox\n" +
                "                Errors\n" +
                "                  Require 12 or more\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Month name abbreviations [] id=DateTimeSymbols-monthNameAbbreviations-TextBox\n" +
                "                Errors\n" +
                "                  Require 12 or more\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Week day names [] id=DateTimeSymbols-weekDayNames-TextBox\n" +
                "                Errors\n" +
                "                  Require 7\n" +
                "          CsvStringListComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Week day names Abbreviations [] id=DateTimeSymbols-weekDayNameAbbreviations-TextBox\n" +
                "                Errors\n" +
                "                  Require 7\n" +
                "          DateTimeSymbolsComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Date Time Symbols []\n" +
                "                Errors\n" +
                "                  Expected 5 tokens but got 0\n" +
                "      LocaleComponent\n" +
                "        SuggestBoxComponent\n" +
                "          Load from Locale []\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=DateTimeSymbols-save-Link\n" +
                "            \"Clear\" [#/1/SpreadsheetName1/spreadsheet/dateTimeSymbols/save/] id=DateTimeSymbols-clear-Link\n" +
                "            \"Undo\" [#/1/SpreadsheetName1/spreadsheet/dateTimeSymbols/save/] id=DateTimeSymbols-undo-Link\n" +
                "            \"Copy Defaults\" DISABLED id=DateTimeSymbols-copyDefaults-Link\n" +
                "            \"Close\" [#/1/SpreadsheetName1/spreadsheet] id=DateTimeSymbols-close-Link\n"
        );
    }

    @Override
    public DateTimeSymbolsDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return DateTimeSymbolsDialogComponent.with(
            DateTimeSymbolsDialogComponentContexts.metadata(
                this.appContext(
                    historyToken,
                    null
                )
            )
        );
    }

    private AppContext appContext(final HistoryToken historyToken,
                                  final DateTimeSymbols dateTimeSymbols) {
        return new FakeAppContext() {

            @Override
            public Runnable addDateTimeSymbolsFetcherWatcher(final DateTimeSymbolsFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // ignore
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
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                    DateTimeSymbolsDialogComponentTest.SPREADSHEET_ID
                ).setOrRemove(
                    SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS,
                    dateTimeSymbols
                );
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<DateTimeSymbolsDialogComponent> type() {
        return DateTimeSymbolsDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
