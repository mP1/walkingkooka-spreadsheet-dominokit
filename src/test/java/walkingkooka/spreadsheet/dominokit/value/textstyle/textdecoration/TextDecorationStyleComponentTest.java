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
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextDecorationStyle;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextDecorationStyleComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, TextDecorationStyle, TextDecorationStyleComponent> {

    @Test
    public void testSetValue() {
        final TextDecorationStyleComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                TextDecorationStyle.SOLID
            )
        );

        this.treePrintAndCheck(
            component,
            "TextDecorationStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Text Decoration Style\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/] id=Test123-textDecorationStyle-Link\n" +
                "            \"Solid\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/SOLID] CHECKED id=Test123-textDecorationStyle-SOLID-Link\n" +
                "            \"Double\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/DOUBLE] id=Test123-textDecorationStyle-DOUBLE-Link\n" +
                "            \"Dashed\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/DASHED] id=Test123-textDecorationStyle-DASHED-Link\n" +
                "            \"Dotted\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/DOTTED] id=Test123-textDecorationStyle-DOTTED-Link\n" +
                "            \"Wavy\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/WAVY] id=Test123-textDecorationStyle-WAVY-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final TextDecorationStyleComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_DECORATION_STYLE,
                        TextDecorationStyle.DASHED
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "TextDecorationStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Text Decoration Style\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/] id=Test123-textDecorationStyle-Link\n" +
                "            \"Solid\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/SOLID] id=Test123-textDecorationStyle-SOLID-Link\n" +
                "            \"Double\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/DOUBLE] id=Test123-textDecorationStyle-DOUBLE-Link\n" +
                "            \"Dashed\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/DASHED] CHECKED id=Test123-textDecorationStyle-DASHED-Link\n" +
                "            \"Dotted\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/DOTTED] id=Test123-textDecorationStyle-DOTTED-Link\n" +
                "            \"Wavy\" [#/1/SpreadsheetName111/cell/A1/style/text-decoration-style/save/WAVY] id=Test123-textDecorationStyle-WAVY-Link\n"
        );
    }

    @Override
    public TextDecorationStyleComponent createComponent() {
        return TextDecorationStyleComponent.with(
            "Test123-",
            new FakeTextDecorationStyleComponentContext() {
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
                            TextStylePropertyName.TEXT_DECORATION_STYLE
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextDecorationStyleComponent> type() {
        return TextDecorationStyleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
