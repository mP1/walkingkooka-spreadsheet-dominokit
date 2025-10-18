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

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.contextmenu.FakeSpreadsheetSelectionMenuContext;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowHistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetNumberParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class SpreadsheetSelectionMenuTest implements PublicStaticHelperTesting<SpreadsheetSelectionMenu>,
    TreePrintableTesting,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    private final static Optional<SpreadsheetCell> NO_SUMMARY = Optional.empty();

    private static final ValidatorSelector VALIDATOR1 = ValidatorSelector.parse("hello-validator-1");

    @Test
    public void testCell() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellRecentSpreadsheetFormatterSelector() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.of(
                SpreadsheetFormatterSelector.parse("date-format-pattern recent-1A"),
                SpreadsheetFormatterSelector.parse("date-format-pattern recent-2B"),
                SpreadsheetFormatterSelector.parse("date-format-pattern recent-3C")
            ),
            Lists.empty(),
            Lists.empty(),
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            NO_SUMMARY
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Date Format Pattern recent-1A\" [/1/SpreadsheetName-1/cell/A1/formatter/save/date-format-pattern%20recent-1A] id=test-formatter-recent-0-MenuItem\n" +
                "    \"Date Format Pattern recent-2B\" [/1/SpreadsheetName-1/cell/A1/formatter/save/date-format-pattern%20recent-2B] id=test-formatter-recent-1-MenuItem\n" +
                "    \"Date Format Pattern recent-3C\" [/1/SpreadsheetName-1/cell/A1/formatter/save/date-format-pattern%20recent-3C] id=test-formatter-recent-2-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellRecentSpreadsheetParserSelector() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetNumberParsePattern parsePattern = SpreadsheetPattern.parseNumberParsePattern("$0.00");

        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(),
            Lists.empty(),
            Lists.of(
                parsePattern.spreadsheetParserSelector()
            ),
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            NO_SUMMARY
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellRecentTextStyleProperties() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(),
            Lists.empty(),
            Lists.empty(),
            Lists.of(
                TextStyleProperty.with(
                    TextStylePropertyName.TEXT_ALIGN,
                    Optional.of(TextAlign.CENTER)
                ),
                TextStyleProperty.with(
                    TextStylePropertyName.COLOR,
                    Optional.of(Color.parse("#123456"))
                ),
                TextStyleProperty.with(
                    TextStylePropertyName.VERTICAL_ALIGN,
                    Optional.empty()
                )
            ), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            NO_SUMMARY
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "    -----\n" +
                "    \"Set Color #123456\" [/1/SpreadsheetName-1/cell/A1/style/color/save/%23123456] id=test-recent-style-0-MenuItem\n" +
                "    \"Set Text Align CENTER\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-recent-style-0-MenuItem\n" +
                "    \"Clear Vertical Align\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/] id=test-recent-style-0-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellRecentSpreadsheetFormatterSelectorAndSpreadsheetParserSelectorAndSpreadsheetFormatterMenus() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.of(
                SpreadsheetFormatterSelector.parse("date-format-pattern recent-1A")
            ), // recent formatters
            Lists.of(
                SpreadsheetFormatterMenu.with(
                    "Short",
                    SpreadsheetPattern.parseDateFormatPattern("yy/mm/dd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Medium",
                    SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
                        .spreadsheetFormatterSelector()
                ),
                SpreadsheetFormatterMenu.with(
                    "Default text",
                    SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                )
            ),
            Lists.of(
                SpreadsheetParserSelector.parse("date-parse-pattern recent-1A"),
                SpreadsheetParserSelector.parse("date-parse-pattern recent-2B")
            ), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            NO_SUMMARY
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    \"Date Format Pattern\" id=test-formatter-date-format-pattern-SubMenu\n" +
                "      \"Short\" [/1/SpreadsheetName-1/cell/A1/formatter/save/date-format-pattern%20yy/mm/dd] id=test-formatter-date-format-pattern-MenuItem\n" +
                "      \"Medium\" [/1/SpreadsheetName-1/cell/A1/formatter/save/date-format-pattern%20yyyy/mm/ddd] id=test-formatter-date-format-pattern-MenuItem\n" +
                "    \"Text Format Pattern\" id=test-formatter-text-format-pattern-SubMenu\n" +
                "      \"Default text\" [/1/SpreadsheetName-1/cell/A1/formatter/save/text-format-pattern%20@] id=test-formatter-text-format-pattern-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Date Format Pattern recent-1A\" [/1/SpreadsheetName-1/cell/A1/formatter/save/date-format-pattern%20recent-1A] id=test-formatter-recent-0-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellDateTimeSymbolsChecked() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setDateTimeSymbols(
                        Optional.of(DATE_TIME_SYMBOLS)
                    )
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] CHECKED id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellDecimalNumberSymbolsChecked() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setDecimalNumberSymbols(
                        Optional.of(DECIMAL_NUMBER_SYMBOLS)
                    )
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] CHECKED id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellLocaleChecked() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setLocale(
                        Optional.of(Locale.ENGLISH)
                    )
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] CHECKED id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellValidators() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.of(
                ValidatorSelector.parse("validator-111"),
                ValidatorSelector.parse("validator-222")
            ), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Validator 111\" [/1/SpreadsheetName-1/cell/A1/validator/save/validator-111] id=test-validator-recent-0-MenuItem\n" +
                "    \"Validator 222\" [/1/SpreadsheetName-1/cell/A1/validator/save/validator-222] id=test-validator-recent-1-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellValidatorsChecked() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.of(
                ValidatorSelector.parse("validator-111"),
                ValidatorSelector.parse("validator-222")
            ), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                    .setValidator(
                        Optional.ofNullable(VALIDATOR1)
                    )
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] CHECKED id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "    -----\n" +
                "    \"Validator 111\" [/1/SpreadsheetName-1/cell/A1/validator/save/validator-111] id=test-validator-recent-0-MenuItem\n" +
                "    \"Validator 222\" [/1/SpreadsheetName-1/cell/A1/validator/save/validator-222] id=test-validator-recent-1-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellValueType() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellValueTypeNumberChecked() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValue(
                        Optional.of(
                            EXPRESSION_NUMBER_KIND.create(123)
                        )
                    )
                )
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] CHECKED id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
    public void testCellValueTypeTextChecked() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cellSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
            token,
            SpreadsheetComparatorNameList.EMPTY,
            Lists.empty(), // recent formatters
            Lists.empty(),
            Lists.empty(), // recent parsers
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            Optional.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValue(
                        Optional.of("HelloValue")
                    )
                )
            )
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/A1/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/A1/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/A1/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/A1/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/A1/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/A1/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/A1/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/A1/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/A1/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/A1/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/A1/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/A1/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/A1/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/A1/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/A1/value/text] CHECKED id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/A1/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/A1/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/A1/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/A1/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/A/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Cell B2:C3 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Column\" id=test-column-sort-SubMenu\n" +
                "    \"Date\" id=test-column-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date%20UP] id=test-column-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date%20DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date-time%20UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=date-time%20DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-month%20UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-month%20DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-week%20UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=day-of-week%20DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-am-pm%20UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-am-pm%20DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-day%20UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=hour-of-day%20DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=minute-of-hour%20UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=minute-of-hour%20DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=month-of-year%20UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=month-of-year%20DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=nano-of-second%20UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=nano-of-second%20DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=number%20UP] id=test-column-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=number%20DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=seconds-of-minute%20UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=seconds-of-minute%20DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text%20UP] id=test-column-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text%20DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text-case-insensitive%20UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=text-case-insensitive%20DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=time%20UP] id=test-column-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=time%20DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=year%20UP] id=test-column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B=year%20DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/edit/B=] id=test-column-sort-edit-MenuItem\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date%20UP] id=test-row-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date%20DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date-time%20UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=date-time%20DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-month%20UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-month%20DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-week%20UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=day-of-week%20DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-am-pm%20UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-am-pm%20DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-day%20UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=hour-of-day%20DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=minute-of-hour%20UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=minute-of-hour%20DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=month-of-year%20UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=month-of-year%20DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=nano-of-second%20UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=nano-of-second%20DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=number%20UP] id=test-row-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=number%20DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=seconds-of-minute%20UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=seconds-of-minute%20DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text%20UP] id=test-row-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text%20DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text-case-insensitive%20UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=text-case-insensitive%20DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=time%20UP] id=test-row-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=time%20DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=year%20UP] id=test-row-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2=year%20DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
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
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/Label123/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/Label123/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/Label123/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/Label123/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/Label123/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/Label123/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/Label123/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/Label123/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/Label123/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/Label123/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/Label123/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/Label123/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/Label123/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/Label123/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/Label123/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/Label123/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/Label123/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/Label123/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/Label123/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/Label123/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/Label123/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/Label123/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/Label123/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/Label123/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/Label123/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/Label123/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/Label123/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/Label123/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/Label123/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/Label123/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/Label123/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/Label123/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/Label123/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/Label123/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/Label123/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/Label123/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/Label123/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/Label123/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/Label123/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/Label123/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/Label123/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/Label123/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/Label123/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/Label123/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/Label123/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/Label123/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/Label123/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/Label123/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/Label123/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/Label123/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/Label123/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/Label123/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/Label123/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/Label123/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/Label123/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/Label123/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/Label123/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/Label123/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/Label123/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/Label123/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/Label123/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/Label123/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/Label123/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/Label123/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/Label123/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/Label123/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/Z/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/Z/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/Z/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/Z/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/Z/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/Z/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/Z/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/Z/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/1/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
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
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Cell A1 Menu\" id=Cell-MenuId\n" +
                "  (mdi-content-cut) \"Cut\" id=test-clipboard-cut-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/cell] id=test-clipboard-cut-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/formula] id=test-clipboard-cut-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/dateTimeSymbols] id=test-clipboard-cut-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/decimalNumberSymbols] id=test-clipboard-cut-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/locale] id=test-clipboard-cut-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/formatter] id=test-clipboard-cut-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/parser] id=test-clipboard-cut-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/value] id=test-clipboard-cut-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/value-type] id=test-clipboard-cut-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/UnknownLabel/cut/validator] id=test-clipboard-cut-validator-MenuItem\n" +
                "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/dateTimeSymbols] id=test-clipboard-copy-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/decimalNumberSymbols] id=test-clipboard-copy-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/locale] id=test-clipboard-copy-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/formatter] id=test-clipboard-copy-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/parser] id=test-clipboard-copy-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/value] id=test-clipboard-copy-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/value-type] id=test-clipboard-copy-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/UnknownLabel/copy/validator] id=test-clipboard-copy-validator-MenuItem\n" +
                "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                "    \"Cell\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                "    \"Formula\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                "    \"Date Time Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/dateTimeSymbols] id=test-clipboard-paste-date-time-symbols-MenuItem\n" +
                "    \"Decimal Number Symbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/decimalNumberSymbols] id=test-clipboard-paste-decimal-number-symbols-MenuItem\n" +
                "    \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/locale] id=test-clipboard-paste-locale-MenuItem\n" +
                "    \"Formatter\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/formatter] id=test-clipboard-paste-formatter-MenuItem\n" +
                "    \"Parser\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/parser] id=test-clipboard-paste-parser-MenuItem\n" +
                "    \"Style\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
                "    \"Value\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/value] id=test-clipboard-paste-value-MenuItem\n" +
                "    \"Value Type\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/value-type] id=test-clipboard-paste-value-type-MenuItem\n" +
                "    \"Validator\" [/1/SpreadsheetName-1/cell/UnknownLabel/paste/validator] id=test-clipboard-paste-validator-MenuItem\n" +
                "  -----\n" +
                "  \"Style\" id=test-style-SubMenu\n" +
                "    \"Alignment\" id=test-alignment-SubMenu\n" +
                "      (mdi-format-align-left) \"Left\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/LEFT] id=test-left-MenuItem key=L \n" +
                "      (mdi-format-align-center) \"Center\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/CENTER] id=test-center-MenuItem key=C \n" +
                "      (mdi-format-align-right) \"Right\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/RIGHT] id=test-right-MenuItem key=R \n" +
                "      (mdi-format-align-justify) \"Justify\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-align/save/JUSTIFY] id=test-justify-MenuItem key=J \n" +
                "    \"Vertical Alignment\" id=test-vertical-alignment-SubMenu\n" +
                "      (mdi-format-align-top) \"Top\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/vertical-align/save/TOP] id=test-top-MenuItem\n" +
                "      (mdi-format-align-middle) \"Middle\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/vertical-align/save/MIDDLE] id=test-middle-MenuItem\n" +
                "      (mdi-format-align-bottom) \"Bottom\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/vertical-align/save/BOTTOM] id=test-bottom-MenuItem\n" +
                "    (mdi-palette) \"Color\" id=test-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                "      SpreadsheetMetadataColorPickerComponent\n" +
                "    (mdi-format-bold) \"Bold\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/font-weight/save/bold] id=test-bold-MenuItem key=b \n" +
                "    (mdi-format-italic) \"Italics\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/font-style/save/ITALIC] id=test-italics-MenuItem key=i \n" +
                "    (mdi-format-strikethrough) \"Strike-thru\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-decoration-line/save/LINE_THROUGH] id=test-strike-thru-MenuItem key=s \n" +
                "    (mdi-format-underline) \"Underline\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-decoration-line/save/UNDERLINE] id=test-underline-MenuItem key=u \n" +
                "    \"Text case\" id=test-text-case-SubMenu\n" +
                "      (mdi-format-letter-case-upper) \"Normal\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/] id=test-normal-MenuItem\n" +
                "      (mdi-format-letter-case) \"Capitalize\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/CAPITALIZE] id=test-capitalize-MenuItem\n" +
                "      (mdi-format-letter-case-lower) \"Lower case\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/LOWERCASE] id=test-lower-MenuItem\n" +
                "      (mdi-format-letter-case-upper) \"Upper case\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/text-transform/save/UPPERCASE] id=test-upper-MenuItem\n" +
                "    \"Wrapping\" id=test-text-wrapping-SubMenu\n" +
                "      (mdi-format-text-wrapping-clip) \"Clip\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/overflow-wrap/save/NORMAL] id=test-clip-MenuItem\n" +
                "      (mdi-format-text-wrapping-overflow) \"Overflow\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/overflow-x/save/VISIBLE] id=test-overflow-MenuItem\n" +
                "      (mdi-format-text-wrapping-wrap) \"Wrap\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/overflow-x/save/HIDDEN] id=test-wrap-MenuItem\n" +
                "    \"Border\" id=test-border-SubMenu\n" +
                "      (mdi-border-top-variant) \"Top\" id=test-border-top-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-top-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/NONE] id=test-border-top-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/DASHED] id=test-border-top-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/DOTTED] id=test-border-top-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/SOLID] id=test-border-top-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-style/save/] id=test-border-top-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-top-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/0px] id=test-border-top-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/1px] id=test-border-top-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/2px] id=test-border-top-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/3px] id=test-border-top-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/4px] id=test-border-top-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-top-width/save/] id=test-border-top-width-clear-MenuItem\n" +
                "      (mdi-border-left-variant) \"Left\" id=test-border-left-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-left-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/NONE] id=test-border-left-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/DASHED] id=test-border-left-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/DOTTED] id=test-border-left-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/SOLID] id=test-border-left-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-style/save/] id=test-border-left-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-left-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/0px] id=test-border-left-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/1px] id=test-border-left-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/2px] id=test-border-left-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/3px] id=test-border-left-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/4px] id=test-border-left-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-left-width/save/] id=test-border-left-width-clear-MenuItem\n" +
                "      (mdi-border-right-variant) \"Right\" id=test-border-right-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-right-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/NONE] id=test-border-right-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/DASHED] id=test-border-right-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/DOTTED] id=test-border-right-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/SOLID] id=test-border-right-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-style/save/] id=test-border-right-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-right-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/0px] id=test-border-right-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/1px] id=test-border-right-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/2px] id=test-border-right-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/3px] id=test-border-right-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/4px] id=test-border-right-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-right-width/save/] id=test-border-right-width-clear-MenuItem\n" +
                "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-bottom-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/NONE] id=test-border-bottom-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/DASHED] id=test-border-bottom-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/DOTTED] id=test-border-bottom-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/SOLID] id=test-border-bottom-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-style/save/] id=test-border-bottom-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-bottom-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/0px] id=test-border-bottom-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/1px] id=test-border-bottom-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/2px] id=test-border-bottom-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/3px] id=test-border-bottom-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/4px] id=test-border-bottom-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-bottom-width/save/] id=test-border-bottom-width-clear-MenuItem\n" +
                "      (mdi-border-all-variant) \"All\" id=test-border-all-SubMenu\n" +
                "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                "          SpreadsheetMetadataColorPickerComponent\n" +
                "        \"Style\" id=test-border-all-style-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/NONE] id=test-border-all-style-none-MenuItem\n" +
                "          \"Dashed\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/DASHED] id=test-border-all-style-dashed-MenuItem\n" +
                "          \"Dotted\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/DOTTED] id=test-border-all-style-dotted-MenuItem\n" +
                "          \"Solid\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/SOLID] id=test-border-all-style-solid-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-style/save/] id=test-border-all-style-clear-MenuItem\n" +
                "        \"Width\" id=test-border-all-width-SubMenu\n" +
                "          \"None\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/0px] id=test-border-all-width-0-MenuItem\n" +
                "          \"1\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/1px] id=test-border-all-width-1-MenuItem\n" +
                "          \"2\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/2px] id=test-border-all-width-2-MenuItem\n" +
                "          \"3\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/3px] id=test-border-all-width-3-MenuItem\n" +
                "          \"4\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/4px] id=test-border-all-width-4-MenuItem\n" +
                "          (mdi-format-clear) \"Clear\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/border-width/save/] id=test-border-all-width-clear-MenuItem\n" +
                "    -----\n" +
                "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/UnknownLabel/style/*/save/] id=test-clear-style-MenuItem\n" +
                "  \"Formatter\" id=test-formatter-SubMenu\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/formatter/save/] id=test-formatter-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/formatter] id=test-formatter-edit-MenuItem\n" +
                "  (mdi-earth) \"DateTimeSymbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/dateTimeSymbols] id=test-dateTimeSymbols-MenuItem\n" +
                "  (mdi-earth) \"DecimalNumberSymbols\" [/1/SpreadsheetName-1/cell/UnknownLabel/decimalNumberSymbols] id=test-decimalNumberSymbols-MenuItem\n" +
                "  (mdi-earth) \"Locale\" [/1/SpreadsheetName-1/cell/UnknownLabel/locale] id=test-locale-MenuItem\n" +
                "  \"Value type\" id=test-valueType-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/boolean] id=test-valueTypes-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/date] id=test-valueTypes-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/date-time] id=test-valueTypes-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/number] id=test-valueTypes-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/text] id=test-valueTypes-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/valueType/save/time] id=test-valueTypes-time-MenuItem\n" +
                "  \"Value\" id=test-value-SubMenu\n" +
                "    \"Boolean\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/boolean] id=test-value-boolean-MenuItem\n" +
                "    \"Date\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/date] id=test-value-date-MenuItem\n" +
                "    \"Date Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/date-time] id=test-value-date-time-MenuItem\n" +
                "    \"Number\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/number] id=test-value-number-MenuItem\n" +
                "    \"Text\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/text] id=test-value-text-MenuItem\n" +
                "    \"Time\" [/1/SpreadsheetName-1/cell/UnknownLabel/value/time] id=test-value-time-MenuItem\n" +
                "  \"Validator\" id=test-validator-SubMenu\n" +
                "    \"Hello Validator 1\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/hello-validator-1] id=test-validator-hello-validator-1-MenuItem\n" +
                "    \"Hello Validator 2\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/hello-validator-2] id=test-validator-hello-validator-2-MenuItem\n" +
                "    \"Hello Validator 3\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/hello-validator-3] id=test-validator-hello-validator-3-MenuItem\n" +
                "    -----\n" +
                "    (mdi-close) \"Clear...\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator/save/] id=test-validator-clear-MenuItem\n" +
                "    -----\n" +
                "    \"Edit...\" [/1/SpreadsheetName-1/cell/UnknownLabel/validator] id=test-validator-edit-MenuItem\n" +
                "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/spreadsheet/hideZeroValues/save/true] id=test-hideIfZero-MenuItem\n" +
                "  -----\n" +
                "  (mdi-close) \"Delete\" [/1/SpreadsheetName-1/cell/UnknownLabel/delete] id=test-delete-MenuItem\n" +
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
    public void testColumn() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.columnSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.parseColumn("B").setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(token);

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Column-MenuId",
                "Column B Menu",
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

        this.treePrintAndCheck(
            menu,
            "\"Column B Menu\" id=Column-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/column/B/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-column-remove) \"Delete\" [/1/SpreadsheetName-1/column/B/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Column\" id=test-column-sort-SubMenu\n" +
                "    \"Date\" id=test-column-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=date%20UP] id=test-column-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=date%20DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=date-time%20UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=date-time%20DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=day-of-month%20UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=day-of-month%20DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=day-of-week%20UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=day-of-week%20DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=hour-of-am-pm%20UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=hour-of-am-pm%20DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=hour-of-day%20UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=hour-of-day%20DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=minute-of-hour%20UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=minute-of-hour%20DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=month-of-year%20UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=month-of-year%20DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=nano-of-second%20UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=nano-of-second%20DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=number%20UP] id=test-column-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=number%20DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=seconds-of-minute%20UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=seconds-of-minute%20DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=text%20UP] id=test-column-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=text%20DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=text-case-insensitive%20UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=text-case-insensitive%20DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=time%20UP] id=test-column-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=time%20DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B/sort/save/B=year%20UP] id=test-column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B/sort/save/B=year%20DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/column/B/sort/edit/B=] id=test-column-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testColumnRangeAnchorLeft() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.columnSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.parseColumnRange("B:C").setAnchor(SpreadsheetViewportAnchor.LEFT)
        );
        final SpreadsheetSelectionMenuContext context = this.context(token);

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Column-MenuId",
                "Column B:C Menu",
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

        this.treePrintAndCheck(
            menu,
            "\"Column B:C Menu\" id=Column-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/column/B:C/left/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-column-remove) \"Delete\" [/1/SpreadsheetName-1/column/B:C/left/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B:C/left/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B:C/left/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B:C/left/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B:C/left/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B:C/left/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B:C/left/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B:C/left/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B:C/left/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Column\" id=test-column-sort-SubMenu\n" +
                "    \"Date\" id=test-column-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=date%20UP] id=test-column-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=date%20DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=date-time%20UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=date-time%20DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=day-of-month%20UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=day-of-month%20DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=day-of-week%20UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=day-of-week%20DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=hour-of-am-pm%20UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=hour-of-am-pm%20DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=hour-of-day%20UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=hour-of-day%20DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=minute-of-hour%20UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=minute-of-hour%20DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=month-of-year%20UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=month-of-year%20DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=nano-of-second%20UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=nano-of-second%20DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=number%20UP] id=test-column-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=number%20DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=seconds-of-minute%20UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=seconds-of-minute%20DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=text%20UP] id=test-column-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=text%20DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=text-case-insensitive%20UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=text-case-insensitive%20DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=time%20UP] id=test-column-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=time%20DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=year%20UP] id=test-column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B=year%20DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/column/B:C/left/sort/edit/B=] id=test-column-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testColumnRangeAnchorRight() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.columnSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
            SpreadsheetSelection.parseColumnRange("B:C").setAnchor(SpreadsheetViewportAnchor.RIGHT)
        );
        final SpreadsheetSelectionMenuContext context = this.context(token);

        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
            Menu.create(
                "Column-MenuId",
                "Column B:C Menu",
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

        this.treePrintAndCheck(
            menu,
            "\"Column B:C Menu\" id=Column-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/column/B:C/right/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-column-remove) \"Delete\" [/1/SpreadsheetName-1/column/B:C/right/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-column-plus-before) \"Insert before column\" id=test-column-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/1] id=test-column-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/2] id=test-column-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/3] id=test-column-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore] id=test-column-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-column-plus-after) \"Insert after column\" id=test-column-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/1] id=test-column-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/2] id=test-column-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/3] id=test-column-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter] id=test-column-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Column\" id=test-column-sort-SubMenu\n" +
                "    \"Date\" id=test-column-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=date%20UP] id=test-column-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=date%20DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=date-time%20UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=date-time%20DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=day-of-month%20UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=day-of-month%20DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=day-of-week%20UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=day-of-week%20DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=hour-of-am-pm%20UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=hour-of-am-pm%20DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=hour-of-day%20UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=hour-of-day%20DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=minute-of-hour%20UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=minute-of-hour%20DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=month-of-year%20UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=month-of-year%20DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=nano-of-second%20UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=nano-of-second%20DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=number%20UP] id=test-column-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=number%20DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=seconds-of-minute%20UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=seconds-of-minute%20DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=text%20UP] id=test-column-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=text%20DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=text-case-insensitive%20UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=text-case-insensitive%20DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=time%20UP] id=test-column-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=time%20DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=year%20UP] id=test-column-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B=year%20DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                "    -----\n" +
                "    \"Edit\" [/1/SpreadsheetName-1/column/B:C/right/sort/edit/B=] id=test-column-sort-edit-MenuItem\n" +
                "  -----\n" +
                "  \"Freeze\" id=test-freeze-MenuItem\n" +
                "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testRow() {
        final SpreadsheetRowHistoryToken token = HistoryToken.rowSelect(
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Row 3 Menu\" id=Row-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/row/3/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-row-remove) \"Delete\" [/1/SpreadsheetName-1/row/3/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/3/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/3/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/3/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/3/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/3/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/3/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/3/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/3/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=date%20UP] id=test-row-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=date%20DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=date-time%20UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=date-time%20DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-month%20UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-month%20DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-week%20UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=day-of-week%20DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-am-pm%20UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-am-pm%20DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-day%20UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=hour-of-day%20DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=minute-of-hour%20UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=minute-of-hour%20DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=month-of-year%20UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=month-of-year%20DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=nano-of-second%20UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=nano-of-second%20DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=number%20UP] id=test-row-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=number%20DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=seconds-of-minute%20UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=seconds-of-minute%20DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=text%20UP] id=test-row-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=text%20DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=text-case-insensitive%20UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=text-case-insensitive%20DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=time%20UP] id=test-row-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=time%20DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3/sort/save/3=year%20UP] id=test-row-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3/sort/save/3=year%20DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
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
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Row 3:4 Menu\" id=Row-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/row/3:4/top/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-row-remove) \"Delete\" [/1/SpreadsheetName-1/row/3:4/top/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/3:4/top/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/3:4/top/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date%20UP] id=test-row-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date%20DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date-time%20UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=date-time%20DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-month%20UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-month%20DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-week%20UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=day-of-week%20DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-am-pm%20UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-am-pm%20DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-day%20UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=hour-of-day%20DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=minute-of-hour%20UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=minute-of-hour%20DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=month-of-year%20UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=month-of-year%20DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=nano-of-second%20UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=nano-of-second%20DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=number%20UP] id=test-row-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=number%20DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=seconds-of-minute%20UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=seconds-of-minute%20DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text%20UP] id=test-row-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text%20DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text-case-insensitive%20UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=text-case-insensitive%20DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=time%20UP] id=test-row-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=time%20DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=year%20UP] id=test-row-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3=year%20DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
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
            SpreadsheetId.with(1), // id
            SpreadsheetName.with("SpreadsheetName-1"), // name
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

        this.treePrintAndCheck(
            menu,
            "\"Row 3:4 Menu\" id=Row-MenuId\n" +
                "  \"Clear\" [/1/SpreadsheetName-1/row/3:4/bottom/clear] id=test-clear-MenuItem\n" +
                "  (mdi-table-row-remove) \"Delete\" [/1/SpreadsheetName-1/row/3:4/bottom/delete] id=test-delete-MenuItem\n" +
                "  -----\n" +
                "  (mdi-table-row-plus-before) \"Insert before row\" id=test-row-insert-before-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore/1] id=test-row-insert-before-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore/2] id=test-row-insert-before-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore/3] id=test-row-insert-before-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/3:4/bottom/insertBefore] id=test-row-insert-before-prompt-MenuItem\n" +
                "  (mdi-table-row-plus-after) \"Insert after row\" id=test-row-insert-after-SubMenu\n" +
                "    \"1\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter/1] id=test-row-insert-after-1-MenuItem\n" +
                "    \"2\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter/2] id=test-row-insert-after-2-MenuItem\n" +
                "    \"3\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter/3] id=test-row-insert-after-3-MenuItem\n" +
                "    \"...\" [/1/SpreadsheetName-1/row/3:4/bottom/insertAfter] id=test-row-insert-after-prompt-MenuItem\n" +
                "  -----\n" +
                "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date%20UP] id=test-row-sort-date-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date%20DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date-time%20UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=date-time%20DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-month%20UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-month%20DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-week%20UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=day-of-week%20DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-am-pm%20UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-am-pm%20DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-day%20UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=hour-of-day%20DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=minute-of-hour%20UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=minute-of-hour%20DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=month-of-year%20UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=month-of-year%20DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=nano-of-second%20UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=nano-of-second%20DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=number%20UP] id=test-row-sort-number-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=number%20DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=seconds-of-minute%20UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=seconds-of-minute%20DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text%20UP] id=test-row-sort-text-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text%20DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text-case-insensitive%20UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=text-case-insensitive%20DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=time%20UP] id=test-row-sort-time-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=time%20DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=year%20UP] id=test-row-sort-year-UP-MenuItem\n" +
                "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3=year%20DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
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
            Lists.empty(), // format patterns
            Lists.empty(), // SpreadsheetFormatterMenu
            Lists.empty(), // parse patterns
            Lists.empty(), // recentTextStyleProperties
            Lists.empty(), // recentValidatorSelectors
            NO_SUMMARY
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final List<SpreadsheetComparatorName> sortComparatorNames,
                                                    final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors,
                                                    final List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus,
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
                ).loadFromLocale(
                    LocaleContexts.jre(LOCALE)
                );
            }
        };
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
