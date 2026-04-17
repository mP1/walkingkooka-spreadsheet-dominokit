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

package walkingkooka.spreadsheet.dominokit.value.textstyle.outline;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.OutlineStyle;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class OutlineStyleComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, OutlineStyle, OutlineStyleComponent> {

    @Test
    public void testSetValue() {
        final OutlineStyleComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                OutlineStyle.DASHED
            )
        );

        this.treePrintAndCheck(
            component,
            "OutlineStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Outline Style\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/] id=Test123-outlineStyle-Link\n" +
                "            \"None\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/NONE] id=Test123-outlineStyle-NONE-Link\n" +
                "            \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/HIDDEN] id=Test123-outlineStyle-HIDDEN-Link\n" +
                "            \"Dotted\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/DOTTED] id=Test123-outlineStyle-DOTTED-Link\n" +
                "            \"Dashed\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/DASHED] CHECKED id=Test123-outlineStyle-DASHED-Link\n" +
                "            \"Solid\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/SOLID] id=Test123-outlineStyle-SOLID-Link\n" +
                "            \"Double\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/DOUBLE] id=Test123-outlineStyle-DOUBLE-Link\n" +
                "            \"Groove\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/GROOVE] id=Test123-outlineStyle-GROOVE-Link\n" +
                "            \"Ridge\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/RIDGE] id=Test123-outlineStyle-RIDGE-Link\n" +
                "            \"Inset\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/INSET] id=Test123-outlineStyle-INSET-Link\n" +
                "            \"Outset\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/OUTSET] id=Test123-outlineStyle-OUTSET-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final OutlineStyleComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.OUTLINE_STYLE,
                        OutlineStyle.SOLID
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "OutlineStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Outline Style\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/] id=Test123-outlineStyle-Link\n" +
                "            \"None\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/NONE] id=Test123-outlineStyle-NONE-Link\n" +
                "            \"Hidden\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/HIDDEN] id=Test123-outlineStyle-HIDDEN-Link\n" +
                "            \"Dotted\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/DOTTED] id=Test123-outlineStyle-DOTTED-Link\n" +
                "            \"Dashed\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/DASHED] id=Test123-outlineStyle-DASHED-Link\n" +
                "            \"Solid\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/SOLID] CHECKED id=Test123-outlineStyle-SOLID-Link\n" +
                "            \"Double\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/DOUBLE] id=Test123-outlineStyle-DOUBLE-Link\n" +
                "            \"Groove\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/GROOVE] id=Test123-outlineStyle-GROOVE-Link\n" +
                "            \"Ridge\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/RIDGE] id=Test123-outlineStyle-RIDGE-Link\n" +
                "            \"Inset\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/INSET] id=Test123-outlineStyle-INSET-Link\n" +
                "            \"Outset\" [#/1/SpreadsheetName111/cell/A1/style/outline-style/save/OUTSET] id=Test123-outlineStyle-OUTSET-Link\n"
        );
    }

    @Override
    public OutlineStyleComponent createComponent() {
        return OutlineStyleComponent.with(
            "Test123-",
            new FakeOutlineStyleComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {
                    };
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.OUTLINE_STYLE
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<OutlineStyleComponent> type() {
        return OutlineStyleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
