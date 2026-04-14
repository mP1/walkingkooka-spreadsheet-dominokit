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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textdecoration;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextDecorationLineComponentTest implements HtmlComponentTesting<TextDecorationLineComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final TextDecorationLineComponent component = TextDecorationLineComponent.with(
            "Test123-",
            new FakeTextDecorationLineComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {};
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.TEXT_DECORATION_LINE
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                TextDecorationLine.LINE_THROUGH
            )
        );

        this.treePrintAndCheck(
            component,
            "TextDecorationLineComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Text Decoration Line\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/] id=Test123-textDecorationLine-Link\n" +
                "            mdi-format-clear \"None\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/NONE] id=Test123-textDecorationLine-NONE-Link\n" +
                "            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/LINE_THROUGH] CHECKED id=Test123-textDecorationLine-LINE_THROUGH-Link\n" +
                "            \"Overline\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/OVERLINE] id=Test123-textDecorationLine-OVERLINE-Link\n" +
                "            mdi-format-underline \"Underline\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/UNDERLINE] id=Test123-textDecorationLine-UNDERLINE-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final TextDecorationLineComponent component = TextDecorationLineComponent.with(
            "Test123-",
            new FakeTextDecorationLineComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {};
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.TEXT_DECORATION_LINE
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_DECORATION_LINE,
                        TextDecorationLine.LINE_THROUGH
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "TextDecorationLineComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Text Decoration Line\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/] id=Test123-textDecorationLine-Link\n" +
                "            mdi-format-clear \"None\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/NONE] id=Test123-textDecorationLine-NONE-Link\n" +
                "            mdi-format-strikethrough \"Strikethrough\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/LINE_THROUGH] CHECKED id=Test123-textDecorationLine-LINE_THROUGH-Link\n" +
                "            \"Overline\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/OVERLINE] id=Test123-textDecorationLine-OVERLINE-Link\n" +
                "            mdi-format-underline \"Underline\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-line/save/UNDERLINE] id=Test123-textDecorationLine-UNDERLINE-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextDecorationLineComponent> type() {
        return TextDecorationLineComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
