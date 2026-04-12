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

package walkingkooka.spreadsheet.dominokit.textalign;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextAlignComponentTest implements HtmlComponentTesting<TextAlignComponent, HTMLDivElement> {

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            TextAlignComponent.with(
                "Test123-",
                new FakeTextAlignComponentContext() {
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
                                TextStylePropertyName.TEXT_ALIGN
                            )
                        );
                    }
                }
            ),
            "TextAlignComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          mdi-format-align-left \"left\" [#/1/SpreadsheetName111/cell/A1/style/text-align/save/LEFT] id=Test123-textAlign-LEFT-Link\n" +
                "          mdi-format-align-right \"right\" [#/1/SpreadsheetName111/cell/A1/style/text-align/save/RIGHT] id=Test123-textAlign-RIGHT-Link\n" +
                "          mdi-format-align-center \"center\" [#/1/SpreadsheetName111/cell/A1/style/text-align/save/CENTER] id=Test123-textAlign-CENTER-Link\n" +
                "          mdi-format-align-right \"justify\" [#/1/SpreadsheetName111/cell/A1/style/text-align/save/JUSTIFY] id=Test123-textAlign-JUSTIFY-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextAlignComponent> type() {
        return TextAlignComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
