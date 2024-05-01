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
                "Cell-MenuId \"Cell A1 Menu\"\n" +
                        "  (mdi-content-cut) test-clipboard-cut-SubMenu \"Cut\"\n" +
                        "    test-clipboard-cut-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell]\n" +
                        "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +
                        "    test-clipboard-cut-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern]\n" +
                        "    test-clipboard-cut-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern]\n" +
                        "    test-clipboard-cut-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style]\n" +
                        "    test-clipboard-cut-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value]\n" +
                        "  (mdi-content-copy) test-clipboard-copy-SubMenu \"Copy\"\n" +
                        "    test-clipboard-copy-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell]\n" +
                        "    test-clipboard-copy-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula]\n" +
                        "    test-clipboard-copy-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern]\n" +
                        "    test-clipboard-copy-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern]\n" +
                        "    test-clipboard-copy-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style]\n" +
                        "    test-clipboard-copy-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value]\n" +
                        "  (mdi-content-paste) test-clipboard-paste-SubMenu \"Paste\"\n" +
                        "    test-clipboard-paste-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell]\n" +
                        "    test-clipboard-paste-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula]\n" +
                        "    test-clipboard-paste-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern]\n" +
                        "    test-clipboard-paste-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern]\n" +
                        "    test-clipboard-paste-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style]\n" +
                        "    test-clipboard-paste-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value]\n" +
                        "  -----\n" +
                        "  test-style-SubMenu \"Style\"\n" +
                        "    test-alignment-SubMenu \"Alignment\"\n" +
                        "      (mdi-format-align-left) test-left-MenuItem key=L \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT]\n" +
                        "      (mdi-format-align-center) test-center-MenuItem key=C \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER]\n" +
                        "      (mdi-format-align-right) test-right-MenuItem key=R \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT]\n" +
                        "      (mdi-format-align-justify) test-justify-MenuItem key=J \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY]\n" +
                        "    test-vertical-alignment-SubMenu \"Vertical Alignment\"\n" +
                        "      (mdi-format-align-top) test-top-MenuItem \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP]\n" +
                        "      (mdi-format-align-middle) test-middle-MenuItem \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE]\n" +
                        "      (mdi-format-align-bottom) test-bottom-MenuItem \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM]\n" +
                        "    (mdi-palette) test-color-SubMenu \"Color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-palette) test-background-color-SubMenu \"Background color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-format-bold) test-bold-MenuItem key=b \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold]\n" +
                        "    (mdi-format-italic) test-italics-MenuItem key=i \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC]\n" +
                        "    (mdi-format-strikethrough) test-strike-thru-MenuItem key=s \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH]\n" +
                        "    (mdi-format-underline) test-underline-MenuItem key=u \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE]\n" +
                        "    test-text-case-SubMenu \"Text case\"\n" +
                        "      (mdi-format-letter-case-upper) test-normal-MenuItem \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/]\n" +
                        "      (mdi-format-letter-case) test-capitalize-MenuItem \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE]\n" +
                        "      (mdi-format-letter-case-lower) test-lower-MenuItem \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE]\n" +
                        "      (mdi-format-letter-case-upper) test-upper-MenuItem \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE]\n" +
                        "    test-text-wrapping-SubMenu \"Wrapping\"\n" +
                        "      (mdi-format-text-wrapping-clip) test-clip-MenuItem \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL]\n" +
                        "      (mdi-format-text-wrapping-overflow) test-overflow-MenuItem \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE]\n" +
                        "      (mdi-format-text-wrapping-wrap) test-wrap-MenuItem \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN]\n" +
                        "    test-border--SubMenu \"Border\"\n" +
                        "      (mdi-border-top-variant) test-border-top--SubMenu \"Top\"\n" +
                        "        (mdi-palette) test-test-border-top-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-top-style-SubMenu \"Style\"\n" +
                        "          test-border-top-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE]\n" +
                        "          test-border-top-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED]\n" +
                        "          test-border-top-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED]\n" +
                        "          test-border-top-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-top-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/]\n" +
                        "        test-border-top-width-SubMenu \"Width\"\n" +
                        "          test-border-top-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px]\n" +
                        "          test-border-top-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px]\n" +
                        "          test-border-top-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px]\n" +
                        "          test-border-top-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px]\n" +
                        "          test-border-top-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-top-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/]\n" +
                        "      (mdi-border-left-variant) test-border-left--SubMenu \"Left\"\n" +
                        "        (mdi-palette) test-test-border-left-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-left-style-SubMenu \"Style\"\n" +
                        "          test-border-left-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE]\n" +
                        "          test-border-left-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED]\n" +
                        "          test-border-left-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED]\n" +
                        "          test-border-left-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-left-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/]\n" +
                        "        test-border-left-width-SubMenu \"Width\"\n" +
                        "          test-border-left-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px]\n" +
                        "          test-border-left-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px]\n" +
                        "          test-border-left-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px]\n" +
                        "          test-border-left-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px]\n" +
                        "          test-border-left-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-left-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/]\n" +
                        "      (mdi-border-right-variant) test-border-right--SubMenu \"Right\"\n" +
                        "        (mdi-palette) test-test-border-right-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-right-style-SubMenu \"Style\"\n" +
                        "          test-border-right-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE]\n" +
                        "          test-border-right-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED]\n" +
                        "          test-border-right-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED]\n" +
                        "          test-border-right-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-right-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/]\n" +
                        "        test-border-right-width-SubMenu \"Width\"\n" +
                        "          test-border-right-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px]\n" +
                        "          test-border-right-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px]\n" +
                        "          test-border-right-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px]\n" +
                        "          test-border-right-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px]\n" +
                        "          test-border-right-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-right-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/]\n" +
                        "      (mdi-border-bottom-variant) test-border-bottom--SubMenu \"Bottom\"\n" +
                        "        (mdi-palette) test-test-border-bottom-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-bottom-style-SubMenu \"Style\"\n" +
                        "          test-border-bottom-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE]\n" +
                        "          test-border-bottom-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED]\n" +
                        "          test-border-bottom-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED]\n" +
                        "          test-border-bottom-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-bottom-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/]\n" +
                        "        test-border-bottom-width-SubMenu \"Width\"\n" +
                        "          test-border-bottom-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px]\n" +
                        "          test-border-bottom-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px]\n" +
                        "          test-border-bottom-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px]\n" +
                        "          test-border-bottom-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px]\n" +
                        "          test-border-bottom-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-bottom-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/]\n" +
                        "      (mdi-border-all-variant) test-border-all--SubMenu \"All\"\n" +
                        "        (mdi-palette) test-test-border-all-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-all-style-SubMenu \"Style\"\n" +
                        "          test-border-all-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE]\n" +
                        "          test-border-all-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED]\n" +
                        "          test-border-all-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED]\n" +
                        "          test-border-all-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-all-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/]\n" +
                        "        test-border-all-width-SubMenu \"Width\"\n" +
                        "          test-border-all-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px]\n" +
                        "          test-border-all-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px]\n" +
                        "          test-border-all-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px]\n" +
                        "          test-border-all-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px]\n" +
                        "          test-border-all-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-all-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/]\n" +
                        "    (mdi-format-clear) test-clear-style-MenuItem \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/]\n" +
                        "  (mdi-format-text) test-format--SubMenu \"Format\"\n" +
                        "    test-format-date-SubMenu \"Date\"\n" +
                        "      test-format-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date]\n" +
                        "    test-format-datetime-SubMenu \"Date Time\"\n" +
                        "      test-format-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time]\n" +
                        "    test-format-number-SubMenu \"Number\"\n" +
                        "      test-format-number-general-MenuItem \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General]\n" +
                        "      test-format-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-format-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-format-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-format-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number]\n" +
                        "    test-format-text-SubMenu \"Text\"\n" +
                        "      test-format-text-default-MenuItem \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text]\n" +
                        "    test-format-time-SubMenu \"Time\"\n" +
                        "      test-format-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-format-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time]\n" +
                        "  (mdi-format-text) test-parse--SubMenu \"Parse\"\n" +
                        "    test-parse-date-SubMenu \"Date\"\n" +
                        "      test-parse-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date]\n" +
                        "    test-parse-datetime-SubMenu \"Date Time\"\n" +
                        "      test-parse-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time]\n" +
                        "    test-parse-number-SubMenu \"Number\"\n" +
                        "      test-parse-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-parse-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-parse-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-parse-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number]\n" +
                        "    test-parse-time-SubMenu \"Time\"\n" +
                        "      test-parse-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-parse-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time]\n" +
                        "  (mdi-star) test-hideIfZero-MenuItem \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true]\n" +
                        "  -----\n" +
                        "  (mdi-close) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-column-plus-before) test-column-insert-before-SubMenu \"Insert before column\"\n" +
                        "    test-column-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1]\n" +
                        "    test-column-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2]\n" +
                        "    test-column-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3]\n" +
                        "    test-column-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-column-insert-after-SubMenu \"Insert after column\"\n" +
                        "    test-column-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1]\n" +
                        "    test-column-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2]\n" +
                        "    test-column-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3]\n" +
                        "    test-column-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertAfter]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-row-insert-before-SubMenu \"Insert before row\"\n" +
                        "    test-row-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1]\n" +
                        "    test-row-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2]\n" +
                        "    test-row-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3]\n" +
                        "    test-row-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-row-insert-after-SubMenu \"Insert after row\"\n" +
                        "    test-row-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1]\n" +
                        "    test-row-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2]\n" +
                        "    test-row-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3]\n" +
                        "    test-row-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\" [/1/SpreadsheetName-1/cell/A1/freeze]\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\" [/1/SpreadsheetName-1/cell/A1/unfreeze]\n" +
                        "  -----\n" +
                        "  test-label-SubMenu \"Labels\" [1]\n" +
                        "    test-label-0-MenuItem \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123]\n" +
                        "    test-label-create-MenuItem \"Create...\" [/1/SpreadsheetName-1/label]\n"
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
                "Cell-MenuId \"Cell A1 Menu\"\n" +
                        "  (mdi-content-cut) test-clipboard-cut-SubMenu \"Cut\"\n" +
                        "    test-clipboard-cut-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell]\n" +
                        "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +
                        "    test-clipboard-cut-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern]\n" +
                        "    test-clipboard-cut-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern]\n" +
                        "    test-clipboard-cut-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style]\n" +
                        "    test-clipboard-cut-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value]\n" +
                        "  (mdi-content-copy) test-clipboard-copy-SubMenu \"Copy\"\n" +
                        "    test-clipboard-copy-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell]\n" +
                        "    test-clipboard-copy-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula]\n" +
                        "    test-clipboard-copy-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern]\n" +
                        "    test-clipboard-copy-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern]\n" +
                        "    test-clipboard-copy-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style]\n" +
                        "    test-clipboard-copy-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value]\n" +
                        "  (mdi-content-paste) test-clipboard-paste-SubMenu \"Paste\"\n" +
                        "    test-clipboard-paste-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell]\n" +
                        "    test-clipboard-paste-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula]\n" +
                        "    test-clipboard-paste-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern]\n" +
                        "    test-clipboard-paste-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern]\n" +
                        "    test-clipboard-paste-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style]\n" +
                        "    test-clipboard-paste-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value]\n" +
                        "  -----\n" +
                        "  test-style-SubMenu \"Style\"\n" +
                        "    test-alignment-SubMenu \"Alignment\"\n" +
                        "      (mdi-format-align-left) test-left-MenuItem key=L \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT]\n" +
                        "      (mdi-format-align-center) test-center-MenuItem key=C \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER]\n" +
                        "      (mdi-format-align-right) test-right-MenuItem key=R \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT]\n" +
                        "      (mdi-format-align-justify) test-justify-MenuItem key=J \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY]\n" +
                        "    test-vertical-alignment-SubMenu \"Vertical Alignment\"\n" +
                        "      (mdi-format-align-top) test-top-MenuItem \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP]\n" +
                        "      (mdi-format-align-middle) test-middle-MenuItem \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE]\n" +
                        "      (mdi-format-align-bottom) test-bottom-MenuItem \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM]\n" +
                        "    (mdi-palette) test-color-SubMenu \"Color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-palette) test-background-color-SubMenu \"Background color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-format-bold) test-bold-MenuItem key=b \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold]\n" +
                        "    (mdi-format-italic) test-italics-MenuItem key=i \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC]\n" +
                        "    (mdi-format-strikethrough) test-strike-thru-MenuItem key=s \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH]\n" +
                        "    (mdi-format-underline) test-underline-MenuItem key=u \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE]\n" +
                        "    test-text-case-SubMenu \"Text case\"\n" +
                        "      (mdi-format-letter-case-upper) test-normal-MenuItem \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/]\n" +
                        "      (mdi-format-letter-case) test-capitalize-MenuItem \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE]\n" +
                        "      (mdi-format-letter-case-lower) test-lower-MenuItem \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE]\n" +
                        "      (mdi-format-letter-case-upper) test-upper-MenuItem \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE]\n" +
                        "    test-text-wrapping-SubMenu \"Wrapping\"\n" +
                        "      (mdi-format-text-wrapping-clip) test-clip-MenuItem \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL]\n" +
                        "      (mdi-format-text-wrapping-overflow) test-overflow-MenuItem \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE]\n" +
                        "      (mdi-format-text-wrapping-wrap) test-wrap-MenuItem \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN]\n" +
                        "    test-border--SubMenu \"Border\"\n" +
                        "      (mdi-border-top-variant) test-border-top--SubMenu \"Top\"\n" +
                        "        (mdi-palette) test-test-border-top-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-top-style-SubMenu \"Style\"\n" +
                        "          test-border-top-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE]\n" +
                        "          test-border-top-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED]\n" +
                        "          test-border-top-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED]\n" +
                        "          test-border-top-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-top-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/]\n" +
                        "        test-border-top-width-SubMenu \"Width\"\n" +
                        "          test-border-top-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px]\n" +
                        "          test-border-top-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px]\n" +
                        "          test-border-top-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px]\n" +
                        "          test-border-top-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px]\n" +
                        "          test-border-top-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-top-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/]\n" +
                        "      (mdi-border-left-variant) test-border-left--SubMenu \"Left\"\n" +
                        "        (mdi-palette) test-test-border-left-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-left-style-SubMenu \"Style\"\n" +
                        "          test-border-left-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE]\n" +
                        "          test-border-left-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED]\n" +
                        "          test-border-left-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED]\n" +
                        "          test-border-left-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-left-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/]\n" +
                        "        test-border-left-width-SubMenu \"Width\"\n" +
                        "          test-border-left-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px]\n" +
                        "          test-border-left-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px]\n" +
                        "          test-border-left-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px]\n" +
                        "          test-border-left-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px]\n" +
                        "          test-border-left-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-left-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/]\n" +
                        "      (mdi-border-right-variant) test-border-right--SubMenu \"Right\"\n" +
                        "        (mdi-palette) test-test-border-right-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-right-style-SubMenu \"Style\"\n" +
                        "          test-border-right-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE]\n" +
                        "          test-border-right-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED]\n" +
                        "          test-border-right-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED]\n" +
                        "          test-border-right-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-right-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/]\n" +
                        "        test-border-right-width-SubMenu \"Width\"\n" +
                        "          test-border-right-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px]\n" +
                        "          test-border-right-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px]\n" +
                        "          test-border-right-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px]\n" +
                        "          test-border-right-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px]\n" +
                        "          test-border-right-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-right-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/]\n" +
                        "      (mdi-border-bottom-variant) test-border-bottom--SubMenu \"Bottom\"\n" +
                        "        (mdi-palette) test-test-border-bottom-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-bottom-style-SubMenu \"Style\"\n" +
                        "          test-border-bottom-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE]\n" +
                        "          test-border-bottom-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED]\n" +
                        "          test-border-bottom-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED]\n" +
                        "          test-border-bottom-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-bottom-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/]\n" +
                        "        test-border-bottom-width-SubMenu \"Width\"\n" +
                        "          test-border-bottom-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px]\n" +
                        "          test-border-bottom-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px]\n" +
                        "          test-border-bottom-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px]\n" +
                        "          test-border-bottom-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px]\n" +
                        "          test-border-bottom-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-bottom-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/]\n" +
                        "      (mdi-border-all-variant) test-border-all--SubMenu \"All\"\n" +
                        "        (mdi-palette) test-test-border-all-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-all-style-SubMenu \"Style\"\n" +
                        "          test-border-all-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE]\n" +
                        "          test-border-all-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED]\n" +
                        "          test-border-all-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED]\n" +
                        "          test-border-all-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-all-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/]\n" +
                        "        test-border-all-width-SubMenu \"Width\"\n" +
                        "          test-border-all-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px]\n" +
                        "          test-border-all-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px]\n" +
                        "          test-border-all-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px]\n" +
                        "          test-border-all-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px]\n" +
                        "          test-border-all-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-all-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/]\n" +
                        "    (mdi-format-clear) test-clear-style-MenuItem \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/]\n" +
                        "  (mdi-format-text) test-format--SubMenu \"Format\"\n" +
                        "    test-format-date-SubMenu \"Date\"\n" +
                        "      test-format-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date]\n" +
                        "    test-format-datetime-SubMenu \"Date Time\"\n" +
                        "      test-format-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time]\n" +
                        "    test-format-number-SubMenu \"Number\"\n" +
                        "      test-format-number-general-MenuItem \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General]\n" +
                        "      test-format-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-format-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-format-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-format-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number]\n" +
                        "    test-format-text-SubMenu \"Text\"\n" +
                        "      test-format-text-default-MenuItem \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text]\n" +
                        "    test-format-time-SubMenu \"Time\"\n" +
                        "      test-format-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-format-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time]\n" +
                        "    -----\n" +
                        "    test-format-recent-0-MenuItem \"dd/mm/yyyy\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dd/mm/yyyy]\n" +
                        "  (mdi-format-text) test-parse--SubMenu \"Parse\"\n" +
                        "    test-parse-date-SubMenu \"Date\"\n" +
                        "      test-parse-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date]\n" +
                        "    test-parse-datetime-SubMenu \"Date Time\"\n" +
                        "      test-parse-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time]\n" +
                        "    test-parse-number-SubMenu \"Number\"\n" +
                        "      test-parse-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-parse-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-parse-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-parse-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number]\n" +
                        "    test-parse-time-SubMenu \"Time\"\n" +
                        "      test-parse-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-parse-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time]\n" +
                        "  (mdi-star) test-hideIfZero-MenuItem \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true]\n" +
                        "  -----\n" +
                        "  (mdi-close) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-column-plus-before) test-column-insert-before-SubMenu \"Insert before column\"\n" +
                        "    test-column-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1]\n" +
                        "    test-column-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2]\n" +
                        "    test-column-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3]\n" +
                        "    test-column-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-column-insert-after-SubMenu \"Insert after column\"\n" +
                        "    test-column-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1]\n" +
                        "    test-column-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2]\n" +
                        "    test-column-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3]\n" +
                        "    test-column-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertAfter]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-row-insert-before-SubMenu \"Insert before row\"\n" +
                        "    test-row-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1]\n" +
                        "    test-row-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2]\n" +
                        "    test-row-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3]\n" +
                        "    test-row-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-row-insert-after-SubMenu \"Insert after row\"\n" +
                        "    test-row-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1]\n" +
                        "    test-row-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2]\n" +
                        "    test-row-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3]\n" +
                        "    test-row-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\" [/1/SpreadsheetName-1/cell/A1/freeze]\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\" [/1/SpreadsheetName-1/cell/A1/unfreeze]\n" +
                        "  -----\n" +
                        "  test-label-SubMenu \"Labels\" [1]\n" +
                        "    test-label-0-MenuItem \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123]\n" +
                        "    test-label-create-MenuItem \"Create...\" [/1/SpreadsheetName-1/label]\n"
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
                "Cell-MenuId \"Cell A1 Menu\"\n" +
                        "  (mdi-content-cut) test-clipboard-cut-SubMenu \"Cut\"\n" +
                        "    test-clipboard-cut-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell]\n" +
                        "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +
                        "    test-clipboard-cut-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern]\n" +
                        "    test-clipboard-cut-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern]\n" +
                        "    test-clipboard-cut-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style]\n" +
                        "    test-clipboard-cut-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value]\n" +
                        "  (mdi-content-copy) test-clipboard-copy-SubMenu \"Copy\"\n" +
                        "    test-clipboard-copy-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell]\n" +
                        "    test-clipboard-copy-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula]\n" +
                        "    test-clipboard-copy-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern]\n" +
                        "    test-clipboard-copy-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern]\n" +
                        "    test-clipboard-copy-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style]\n" +
                        "    test-clipboard-copy-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value]\n" +
                        "  (mdi-content-paste) test-clipboard-paste-SubMenu \"Paste\"\n" +
                        "    test-clipboard-paste-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell]\n" +
                        "    test-clipboard-paste-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula]\n" +
                        "    test-clipboard-paste-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern]\n" +
                        "    test-clipboard-paste-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern]\n" +
                        "    test-clipboard-paste-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style]\n" +
                        "    test-clipboard-paste-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value]\n" +
                        "  -----\n" +
                        "  test-style-SubMenu \"Style\"\n" +
                        "    test-alignment-SubMenu \"Alignment\"\n" +
                        "      (mdi-format-align-left) test-left-MenuItem key=L \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT]\n" +
                        "      (mdi-format-align-center) test-center-MenuItem key=C \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER]\n" +
                        "      (mdi-format-align-right) test-right-MenuItem key=R \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT]\n" +
                        "      (mdi-format-align-justify) test-justify-MenuItem key=J \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY]\n" +
                        "    test-vertical-alignment-SubMenu \"Vertical Alignment\"\n" +
                        "      (mdi-format-align-top) test-top-MenuItem \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP]\n" +
                        "      (mdi-format-align-middle) test-middle-MenuItem \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE]\n" +
                        "      (mdi-format-align-bottom) test-bottom-MenuItem \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM]\n" +
                        "    (mdi-palette) test-color-SubMenu \"Color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-palette) test-background-color-SubMenu \"Background color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-format-bold) test-bold-MenuItem key=b \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold]\n" +
                        "    (mdi-format-italic) test-italics-MenuItem key=i \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC]\n" +
                        "    (mdi-format-strikethrough) test-strike-thru-MenuItem key=s \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH]\n" +
                        "    (mdi-format-underline) test-underline-MenuItem key=u \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE]\n" +
                        "    test-text-case-SubMenu \"Text case\"\n" +
                        "      (mdi-format-letter-case-upper) test-normal-MenuItem \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/]\n" +
                        "      (mdi-format-letter-case) test-capitalize-MenuItem \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE]\n" +
                        "      (mdi-format-letter-case-lower) test-lower-MenuItem \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE]\n" +
                        "      (mdi-format-letter-case-upper) test-upper-MenuItem \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE]\n" +
                        "    test-text-wrapping-SubMenu \"Wrapping\"\n" +
                        "      (mdi-format-text-wrapping-clip) test-clip-MenuItem \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL]\n" +
                        "      (mdi-format-text-wrapping-overflow) test-overflow-MenuItem \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE]\n" +
                        "      (mdi-format-text-wrapping-wrap) test-wrap-MenuItem \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN]\n" +
                        "    test-border--SubMenu \"Border\"\n" +
                        "      (mdi-border-top-variant) test-border-top--SubMenu \"Top\"\n" +
                        "        (mdi-palette) test-test-border-top-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-top-style-SubMenu \"Style\"\n" +
                        "          test-border-top-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE]\n" +
                        "          test-border-top-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED]\n" +
                        "          test-border-top-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED]\n" +
                        "          test-border-top-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-top-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/]\n" +
                        "        test-border-top-width-SubMenu \"Width\"\n" +
                        "          test-border-top-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px]\n" +
                        "          test-border-top-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px]\n" +
                        "          test-border-top-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px]\n" +
                        "          test-border-top-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px]\n" +
                        "          test-border-top-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-top-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/]\n" +
                        "      (mdi-border-left-variant) test-border-left--SubMenu \"Left\"\n" +
                        "        (mdi-palette) test-test-border-left-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-left-style-SubMenu \"Style\"\n" +
                        "          test-border-left-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE]\n" +
                        "          test-border-left-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED]\n" +
                        "          test-border-left-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED]\n" +
                        "          test-border-left-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-left-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/]\n" +
                        "        test-border-left-width-SubMenu \"Width\"\n" +
                        "          test-border-left-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px]\n" +
                        "          test-border-left-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px]\n" +
                        "          test-border-left-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px]\n" +
                        "          test-border-left-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px]\n" +
                        "          test-border-left-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-left-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/]\n" +
                        "      (mdi-border-right-variant) test-border-right--SubMenu \"Right\"\n" +
                        "        (mdi-palette) test-test-border-right-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-right-style-SubMenu \"Style\"\n" +
                        "          test-border-right-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE]\n" +
                        "          test-border-right-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED]\n" +
                        "          test-border-right-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED]\n" +
                        "          test-border-right-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-right-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/]\n" +
                        "        test-border-right-width-SubMenu \"Width\"\n" +
                        "          test-border-right-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px]\n" +
                        "          test-border-right-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px]\n" +
                        "          test-border-right-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px]\n" +
                        "          test-border-right-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px]\n" +
                        "          test-border-right-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-right-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/]\n" +
                        "      (mdi-border-bottom-variant) test-border-bottom--SubMenu \"Bottom\"\n" +
                        "        (mdi-palette) test-test-border-bottom-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-bottom-style-SubMenu \"Style\"\n" +
                        "          test-border-bottom-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE]\n" +
                        "          test-border-bottom-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED]\n" +
                        "          test-border-bottom-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED]\n" +
                        "          test-border-bottom-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-bottom-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/]\n" +
                        "        test-border-bottom-width-SubMenu \"Width\"\n" +
                        "          test-border-bottom-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px]\n" +
                        "          test-border-bottom-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px]\n" +
                        "          test-border-bottom-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px]\n" +
                        "          test-border-bottom-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px]\n" +
                        "          test-border-bottom-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-bottom-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/]\n" +
                        "      (mdi-border-all-variant) test-border-all--SubMenu \"All\"\n" +
                        "        (mdi-palette) test-test-border-all-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-all-style-SubMenu \"Style\"\n" +
                        "          test-border-all-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE]\n" +
                        "          test-border-all-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED]\n" +
                        "          test-border-all-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED]\n" +
                        "          test-border-all-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-all-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/]\n" +
                        "        test-border-all-width-SubMenu \"Width\"\n" +
                        "          test-border-all-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px]\n" +
                        "          test-border-all-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px]\n" +
                        "          test-border-all-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px]\n" +
                        "          test-border-all-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px]\n" +
                        "          test-border-all-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-all-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/]\n" +
                        "    (mdi-format-clear) test-clear-style-MenuItem \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/]\n" +
                        "  (mdi-format-text) test-format--SubMenu \"Format\"\n" +
                        "    test-format-date-SubMenu \"Date\"\n" +
                        "      test-format-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date]\n" +
                        "    test-format-datetime-SubMenu \"Date Time\"\n" +
                        "      test-format-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time]\n" +
                        "    test-format-number-SubMenu \"Number\"\n" +
                        "      test-format-number-general-MenuItem \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General]\n" +
                        "      test-format-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-format-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-format-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-format-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number]\n" +
                        "    test-format-text-SubMenu \"Text\"\n" +
                        "      test-format-text-default-MenuItem \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text]\n" +
                        "    test-format-time-SubMenu \"Time\"\n" +
                        "      test-format-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-format-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time]\n" +
                        "  (mdi-format-text) test-parse--SubMenu \"Parse\"\n" +
                        "    test-parse-date-SubMenu \"Date\"\n" +
                        "      test-parse-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date]\n" +
                        "    test-parse-datetime-SubMenu \"Date Time\"\n" +
                        "      test-parse-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time]\n" +
                        "    test-parse-number-SubMenu \"Number\"\n" +
                        "      test-parse-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-parse-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-parse-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-parse-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number]\n" +
                        "    test-parse-time-SubMenu \"Time\"\n" +
                        "      test-parse-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-parse-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time]\n" +
                        "    -----\n" +
                        "    test-parse-recent-0-MenuItem \"$0.00\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%240.00]\n" +
                        "  (mdi-star) test-hideIfZero-MenuItem \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true]\n" +
                        "  -----\n" +
                        "  (mdi-close) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-column-plus-before) test-column-insert-before-SubMenu \"Insert before column\"\n" +
                        "    test-column-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1]\n" +
                        "    test-column-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2]\n" +
                        "    test-column-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3]\n" +
                        "    test-column-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-column-insert-after-SubMenu \"Insert after column\"\n" +
                        "    test-column-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1]\n" +
                        "    test-column-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2]\n" +
                        "    test-column-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3]\n" +
                        "    test-column-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertAfter]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-row-insert-before-SubMenu \"Insert before row\"\n" +
                        "    test-row-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1]\n" +
                        "    test-row-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2]\n" +
                        "    test-row-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3]\n" +
                        "    test-row-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-row-insert-after-SubMenu \"Insert after row\"\n" +
                        "    test-row-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1]\n" +
                        "    test-row-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2]\n" +
                        "    test-row-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3]\n" +
                        "    test-row-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\" [/1/SpreadsheetName-1/cell/A1/freeze]\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\" [/1/SpreadsheetName-1/cell/A1/unfreeze]\n" +
                        "  -----\n" +
                        "  test-label-SubMenu \"Labels\" [1]\n" +
                        "    test-label-0-MenuItem \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123]\n" +
                        "    test-label-create-MenuItem \"Create...\" [/1/SpreadsheetName-1/label]\n"
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
                "Cell-MenuId \"Cell A1 Menu\"\n" +
                        "  (mdi-content-cut) test-clipboard-cut-SubMenu \"Cut\"\n" +
                        "    test-clipboard-cut-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/cut/cell]\n" +
                        "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/cut/formula]\n" +
                        "    test-clipboard-cut-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/format-pattern]\n" +
                        "    test-clipboard-cut-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/cut/parse-pattern]\n" +
                        "    test-clipboard-cut-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/cut/style]\n" +
                        "    test-clipboard-cut-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/cut/formatted-value]\n" +
                        "  (mdi-content-copy) test-clipboard-copy-SubMenu \"Copy\"\n" +
                        "    test-clipboard-copy-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/copy/cell]\n" +
                        "    test-clipboard-copy-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/copy/formula]\n" +
                        "    test-clipboard-copy-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/format-pattern]\n" +
                        "    test-clipboard-copy-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/copy/parse-pattern]\n" +
                        "    test-clipboard-copy-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/copy/style]\n" +
                        "    test-clipboard-copy-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/copy/formatted-value]\n" +
                        "  (mdi-content-paste) test-clipboard-paste-SubMenu \"Paste\"\n" +
                        "    test-clipboard-paste-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/A1/paste/cell]\n" +
                        "    test-clipboard-paste-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/A1/paste/formula]\n" +
                        "    test-clipboard-paste-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/format-pattern]\n" +
                        "    test-clipboard-paste-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/A1/paste/parse-pattern]\n" +
                        "    test-clipboard-paste-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/A1/paste/style]\n" +
                        "    test-clipboard-paste-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/A1/paste/formatted-value]\n" +
                        "  -----\n" +
                        "  test-style-SubMenu \"Style\"\n" +
                        "    test-alignment-SubMenu \"Alignment\"\n" +
                        "      (mdi-format-align-left) test-left-MenuItem key=L \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT]\n" +
                        "      (mdi-format-align-center) test-center-MenuItem key=C \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER]\n" +
                        "      (mdi-format-align-right) test-right-MenuItem key=R \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT]\n" +
                        "      (mdi-format-align-justify) test-justify-MenuItem key=J \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY]\n" +
                        "    test-vertical-alignment-SubMenu \"Vertical Alignment\"\n" +
                        "      (mdi-format-align-top) test-top-MenuItem \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP]\n" +
                        "      (mdi-format-align-middle) test-middle-MenuItem \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE]\n" +
                        "      (mdi-format-align-bottom) test-bottom-MenuItem \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM]\n" +
                        "    (mdi-palette) test-color-SubMenu \"Color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-palette) test-background-color-SubMenu \"Background color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-format-bold) test-bold-MenuItem key=b \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold]\n" +
                        "    (mdi-format-italic) test-italics-MenuItem key=i \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC]\n" +
                        "    (mdi-format-strikethrough) test-strike-thru-MenuItem key=s \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH]\n" +
                        "    (mdi-format-underline) test-underline-MenuItem key=u \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE]\n" +
                        "    test-text-case-SubMenu \"Text case\"\n" +
                        "      (mdi-format-letter-case-upper) test-normal-MenuItem \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/]\n" +
                        "      (mdi-format-letter-case) test-capitalize-MenuItem \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE]\n" +
                        "      (mdi-format-letter-case-lower) test-lower-MenuItem \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE]\n" +
                        "      (mdi-format-letter-case-upper) test-upper-MenuItem \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE]\n" +
                        "    test-text-wrapping-SubMenu \"Wrapping\"\n" +
                        "      (mdi-format-text-wrapping-clip) test-clip-MenuItem \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL]\n" +
                        "      (mdi-format-text-wrapping-overflow) test-overflow-MenuItem \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE]\n" +
                        "      (mdi-format-text-wrapping-wrap) test-wrap-MenuItem \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN]\n" +
                        "    test-border--SubMenu \"Border\"\n" +
                        "      (mdi-border-top-variant) test-border-top--SubMenu \"Top\"\n" +
                        "        (mdi-palette) test-test-border-top-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-top-style-SubMenu \"Style\"\n" +
                        "          test-border-top-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/NONE]\n" +
                        "          test-border-top-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DASHED]\n" +
                        "          test-border-top-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/DOTTED]\n" +
                        "          test-border-top-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-top-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-style/save/]\n" +
                        "        test-border-top-width-SubMenu \"Width\"\n" +
                        "          test-border-top-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/0px]\n" +
                        "          test-border-top-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/1px]\n" +
                        "          test-border-top-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/2px]\n" +
                        "          test-border-top-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/3px]\n" +
                        "          test-border-top-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-top-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-top-width/save/]\n" +
                        "      (mdi-border-left-variant) test-border-left--SubMenu \"Left\"\n" +
                        "        (mdi-palette) test-test-border-left-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-left-style-SubMenu \"Style\"\n" +
                        "          test-border-left-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/NONE]\n" +
                        "          test-border-left-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DASHED]\n" +
                        "          test-border-left-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/DOTTED]\n" +
                        "          test-border-left-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-left-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-style/save/]\n" +
                        "        test-border-left-width-SubMenu \"Width\"\n" +
                        "          test-border-left-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/0px]\n" +
                        "          test-border-left-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/1px]\n" +
                        "          test-border-left-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/2px]\n" +
                        "          test-border-left-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/3px]\n" +
                        "          test-border-left-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-left-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-left-width/save/]\n" +
                        "      (mdi-border-right-variant) test-border-right--SubMenu \"Right\"\n" +
                        "        (mdi-palette) test-test-border-right-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-right-style-SubMenu \"Style\"\n" +
                        "          test-border-right-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/NONE]\n" +
                        "          test-border-right-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DASHED]\n" +
                        "          test-border-right-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/DOTTED]\n" +
                        "          test-border-right-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-right-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-style/save/]\n" +
                        "        test-border-right-width-SubMenu \"Width\"\n" +
                        "          test-border-right-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/0px]\n" +
                        "          test-border-right-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/1px]\n" +
                        "          test-border-right-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/2px]\n" +
                        "          test-border-right-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/3px]\n" +
                        "          test-border-right-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-right-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-right-width/save/]\n" +
                        "      (mdi-border-bottom-variant) test-border-bottom--SubMenu \"Bottom\"\n" +
                        "        (mdi-palette) test-test-border-bottom-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-bottom-style-SubMenu \"Style\"\n" +
                        "          test-border-bottom-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/NONE]\n" +
                        "          test-border-bottom-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DASHED]\n" +
                        "          test-border-bottom-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/DOTTED]\n" +
                        "          test-border-bottom-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-bottom-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-style/save/]\n" +
                        "        test-border-bottom-width-SubMenu \"Width\"\n" +
                        "          test-border-bottom-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/0px]\n" +
                        "          test-border-bottom-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/1px]\n" +
                        "          test-border-bottom-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/2px]\n" +
                        "          test-border-bottom-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/3px]\n" +
                        "          test-border-bottom-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-bottom-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-bottom-width/save/]\n" +
                        "      (mdi-border-all-variant) test-border-all--SubMenu \"All\"\n" +
                        "        (mdi-palette) test-test-border-all-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-all-style-SubMenu \"Style\"\n" +
                        "          test-border-all-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/NONE]\n" +
                        "          test-border-all-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DASHED]\n" +
                        "          test-border-all-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/DOTTED]\n" +
                        "          test-border-all-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-all-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-style/save/]\n" +
                        "        test-border-all-width-SubMenu \"Width\"\n" +
                        "          test-border-all-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/0px]\n" +
                        "          test-border-all-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/1px]\n" +
                        "          test-border-all-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/2px]\n" +
                        "          test-border-all-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/3px]\n" +
                        "          test-border-all-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-all-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/A1/style/border-width/save/]\n" +
                        "    (mdi-format-clear) test-clear-style-MenuItem \"Clear style\" [/1/SpreadsheetName-1/cell/A1/style/*/save/]\n" +
                        "  (mdi-format-text) test-format--SubMenu \"Format\"\n" +
                        "    test-format-date-SubMenu \"Date\"\n" +
                        "      test-format-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date]\n" +
                        "    test-format-datetime-SubMenu \"Date Time\"\n" +
                        "      test-format-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date-time]\n" +
                        "    test-format-number-SubMenu \"Number\"\n" +
                        "      test-format-number-general-MenuItem \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/General]\n" +
                        "      test-format-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-format-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-format-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-format-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/number]\n" +
                        "    test-format-text-SubMenu \"Text\"\n" +
                        "      test-format-text-default-MenuItem \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text/save/@]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/text]\n" +
                        "    test-format-time-SubMenu \"Time\"\n" +
                        "      test-format-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-format-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/format-pattern/time]\n" +
                        "    -----\n" +
                        "    test-format-recent-0-MenuItem \"dd/mm/yyyy\" [/1/SpreadsheetName-1/cell/A1/format-pattern/date/save/dd/mm/yyyy]\n" +
                        "  (mdi-format-text) test-parse--SubMenu \"Parse\"\n" +
                        "    test-parse-date-SubMenu \"Date\"\n" +
                        "      test-parse-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date]\n" +
                        "    test-parse-datetime-SubMenu \"Date Time\"\n" +
                        "      test-parse-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/date-time]\n" +
                        "    test-parse-number-SubMenu \"Number\"\n" +
                        "      test-parse-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-parse-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-parse-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-parse-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number]\n" +
                        "    test-parse-time-SubMenu \"Time\"\n" +
                        "      test-parse-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-parse-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/time]\n" +
                        "    -----\n" +
                        "    test-parse-recent-0-MenuItem \"$0.00\" [/1/SpreadsheetName-1/cell/A1/parse-pattern/number/save/%240.00]\n" +
                        "  (mdi-star) test-hideIfZero-MenuItem \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true]\n" +
                        "  -----\n" +
                        "  (mdi-close) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/cell/A1/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-column-plus-before) test-column-insert-before-SubMenu \"Insert before column\"\n" +
                        "    test-column-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1]\n" +
                        "    test-column-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2]\n" +
                        "    test-column-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3]\n" +
                        "    test-column-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-column-insert-after-SubMenu \"Insert after column\"\n" +
                        "    test-column-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1]\n" +
                        "    test-column-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2]\n" +
                        "    test-column-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3]\n" +
                        "    test-column-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertAfter]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-row-insert-before-SubMenu \"Insert before row\"\n" +
                        "    test-row-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1]\n" +
                        "    test-row-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2]\n" +
                        "    test-row-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3]\n" +
                        "    test-row-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-row-insert-after-SubMenu \"Insert after row\"\n" +
                        "    test-row-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1]\n" +
                        "    test-row-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2]\n" +
                        "    test-row-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3]\n" +
                        "    test-row-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\" [/1/SpreadsheetName-1/cell/A1/freeze]\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\" [/1/SpreadsheetName-1/cell/A1/unfreeze]\n" +
                        "  -----\n" +
                        "  test-label-SubMenu \"Labels\" [1]\n" +
                        "    test-label-0-MenuItem \"Label123 (A1)\" [/1/SpreadsheetName-1/label/Label123]\n" +
                        "    test-label-create-MenuItem \"Create...\" [/1/SpreadsheetName-1/label]\n"
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
                Lists.empty(),
                Lists.empty()
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
                "Cell-MenuId \"Cell B2:C3 Menu\"\n" +
                        "  (mdi-content-cut) test-clipboard-cut-SubMenu \"Cut\"\n" +
                        "    test-clipboard-cut-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/cell]\n" +
                        "    test-clipboard-cut-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formula]\n" +
                        "    test-clipboard-cut-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/format-pattern]\n" +
                        "    test-clipboard-cut-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/parse-pattern]\n" +
                        "    test-clipboard-cut-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/style]\n" +
                        "    test-clipboard-cut-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/cut/formatted-value]\n" +
                        "  (mdi-content-copy) test-clipboard-copy-SubMenu \"Copy\"\n" +
                        "    test-clipboard-copy-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/cell]\n" +
                        "    test-clipboard-copy-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formula]\n" +
                        "    test-clipboard-copy-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/format-pattern]\n" +
                        "    test-clipboard-copy-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/parse-pattern]\n" +
                        "    test-clipboard-copy-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/style]\n" +
                        "    test-clipboard-copy-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/copy/formatted-value]\n" +
                        "  (mdi-content-paste) test-clipboard-paste-SubMenu \"Paste\"\n" +
                        "    test-clipboard-paste-cell-MenuItem \"Cell\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/cell]\n" +
                        "    test-clipboard-paste-formula-MenuItem \"Formula\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formula]\n" +
                        "    test-clipboard-paste-format-pattern-MenuItem \"Format Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/format-pattern]\n" +
                        "    test-clipboard-paste-parse-pattern-MenuItem \"Parse Pattern\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/parse-pattern]\n" +
                        "    test-clipboard-paste-style-MenuItem \"Style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/style]\n" +
                        "    test-clipboard-paste-formatted-value-MenuItem \"Formatted Value\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/paste/formatted-value]\n" +
                        "  -----\n" +
                        "  test-style-SubMenu \"Style\"\n" +
                        "    test-alignment-SubMenu \"Alignment\"\n" +
                        "      (mdi-format-align-left) test-left-MenuItem key=L \"Left\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/LEFT]\n" +
                        "      (mdi-format-align-center) test-center-MenuItem key=C \"Center\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/CENTER]\n" +
                        "      (mdi-format-align-right) test-right-MenuItem key=R \"Right\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/RIGHT]\n" +
                        "      (mdi-format-align-justify) test-justify-MenuItem key=J \"Justify\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-align/save/JUSTIFY]\n" +
                        "    test-vertical-alignment-SubMenu \"Vertical Alignment\"\n" +
                        "      (mdi-format-align-top) test-top-MenuItem \"Top\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/TOP]\n" +
                        "      (mdi-format-align-middle) test-middle-MenuItem \"Middle\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/MIDDLE]\n" +
                        "      (mdi-format-align-bottom) test-bottom-MenuItem \"Bottom\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/vertical-align/save/BOTTOM]\n" +
                        "    (mdi-palette) test-color-SubMenu \"Color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-palette) test-background-color-SubMenu \"Background color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-format-bold) test-bold-MenuItem key=b \"Bold\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/font-weight/save/bold]\n" +
                        "    (mdi-format-italic) test-italics-MenuItem key=i \"Italics\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/font-style/save/ITALIC]\n" +
                        "    (mdi-format-strikethrough) test-strike-thru-MenuItem key=s \"Strike-thru\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-decoration-line/save/LINE_THROUGH]\n" +
                        "    (mdi-format-underline) test-underline-MenuItem key=u \"Underline\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-decoration-line/save/UNDERLINE]\n" +
                        "    test-text-case-SubMenu \"Text case\"\n" +
                        "      (mdi-format-letter-case-upper) test-normal-MenuItem \"Normal\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/]\n" +
                        "      (mdi-format-letter-case) test-capitalize-MenuItem \"Capitalize\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/CAPITALIZE]\n" +
                        "      (mdi-format-letter-case-lower) test-lower-MenuItem \"Lower case\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/LOWERCASE]\n" +
                        "      (mdi-format-letter-case-upper) test-upper-MenuItem \"Upper case\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/text-transform/save/UPPERCASE]\n" +
                        "    test-text-wrapping-SubMenu \"Wrapping\"\n" +
                        "      (mdi-format-text-wrapping-clip) test-clip-MenuItem \"Clip\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-wrap/save/NORMAL]\n" +
                        "      (mdi-format-text-wrapping-overflow) test-overflow-MenuItem \"Overflow\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-x/save/VISIBLE]\n" +
                        "      (mdi-format-text-wrapping-wrap) test-wrap-MenuItem \"Wrap\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/overflow-x/save/HIDDEN]\n" +
                        "    test-border--SubMenu \"Border\"\n" +
                        "      (mdi-border-top-variant) test-border-top--SubMenu \"Top\"\n" +
                        "        (mdi-palette) test-test-border-top-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-top-style-SubMenu \"Style\"\n" +
                        "          test-border-top-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/NONE]\n" +
                        "          test-border-top-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DASHED]\n" +
                        "          test-border-top-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/DOTTED]\n" +
                        "          test-border-top-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-top-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-style/save/]\n" +
                        "        test-border-top-width-SubMenu \"Width\"\n" +
                        "          test-border-top-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/0px]\n" +
                        "          test-border-top-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/1px]\n" +
                        "          test-border-top-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/2px]\n" +
                        "          test-border-top-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/3px]\n" +
                        "          test-border-top-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-top-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-top-width/save/]\n" +
                        "      (mdi-border-left-variant) test-border-left--SubMenu \"Left\"\n" +
                        "        (mdi-palette) test-test-border-left-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-left-style-SubMenu \"Style\"\n" +
                        "          test-border-left-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/NONE]\n" +
                        "          test-border-left-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DASHED]\n" +
                        "          test-border-left-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/DOTTED]\n" +
                        "          test-border-left-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-left-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-style/save/]\n" +
                        "        test-border-left-width-SubMenu \"Width\"\n" +
                        "          test-border-left-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/0px]\n" +
                        "          test-border-left-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/1px]\n" +
                        "          test-border-left-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/2px]\n" +
                        "          test-border-left-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/3px]\n" +
                        "          test-border-left-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-left-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-left-width/save/]\n" +
                        "      (mdi-border-right-variant) test-border-right--SubMenu \"Right\"\n" +
                        "        (mdi-palette) test-test-border-right-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-right-style-SubMenu \"Style\"\n" +
                        "          test-border-right-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/NONE]\n" +
                        "          test-border-right-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DASHED]\n" +
                        "          test-border-right-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/DOTTED]\n" +
                        "          test-border-right-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-right-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-style/save/]\n" +
                        "        test-border-right-width-SubMenu \"Width\"\n" +
                        "          test-border-right-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/0px]\n" +
                        "          test-border-right-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/1px]\n" +
                        "          test-border-right-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/2px]\n" +
                        "          test-border-right-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/3px]\n" +
                        "          test-border-right-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-right-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-right-width/save/]\n" +
                        "      (mdi-border-bottom-variant) test-border-bottom--SubMenu \"Bottom\"\n" +
                        "        (mdi-palette) test-test-border-bottom-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-bottom-style-SubMenu \"Style\"\n" +
                        "          test-border-bottom-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/NONE]\n" +
                        "          test-border-bottom-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DASHED]\n" +
                        "          test-border-bottom-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/DOTTED]\n" +
                        "          test-border-bottom-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-bottom-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-style/save/]\n" +
                        "        test-border-bottom-width-SubMenu \"Width\"\n" +
                        "          test-border-bottom-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/0px]\n" +
                        "          test-border-bottom-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/1px]\n" +
                        "          test-border-bottom-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/2px]\n" +
                        "          test-border-bottom-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/3px]\n" +
                        "          test-border-bottom-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-bottom-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-bottom-width/save/]\n" +
                        "      (mdi-border-all-variant) test-border-all--SubMenu \"All\"\n" +
                        "        (mdi-palette) test-test-border-all-color-SubMenu \"Color\"\n" +
                        "          metadata color picker\n" +
                        "        test-border-all-style-SubMenu \"Style\"\n" +
                        "          test-border-all-style-none-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/NONE]\n" +
                        "          test-border-all-style-dashed-MenuItem \"Dashed\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DASHED]\n" +
                        "          test-border-all-style-dotted-MenuItem \"Dotted\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/DOTTED]\n" +
                        "          test-border-all-style-solid-MenuItem \"Solid\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/SOLID]\n" +
                        "          (mdi-format-clear) test-border-all-style-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-style/save/]\n" +
                        "        test-border-all-width-SubMenu \"Width\"\n" +
                        "          test-border-all-width-0-MenuItem \"None\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/0px]\n" +
                        "          test-border-all-width-1-MenuItem \"1\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/1px]\n" +
                        "          test-border-all-width-2-MenuItem \"2\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/2px]\n" +
                        "          test-border-all-width-3-MenuItem \"3\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/3px]\n" +
                        "          test-border-all-width-4-MenuItem \"4\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/4px]\n" +
                        "          (mdi-format-clear) test-border-all-width-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/border-width/save/]\n" +
                        "    (mdi-format-clear) test-clear-style-MenuItem \"Clear style\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/style/*/save/]\n" +
                        "  (mdi-format-text) test-format--SubMenu \"Format\"\n" +
                        "    test-format-date-SubMenu \"Date\"\n" +
                        "      test-format-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date]\n" +
                        "    test-format-datetime-SubMenu \"Date Time\"\n" +
                        "      test-format-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d/m/yy]\n" +
                        "      test-format-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-format-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-format-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/date-time]\n" +
                        "    test-format-number-SubMenu \"Number\"\n" +
                        "      test-format-number-general-MenuItem \"General \\\"General\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/General]\n" +
                        "      test-format-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-format-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-format-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-format-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/number]\n" +
                        "    test-format-text-SubMenu \"Text\"\n" +
                        "      test-format-text-default-MenuItem \"Default \\\"@\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/text/save/@]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/text]\n" +
                        "    test-format-time-SubMenu \"Time\"\n" +
                        "      test-format-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-format-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-format-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/format-pattern/time]\n" +
                        "  (mdi-format-text) test-parse--SubMenu \"Parse\"\n" +
                        "    test-parse-date-SubMenu \"Date\"\n" +
                        "      test-parse-date-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-date-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-date-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-date-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date]\n" +
                        "    test-parse-datetime-SubMenu \"Date Time\"\n" +
                        "      test-parse-datetime-short-MenuItem \"Short \\\"d/m/yy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d/m/yy]\n" +
                        "      test-parse-datetime-medium-MenuItem \"Medium \\\"d mmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmm+yyyy]\n" +
                        "      test-parse-datetime-long-MenuItem \"Long \\\"d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/d+mmmm+yyyy]\n" +
                        "      test-parse-datetime-full-MenuItem \"Full \\\"dddd, d mmmm yyyy\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date/save/dddd%2C+d+mmmm+yyyy]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/date-time]\n" +
                        "    test-parse-number-SubMenu \"Number\"\n" +
                        "      test-parse-number-number-MenuItem \"Number \\\"#,##0.###\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%23%2C%23%230.%23%23%23]\n" +
                        "      test-parse-number-integer-MenuItem \"Integer \\\"#,##0\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%23%2C%23%230]\n" +
                        "      test-parse-number-percent-MenuItem \"Percent \\\"#,##0%\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%23%2C%23%230%25]\n" +
                        "      test-parse-number-currency-MenuItem \"Currency \\\"$#,##0.00\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number/save/%24%23%2C%23%230.00]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/number]\n" +
                        "    test-parse-time-SubMenu \"Time\"\n" +
                        "      test-parse-time-short-MenuItem \"Short \\\"h:mm AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm+AM/PM]\n" +
                        "      test-parse-time-medium-MenuItem \"Medium \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-long-MenuItem \"Long \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-time-full-MenuItem \"Full \\\"h:mm:ss AM/PM\\\"\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time/save/h:mm:ss+AM/PM]\n" +
                        "      test-parse-edit-MenuItem \"Edit...\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/parse-pattern/time]\n" +
                        "  (mdi-star) test-hideIfZero-MenuItem \"Hide Zero Values\" [/1/SpreadsheetName-1/metadata/hide-zero-values/save/true]\n" +
                        "  -----\n" +
                        "  (mdi-close) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/cell/B2:C3/bottom-right/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-column-plus-before) test-column-insert-before-SubMenu \"Insert before column\"\n" +
                        "    test-column-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/1]\n" +
                        "    test-column-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/2]\n" +
                        "    test-column-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore/3]\n" +
                        "    test-column-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-column-insert-after-SubMenu \"Insert after column\"\n" +
                        "    test-column-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/1]\n" +
                        "    test-column-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/2]\n" +
                        "    test-column-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter/3]\n" +
                        "    test-column-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/B:C/right/insertAfter]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-row-insert-before-SubMenu \"Insert before row\"\n" +
                        "    test-row-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/1]\n" +
                        "    test-row-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/2]\n" +
                        "    test-row-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore/3]\n" +
                        "    test-row-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/2:3/bottom/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-row-insert-after-SubMenu \"Insert after row\"\n" +
                        "    test-row-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/1]\n" +
                        "    test-row-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/2]\n" +
                        "    test-row-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter/3]\n" +
                        "    test-row-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/2:3/bottom/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\"\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\"\n" +
                        "  -----\n" +
                        "  test-label-SubMenu \"Labels\" [1]\n" +
                        "    test-label-0-MenuItem \"Label123 (B2)\" [/1/SpreadsheetName-1/label/Label123]\n" +
                        "    test-label-create-MenuItem \"Create...\" [/1/SpreadsheetName-1/label]\n"
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
                "Column-MenuId \"Column B Menu\"\n" +
                        "  test-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/column/B/clear]\n" +
                        "  (mdi-table-column-remove) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/column/B/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-column-plus-before) test-column-insert-before-SubMenu \"Insert before column\"\n" +
                        "    test-column-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/B/insertBefore/1]\n" +
                        "    test-column-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/B/insertBefore/2]\n" +
                        "    test-column-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/B/insertBefore/3]\n" +
                        "    test-column-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/B/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-column-insert-after-SubMenu \"Insert after column\"\n" +
                        "    test-column-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/column/B/insertAfter/1]\n" +
                        "    test-column-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/column/B/insertAfter/2]\n" +
                        "    test-column-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/column/B/insertAfter/3]\n" +
                        "    test-column-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/column/B/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\"\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\"\n"
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
                "Row-MenuId \"Row 3 Menu\"\n" +
                        "  test-clear-MenuItem \"Clear\" [/1/SpreadsheetName-1/row/3/clear]\n" +
                        "  (mdi-table-row-remove) test-delete-MenuItem \"Delete\" [/1/SpreadsheetName-1/row/3/delete]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-row-insert-before-SubMenu \"Insert before row\"\n" +
                        "    test-row-insert-before-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/3/insertBefore/1]\n" +
                        "    test-row-insert-before-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/3/insertBefore/2]\n" +
                        "    test-row-insert-before-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/3/insertBefore/3]\n" +
                        "    test-row-insert-before-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/3/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-row-insert-after-SubMenu \"Insert after row\"\n" +
                        "    test-row-insert-after-1-MenuItem \"1\" [/1/SpreadsheetName-1/row/3/insertAfter/1]\n" +
                        "    test-row-insert-after-2-MenuItem \"2\" [/1/SpreadsheetName-1/row/3/insertAfter/2]\n" +
                        "    test-row-insert-after-3-MenuItem \"3\" [/1/SpreadsheetName-1/row/3/insertAfter/3]\n" +
                        "    test-row-insert-after-prompt-MenuItem \"...\" [/1/SpreadsheetName-1/row/3/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\"\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\"\n"
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
