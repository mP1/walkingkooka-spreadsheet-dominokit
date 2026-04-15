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

package walkingkooka.spreadsheet.dominokit.value.textstyle.verticalalign;

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
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Optional;

public final class VerticalAlignComponentTest implements HtmlComponentTesting<VerticalAlignComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final VerticalAlignComponent component = VerticalAlignComponent.with(
            "Test123-",
            new FakeVerticalAlignComponentContext() {
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
                            TextStylePropertyName.VERTICAL_ALIGN
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                VerticalAlign.MIDDLE
            )
        );

        this.treePrintAndCheck(
            component,
            "VerticalAlignComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Vertical Align\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            mdi-format-clear \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/] id=Test123-verticalAlign-Link\n" +
                "            mdi-format-align-top \"Top\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/TOP] id=Test123-verticalAlign-TOP-Link\n" +
                "            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/MIDDLE] CHECKED id=Test123-verticalAlign-MIDDLE-Link\n" +
                "            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/BOTTOM] id=Test123-verticalAlign-BOTTOM-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final VerticalAlignComponent component = VerticalAlignComponent.with(
            "Test123-",
            new FakeVerticalAlignComponentContext() {
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
                            TextStylePropertyName.VERTICAL_ALIGN
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.VERTICAL_ALIGN,
                        VerticalAlign.MIDDLE
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "VerticalAlignComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Vertical Align\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            mdi-format-clear \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/] id=Test123-verticalAlign-Link\n" +
                "            mdi-format-align-top \"Top\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/TOP] id=Test123-verticalAlign-TOP-Link\n" +
                "            mdi-format-align-middle \"Middle\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/MIDDLE] CHECKED id=Test123-verticalAlign-MIDDLE-Link\n" +
                "            mdi-format-align-bottom \"Bottom\" [#/1/SpreadsheetName111/cell/A1/style/vertical-align/save/BOTTOM] id=Test123-verticalAlign-BOTTOM-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<VerticalAlignComponent> type() {
        return VerticalAlignComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
