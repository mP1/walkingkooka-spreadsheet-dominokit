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

package walkingkooka.spreadsheet.dominokit.viewport.menu;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowHistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.validation.provider.ValidatorSelector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// too avoid javac String literal too long
// replace
// TR\n",
// with
// TR\n",
public final class SpreadsheetSelectionMenuTest implements PublicStaticHelperTesting<SpreadsheetSelectionMenu>,
    TreePrintableTesting,
    SpreadsheetMetadataTesting {

    private static final SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private static final SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName-1");

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    private final static Optional<SpreadsheetCell> NO_SUMMARY = Optional.empty();

    private static final SpreadsheetParserSelector PARSER1 = SpreadsheetParserSelector.parse("hello-parser-1");

    private static final ValidatorSelector VALIDATOR1 = ValidatorSelector.parse("hello-validator-1");

    @Test
    public void testCell() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Cell-MenuId",
                "Cell A1 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        this.treePrintAndCheck2(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/A1/cut/currency] id=test-clipboard-cut-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/A1/copy/currency] id=test-clipboard-copy-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/A1/paste/currency] id=test-clipboard-paste-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "  -----\n" +
                "  \"Currency\" id=test-Currency-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/currency/save/] id=test-Currency-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/currency] id=test-Currency-edit-MenuItem\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-Formatter-edit-MenuItem\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-Locale-edit-MenuItem\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Hello Parser 1\" [/1/SpreadsheetName-1/cell/A1/parser/save/hello-parser-1] id=test-Parser-hello-parser-1-MenuItem\n" +
                "    \"Test Parser 2\" [/1/SpreadsheetName-1/cell/A1/parser/save/test-parser-2] id=test-Parser-test-parser-2-MenuItem\n" +
                "    \"Test Parser 3\" [/1/SpreadsheetName-1/cell/A1/parser/save/test-parser-3] id=test-Parser-test-parser-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parser] id=test-Parser-edit-MenuItem\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/A1/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/style] id=test-Style-edit-MenuItem\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-ValueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-ValueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-ValueTypes-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/A1/valueType/save/email] id=test-ValueTypes-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-ValueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-ValueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-ValueTypes-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/A1/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/whole-number] id=test-ValueTypes-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/valueType/save/] id=test-ValueType-clear-MenuItem\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-Value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-Value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-Value-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/A1/value/email] id=test-Value-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-Value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-Value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-Value-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/A1/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/A1/value/whole-number] id=test-Value-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/value] id=test-Value-clear-MenuItem\n" +
                "  \"Validator\" id=test-Validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-Validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-Validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-Validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-Validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-Validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu\n" +
                "    (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "    -----\n" +
                "    (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" [/1/SpreadsheetName-1/cell/A1/freeze] id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" [/1/SpreadsheetName-1/cell/A1/unfreeze] id=test-unfreeze-MenuItem\n" +
                "  -----\n" +
                "  \"Labels\" [1] id=test-label-SubMenu\n" +
                "    \"Create...\" [/1/SpreadsheetName-1/cell/A1/label] id=test-label-create-MenuItem\n" +
                "    \"List...\" [/1/SpreadsheetName-1/cell/A1/labels] id=test-labels-list-MenuItem\n" +
                "    \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                "  \"References\" [/1/SpreadsheetName-1/cell/A1/references] [1] id=test-references-MenuItem\n" +
                "  -----\n" +
                "  \"Reload\" [/1/SpreadsheetName-1/cell/A1/reload] id=test-reload-MenuItem\n"
        );
    }

    @Test
    public void testCellRange() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            SpreadsheetSelection.parseCellRange("B2:C3")
                .setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Cell-MenuId",
                "Cell B2:C3 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        // too avoid java "String too long" replace TABLE\n and TR\n PLUS with TABLE\n COMMA and TR\n COMMA
        this.treePrintAndCheck2(
            menu,
            "\"Cell B2:C3 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/currency] id=test-clipboard-cut-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/currency] id=test-clipboard-copy-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/currency] id=test-clipboard-paste-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "  -----\n" +
                "  \"Currency\" id=test-Currency-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/currency/save/] id=test-Currency-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/currency] id=test-Currency-edit-MenuItem\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/formatter] id=test-Formatter-edit-MenuItem\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/locale] id=test-Locale-edit-MenuItem\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Hello Parser 1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parser/save/hello-parser-1] id=test-Parser-hello-parser-1-MenuItem\n" +
                "    \"Test Parser 2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parser/save/test-parser-2] id=test-Parser-test-parser-2-MenuItem\n" +
                "    \"Test Parser 3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parser/save/test-parser-3] id=test-Parser-test-parser-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parser] id=test-Parser-edit-MenuItem\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/LEFT] id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/font-weight/save/bold] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style] id=test-Style-edit-MenuItem\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/boolean] id=test-ValueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/date] id=test-ValueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/date-time] id=test-ValueTypes-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/email] id=test-ValueTypes-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/number] id=test-ValueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/text] id=test-ValueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/time] id=test-ValueTypes-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/whole-number] id=test-ValueTypes-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/] id=test-ValueType-clear-MenuItem\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/boolean] id=test-Value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/date] id=test-Value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/date-time] id=test-Value-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/email] id=test-Value-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/number] id=test-Value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/text] id=test-Value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/time] id=test-Value-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/whole-number] id=test-Value-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value] id=test-Value-clear-MenuItem\n" +
                "  \"Validator\" id=test-Validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/hello-validator-1] id=test-Validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/hello-validator-2] id=test-Validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/hello-validator-3] id=test-Validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/] id=test-Validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator] id=test-Validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu\n" +
                "    (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "    -----\n" +
                "    (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Column\" id=test-column-sort-SubMenu\n" +
                "    \"Date\" id=test-column-sort-date-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date] id=test-column-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date-reversed] id=test-column-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date-time] id=test-column-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date-time-reversed] id=test-column-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-month] id=test-column-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-month-reversed] id=test-column-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-week] id=test-column-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-week-reversed] id=test-column-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-am-pm] id=test-column-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-am-pm-reversed] id=test-column-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-day] id=test-column-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-day-reversed] id=test-column-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=minute-of-hour] id=test-column-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=minute-of-hour-reversed] id=test-column-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=month-of-year] id=test-column-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=month-of-year-reversed] id=test-column-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=nano-of-second] id=test-column-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=nano-of-second-reversed] id=test-column-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=number] id=test-column-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=number-reversed] id=test-column-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=seconds-of-minute] id=test-column-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=seconds-of-minute-reversed] id=test-column-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text] id=test-column-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text-reversed] id=test-column-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text-case-insensitive] id=test-column-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text-case-insensitive-reversed] id=test-column-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=time] id=test-column-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=time-reversed] id=test-column-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=year] id=test-column-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=year-reversed] id=test-column-sort-year-reverse-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/edit/B=] id=test-column-sort-edit-MenuItem\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date] id=test-row-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date-reversed] id=test-row-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date-time] id=test-row-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date-time-reversed] id=test-row-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-month] id=test-row-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-month-reversed] id=test-row-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-week] id=test-row-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-week-reversed] id=test-row-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-am-pm] id=test-row-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-am-pm-reversed] id=test-row-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-day] id=test-row-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-day-reversed] id=test-row-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=minute-of-hour] id=test-row-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=minute-of-hour-reversed] id=test-row-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=month-of-year] id=test-row-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=month-of-year-reversed] id=test-row-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=nano-of-second] id=test-row-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=nano-of-second-reversed] id=test-row-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=number] id=test-row-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=number-reversed] id=test-row-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=seconds-of-minute] id=test-row-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=seconds-of-minute-reversed] id=test-row-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text] id=test-row-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text-reversed] id=test-row-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text-case-insensitive] id=test-row-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text-case-insensitive-reversed] id=test-row-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=time] id=test-row-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=time-reversed] id=test-row-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=year] id=test-row-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=year-reversed] id=test-row-sort-year-reverse-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/edit/2=] id=test-row-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n" +
                "  -----\n" +
                "  \"Labels\" [1] id=test-label-SubMenu\n" +
                "    \"Create...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/label] id=test-label-create-MenuItem\n" +
                "    \"List...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/labels] id=test-labels-list-MenuItem\n" +
                "    \"Label123 (B2)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                "  \"References\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/references] [0] id=test-references-MenuItem\n" +
                "  -----\n" +
                "  \"Reload\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/reload] id=test-reload-MenuItem\n"
        );
    }

    @Test
    public void testLabel() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            LABEL.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Cell-MenuId",
                "Cell A1 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        this.treePrintAndCheck2(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/Label123/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/Label123/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/Label123/cut/currency] id=test-clipboard-cut-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/Label123/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/Label123/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/Label123/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/Label123/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/Label123/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/Label123/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/Label123/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/Label123/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/Label123/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/Label123/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/Label123/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/Label123/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/Label123/copy/currency] id=test-clipboard-copy-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/Label123/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/Label123/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/Label123/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/Label123/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/Label123/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/Label123/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/Label123/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/Label123/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/Label123/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/Label123/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/Label123/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/Label123/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/Label123/paste/currency] id=test-clipboard-paste-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/Label123/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/Label123/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/Label123/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/Label123/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/Label123/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/Label123/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/Label123/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/Label123/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/Label123/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/Label123/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "  -----\n" +
                "  \"Currency\" id=test-Currency-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/currency/save/] id=test-Currency-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/currency] id=test-Currency-edit-MenuItem\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/formatter] id=test-Formatter-edit-MenuItem\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/locale] id=test-Locale-edit-MenuItem\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Hello Parser 1\" [/1/SpreadsheetName-1/cell/Label123/parser/save/hello-parser-1] id=test-Parser-hello-parser-1-MenuItem\n" +
                "    \"Test Parser 2\" [/1/SpreadsheetName-1/cell/Label123/parser/save/test-parser-2] id=test-Parser-test-parser-2-MenuItem\n" +
                "    \"Test Parser 3\" [/1/SpreadsheetName-1/cell/Label123/parser/save/test-parser-3] id=test-Parser-test-parser-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/parser] id=test-Parser-edit-MenuItem\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/LEFT] id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/Label123/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/Label123/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/Label123/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/Label123/style/font-weight/save/bold] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/Label123/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/Label123/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/Label123/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/Label123/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/Label123/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/Label123/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/Label123/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/style] id=test-Style-edit-MenuItem\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/boolean] id=test-ValueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/date] id=test-ValueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/date-time] id=test-ValueTypes-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/email] id=test-ValueTypes-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/number] id=test-ValueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/text] id=test-ValueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/time] id=test-ValueTypes-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/whole-number] id=test-ValueTypes-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/] id=test-ValueType-clear-MenuItem\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/Label123/value/boolean] id=test-Value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/Label123/value/date] id=test-Value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/Label123/value/date-time] id=test-Value-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/Label123/value/email] id=test-Value-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/Label123/value/number] id=test-Value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/Label123/value/text] id=test-Value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/Label123/value/time] id=test-Value-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/Label123/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/Label123/value/whole-number] id=test-Value-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/value] id=test-Value-clear-MenuItem\n" +
                "  \"Validator\" id=test-Validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/Label123/validator/save/hello-validator-1] id=test-Validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/Label123/validator/save/hello-validator-2] id=test-Validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/Label123/validator/save/hello-validator-3] id=test-Validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/validator/save/] id=test-Validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/validator] id=test-Validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/Label123/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu\n" +
                "    (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/column/Z/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/column/Z/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/column/Z/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/column/Z/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/column/Z/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/column/Z/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/column/Z/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/column/Z/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "    -----\n" +
                "    (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" [/1/SpreadsheetName-1/cell/Label123/freeze] id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" [/1/SpreadsheetName-1/cell/Label123/unfreeze] id=test-unfreeze-MenuItem\n" +
                "  -----\n" +
                "  \"Labels\" [1] id=test-label-SubMenu\n" +
                "    \"Create...\" [/1/SpreadsheetName-1/cell/Label123/label] id=test-label-create-MenuItem\n" +
                "    \"List...\" [/1/SpreadsheetName-1/cell/Label123/labels] id=test-labels-list-MenuItem\n" +
                "    \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                "  \"References\" [/1/SpreadsheetName-1/cell/Label123/references] [0] id=test-references-MenuItem\n" +
                "  -----\n" +
                "  \"Reload\" [/1/SpreadsheetName-1/cell/Label123/reload] id=test-reload-MenuItem\n"
        );
    }

    // because label is unknown it is not possible to create insertXXX column/row because the column/row components are unavailable.
    @Test
    public void testUnknownLabel() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            SpreadsheetSelection.labelName("UnknownLabel")
                .setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token
        );

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Cell-MenuId",
                "Cell A1 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        this.treePrintAndCheck2(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/currency] id=test-clipboard-cut-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/currency] id=test-clipboard-copy-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Currency\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/currency] id=test-clipboard-paste-currency-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "  -----\n" +
                "  \"Currency\" id=test-Currency-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/currency/save/] id=test-Currency-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/currency] id=test-Currency-edit-MenuItem\n" +
                "  \"DateTimeSymbols\" id=test-DateTimeSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/dateTimeSymbols/save/] id=test-DateTimeSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/dateTimeSymbols] id=test-DateTimeSymbols-edit-MenuItem\n" +
                "  \"DecimalNumberSymbols\" id=test-DecimalNumberSymbols-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/decimalNumberSymbols/save/] id=test-DecimalNumberSymbols-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/decimalNumberSymbols] id=test-DecimalNumberSymbols-edit-MenuItem\n" +
                "  \"Formatter\" id=test-Formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/formatter/save/] id=test-Formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/formatter] id=test-Formatter-edit-MenuItem\n" +
                "  \"Locale\" id=test-Locale-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/locale/save/] id=test-Locale-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/locale] id=test-Locale-edit-MenuItem\n" +
                "  \"Parser\" id=test-Parser-SubMenu\n" +
                "    \"Hello Parser 1\" [/1/SpreadsheetName-1/cell/UnknownLabel/parser/save/hello-parser-1] id=test-Parser-hello-parser-1-MenuItem\n" +
                "    \"Test Parser 2\" [/1/SpreadsheetName-1/cell/UnknownLabel/parser/save/test-parser-2] id=test-Parser-test-parser-2-MenuItem\n" +
                "    \"Test Parser 3\" [/1/SpreadsheetName-1/cell/UnknownLabel/parser/save/test-parser-3] id=test-Parser-test-parser-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/parser/save/] id=test-Parser-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/parser] id=test-Parser-edit-MenuItem\n" +
                "  \"Style\" id=test-Style-SubMenu\n" +
                "    \"Alignment\" id=test-Style-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/LEFT] id=test-Style-alignment-left-MenuItem\n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/CENTER] id=test-Style-alignment-center-MenuItem\n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/RIGHT] id=test-Style-alignment-right-MenuItem\n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/JUSTIFY] id=test-Style-alignment-justify-MenuItem\n" +
                "    \"Vertical Alignment\" id=test-Style-verticalAlignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/vertical-align/save/TOP] id=test-Style-verticalAlignment-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/vertical-align/save/MIDDLE] id=test-Style-verticalAlignment-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/vertical-align/save/BOTTOM] id=test-Style-verticalAlignment-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-Style-color-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-color-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-color-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/color/save/] id=test-Style-color-color-clear-Link\n" +
                "    (mdi-palette) \"Background Color\" id=test-Style-backgroundColor-SubMenu\n" +
                "      ColorPaletteComponent\n" +
                "        TABLE\n" +
                "          id=\"test-Style-backgroundColor-Table\" className=dui dui-menu-item\n" +
                "            TBODY\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-1-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-2-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-3-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-4-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-5-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-6-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-7-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-8-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-9-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-10-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-11-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-12-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-13-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-14-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-15-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-16-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-17-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-18-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-19-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-20-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-21-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-22-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-23-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-24-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-25-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-26-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-27-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-28-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-29-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-30-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-31-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-32-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-33-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-34-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-35-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-36-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-37-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-38-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-39-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-40-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-41-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-42-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-43-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-44-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-45-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-46-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-47-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-48-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-49-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-50-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-51-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-52-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-53-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-54-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-55-Link\n" +
                "                TD\n" +
                "                  style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                    DISABLED id=test-Style-backgroundColor-color-56-Link\n" +
                "              TR\n",
                "                TD\n" +
                "                  colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                    \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/background-color/save/] id=test-Style-backgroundColor-color-clear-Link\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/font-weight/save/bold] id=test-Style-bold-MenuItem\n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/font-style/save/ITALIC] id=test-Style-italics-MenuItem\n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-decoration-line/save/LINE_THROUGH] id=test-strikeThru-MenuItem\n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem\n" +
                "    \"Text case\" id=test-Style-textCase-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/] id=test-Style-textCase-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/CAPITALIZE] id=test-Style-textCase-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/LOWERCASE] id=test-Style-textCase-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/UPPERCASE] id=test-Style-textCase-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-Style-textWrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/overflow-wrap/save/NORMAL] id=test-Style-textWrapping-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/overflow-x/save/VISIBLE] id=test-Style-textWrapping-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/overflow-x/save/HIDDEN] id=test-Style-textWrapping-wrap-MenuItem\n" +
                "    \"Border\" id=test-Style-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Top Color\" id=test-Style-borderTopColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderTopColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderTopColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-color/save/] id=test-Style-borderTopColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderTopStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/NONE] id=test-Style-borderTopStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/HIDDEN] id=test-Style-borderTopStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/DOTTED] id=test-Style-borderTopStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/DASHED] id=test-Style-borderTopStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/SOLID] id=test-Style-borderTopStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/DOUBLE] id=test-Style-borderTopStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/GROOVE] id=test-Style-borderTopStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/RIDGE] id=test-Style-borderTopStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/INSET] id=test-Style-borderTopStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/OUTSET] id=test-Style-borderTopStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/] id=test-Style-borderTopStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderTopWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/0px] id=test-Style-borderTopWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/1px] id=test-Style-borderTopWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/2px] id=test-Style-borderTopWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/3px] id=test-Style-borderTopWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/4px] id=test-Style-borderTopWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/] id=test-Style-borderTopWidth-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Left Color\" id=test-Style-borderLeftColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderLeftColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderLeftColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-color/save/] id=test-Style-borderLeftColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderLeftStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/NONE] id=test-Style-borderLeftStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/HIDDEN] id=test-Style-borderLeftStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/DOTTED] id=test-Style-borderLeftStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/DASHED] id=test-Style-borderLeftStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/SOLID] id=test-Style-borderLeftStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/DOUBLE] id=test-Style-borderLeftStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/GROOVE] id=test-Style-borderLeftStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/RIDGE] id=test-Style-borderLeftStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/INSET] id=test-Style-borderLeftStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/OUTSET] id=test-Style-borderLeftStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/] id=test-Style-borderLeftStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderLeftWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/0px] id=test-Style-borderLeftWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/1px] id=test-Style-borderLeftWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/2px] id=test-Style-borderLeftWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/3px] id=test-Style-borderLeftWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/4px] id=test-Style-borderLeftWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/] id=test-Style-borderLeftWidth-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Right Color\" id=test-Style-borderRightColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderRightColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderRightColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-color/save/] id=test-Style-borderRightColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderRightStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/NONE] id=test-Style-borderRightStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/HIDDEN] id=test-Style-borderRightStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/DOTTED] id=test-Style-borderRightStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/DASHED] id=test-Style-borderRightStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/SOLID] id=test-Style-borderRightStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/DOUBLE] id=test-Style-borderRightStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/GROOVE] id=test-Style-borderRightStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/RIDGE] id=test-Style-borderRightStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/INSET] id=test-Style-borderRightStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/OUTSET] id=test-Style-borderRightStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/] id=test-Style-borderRightStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderRightWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/0px] id=test-Style-borderRightWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/1px] id=test-Style-borderRightWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/2px] id=test-Style-borderRightWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/3px] id=test-Style-borderRightWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/4px] id=test-Style-borderRightWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/] id=test-Style-borderRightWidth-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Bottom Color\" id=test-Style-borderBottomColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderBottomColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderBottomColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-color/save/] id=test-Style-borderBottomColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderBottomStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/NONE] id=test-Style-borderBottomStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/HIDDEN] id=test-Style-borderBottomStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/DOTTED] id=test-Style-borderBottomStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/DASHED] id=test-Style-borderBottomStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/SOLID] id=test-Style-borderBottomStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/DOUBLE] id=test-Style-borderBottomStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/GROOVE] id=test-Style-borderBottomStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/RIDGE] id=test-Style-borderBottomStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/INSET] id=test-Style-borderBottomStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/OUTSET] id=test-Style-borderBottomStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/] id=test-Style-borderBottomStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderBottomWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/0px] id=test-Style-borderBottomWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/1px] id=test-Style-borderBottomWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/2px] id=test-Style-borderBottomWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/3px] id=test-Style-borderBottomWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/4px] id=test-Style-borderBottomWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/] id=test-Style-borderBottomWidth-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-Style-border-SubMenu\n" +
                "        (mdi-palette) \"Border Color\" id=test-Style-borderColor-SubMenu\n" +
                "          ColorPaletteComponent\n" +
                "            TABLE\n" +
                "              id=\"test-Style-borderColor-Table\" className=dui dui-menu-item\n" +
                "                TBODY\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-1-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-2-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-3-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-4-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-5-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-6-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-7-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-8-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-9-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-10-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-11-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-12-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-13-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-14-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-15-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-16-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-17-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-18-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-19-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-20-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-21-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-22-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-23-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-24-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-25-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-26-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-27-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-28-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-29-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-30-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-31-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-32-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-33-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-34-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-35-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-36-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-37-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-38-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-39-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-40-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-41-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-42-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-43-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-44-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-45-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-46-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-47-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-48-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-49-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-50-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-51-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-52-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-53-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-54-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-55-Link\n" +
                "                    TD\n" +
                "                      style=\"border-color: black; border-style: solid; border-width: 2px; height: 32px; text-align: center; width: 64px;\"\n" +
                "                        DISABLED id=test-Style-borderColor-color-56-Link\n" +
                "                  TR\n",
                "                    TD\n" +
                "                      colspan=8 style=\"height: 32px; text-align: center; width: 100%;\"\n" +
                "                        \"Clear\" [#/1/SpreadsheetName-1/cell/UnknownLabel/style/border-color/save/] id=test-Style-borderColor-color-clear-Link\n" +
                "        \"Style\" id=test-Style-borderAllStyle-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/NONE] id=test-Style-borderAllStyle-none-MenuItem\n" +
                "          \"Hidden\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/HIDDEN] id=test-Style-borderAllStyle-hidden-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/DOTTED] id=test-Style-borderAllStyle-dotted-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/DASHED] id=test-Style-borderAllStyle-dashed-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/SOLID] id=test-Style-borderAllStyle-solid-MenuItem\n" +
                "          \"Double\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/DOUBLE] id=test-Style-borderAllStyle-double-MenuItem\n" +
                "          \"Groove\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/GROOVE] id=test-Style-borderAllStyle-groove-MenuItem\n" +
                "          \"Ridge\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/RIDGE] id=test-Style-borderAllStyle-ridge-MenuItem\n" +
                "          \"Inset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/INSET] id=test-Style-borderAllStyle-inset-MenuItem\n" +
                "          \"Outset\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/OUTSET] id=test-Style-borderAllStyle-outset-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/] id=test-Style-borderAllStyle-clear-MenuItem\n" +
                "        \"Width\" id=test-Style-borderAllWidth-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/0px] id=test-Style-borderAllWidth-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/1px] id=test-Style-borderAllWidth-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/2px] id=test-Style-borderAllWidth-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/3px] id=test-Style-borderAllWidth-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/4px] id=test-Style-borderAllWidth-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/] id=test-Style-borderAllWidth-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/*/save/] id=test-Style-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/style] id=test-Style-edit-MenuItem\n" +
                "  \"Value Type\" id=test-ValueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/boolean] id=test-ValueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/date] id=test-ValueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/date-time] id=test-ValueTypes-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/email] id=test-ValueTypes-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/number] id=test-ValueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/text] id=test-ValueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/time] id=test-ValueTypes-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/url] id=test-ValueTypes-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/whole-number] id=test-ValueTypes-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/] id=test-ValueType-clear-MenuItem\n" +
                "  \"Value\" id=test-Value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/boolean] id=test-Value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/date] id=test-Value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/date-time] id=test-Value-date-time-MenuItem\n" +
                "    \"Email\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/email] id=test-Value-email-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/number] id=test-Value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/text] id=test-Value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/time] id=test-Value-time-MenuItem\n" +
                "    \"Url\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/url] id=test-Value-url-MenuItem\n" +
                "    \"Whole Number\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/whole-number] id=test-Value-whole-number-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/value] id=test-Value-clear-MenuItem\n" +
                "  \"Validator\" id=test-Validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/hello-validator-1] id=test-Validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/hello-validator-2] id=test-Validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/hello-validator-3] id=test-Validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/] id=test-Validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator] id=test-Validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/UnknownLabel/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu DISABLED\n" +
                "  -----\n" +
                "  \"Freeze\" [/1/SpreadsheetName-1/cell/UnknownLabel/freeze] id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" [/1/SpreadsheetName-1/cell/UnknownLabel/unfreeze] id=test-unfreeze-MenuItem\n" +
                "  -----\n" +
                "  \"Labels\" [1] id=test-label-SubMenu\n" +
                "    \"Create...\" [/1/SpreadsheetName-1/cell/UnknownLabel/label] id=test-label-create-MenuItem\n" +
                "    \"List...\" [/1/SpreadsheetName-1/cell/UnknownLabel/labels] id=test-labels-list-MenuItem\n" +
                "    \"UnknownLabel (A1)\" [/1/SpreadsheetName-1/label/UnknownLabel] id=test-label-0-MenuItem\n" +
                "  \"References\" [/1/SpreadsheetName-1/cell/UnknownLabel/references] [0] id=test-references-MenuItem\n" +
                "  -----\n" +
                "  \"Reload\" [/1/SpreadsheetName-1/cell/UnknownLabel/reload] id=test-reload-MenuItem\n"
        );
    }

    @Test
    public void testRow() {
        final SpreadsheetRowHistoryToken token = HistoryToken.rowSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            SpreadsheetSelection.parseRow("3").setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(token);

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Row-MenuId",
                "Row 3 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        this.treePrintAndCheck2(
            menu,
            "\"Row 3 Menu\" id=Row-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/row/3/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-row-remove) \"Delete\" [/1/SpreadsheetName-1/row/3/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu\n" +
                "    (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/3/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/3/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/3/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/3/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/3/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/3/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/3/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/3/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=date] id=test-row-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=date-reversed] id=test-row-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=date-time] id=test-row-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=date-time-reversed] id=test-row-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-month] id=test-row-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-month-reversed] id=test-row-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-week] id=test-row-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-week-reversed] id=test-row-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-am-pm] id=test-row-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-am-pm-reversed] id=test-row-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-day] id=test-row-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-day-reversed] id=test-row-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=minute-of-hour] id=test-row-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=minute-of-hour-reversed] id=test-row-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=month-of-year] id=test-row-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=month-of-year-reversed] id=test-row-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=nano-of-second] id=test-row-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=nano-of-second-reversed] id=test-row-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=number] id=test-row-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=number-reversed] id=test-row-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=seconds-of-minute] id=test-row-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=seconds-of-minute-reversed] id=test-row-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=text] id=test-row-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=text-reversed] id=test-row-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=text-case-insensitive] id=test-row-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=text-case-insensitive-reversed] id=test-row-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=time] id=test-row-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=time-reversed] id=test-row-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3/sort/save/3=year] id=test-row-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3/sort/save/3=year-reversed] id=test-row-sort-year-reverse-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/row/3/sort/edit/3=] id=test-row-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testRowRangeAnchorTop() {
        final SpreadsheetRowHistoryToken token = HistoryToken.rowSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            SpreadsheetSelection.parseRowRange("3:4").setAnchor(SpreadsheetViewportAnchor.TOP)
        );
        final SpreadsheetSelectionMenuContext context = this.context(token);

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Row-MenuId",
                "Row 3:4 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        this.treePrintAndCheck2(
            menu,
            "\"Row 3:4 Menu\" id=Row-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/row/3:4/top/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-row-remove) \"Delete\" [/1/SpreadsheetName-1/row/3:4/top/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu\n" +
                "    (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date] id=test-row-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date-reversed] id=test-row-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date-time] id=test-row-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date-time-reversed] id=test-row-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-month] id=test-row-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-month-reversed] id=test-row-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-week] id=test-row-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-week-reversed] id=test-row-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-am-pm] id=test-row-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-am-pm-reversed] id=test-row-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-day] id=test-row-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-day-reversed] id=test-row-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=minute-of-hour] id=test-row-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=minute-of-hour-reversed] id=test-row-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=month-of-year] id=test-row-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=month-of-year-reversed] id=test-row-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=nano-of-second] id=test-row-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=nano-of-second-reversed] id=test-row-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=number] id=test-row-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=number-reversed] id=test-row-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=seconds-of-minute] id=test-row-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=seconds-of-minute-reversed] id=test-row-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text] id=test-row-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text-reversed] id=test-row-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text-case-insensitive] id=test-row-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text-case-insensitive-reversed] id=test-row-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=time] id=test-row-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=time-reversed] id=test-row-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=year] id=test-row-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=year-reversed] id=test-row-sort-year-reverse-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/row/3:4/top/sort/edit/3=] id=test-row-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testRowRangeAnchorBottom() {
        final SpreadsheetRowHistoryToken token = HistoryToken.rowSelect(
            SPREADSHEET_ID, // id
            SPREADSHEET_NAME, // name
            SpreadsheetSelection.parseRowRange("3:4").setAnchor(SpreadsheetViewportAnchor.BOTTOM)
        );
        final SpreadsheetSelectionMenuContext context = this.context(token);

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Row-MenuId",
                "Row 3:4 Menu",
                Optional.empty(), // no icon
                Optional.empty() // no badge
            ),
            context
        );

        SpreadsheetSelectionMenu.build(
            token,
            menu,
            context
        );

        this.treePrintAndCheck2(
            menu,
            "\"Row 3:4 Menu\" id=Row-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/row/3:4/bottom/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-row-remove) \"Delete\" [/1/SpreadsheetName-1/row/3:4/bottom/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  \"Insert\" id=test-insert-SubMenu\n" +
                "    (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "    (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "      \"1\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "      \"2\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "      \"3\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "      \"...\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date] id=test-row-sort-date-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date-reversed] id=test-row-sort-date-reverse-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date-time] id=test-row-sort-date-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date-time-reversed] id=test-row-sort-date-time-reverse-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-month] id=test-row-sort-day-of-month-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-month-reversed] id=test-row-sort-day-of-month-reverse-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-week] id=test-row-sort-day-of-week-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-week-reversed] id=test-row-sort-day-of-week-reverse-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-am-pm] id=test-row-sort-hour-of-am-pm-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-am-pm-reversed] id=test-row-sort-hour-of-am-pm-reverse-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-day] id=test-row-sort-hour-of-day-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-day-reversed] id=test-row-sort-hour-of-day-reverse-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=minute-of-hour] id=test-row-sort-minute-of-hour-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=minute-of-hour-reversed] id=test-row-sort-minute-of-hour-reverse-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=month-of-year] id=test-row-sort-month-of-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=month-of-year-reversed] id=test-row-sort-month-of-year-reverse-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=nano-of-second] id=test-row-sort-nano-of-second-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=nano-of-second-reversed] id=test-row-sort-nano-of-second-reverse-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=number] id=test-row-sort-number-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=number-reversed] id=test-row-sort-number-reverse-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=seconds-of-minute] id=test-row-sort-seconds-of-minute-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=seconds-of-minute-reversed] id=test-row-sort-seconds-of-minute-reverse-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text] id=test-row-sort-text-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text-reversed] id=test-row-sort-text-reverse-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text-case-insensitive] id=test-row-sort-text-case-insensitive-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text-case-insensitive-reversed] id=test-row-sort-text-case-insensitive-reverse-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=time] id=test-row-sort-time-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=time-reversed] id=test-row-sort-time-reverse-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Forward\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=year] id=test-row-sort-year-MenuItem\n" +
                "      \"Reverse\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=year-reversed] id=test-row-sort-year-reverse-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/edit/3=] id=test-row-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken) {
        return this.context(
            historyToken,
            SPREADSHEET_COMPARATOR_PROVIDER.spreadsheetComparatorInfos()
                .stream()
                .map(SpreadsheetComparatorInfo::name)
                .collect(Collectors.toList()),
            Lists.empty(), // recentCurrencies
            Lists.empty(), // recentDateTimeSymbols
            Lists.empty(), // recentDecimalNumberSymbols
            Lists.empty(), // formatter patterns
            Lists.empty(), // SpreadsheetFormatterMenu
            Lists.empty(), // locales
            Lists.empty(), // parse patterns
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            NO_SUMMARY
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final List<SpreadsheetComparatorName> sortComparatorNames,
                                                    final List<Currency> recentCurrencies,
                                                    final List<DateTimeSymbols> recentDateTimeSymbols,
                                                    final List<DecimalNumberSymbols> recentDecimalNumberSymbols,
                                                    final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors,
                                                    final List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus,
                                                    final List<Locale> locales,
                                                    final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors,
                                                    final List<TextStyleProperty<?>> recentTextStyleProperties,
                                                    final List<ValidatorSelector> recentValidatorSelectors,
                                                    final Optional<SpreadsheetCell> summary) {
        return new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public List<SpreadsheetComparatorName> sortComparatorNames() {
                return sortComparatorNames;
            }

            @Override
            public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
                return Sets.of(
                    selection.isLabelName() ?
                        selection.toLabelName()
                            .setLabelMappingReference(SpreadsheetSelection.A1) :
                        LABEL.setLabelMappingReference(
                            selection.toCell()
                        )
                );
            }

            @Override
            public Optional<String> localeText(final Locale locale) {
                return Optional.of(
                    locale.getDisplayName()
                );
            }

            @Override
            public List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors() {
                return recentSpreadsheetFormatterSelectors;
            }

            @Override
            public Set<SpreadsheetExpressionReference> references(final SpreadsheetSelection selection) {
                return selection.equalsIgnoreReferenceKind(SpreadsheetSelection.A1) ?
                    Sets.of(
                        SpreadsheetSelection.parseCell("Z99")
                    ) :
                    Sets.empty();
            }

            // SpreadsheetLabelNameResolver.....................................................................................

            @Override
            public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
                return Optional.ofNullable(
                    spreadsheetLabelName.equals(LABEL) ?
                        SpreadsheetSelection.parseCell("Z1") :
                        null
                );
            }

            @Override
            public List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus() {
                return spreadsheetFormatterMenus;
            }

            @Override
            public List<Currency> recentCurrencies() {
                return recentCurrencies;
            }

            @Override
            public List<DateTimeSymbols> recentDateTimeSymbols() {
                return recentDateTimeSymbols;
            }

            @Override
            public List<DecimalNumberSymbols> recentDecimalNumberSymbols() {
                return recentDecimalNumberSymbols;
            }

            @Override
            public List<Locale> recentLocales() {
                return locales;
            }

            @Override
            public List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors() {
                return recentSpreadsheetParserSelectors;
            }

            @Override
            public List<TextStyleProperty<?>> recentTextStyleProperties() {
                return recentTextStyleProperties;
            }

            @Override
            public List<ValidatorSelector> recentValidatorSelectors() {
                return recentValidatorSelectors;
            }

            @Override
            public List<SpreadsheetParserSelector> spreadsheetParserSelectors() {
                return Lists.of(
                    PARSER1,
                    SpreadsheetParserSelector.parse("test-parser-2"),
                    SpreadsheetParserSelector.parse("test-parser-3")
                );
            }

            @Override
            public List<ValidatorSelector> validatorSelectors() {
                return Lists.of(
                    VALIDATOR1,
                    ValidatorSelector.parse("hello-validator-3"),
                    ValidatorSelector.parse("hello-validator-2")
                );
            }

            @Override
            public Optional<SpreadsheetCell> selectionSummary() {
                return summary;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.LOCALE,
                    LOCALE
                ).loadFromLocale(CURRENCY_LOCALE_CONTEXT);
            }
        };
    }

    private void treePrintAndCheck2(final TreePrintable printable,
                                    final String... expected) {
        this.treePrintAndCheck(
            printable,
            Arrays.stream(expected)
                .collect(Collectors.joining())
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenu> type() {
        return SpreadsheetSelectionMenu.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
