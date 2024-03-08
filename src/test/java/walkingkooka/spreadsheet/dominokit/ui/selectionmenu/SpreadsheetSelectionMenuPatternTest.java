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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuFactory;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetSelectionSummary;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetSelectionMenuPatternTest implements ClassTesting<SpreadsheetSelectionMenuPattern<?>>,
        TreePrintableTesting {

    @Test
    public void testCell() {
        final SpreadsheetCellHistoryToken token = HistoryToken.cell(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.A1.setDefaultAnchor()
        );
        final FakeSpreadsheetSelectionMenuContext context = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return token;
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
                return Sets.of(
                        SpreadsheetSelection.labelName("Label123").mapping(selection.toCell())
                );
            }

            @Override
            public List<HistoryToken> recentFormatPatterns() {
                return Lists.of(
                        token.setPattern(
                                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                        )
                );
            }

            @Override
            public List<HistoryToken> recentParsePatterns() {
                return Lists.of(
                        token.setPattern(
                                SpreadsheetPattern.parseNumberParsePattern("$0.00")
                        )
                );
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
        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "Cell-MenuId",
                        "Cell A1 Menu",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                context
        );

        SpreadsheetSelectionMenu.render(
                token,
                menu,
                context
        );

        this.treePrintAndCheck(
                menu,
                "Cell-MenuId \"Cell A1 Menu\"\n" +
                        "  test-style-SubMenu \"Style\"\n" +
                        "    test-alignment-SubMenu \"Alignment\"\n" +
                        "      (mdi-format-align-left) test-left-MenuItem \"Left\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/LEFT]\n" +
                        "      (mdi-format-align-center) test-center-MenuItem \"Center\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/CENTER]\n" +
                        "      (mdi-format-align-right) test-right-MenuItem \"Right\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/RIGHT]\n" +
                        "      (mdi-format-align-justify) test-justify-MenuItem \"Justify\" [/1/SpreadsheetName-1/cell/A1/style/text-align/save/JUSTIFY]\n" +
                        "    test-vertical-alignment-SubMenu \"Vertical Alignment\"\n" +
                        "      (mdi-format-align-top) test-top-MenuItem \"Top\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/TOP]\n" +
                        "      (mdi-format-align-middle) test-middle-MenuItem \"Middle\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/MIDDLE]\n" +
                        "      (mdi-format-align-bottom) test-bottom-MenuItem \"Bottom\" [/1/SpreadsheetName-1/cell/A1/style/vertical-align/save/BOTTOM]\n" +
                        "    (mdi-palette) test-color-SubMenu \"Color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-palette) test-background-color-SubMenu \"Background color\"\n" +
                        "      metadata color picker\n" +
                        "    (mdi-format-bold) test-bold-MenuItem \"Bold\" [/1/SpreadsheetName-1/cell/A1/style/font-weight/save/bold]\n" +
                        "    (mdi-format-italic) test-italics-MenuItem \"Italics\" [/1/SpreadsheetName-1/cell/A1/style/font-style/save/ITALIC]\n" +
                        "    (mdi-format-strikethrough) test-strike-thru-MenuItem \"Strike-thru\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/LINE_THROUGH]\n" +
                        "    (mdi-format-underline) test-underline-MenuItem \"Underline\" [/1/SpreadsheetName-1/cell/A1/style/text-decoration-line/save/UNDERLINE]\n" +
                        "    test-text-case-SubMenu \"Text case\"\n" +
                        "      (mdi-format-letter-case-upper) test-normal-MenuItem \"Normal\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/]\n" +
                        "      (mdi-format-letter-case) test-capitalize-MenuItem \"Capitalize\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/CAPITALIZE]\n" +
                        "      (mdi-format-letter-case-lower) test-lower-MenuItem \"Lower case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/LOWERCASE]\n" +
                        "      (mdi-format-letter-case-upper) test-upper-MenuItem \"Upper case\" [/1/SpreadsheetName-1/cell/A1/style/text-transform/save/UPPERCASE]\n" +
                        "    test-text-wrapping-SubMenu \"Wrapping\"\n" +
                        "      (mdi-format-text-wrapping-clip) test-clip-MenuItem \"Clip\" [/1/SpreadsheetName-1/cell/A1/style/overflow-wrap/save/NORMAL]\n" +
                        "      (mdi-format-text-wrapping-overflow) test-overflow-MenuItem \"Overflow\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/VISIBLE]\n" +
                        "      (mdi-format-text-wrapping-wrap) test-wrap-MenuItem \"Wrap\" [/1/SpreadsheetName-1/cell/A1/style/overflow-x/save/HIDDEN]\n" +
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
                        "  (mdi-table-column-plus-before) test-insert-before-column-SubMenu \"Insert before column\"\n" +
                        "    test-insert-before-column--1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertBefore/1]\n" +
                        "    test-insert-before-column--2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertBefore/2]\n" +
                        "    test-insert-before-column--3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertBefore/3]\n" +
                        "    test-insert-before-column--MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-insert-after-column-SubMenu \"Insert after column\"\n" +
                        "    test-insert-after-column--1-MenuItem \"1\" [/1/SpreadsheetName-1/column/A/insertAfter/1]\n" +
                        "    test-insert-after-column--2-MenuItem \"2\" [/1/SpreadsheetName-1/column/A/insertAfter/2]\n" +
                        "    test-insert-after-column--3-MenuItem \"3\" [/1/SpreadsheetName-1/column/A/insertAfter/3]\n" +
                        "    test-insert-after-column--MenuItem \"...\" [/1/SpreadsheetName-1/column/A/insertAfter]\n" +
                        "  -----\n" +
                        "  (mdi-table-row-plus-before) test-insert-row-before-SubMenu \"Insert before row\"\n" +
                        "    test-insert-row-before--1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertBefore/1]\n" +
                        "    test-insert-row-before--2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertBefore/2]\n" +
                        "    test-insert-row-before--3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertBefore/3]\n" +
                        "    test-insert-row-before--MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-insert-row-before-SubMenu \"Insert after row\"\n" +
                        "    test-insert-row-before--1-MenuItem \"1\" [/1/SpreadsheetName-1/row/1/insertAfter/1]\n" +
                        "    test-insert-row-before--2-MenuItem \"2\" [/1/SpreadsheetName-1/row/1/insertAfter/2]\n" +
                        "    test-insert-row-before--3-MenuItem \"3\" [/1/SpreadsheetName-1/row/1/insertAfter/3]\n" +
                        "    test-insert-row-before--MenuItem \"...\" [/1/SpreadsheetName-1/row/1/insertAfter]\n" +
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
    public void testColumn() {
        final SpreadsheetColumnHistoryToken token = HistoryToken.column(
                SpreadsheetId.with(1), // id
                SpreadsheetName.with("SpreadsheetName-1"), // name
                SpreadsheetSelection.parseColumn("B").setDefaultAnchor()
        );
        final FakeSpreadsheetSelectionMenuContext context = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return token;
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
                return Sets.of(
                        SpreadsheetSelection.labelName("Label123").mapping(selection.toCell())
                );
            }

            @Override
            public SpreadsheetSelectionSummary selectionSummary() {
                return SpreadsheetSelectionSummary.EMPTY;
            }
        };
        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "Column-MenuId",
                        "Column B Menu",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                context
        );

        SpreadsheetSelectionMenu.render(
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
                        "  (mdi-table-column-plus-before) test-insert-before-column-SubMenu \"Insert before column\"\n" +
                        "    test-insert-before-column--1-MenuItem \"1\" [/1/SpreadsheetName-1/column/B/insertBefore/1]\n" +
                        "    test-insert-before-column--2-MenuItem \"2\" [/1/SpreadsheetName-1/column/B/insertBefore/2]\n" +
                        "    test-insert-before-column--3-MenuItem \"3\" [/1/SpreadsheetName-1/column/B/insertBefore/3]\n" +
                        "    test-insert-before-column--MenuItem \"...\" [/1/SpreadsheetName-1/column/B/insertBefore]\n" +
                        "  (mdi-table-column-plus-after) test-insert-after-column-SubMenu \"Insert after column\"\n" +
                        "    test-insert-after-column--1-MenuItem \"1\" [/1/SpreadsheetName-1/column/B/insertAfter/1]\n" +
                        "    test-insert-after-column--2-MenuItem \"2\" [/1/SpreadsheetName-1/column/B/insertAfter/2]\n" +
                        "    test-insert-after-column--3-MenuItem \"3\" [/1/SpreadsheetName-1/column/B/insertAfter/3]\n" +
                        "    test-insert-after-column--MenuItem \"...\" [/1/SpreadsheetName-1/column/B/insertAfter]\n" +
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
        final FakeSpreadsheetSelectionMenuContext context = new FakeSpreadsheetSelectionMenuContext() {

            @Override
            public HistoryToken historyToken() {
                return token;
            }

            @Override
            public String idPrefix() {
                return "test-";
            }

            @Override
            public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
                return Sets.of(
                        SpreadsheetSelection.labelName("Label123").mapping(selection.toCell())
                );
            }

            @Override
            public SpreadsheetSelectionSummary selectionSummary() {
                return SpreadsheetSelectionSummary.EMPTY;
            }
        };
        final SpreadsheetContextMenu menu = SpreadsheetContextMenuFactory.with(
                Menu.create(
                        "Row-MenuId",
                        "Row 3 Menu",
                        Optional.empty(), // no icon
                        Optional.empty() // no badge
                ),
                context
        );

        SpreadsheetSelectionMenu.render(
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
                        "  (mdi-table-row-plus-before) test-insert-row-before-SubMenu \"Insert before row\"\n" +
                        "    test-insert-row-before--1-MenuItem \"1\" [/1/SpreadsheetName-1/row/3/insertBefore/1]\n" +
                        "    test-insert-row-before--2-MenuItem \"2\" [/1/SpreadsheetName-1/row/3/insertBefore/2]\n" +
                        "    test-insert-row-before--3-MenuItem \"3\" [/1/SpreadsheetName-1/row/3/insertBefore/3]\n" +
                        "    test-insert-row-before--MenuItem \"...\" [/1/SpreadsheetName-1/row/3/insertBefore]\n" +
                        "  (mdi-table-row-plus-after) test-insert-row-before-SubMenu \"Insert after row\"\n" +
                        "    test-insert-row-before--1-MenuItem \"1\" [/1/SpreadsheetName-1/row/3/insertAfter/1]\n" +
                        "    test-insert-row-before--2-MenuItem \"2\" [/1/SpreadsheetName-1/row/3/insertAfter/2]\n" +
                        "    test-insert-row-before--3-MenuItem \"3\" [/1/SpreadsheetName-1/row/3/insertAfter/3]\n" +
                        "    test-insert-row-before--MenuItem \"...\" [/1/SpreadsheetName-1/row/3/insertAfter]\n" +
                        "  -----\n" +
                        "  test-freeze-MenuItem \"Freeze\"\n" +
                        "  test-unfreeze-MenuItem \"Unfreeze\"\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSelectionMenuPattern<?>> type() {
        return Cast.to(SpreadsheetSelectionMenuPattern.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
