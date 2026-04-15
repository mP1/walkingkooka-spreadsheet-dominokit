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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.icons.Icon;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.EmptyTextException;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasNameTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontStretch;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyEnumHistoryTokenAnchorListComponentTest implements ValueComponentTesting<HTMLFieldSetElement, TextAlign, TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign>>,
    ComponentLifecycleMatcherTesting,
    HasNameTesting<TextStylePropertyName<TextAlign>>,
    ToStringTesting<TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign>> {

    private final static String ID_PREFIX = "TestId123";
    private final static TextStylePropertyName<TextAlign> PROPERTY_NAME = TextStylePropertyName.TEXT_ALIGN;
    private final static List<TextAlign> VALUES = Arrays.asList(
        null,
        TextAlign.LEFT,
        TextAlign.CENTER,
        TextAlign.RIGHT,
        TextAlign.JUSTIFY
    );

    private final static Function<Optional<TextAlign>, String> VALUE_TO_TEXT = (Optional<TextAlign> value) -> value.map(
        (TextAlign v) ->
            CaseKind.SNAKE.change(
                v.name()
                    .toLowerCase(),
                CaseKind.TITLE
            ) + "!!"
    ).orElse("Clear!");

    private final static Function<Optional<TextAlign>, Optional<Icon<?>>> VALUE_TO_ICON = (Optional<TextAlign> value) -> Optional.ofNullable(
        value.isEmpty() ?
            SpreadsheetIcons.clearStyle() :
            null
    );

    private final static TextStylePropertyEnumHistoryTokenAnchorListComponentContext CONTEXT = new FakeTextStylePropertyEnumHistoryTokenAnchorListComponentContext();

    // with.............................................................................................................

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                null,
                PROPERTY_NAME,
                VALUES,
                VALUE_TO_TEXT,
                VALUE_TO_ICON,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            EmptyTextException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                "",
                PROPERTY_NAME,
                VALUES,
                VALUE_TO_TEXT,
                VALUE_TO_ICON,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullPropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                ID_PREFIX,
                null,
                VALUES,
                VALUE_TO_TEXT,
                VALUE_TO_ICON,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullValuesFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                ID_PREFIX,
                PROPERTY_NAME,
                null,
                VALUE_TO_TEXT,
                VALUE_TO_ICON,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullValueToTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                ID_PREFIX,
                PROPERTY_NAME,
                VALUES,
                null,
                VALUE_TO_ICON,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullValueToIconFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                ID_PREFIX,
                PROPERTY_NAME,
                VALUES,
                VALUE_TO_TEXT,
                null,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
                ID_PREFIX,
                PROPERTY_NAME,
                VALUES,
                VALUE_TO_TEXT,
                VALUE_TO_ICON,
                null
            )
        );
    }

    // treePrint........................................................................................................

    @Test
    public void testTreePrintWhenCellSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                )
            ),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/cell/A1/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Test
    public void testTreePrintWhenColumnSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.columnSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    SpreadsheetSelection.A1.column()
                        .setDefaultAnchor()
                )
            ),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" DISABLED id=TestId123textAlign-Link\n" +
                "          \"Left!!\" DISABLED id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" DISABLED id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" DISABLED id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" DISABLED id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Test
    public void testTreePrintWhenSpreadsheetSelectHistoryToken() {
        this.treePrintAndCheck(
            this.createComponent(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            ),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Test
    public void testTreePrintWhenCellSelectHistoryTokenChange() {
        final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        this.treePrintAndCheck(
            anchor,
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValue() {
        final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        anchor.setValue(
            Optional.of(TextAlign.CENTER)
        );

        this.treePrintAndCheck(
            anchor,
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/CENTER] CHECKED id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Test
    public void testSetValueTwice() {
        final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        anchor.setValue(
            Optional.of(TextAlign.RIGHT)
        );

        anchor.setValue(
            Optional.of(TextAlign.CENTER)
        );

        this.treePrintAndCheck(
            anchor,
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/CENTER] CHECKED id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    // HasName..........................................................................................................

    @Test
    public void testHasName() {
        this.nameAndCheck(
            this.createComponent(),
            TextStylePropertyName.TEXT_ALIGN
        );
    }

    // textStyleValueWatcher............................................................................................

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        anchor.setValue(
            Optional.of(
                TextAlign.LEFT
            )
        );

        anchor.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.CENTER
                    )
                )
            );

        this.treePrintAndCheck(
            anchor,
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/CENTER] CHECKED id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChangeCleared() {
        final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context = this.createContext(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> anchor = this.createComponent(context);

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            )
        );

        anchor.setValue(
            Optional.of(
                TextAlign.LEFT
            )
        );

        anchor.textStyleValueWatcher()
            .onValue(
                Optional.of(TextStyle.EMPTY)
            );

        this.treePrintAndCheck(
            anchor,
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/] CHECKED id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/cell/B2/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> createComponent() {
        return this.createComponent(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    private TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> createComponent(final HistoryToken historyToken) {
        return this.createComponent(
            this.createContext(historyToken)
        );
    }

    private TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> createComponent(final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context) {
        return TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
            ID_PREFIX,
            PROPERTY_NAME,
            VALUES,
            VALUE_TO_TEXT,
            VALUE_TO_ICON,
            context
        );
    }

    private TextStylePropertyEnumHistoryTokenAnchorListComponentContext createContext(final HistoryToken historyToken) {
        return new FakeTextStylePropertyEnumHistoryTokenAnchorListComponentContext() {

            @Override
            public HistoryToken historyToken() {
                return this.current;
            }

            private HistoryToken current = historyToken;

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                final HistoryToken previous = this.current;
                this.current = token;
                this.watchers.onHistoryTokenChange(
                    previous,
                    AppContexts.fake()
                );
            }

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return this.watchers.add(watcher);
            }

            private final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();
        };
    }

    // valueToText......................................................................................................

    @Test
    public void testValueToText() {
        this.valueToTextAndCheck(
            Optional.of(
                BorderStyle.DASHED
            ),
            "Dashed"
        );
    }

    @Test
    public void testValueToText2() {
        this.valueToTextAndCheck(
            Optional.of(
                FontStretch.EXTRA_CONDENSED
            ),
            "Extra Condensed"
        );
    }

    private <T extends Enum<T>> void valueToTextAndCheck(final Optional<T> value,
                                                         final String expected) {
        this.checkEquals(
            expected,
            TextStylePropertyEnumHistoryTokenAnchorListComponent.<T>valueToText()
                .apply(value)
        );
    }

    // printTree........................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            this.createComponent(),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n"
        );
    }

    @Test
    public void testPrintTreeHelperText() {
        this.treePrintAndCheck(
            this.createComponent()
                .setHelperText(
                    Optional.of("HelperText 123")
                ),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n" +
                "      HelperText 123\n"
        );
    }

    @Test
    public void testPrintTreeErrors() {
        this.treePrintAndCheck(
            this.createComponent()
                .setErrors(
                    Lists.of(
                        "Error111",
                        "Error222",
                        "Error333"
                    )
                ),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n" +
                "      Error(s)\n" +
                "        Error111\n" +
                "        Error222\n" +
                "        Error333\n"
        );
    }

    @Test
    public void testPrintTreeHelperTextErrors() {
        this.treePrintAndCheck(
            this.createComponent()
                .setHelperText(
                    Optional.of("HelperText 123")
                ).setErrors(
                    Lists.of(
                        "Error111",
                        "Error222",
                        "Error333"
                    )
                ),
            "TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "  Text Align\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TestId123textAlign-Link\n" +
                "          \"Left!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link\n" +
                "          \"Center!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link\n" +
                "          \"Right!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link\n" +
                "          \"Justify!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link\n" +
                "      HelperText 123\n" +
                "      Error(s)\n" +
                "        Error111\n" +
                "        Error222\n" +
                "        Error333\n"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createComponent(),
            "ROW DIV [mdi-format-clear \"Clear!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/] id=TestId123textAlign-Link, \"Left!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/LEFT] id=TestId123textAlign-LEFT-Link, \"Center!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/CENTER] id=TestId123textAlign-CENTER-Link, \"Right!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/RIGHT] id=TestId123textAlign-RIGHT-Link, \"Justify!!\" [#/1/SpreadsheetName1/spreadsheet/style/text-align/save/JUSTIFY] id=TestId123textAlign-JUSTIFY-Link]"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign>> type() {
        return Cast.to(TextStylePropertyEnumHistoryTokenAnchorListComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
