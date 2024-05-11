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

package walkingkooka.spreadsheet.dominokit.ui.selectionmenu;

import org.dominokit.domino.ui.menu.Menu;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetSelectionSummary;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.text.printer.TreePrintableTesting;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class SpreadsheetSelectionMenuTest implements PublicStaticHelperTesting<SpreadsheetSelectionMenu>,
        TreePrintableTesting {

    @Test
    public void testCell() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
                token,
                Lists.empty(),
                Lists.empty()
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
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern] id=test-clipboard-cut-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern] id=test-clipboard-cut-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                        "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern] id=test-clipboard-copy-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern] id=test-clipboard-copy-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                        "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern] id=test-clipboard-paste-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern] id=test-clipboard-paste-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
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
                        "      metadata color picker\n" +
                        "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                        "      metadata color picker\n" +
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
                        "      (mdi-border-top-variant) \"Top\" id=test-border-top--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-left-variant) \"Left\" id=test-border-left--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-right-variant) \"Right\" id=test-border-right--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-all-variant) \"All\" id=test-border-all--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                        "  (mdi-format-text) \"Format\" id=test-format--SubMenu\n" +
                        "    \"Date\" id=test-format-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date] id=test-format-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-format-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time] id=test-format-edit-MenuItem\n" +
                        "    \"Number\" id=test-format-number-SubMenu\n" +
                        "      \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General] id=test-format-number-general-MenuItem\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-format-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230] id=test-format-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25] id=test-format-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00] id=test-format-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number] id=test-format-edit-MenuItem\n" +
                        "    \"Text\" id=test-format-text-SubMenu\n" +
                        "      \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@] id=test-format-text-default-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text] id=test-format-edit-MenuItem\n" +
                        "    \"Time\" id=test-format-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM] id=test-format-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time] id=test-format-edit-MenuItem\n" +
                        "  (mdi-format-text) \"Parse\" id=test-parse--SubMenu\n" +
                        "    \"Date\" id=test-parse-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date] id=test-parse-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-parse-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time] id=test-parse-edit-MenuItem\n" +
                        "    \"Number\" id=test-parse-number-SubMenu\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-parse-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230] id=test-parse-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25] id=test-parse-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00] id=test-parse-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number] id=test-parse-edit-MenuItem\n" +
                        "    \"Time\" id=test-parse-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM] id=test-parse-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time] id=test-parse-edit-MenuItem\n" +
                        "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true] id=test-hideIfZero-MenuItem\n" +
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
                        "    \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                        "    \"Create...\" [/1/SpreadsheetName-1/label] id=test-label-create-MenuItem\n"
        );
    }

    @Test
    public void testCellRecentFormatPatternsPatterns() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
                token,
                Lists.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                ),
                Lists.empty()
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
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern] id=test-clipboard-cut-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern] id=test-clipboard-cut-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                        "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern] id=test-clipboard-copy-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern] id=test-clipboard-copy-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                        "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern] id=test-clipboard-paste-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern] id=test-clipboard-paste-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
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
                        "      metadata color picker\n" +
                        "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                        "      metadata color picker\n" +
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
                        "      (mdi-border-top-variant) \"Top\" id=test-border-top--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-left-variant) \"Left\" id=test-border-left--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-right-variant) \"Right\" id=test-border-right--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-all-variant) \"All\" id=test-border-all--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                        "  (mdi-format-text) \"Format\" id=test-format--SubMenu\n" +
                        "    \"Date\" id=test-format-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date] id=test-format-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-format-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time] id=test-format-edit-MenuItem\n" +
                        "    \"Number\" id=test-format-number-SubMenu\n" +
                        "      \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General] id=test-format-number-general-MenuItem\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-format-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230] id=test-format-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25] id=test-format-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00] id=test-format-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number] id=test-format-edit-MenuItem\n" +
                        "    \"Text\" id=test-format-text-SubMenu\n" +
                        "      \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@] id=test-format-text-default-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text] id=test-format-edit-MenuItem\n" +
                        "    \"Time\" id=test-format-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM] id=test-format-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time] id=test-format-edit-MenuItem\n" +
                        "    -----\n" +
                        "    \"dd/mm/yyyy\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=test-format-recent-0-MenuItem\n" +
                        "  (mdi-format-text) \"Parse\" id=test-parse--SubMenu\n" +
                        "    \"Date\" id=test-parse-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date] id=test-parse-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-parse-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time] id=test-parse-edit-MenuItem\n" +
                        "    \"Number\" id=test-parse-number-SubMenu\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-parse-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230] id=test-parse-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25] id=test-parse-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00] id=test-parse-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number] id=test-parse-edit-MenuItem\n" +
                        "    \"Time\" id=test-parse-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM] id=test-parse-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time] id=test-parse-edit-MenuItem\n" +
                        "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true] id=test-hideIfZero-MenuItem\n" +
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
                        "    \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                        "    \"Create...\" [/1/SpreadsheetName-1/label] id=test-label-create-MenuItem\n"
        );
    }

    @Test
    public void testCellRecentParsePatterns() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
                token,
                Lists.empty(),
                Lists.of(
                        SpreadsheetPattern.parseNumberParsePattern("$0.00")
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
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern] id=test-clipboard-cut-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern] id=test-clipboard-cut-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                        "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern] id=test-clipboard-copy-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern] id=test-clipboard-copy-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                        "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern] id=test-clipboard-paste-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern] id=test-clipboard-paste-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
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
                        "      metadata color picker\n" +
                        "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                        "      metadata color picker\n" +
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
                        "      (mdi-border-top-variant) \"Top\" id=test-border-top--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-left-variant) \"Left\" id=test-border-left--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-right-variant) \"Right\" id=test-border-right--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-all-variant) \"All\" id=test-border-all--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                        "  (mdi-format-text) \"Format\" id=test-format--SubMenu\n" +
                        "    \"Date\" id=test-format-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date] id=test-format-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-format-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time] id=test-format-edit-MenuItem\n" +
                        "    \"Number\" id=test-format-number-SubMenu\n" +
                        "      \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General] id=test-format-number-general-MenuItem\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-format-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230] id=test-format-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25] id=test-format-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00] id=test-format-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number] id=test-format-edit-MenuItem\n" +
                        "    \"Text\" id=test-format-text-SubMenu\n" +
                        "      \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@] id=test-format-text-default-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text] id=test-format-edit-MenuItem\n" +
                        "    \"Time\" id=test-format-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM] id=test-format-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time] id=test-format-edit-MenuItem\n" +
                        "  (mdi-format-text) \"Parse\" id=test-parse--SubMenu\n" +
                        "    \"Date\" id=test-parse-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date] id=test-parse-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-parse-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time] id=test-parse-edit-MenuItem\n" +
                        "    \"Number\" id=test-parse-number-SubMenu\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-parse-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230] id=test-parse-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25] id=test-parse-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00] id=test-parse-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number] id=test-parse-edit-MenuItem\n" +
                        "    \"Time\" id=test-parse-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM] id=test-parse-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time] id=test-parse-edit-MenuItem\n" +
                        "    -----\n" +
                        "    \"$0.00\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%240.00] id=test-parse-recent-0-MenuItem\n" +
                        "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true] id=test-hideIfZero-MenuItem\n" +
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
                        "    \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                        "    \"Create...\" [/1/SpreadsheetName-1/label] id=test-label-create-MenuItem\n"
        );
    }

    @Test
    public void testCellRecentFormatPatternsAndRecentParsePatterns() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
                token,
                Lists.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                ),
                Lists.of(
                        SpreadsheetPattern.parseNumberParsePattern("$0.00")
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
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern] id=test-clipboard-cut-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern] id=test-clipboard-cut-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                        "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern] id=test-clipboard-copy-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern] id=test-clipboard-copy-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                        "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern] id=test-clipboard-paste-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern] id=test-clipboard-paste-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
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
                        "      metadata color picker\n" +
                        "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                        "      metadata color picker\n" +
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
                        "      (mdi-border-top-variant) \"Top\" id=test-border-top--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-left-variant) \"Left\" id=test-border-left--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-right-variant) \"Right\" id=test-border-right--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-all-variant) \"All\" id=test-border-all--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/] id=test-clear-style-MenuItem\n" +
                        "  (mdi-format-text) \"Format\" id=test-format--SubMenu\n" +
                        "    \"Date\" id=test-format-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date] id=test-format-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-format-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy] id=test-format-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy] id=test-format-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy] id=test-format-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time] id=test-format-edit-MenuItem\n" +
                        "    \"Number\" id=test-format-number-SubMenu\n" +
                        "      \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General] id=test-format-number-general-MenuItem\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-format-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230] id=test-format-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25] id=test-format-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00] id=test-format-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number] id=test-format-edit-MenuItem\n" +
                        "    \"Text\" id=test-format-text-SubMenu\n" +
                        "      \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@] id=test-format-text-default-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text] id=test-format-edit-MenuItem\n" +
                        "    \"Time\" id=test-format-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM] id=test-format-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time] id=test-format-edit-MenuItem\n" +
                        "    -----\n" +
                        "    \"dd/mm/yyyy\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=test-format-recent-0-MenuItem\n" +
                        "  (mdi-format-text) \"Parse\" id=test-parse--SubMenu\n" +
                        "    \"Date\" id=test-parse-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date] id=test-parse-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-parse-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy] id=test-parse-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time] id=test-parse-edit-MenuItem\n" +
                        "    \"Number\" id=test-parse-number-SubMenu\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-parse-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230] id=test-parse-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25] id=test-parse-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00] id=test-parse-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number] id=test-parse-edit-MenuItem\n" +
                        "    \"Time\" id=test-parse-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM] id=test-parse-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time] id=test-parse-edit-MenuItem\n" +
                        "    -----\n" +
                        "    \"$0.00\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%240.00] id=test-parse-recent-0-MenuItem\n" +
                        "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true] id=test-hideIfZero-MenuItem\n" +
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
                        "    \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                        "    \"Create...\" [/1/SpreadsheetName-1/label] id=test-label-create-MenuItem\n"
        );
    }

    @Test
    public void testCellRange() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.parseCellRange("B2:C3")
                        .setDefaultAnchor()
        );
        final SpreadsheetSelectionMenuContext context = this.context(
                token,
                Lists.empty(), // no recent format patterns
                Lists.empty() // no recent parse patterns
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
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/format-pattern] id=test-clipboard-cut-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/parse-pattern] id=test-clipboard-cut-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/style] id=test-clipboard-cut-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formatted-value] id=test-clipboard-cut-formatted-value-MenuItem\n" +
                        "  (mdi-content-copy) \"Copy\" id=test-clipboard-copy-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/cell] id=test-clipboard-copy-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formula] id=test-clipboard-copy-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/format-pattern] id=test-clipboard-copy-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/parse-pattern] id=test-clipboard-copy-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/style] id=test-clipboard-copy-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formatted-value] id=test-clipboard-copy-formatted-value-MenuItem\n" +
                        "  (mdi-content-paste) \"Paste\" id=test-clipboard-paste-SubMenu\n" +
                        "    \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/cell] id=test-clipboard-paste-cell-MenuItem\n" +
                        "    \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formula] id=test-clipboard-paste-formula-MenuItem\n" +
                        "    \"Format Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/format-pattern] id=test-clipboard-paste-format-pattern-MenuItem\n" +
                        "    \"Parse Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/parse-pattern] id=test-clipboard-paste-parse-pattern-MenuItem\n" +
                        "    \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/style] id=test-clipboard-paste-style-MenuItem\n" +
                        "    \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formatted-value] id=test-clipboard-paste-formatted-value-MenuItem\n" +
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
                        "      metadata color picker\n" +
                        "    (mdi-palette) \"Background color\" id=test-background-color-SubMenu\n" +
                        "      metadata color picker\n" +
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
                        "      (mdi-border-top-variant) \"Top\" id=test-border-top--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-top-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-left-variant) \"Left\" id=test-border-left--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-left-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-right-variant) \"Right\" id=test-border-right--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-right-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-bottom-variant) \"Bottom\" id=test-border-bottom--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-bottom-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "      (mdi-border-all-variant) \"All\" id=test-border-all--SubMenu\n" +
                        "        (mdi-palette) \"Color\" id=test-test-border-all-color-SubMenu\n" +
                        "          metadata color picker\n" +
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
                        "    (mdi-format-clear) \"Clear style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/*/save/] id=test-clear-style-MenuItem\n" +
                        "  (mdi-format-text) \"Format\" id=test-format--SubMenu\n" +
                        "    \"Date\" id=test-format-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d/m/yy] id=test-format-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmm+yyyy] id=test-format-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmmm+yyyy] id=test-format-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date] id=test-format-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-format-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d/m/yy] id=test-format-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmm+yyyy] id=test-format-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmmm+yyyy] id=test-format-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-format-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date-time] id=test-format-edit-MenuItem\n" +
                        "    \"Number\" id=test-format-number-SubMenu\n" +
                        "      \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/General] id=test-format-number-general-MenuItem\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-format-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%23%2C%23%230] id=test-format-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%23%2C%23%230%25] id=test-format-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%24%23%2C%23%230.00] id=test-format-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number] id=test-format-edit-MenuItem\n" +
                        "    \"Text\" id=test-format-text-SubMenu\n" +
                        "      \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/text/save/@] id=test-format-text-default-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/text] id=test-format-edit-MenuItem\n" +
                        "    \"Time\" id=test-format-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm+AM/PM] id=test-format-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm:ss+AM/PM] id=test-format-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time] id=test-format-edit-MenuItem\n" +
                        "  (mdi-format-text) \"Parse\" id=test-parse--SubMenu\n" +
                        "    \"Date\" id=test-parse-date-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d/m/yy] id=test-parse-date-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-date-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-date-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-date-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date] id=test-parse-edit-MenuItem\n" +
                        "    \"Date Time\" id=test-parse-datetime-SubMenu\n" +
                        "      \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d/m/yy] id=test-parse-datetime-short-MenuItem\n" +
                        "      \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmm+yyyy] id=test-parse-datetime-medium-MenuItem\n" +
                        "      \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmmm+yyyy] id=test-parse-datetime-long-MenuItem\n" +
                        "      \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy] id=test-parse-datetime-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date-time] id=test-parse-edit-MenuItem\n" +
                        "    \"Number\" id=test-parse-number-SubMenu\n" +
                        "      \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%23%2C%23%230.%23%23%23] id=test-parse-number-number-MenuItem\n" +
                        "      \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%23%2C%23%230] id=test-parse-number-integer-MenuItem\n" +
                        "      \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%23%2C%23%230%25] id=test-parse-number-percent-MenuItem\n" +
                        "      \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%24%23%2C%23%230.00] id=test-parse-number-currency-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number] id=test-parse-edit-MenuItem\n" +
                        "    \"Time\" id=test-parse-time-SubMenu\n" +
                        "      \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm+AM/PM] id=test-parse-time-short-MenuItem\n" +
                        "      \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-medium-MenuItem\n" +
                        "      \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-long-MenuItem\n" +
                        "      \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm:ss+AM/PM] id=test-parse-time-full-MenuItem\n" +
                        "      \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time] id=test-parse-edit-MenuItem\n" +
                        "  (mdi-star) \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true] id=test-hideIfZero-MenuItem\n" +
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
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Ddate+UP] id=test-column-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Ddate+DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                        "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Ddate-time+UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Ddate-time+DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                        "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dday-of-month+UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dday-of-month+DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                        "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dday-of-week+UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dday-of-week+DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                        "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dhour-of-am-pm+UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dhour-of-am-pm+DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dhour-of-day+UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dhour-of-day+DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dminute-of-hour+UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dminute-of-hour+DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dmonth-of-year+UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dmonth-of-year+DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                        "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dnano-of-second+UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dnano-of-second+DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dnumber+UP] id=test-column-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dnumber+DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                        "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dseconds-of-minute+UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dseconds-of-minute+DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dtext+UP] id=test-column-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dtext+DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                        "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dtext-case-insensitive+UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dtext-case-insensitive+DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dtime+UP] id=test-column-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dtime+DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                        "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dyear+UP] id=test-column-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/B%3Dyear+DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/edit/B%3D] id=test-column-sort-edit-MenuItem\n" +
                        "  (mdi-sort) \"Sort Row\" id=test-row-sort-SubMenu\n" +
                        "    \"Date\" id=test-row-sort-date-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Ddate+UP] id=test-row-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Ddate+DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                        "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Ddate-time+UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Ddate-time+DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                        "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dday-of-month+UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dday-of-month+DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                        "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dday-of-week+UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dday-of-week+DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                        "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dhour-of-am-pm+UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dhour-of-am-pm+DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dhour-of-day+UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dhour-of-day+DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dminute-of-hour+UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dminute-of-hour+DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dmonth-of-year+UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dmonth-of-year+DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                        "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dnano-of-second+UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dnano-of-second+DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dnumber+UP] id=test-row-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dnumber+DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                        "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dseconds-of-minute+UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dseconds-of-minute+DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dtext+UP] id=test-row-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dtext+DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                        "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dtext-case-insensitive+UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dtext-case-insensitive+DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dtime+UP] id=test-row-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dtime+DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                        "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dyear+UP] id=test-row-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/save/2%3Dyear+DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/sort/edit/2%3D] id=test-row-sort-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n" +
                        "  -----\n" +
                        "  \"Labels\" [1] id=test-label-SubMenu\n" +
                        "    \"Label123 (B2)\" [/1/SpreadsheetName-1/label/Label123] id=test-label-0-MenuItem\n" +
                        "    \"Create...\" [/1/SpreadsheetName-1/label] id=test-label-create-MenuItem\n"
        );
    }

    @Test
    public void testColumn() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.column(
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
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testColumnRangeAnchorLeft() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.column(
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
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Ddate+UP] id=test-column-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Ddate+DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                        "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Ddate-time+UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Ddate-time+DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                        "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dday-of-month+UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dday-of-month+DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                        "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dday-of-week+UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dday-of-week+DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                        "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dhour-of-am-pm+UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dhour-of-am-pm+DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dhour-of-day+UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dhour-of-day+DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dminute-of-hour+UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dminute-of-hour+DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dmonth-of-year+UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dmonth-of-year+DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                        "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dnano-of-second+UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dnano-of-second+DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dnumber+UP] id=test-column-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dnumber+DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                        "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dseconds-of-minute+UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dseconds-of-minute+DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dtext+UP] id=test-column-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dtext+DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                        "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dtext-case-insensitive+UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dtext-case-insensitive+DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dtime+UP] id=test-column-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dtime+DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                        "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dyear+UP] id=test-column-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/left/sort/save/B%3Dyear+DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/column/B:C/left/sort/edit/B%3D] id=test-column-sort-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testColumnRangeAnchorRight() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.column(
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
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Ddate+UP] id=test-column-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Ddate+DOWN] id=test-column-sort-date-DOWN-MenuItem\n" +
                        "    \"Date Time\" id=test-column-sort-date-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Ddate-time+UP] id=test-column-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Ddate-time+DOWN] id=test-column-sort-date-time-DOWN-MenuItem\n" +
                        "    \"Day Of Month\" id=test-column-sort-day-of-month-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dday-of-month+UP] id=test-column-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dday-of-month+DOWN] id=test-column-sort-day-of-month-DOWN-MenuItem\n" +
                        "    \"Day Of Week\" id=test-column-sort-day-of-week-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dday-of-week+UP] id=test-column-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dday-of-week+DOWN] id=test-column-sort-day-of-week-DOWN-MenuItem\n" +
                        "    \"Hour Of Am Pm\" id=test-column-sort-hour-of-am-pm-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dhour-of-am-pm+UP] id=test-column-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dhour-of-am-pm+DOWN] id=test-column-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    \"Hour Of Day\" id=test-column-sort-hour-of-day-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dhour-of-day+UP] id=test-column-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dhour-of-day+DOWN] id=test-column-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    \"Minute Of Hour\" id=test-column-sort-minute-of-hour-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dminute-of-hour+UP] id=test-column-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dminute-of-hour+DOWN] id=test-column-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    \"Month Of Year\" id=test-column-sort-month-of-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dmonth-of-year+UP] id=test-column-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dmonth-of-year+DOWN] id=test-column-sort-month-of-year-DOWN-MenuItem\n" +
                        "    \"Nano Of Second\" id=test-column-sort-nano-of-second-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dnano-of-second+UP] id=test-column-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dnano-of-second+DOWN] id=test-column-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    \"Number\" id=test-column-sort-number-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dnumber+UP] id=test-column-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dnumber+DOWN] id=test-column-sort-number-DOWN-MenuItem\n" +
                        "    \"Seconds Of Minute\" id=test-column-sort-seconds-of-minute-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dseconds-of-minute+UP] id=test-column-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dseconds-of-minute+DOWN] id=test-column-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    \"Text\" id=test-column-sort-text-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dtext+UP] id=test-column-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dtext+DOWN] id=test-column-sort-text-DOWN-MenuItem\n" +
                        "    \"Text Case Insensitive\" id=test-column-sort-text-case-insensitive-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dtext-case-insensitive+UP] id=test-column-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dtext-case-insensitive+DOWN] id=test-column-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    \"Time\" id=test-column-sort-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dtime+UP] id=test-column-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dtime+DOWN] id=test-column-sort-time-DOWN-MenuItem\n" +
                        "    \"Year\" id=test-column-sort-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dyear+UP] id=test-column-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/column/B:C/right/sort/save/B%3Dyear+DOWN] id=test-column-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/column/B:C/right/sort/edit/B%3D] id=test-column-sort-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testRow() {
        final SpreadsheetRowHistoryToken token = HistoryToken.row(
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
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testRowRangeAnchorTop() {
        final SpreadsheetRowHistoryToken token = HistoryToken.row(
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
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Ddate+UP] id=test-row-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Ddate+DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                        "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Ddate-time+UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Ddate-time+DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                        "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dday-of-month+UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dday-of-month+DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                        "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dday-of-week+UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dday-of-week+DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                        "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dhour-of-am-pm+UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dhour-of-am-pm+DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dhour-of-day+UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dhour-of-day+DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dminute-of-hour+UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dminute-of-hour+DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dmonth-of-year+UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dmonth-of-year+DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                        "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dnano-of-second+UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dnano-of-second+DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dnumber+UP] id=test-row-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dnumber+DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                        "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dseconds-of-minute+UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dseconds-of-minute+DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dtext+UP] id=test-row-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dtext+DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                        "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dtext-case-insensitive+UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dtext-case-insensitive+DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dtime+UP] id=test-row-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dtime+DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                        "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dyear+UP] id=test-row-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/top/sort/save/3%3Dyear+DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/row/3:4/top/sort/edit/3%3D] id=test-row-sort-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    @Test
    public void testRowRangeAnchorBottom() {
        final SpreadsheetRowHistoryToken token = HistoryToken.row(
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
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Ddate+UP] id=test-row-sort-date-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Ddate+DOWN] id=test-row-sort-date-DOWN-MenuItem\n" +
                        "    \"Date Time\" id=test-row-sort-date-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Ddate-time+UP] id=test-row-sort-date-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Ddate-time+DOWN] id=test-row-sort-date-time-DOWN-MenuItem\n" +
                        "    \"Day Of Month\" id=test-row-sort-day-of-month-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dday-of-month+UP] id=test-row-sort-day-of-month-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dday-of-month+DOWN] id=test-row-sort-day-of-month-DOWN-MenuItem\n" +
                        "    \"Day Of Week\" id=test-row-sort-day-of-week-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dday-of-week+UP] id=test-row-sort-day-of-week-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dday-of-week+DOWN] id=test-row-sort-day-of-week-DOWN-MenuItem\n" +
                        "    \"Hour Of Am Pm\" id=test-row-sort-hour-of-am-pm-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dhour-of-am-pm+UP] id=test-row-sort-hour-of-am-pm-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dhour-of-am-pm+DOWN] id=test-row-sort-hour-of-am-pm-DOWN-MenuItem\n" +
                        "    \"Hour Of Day\" id=test-row-sort-hour-of-day-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dhour-of-day+UP] id=test-row-sort-hour-of-day-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dhour-of-day+DOWN] id=test-row-sort-hour-of-day-DOWN-MenuItem\n" +
                        "    \"Minute Of Hour\" id=test-row-sort-minute-of-hour-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dminute-of-hour+UP] id=test-row-sort-minute-of-hour-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dminute-of-hour+DOWN] id=test-row-sort-minute-of-hour-DOWN-MenuItem\n" +
                        "    \"Month Of Year\" id=test-row-sort-month-of-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dmonth-of-year+UP] id=test-row-sort-month-of-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dmonth-of-year+DOWN] id=test-row-sort-month-of-year-DOWN-MenuItem\n" +
                        "    \"Nano Of Second\" id=test-row-sort-nano-of-second-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dnano-of-second+UP] id=test-row-sort-nano-of-second-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dnano-of-second+DOWN] id=test-row-sort-nano-of-second-DOWN-MenuItem\n" +
                        "    \"Number\" id=test-row-sort-number-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dnumber+UP] id=test-row-sort-number-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dnumber+DOWN] id=test-row-sort-number-DOWN-MenuItem\n" +
                        "    \"Seconds Of Minute\" id=test-row-sort-seconds-of-minute-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dseconds-of-minute+UP] id=test-row-sort-seconds-of-minute-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dseconds-of-minute+DOWN] id=test-row-sort-seconds-of-minute-DOWN-MenuItem\n" +
                        "    \"Text\" id=test-row-sort-text-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dtext+UP] id=test-row-sort-text-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dtext+DOWN] id=test-row-sort-text-DOWN-MenuItem\n" +
                        "    \"Text Case Insensitive\" id=test-row-sort-text-case-insensitive-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dtext-case-insensitive+UP] id=test-row-sort-text-case-insensitive-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dtext-case-insensitive+DOWN] id=test-row-sort-text-case-insensitive-DOWN-MenuItem\n" +
                        "    \"Time\" id=test-row-sort-time-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dtime+UP] id=test-row-sort-time-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dtime+DOWN] id=test-row-sort-time-DOWN-MenuItem\n" +
                        "    \"Year\" id=test-row-sort-year-SubMenu\n" +
                        "      \"Up\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dyear+UP] id=test-row-sort-year-UP-MenuItem\n" +
                        "      \"Down\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/save/3%3Dyear+DOWN] id=test-row-sort-year-DOWN-MenuItem\n" +
                        "    -----\n" +
                        "    \"Edit\" [/1/SpreadsheetName-1/row/3:4/bottom/sort/edit/3%3D] id=test-row-sort-edit-MenuItem\n" +
                        "  -----\n" +
                        "  \"Freeze\" id=test-freeze-MenuItem\n" +
                        "  \"Unfreeze\" id=test-unfreeze-MenuItem\n"
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken) {
        return this.context(
                historyToken,
                Lists.empty(), // format patterns
                Lists.empty() // parse patterns
        );
    }

    private SpreadsheetSelectionMenuContext context(final HistoryToken historyToken,
                                                    final List<SpreadsheetFormatPattern> formatPatterns,
                                                    final List<SpreadsheetParsePattern> parsePatterns) {
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
            public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
                return Sets.of(
                        SpreadsheetSelection.labelName("Label123")
                                .mapping(selection.toCell())
                );
            }

            @Override
            public List<HistoryToken> recentFormatPatterns() {
                return formatPatterns.stream()
                        .map(historyToken::setPattern)
                        .collect(Collectors.toList());
            }

            @Override
            public List<HistoryToken> recentParsePatterns() {
                return parsePatterns.stream()
                        .map(historyToken::setPattern)
                        .collect(Collectors.toList());
            }

            @Override
            public SpreadsheetSelectionSummary selectionSummary() {
                return SpreadsheetSelectionSummary.EMPTY;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadata.EMPTY.set(
                        SpreadsheetMetadataPropertyName.LOCALE,
                        Locale.forLanguageTag("EN-AU")
                ).loadFromLocale();
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
