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

package walkingkooka.spreadsheet.dominokit.meta;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Locale;

public final class SpreadsheetMetadataPanelComponentTest implements TreePrintableTesting,
    SpreadsheetMetadataTesting,
    ClassTesting<SpreadsheetMetadataPanelComponent> {

    @Test
    public void testRender() {
        final SpreadsheetMetadataPanelComponentContext context = new FakeSpreadsheetMetadataPanelComponentContext() {

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return () -> {};
            }

            @Override
            public Locale locale() {
                return METADATA_EN_AU.locale();
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA_EN_AU.setDefaults(
                    SpreadsheetMetadata.NON_LOCALE_DEFAULTS
                );
            }

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.metadataPropertySelect(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName111"),
                    SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET
                );
            }
        };
        final SpreadsheetMetadataPanelComponent component = SpreadsheetMetadataPanelComponent.with(context);
        component.refresh(context);

        this.treePrintAndCheck(
            component,
            "SpreadsheetMetadataPanelComponent\n" +
                "  SpreadsheetMetadataPanelComponentItemReadOnlyText\n" +
                "    DIV\n" +
                "      style=\"padding-bottom: 5px; padding-top: 5px;\"\n" +
                "  SpreadsheetMetadataPanelComponentItemSpreadsheetName\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            SpreadsheetNameComponent\n" +
                "              ValueTextBoxComponent\n" +
                "                TextBoxComponent\n" +
                "                  [] id=metadata-spreadsheetName-TextBox\n" +
                "                  Errors\n" +
                "                    Empty \"name\"\n" +
                "  SpreadsheetMetadataPanelComponentItemReadOnlyText\n" +
                "    DIV\n" +
                "      style=\"padding-bottom: 5px; padding-top: 5px;\"\n" +
                "        \"user@example.com\"\n" +
                "  SpreadsheetMetadataPanelComponentItemReadOnlyText\n" +
                "    DIV\n" +
                "      style=\"padding-bottom: 5px; padding-top: 5px;\"\n" +
                "        \"31/12/99, 12:58 pm\"\n" +
                "  SpreadsheetMetadataPanelComponentItemReadOnlyText\n" +
                "    DIV\n" +
                "      style=\"padding-bottom: 5px; padding-top: 5px;\"\n" +
                "        \"user@example.com\"\n" +
                "  SpreadsheetMetadataPanelComponentItemReadOnlyText\n" +
                "    DIV\n" +
                "      style=\"padding-bottom: 5px; padding-top: 5px;\"\n" +
                "        \"31/12/99, 12:58 pm\"\n" +
                "  SpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"locale\" [#/1/SpreadsheetName111/spreadsheet/locale] id=locale-LinkSpreadsheetMetadataPanelComponentItemEnum\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Big Decimal\" DISABLED id=metadata-expressionNumberKind-big-decimal-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Double\" [#/1/SpreadsheetName111/spreadsheet/expressionNumberKind/save/DOUBLE] id=metadata-expressionNumberKind-double-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/expressionNumberKind/save/] id=metadata-expressionNumberKind-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"Big Decimal\"\n" +
                "  SpreadsheetMetadataPanelComponentItemPrecision\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            IntegerBoxComponent\n" +
                "              [7]\n" +
                "               min: 0 max: 128 step: 1\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Unlimited\" [#/1/SpreadsheetName111/spreadsheet/precision/save/0] id=metadata-precision-0-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"32\" [#/1/SpreadsheetName111/spreadsheet/precision/save/32] id=metadata-precision-32-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"64\" [#/1/SpreadsheetName111/spreadsheet/precision/save/64] id=metadata-precision-64-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"128\" [#/1/SpreadsheetName111/spreadsheet/precision/save/128] id=metadata-precision-128-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/precision/save/] id=metadata-precision-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"10\"\n" +
                "  SpreadsheetMetadataPanelComponentItemEnum\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Up\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/UP] id=metadata-roundingMode-up-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Down\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/DOWN] id=metadata-roundingMode-down-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Ceiling\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/CEILING] id=metadata-roundingMode-ceiling-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Floor\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/FLOOR] id=metadata-roundingMode-floor-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Half Up\" DISABLED id=metadata-roundingMode-half-up-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Half Down\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/HALF_DOWN] id=metadata-roundingMode-half-down-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Half Even\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/HALF_EVEN] id=metadata-roundingMode-half-even-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"Unnecessary\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/UNNECESSARY] id=metadata-roundingMode-unnecessary-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/roundingMode/save/] id=metadata-roundingMode-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"Half Up\"\n" +
                "  SpreadsheetMetadataPanelComponentItemReadOnlyText\n" +
                "    DIV\n" +
                "      style=\"padding-bottom: 5px; padding-top: 5px;\"\n" +
                "        \",\"\n" +
                "  SpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"currency\" [#/1/SpreadsheetName111/spreadsheet/currency] id=currency-LinkSpreadsheetMetadataPanelComponentItemDateTimeOffset\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            DateComponent\n" +
                "              [1899-12-30] id=metadata-dateTimeOffset-Date\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"1900\" [#/1/SpreadsheetName111/spreadsheet/dateTimeOffset/save/0] id=metadata-dateTimeOffset-0-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"1904\" [#/1/SpreadsheetName111/spreadsheet/dateTimeOffset/save/1462] id=metadata-dateTimeOffset-1462-Link\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/dateTimeOffset/save/] id=metadata-dateTimeOffset-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"1904\"\n" +
                "  SpreadsheetMetadataPanelComponentItemNumber\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            IntegerBoxComponent\n" +
                "              [2000]\n" +
                "               min: 1800 max: 2100 step: 1 pattern: \"#\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/defaultYear/save/] id=metadata-defaultYear-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"1900\"\n" +
                "  SpreadsheetMetadataPanelComponentItemNumber\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            IntegerBoxComponent\n" +
                "              [50]\n" +
                "               min: 0 max: 99 step: 1 pattern: \"#\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/twoDigitYear/save/] id=metadata-twoDigitYear-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"20\"\n" +
                "  SpreadsheetMetadataPanelComponentItemDecimalNumberDigitCount\n" +
                "    UL\n" +
                "      style=\"align-items: center; display: flex; flex-wrap: wrap; justify-content: flex-start; list-style-type: none; margin-bottom: 0px; margin-left: 0px; margin-right: 0px; margin-top: 0px; padding-left: 0;\"\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            IntegerBoxComponent\n" +
                "              [8]\n" +
                "               min: 0 step: 1\n" +
                "        LI\n" +
                "          style=\"display: flex; padding-bottom: 0px; padding-left: 0px; padding-right: 0px; padding-top: 0px;\"\n" +
                "            \"default\" [#/1/SpreadsheetName111/spreadsheet/decimalNumberDigitCount/save/] id=metadata-decimalNumberDigitCount-default-Link\n" +
                "              TooltipComponent\n" +
                "                \"9\"\n" +
                "  SpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"dateTimeSymbols\" [#/1/SpreadsheetName111/spreadsheet/dateTimeSymbols] id=datetimesymbols-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"dateFormatter\" [#/1/SpreadsheetName111/spreadsheet/dateFormatter] id=dateformatter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"dateParser\" [#/1/SpreadsheetName111/spreadsheet/dateParser] id=dateparser-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"dateTimeFormatter\" [#/1/SpreadsheetName111/spreadsheet/dateTimeFormatter] id=datetimeformatter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"dateTimeParser\" [#/1/SpreadsheetName111/spreadsheet/dateTimeParser] id=datetimeparser-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"decimalNumberSymbols\" [#/1/SpreadsheetName111/spreadsheet/decimalNumberSymbols] id=decimalnumbersymbols-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"numberFormatter\" [#/1/SpreadsheetName111/spreadsheet/numberFormatter] id=numberformatter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"numberParser\" [#/1/SpreadsheetName111/spreadsheet/numberParser] id=numberparser-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"textFormatter\" [#/1/SpreadsheetName111/spreadsheet/textFormatter] id=textformatter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"timeFormatter\" [#/1/SpreadsheetName111/spreadsheet/timeFormatter] id=timeformatter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"timeParser\" [#/1/SpreadsheetName111/spreadsheet/timeParser] id=timeparser-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"plugins\" [#/1/SpreadsheetName111/spreadsheet/plugins] id=plugins-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"comparators\" [#/1/SpreadsheetName111/spreadsheet/comparators] id=comparators-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"converters\" [#/1/SpreadsheetName111/spreadsheet/converters] id=converters-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"exporters\" [#/1/SpreadsheetName111/spreadsheet/exporters] id=exporters-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"functions\" [#/1/SpreadsheetName111/spreadsheet/functions] id=functions-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"formatters\" [#/1/SpreadsheetName111/spreadsheet/formatters] id=formatters-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"formHandlers\" [#/1/SpreadsheetName111/spreadsheet/formHandlers] id=formhandlers-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"importers\" [#/1/SpreadsheetName111/spreadsheet/importers] id=importers-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"parsers\" [#/1/SpreadsheetName111/spreadsheet/parsers] id=parsers-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"validators\" [#/1/SpreadsheetName111/spreadsheet/validators] id=validators-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"findConverter\" [#/1/SpreadsheetName111/spreadsheet/findConverter] id=findconverter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"findFunctions\" [#/1/SpreadsheetName111/spreadsheet/findFunctions] id=findfunctions-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"formulaConverter\" [#/1/SpreadsheetName111/spreadsheet/formulaConverter] id=formulaconverter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"formulaFunctions\" [#/1/SpreadsheetName111/spreadsheet/formulaFunctions] id=formulafunctions-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"formattingConverter\" [#/1/SpreadsheetName111/spreadsheet/formattingConverter] id=formattingconverter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"formattingFunctions\" [#/1/SpreadsheetName111/spreadsheet/formattingFunctions] id=formattingfunctions-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"defaultFormHandler\" [#/1/SpreadsheetName111/spreadsheet/defaultFormHandler] id=defaultformhandler-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"scriptingFunctions\" [#/1/SpreadsheetName111/spreadsheet/scriptingFunctions] id=scriptingfunctions-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"scriptingConverter\" [#/1/SpreadsheetName111/spreadsheet/scriptingConverter] id=scriptingconverter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"sortComparators\" [#/1/SpreadsheetName111/spreadsheet/sortComparators] id=sortcomparators-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"sortConverter\" [#/1/SpreadsheetName111/spreadsheet/sortConverter] id=sortconverter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"validationConverter\" [#/1/SpreadsheetName111/spreadsheet/validationConverter] id=validationconverter-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"validationFunctions\" [#/1/SpreadsheetName111/spreadsheet/validationFunctions] id=validationfunctions-LinkSpreadsheetMetadataPanelComponentItemAnchor\n" +
                "    \"validationValidators\" [#/1/SpreadsheetName111/spreadsheet/validationValidators] id=validationvalidators-Link"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetMetadataPanelComponent> type() {
        return SpreadsheetMetadataPanelComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
