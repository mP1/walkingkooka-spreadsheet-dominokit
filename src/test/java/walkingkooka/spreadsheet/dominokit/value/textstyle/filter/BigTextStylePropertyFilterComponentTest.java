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

package walkingkooka.spreadsheet.dominokit.value.textstyle.filter;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class BigTextStylePropertyFilterComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, TextStylePropertyFilter, BigTextStylePropertyFilterComponent>,
    ComponentLifecycleMatcherTesting {

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigTextStylePropertyFilterComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextStylePropertyFilterComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [] icons=mdi-close-circle id=TestIdPrefix123-BigTextStylePropertyFilter-text-TextBox REQUIRED\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Border\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BORDER] id=TestIdPrefix123-BigTextStylePropertyFilter-BORDER-Link\n" +
                "              \"Box\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BOX] id=TestIdPrefix123-BigTextStylePropertyFilter-BOX-Link\n" +
                "              \"Break\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BREAK] id=TestIdPrefix123-BigTextStylePropertyFilter-BREAK-Link\n" +
                "              \"Color\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/COLOR] id=TestIdPrefix123-BigTextStylePropertyFilter-COLOR-Link\n" +
                "              \"Font\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/FONT] id=TestIdPrefix123-BigTextStylePropertyFilter-FONT-Link\n" +
                "              \"Measure\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/MEASURE] id=TestIdPrefix123-BigTextStylePropertyFilter-MEASURE-Link\n" +
                "              \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/OVERFLOW] id=TestIdPrefix123-BigTextStylePropertyFilter-OVERFLOW-Link\n" +
                "              \"Text\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/TEXT] id=TestIdPrefix123-BigTextStylePropertyFilter-TEXT-Link\n" +
                "              \"Whitespace\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/WHITESPACE] id=TestIdPrefix123-BigTextStylePropertyFilter-WHITESPACE-Link\n"
        );
    }

    @Test
    public void testOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional(),
            "BigTextStylePropertyFilterComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextStylePropertyFilterComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [] icons=mdi-close-circle id=TestIdPrefix123-BigTextStylePropertyFilter-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Border\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BORDER] id=TestIdPrefix123-BigTextStylePropertyFilter-BORDER-Link\n" +
                "              \"Box\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BOX] id=TestIdPrefix123-BigTextStylePropertyFilter-BOX-Link\n" +
                "              \"Break\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BREAK] id=TestIdPrefix123-BigTextStylePropertyFilter-BREAK-Link\n" +
                "              \"Color\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/COLOR] id=TestIdPrefix123-BigTextStylePropertyFilter-COLOR-Link\n" +
                "              \"Font\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/FONT] id=TestIdPrefix123-BigTextStylePropertyFilter-FONT-Link\n" +
                "              \"Measure\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/MEASURE] id=TestIdPrefix123-BigTextStylePropertyFilter-MEASURE-Link\n" +
                "              \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/OVERFLOW] id=TestIdPrefix123-BigTextStylePropertyFilter-OVERFLOW-Link\n" +
                "              \"Text\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/TEXT] id=TestIdPrefix123-BigTextStylePropertyFilter-TEXT-Link\n" +
                "              \"Whitespace\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/WHITESPACE] id=TestIdPrefix123-BigTextStylePropertyFilter-WHITESPACE-Link\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigTextStylePropertyFilterComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextStylePropertyFilterComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [] icons=mdi-close-circle id=TestIdPrefix123-BigTextStylePropertyFilter-text-TextBox REQUIRED\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Border\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BORDER] id=TestIdPrefix123-BigTextStylePropertyFilter-BORDER-Link\n" +
                "              \"Box\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BOX] id=TestIdPrefix123-BigTextStylePropertyFilter-BOX-Link\n" +
                "              \"Break\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/BREAK] id=TestIdPrefix123-BigTextStylePropertyFilter-BREAK-Link\n" +
                "              \"Color\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/COLOR] id=TestIdPrefix123-BigTextStylePropertyFilter-COLOR-Link\n" +
                "              \"Font\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/FONT] id=TestIdPrefix123-BigTextStylePropertyFilter-FONT-Link\n" +
                "              \"Measure\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/MEASURE] id=TestIdPrefix123-BigTextStylePropertyFilter-MEASURE-Link\n" +
                "              \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/OVERFLOW] id=TestIdPrefix123-BigTextStylePropertyFilter-OVERFLOW-Link\n" +
                "              \"Text\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/TEXT] id=TestIdPrefix123-BigTextStylePropertyFilter-TEXT-Link\n" +
                "              \"Whitespace\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/WHITESPACE] id=TestIdPrefix123-BigTextStylePropertyFilter-WHITESPACE-Link\n"
        );
    }

    @Test
    public void testSetValueString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextStylePropertyFilter.with("123")
                    )
                ),
            "BigTextStylePropertyFilterComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextStylePropertyFilterComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [123] icons=mdi-close-circle id=TestIdPrefix123-BigTextStylePropertyFilter-text-TextBox REQUIRED\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Border\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20BORDER] id=TestIdPrefix123-BigTextStylePropertyFilter-BORDER-Link\n" +
                "              \"Box\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20BOX] id=TestIdPrefix123-BigTextStylePropertyFilter-BOX-Link\n" +
                "              \"Break\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20BREAK] id=TestIdPrefix123-BigTextStylePropertyFilter-BREAK-Link\n" +
                "              \"Color\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20COLOR] id=TestIdPrefix123-BigTextStylePropertyFilter-COLOR-Link\n" +
                "              \"Font\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20FONT] id=TestIdPrefix123-BigTextStylePropertyFilter-FONT-Link\n" +
                "              \"Measure\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20MEASURE] id=TestIdPrefix123-BigTextStylePropertyFilter-MEASURE-Link\n" +
                "              \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20OVERFLOW] id=TestIdPrefix123-BigTextStylePropertyFilter-OVERFLOW-Link\n" +
                "              \"Text\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20TEXT] id=TestIdPrefix123-BigTextStylePropertyFilter-TEXT-Link\n" +
                "              \"Whitespace\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/123%20WHITESPACE] id=TestIdPrefix123-BigTextStylePropertyFilter-WHITESPACE-Link\n"
        );
    }

    @Test
    public void testSetValueStringIncludingKinds() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextStylePropertyFilter.with("xyz " + TextStylePropertyFilterKind.BOX + " " + TextStylePropertyFilterKind.BORDER)
                    )
                ),
            "BigTextStylePropertyFilterComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextStylePropertyFilterComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              [xyz BOX BORDER] icons=mdi-close-circle id=TestIdPrefix123-BigTextStylePropertyFilter-text-TextBox REQUIRED\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Border\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER] CHECKED id=TestIdPrefix123-BigTextStylePropertyFilter-BORDER-Link\n" +
                "              \"Box\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER] CHECKED id=TestIdPrefix123-BigTextStylePropertyFilter-BOX-Link\n" +
                "              \"Break\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20BREAK] id=TestIdPrefix123-BigTextStylePropertyFilter-BREAK-Link\n" +
                "              \"Color\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20COLOR] id=TestIdPrefix123-BigTextStylePropertyFilter-COLOR-Link\n" +
                "              \"Font\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20FONT] id=TestIdPrefix123-BigTextStylePropertyFilter-FONT-Link\n" +
                "              \"Measure\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20MEASURE] id=TestIdPrefix123-BigTextStylePropertyFilter-MEASURE-Link\n" +
                "              \"Overflow\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20OVERFLOW] id=TestIdPrefix123-BigTextStylePropertyFilter-OVERFLOW-Link\n" +
                "              \"Text\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20TEXT] id=TestIdPrefix123-BigTextStylePropertyFilter-TEXT-Link\n" +
                "              \"Whitespace\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/filter/xyz%20BOX%20BORDER%20WHITESPACE] id=TestIdPrefix123-BigTextStylePropertyFilter-WHITESPACE-Link\n"
        );
    }

    @Override
    public BigTextStylePropertyFilterComponent createComponent() {
        return this.createComponent(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.of(
                    TextStylePropertyName.FONT_WEIGHT
                ),
                Optional.empty() // filter
            )
        );
    }

    private BigTextStylePropertyFilterComponent createComponent(final HistoryToken historyToken) {
        return BigTextStylePropertyFilterComponent.with(
            "TestIdPrefix123-BigTextStylePropertyFilter-",
            new FakeBigTextStylePropertyFilterComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return historyToken;
                }

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return this.watchers.add(watcher);
                }

                private final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<BigTextStylePropertyFilterComponent> type() {
        return BigTextStylePropertyFilterComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
