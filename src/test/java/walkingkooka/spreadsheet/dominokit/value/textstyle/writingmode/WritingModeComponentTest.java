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

package walkingkooka.spreadsheet.dominokit.value.textstyle.writingmode;

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
import walkingkooka.tree.text.WritingMode;

import java.util.Optional;

public final class WritingModeComponentTest implements HtmlComponentTesting<WritingModeComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final WritingModeComponent component = WritingModeComponent.with(
            "Test123-",
            new FakeWritingModeComponentContext() {
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
                            TextStylePropertyName.WRITING_MODE
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                WritingMode.HORIZONTAL_TB
            )
        );

        this.treePrintAndCheck(
            component,
            "WritingModeComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Writing Mode\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/] id=Test123-writingMode-Link\n" +
                "            \"Horizontal Tb\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/HORIZONTAL_TB] CHECKED id=Test123-writingMode-HORIZONTAL_TB-Link\n" +
                "            \"Vertical Lr\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/VERTICAL_LR] id=Test123-writingMode-VERTICAL_LR-Link\n" +
                "            \"Vertical Rl\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/VERTICAL_RL] id=Test123-writingMode-VERTICAL_RL-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final WritingModeComponent component = WritingModeComponent.with(
            "Test123-",
            new FakeWritingModeComponentContext() {
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
                            TextStylePropertyName.WRITING_MODE
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.WRITING_MODE,
                        WritingMode.VERTICAL_LR
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "WritingModeComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Writing Mode\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/] id=Test123-writingMode-Link\n" +
                "            \"Horizontal Tb\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/HORIZONTAL_TB] id=Test123-writingMode-HORIZONTAL_TB-Link\n" +
                "            \"Vertical Lr\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/VERTICAL_LR] CHECKED id=Test123-writingMode-VERTICAL_LR-Link\n" +
                "            \"Vertical Rl\" [#/1/SpreadsheetName111/cell/A1/style/writing-mode/save/VERTICAL_RL] id=Test123-writingMode-VERTICAL_RL-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<WritingModeComponent> type() {
        return WritingModeComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
