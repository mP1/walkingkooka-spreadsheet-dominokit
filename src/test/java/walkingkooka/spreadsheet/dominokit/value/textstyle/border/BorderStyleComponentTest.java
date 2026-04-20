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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class BorderStyleComponentTest extends BorderStyleSharedComponentTestCase<BorderStyleComponent> {

    @Test
    public void testSetValue() {
        final BorderStyleComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                BorderStyle.DASHED
            )
        );

        this.treePrintAndCheck(
            component,
            "BorderStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/] id=Test123-borderStyle-Link\n" +
                "          \"None\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/NONE] id=Test123-borderStyle-NONE-Link\n" +
                "          \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/HIDDEN] id=Test123-borderStyle-HIDDEN-Link\n" +
                "          \"Dotted\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/DOTTED] id=Test123-borderStyle-DOTTED-Link\n" +
                "          \"Dashed\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/DASHED] CHECKED id=Test123-borderStyle-DASHED-Link\n" +
                "          \"Solid\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/SOLID] id=Test123-borderStyle-SOLID-Link\n" +
                "          \"Double\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/DOUBLE] id=Test123-borderStyle-DOUBLE-Link\n" +
                "          \"Groove\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/GROOVE] id=Test123-borderStyle-GROOVE-Link\n" +
                "          \"Ridge\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/RIDGE] id=Test123-borderStyle-RIDGE-Link\n" +
                "          \"Inset\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/INSET] id=Test123-borderStyle-INSET-Link\n" +
                "          \"Outset\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/OUTSET] id=Test123-borderStyle-OUTSET-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final BorderStyleComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.BORDER_STYLE,
                        BorderStyle.SOLID
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "BorderStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/] id=Test123-borderStyle-Link\n" +
                "          \"None\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/NONE] id=Test123-borderStyle-NONE-Link\n" +
                "          \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/HIDDEN] id=Test123-borderStyle-HIDDEN-Link\n" +
                "          \"Dotted\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/DOTTED] id=Test123-borderStyle-DOTTED-Link\n" +
                "          \"Dashed\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/DASHED] id=Test123-borderStyle-DASHED-Link\n" +
                "          \"Solid\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/SOLID] CHECKED id=Test123-borderStyle-SOLID-Link\n" +
                "          \"Double\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/DOUBLE] id=Test123-borderStyle-DOUBLE-Link\n" +
                "          \"Groove\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/GROOVE] id=Test123-borderStyle-GROOVE-Link\n" +
                "          \"Ridge\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/RIDGE] id=Test123-borderStyle-RIDGE-Link\n" +
                "          \"Inset\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/INSET] id=Test123-borderStyle-INSET-Link\n" +
                "          \"Outset\" [#/1/SpreadsheetName111/cell/A1/style/border-style/save/OUTSET] id=Test123-borderStyle-OUTSET-Link\n"
        );
    }

    @Override
    public BorderStyleComponent createComponent() {
        return BorderStyleComponent.with(
            "Test123-",
            new FakeBorderStyleComponentContext() {
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
                            TextStylePropertyName.BORDER_STYLE
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<BorderStyleComponent> type() {
        return BorderStyleComponent.class;
    }
}
