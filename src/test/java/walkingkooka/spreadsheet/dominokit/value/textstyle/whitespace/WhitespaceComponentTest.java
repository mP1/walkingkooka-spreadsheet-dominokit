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

package walkingkooka.spreadsheet.dominokit.value.textstyle.whitespace;

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
import walkingkooka.tree.text.TextWhitespace;

import java.util.Optional;

public final class WhitespaceComponentTest implements HtmlComponentTesting<WhitespaceComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final WhitespaceComponent component = WhitespaceComponent.with(
            "Test123-",
            new FakeWhitespaceComponentContext() {
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
                            TextStylePropertyName.WHITE_SPACE
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                TextWhitespace.PRE_LINE
            )
        );

        this.treePrintAndCheck(
            component,
            "WhitespaceComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    White Space\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/] id=Test123-whiteSpace-Link\n" +
                "            \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/NORMAL] id=Test123-whiteSpace-NORMAL-Link\n" +
                "            \"Nowrap\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/NOWRAP] id=Test123-whiteSpace-NOWRAP-Link\n" +
                "            \"Pre\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/PRE] id=Test123-whiteSpace-PRE-Link\n" +
                "            \"Pre Line\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/PRE_LINE] CHECKED id=Test123-whiteSpace-PRE_LINE-Link\n" +
                "            \"Pre Wrap\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/PRE_WRAP] id=Test123-whiteSpace-PRE_WRAP-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final WhitespaceComponent component = WhitespaceComponent.with(
            "Test123-",
            new FakeWhitespaceComponentContext() {
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
                            TextStylePropertyName.WHITE_SPACE
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.WHITE_SPACE,
                        TextWhitespace.NORMAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "WhitespaceComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    White Space\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/] id=Test123-whiteSpace-Link\n" +
                "            \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/NORMAL] CHECKED id=Test123-whiteSpace-NORMAL-Link\n" +
                "            \"Nowrap\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/NOWRAP] id=Test123-whiteSpace-NOWRAP-Link\n" +
                "            \"Pre\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/PRE] id=Test123-whiteSpace-PRE-Link\n" +
                "            \"Pre Line\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/PRE_LINE] id=Test123-whiteSpace-PRE_LINE-Link\n" +
                "            \"Pre Wrap\" [#/1/SpreadsheetName111/cell/A1/style/white-space/save/PRE_WRAP] id=Test123-whiteSpace-PRE_WRAP-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<WhitespaceComponent> type() {
        return WhitespaceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
