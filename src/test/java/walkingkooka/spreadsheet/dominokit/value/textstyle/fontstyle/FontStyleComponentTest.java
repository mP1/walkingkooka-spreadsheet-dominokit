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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;

public final class FontStyleComponentTest implements TextStylePropertyEnumComponentTesting<FontStyle, FontStyleComponent> {

    @Test
    public void testSetValue() {
        final FontStyleComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                FontStyle.ITALIC
            )
        );

        this.treePrintAndCheck(
            component,
            "FontStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/] id=Test123-fontStyle-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/NORMAL] id=Test123-fontStyle-NORMAL-Link\n" +
                "          mdi-format-italic \"Italic\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/ITALIC] CHECKED id=Test123-fontStyle-ITALIC-Link\n" +
                "          \"Oblique\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/OBLIQUE] id=Test123-fontStyle-OBLIQUE-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final FontStyleComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.FONT_STYLE,
                        FontStyle.OBLIQUE
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "FontStyleComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/] id=Test123-fontStyle-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/NORMAL] id=Test123-fontStyle-NORMAL-Link\n" +
                "          mdi-format-italic \"Italic\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/ITALIC] id=Test123-fontStyle-ITALIC-Link\n" +
                "          \"Oblique\" [#/1/SpreadsheetName111/cell/A1/style/font-style/save/OBLIQUE] CHECKED id=Test123-fontStyle-OBLIQUE-Link\n"
        );
    }

    @Override
    public FontStyleComponent createComponent() {
        return FontStyleComponent.with(
            "Test123-",
            new FakeFontStyleComponentContext() {
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
                            TextStylePropertyName.FONT_STYLE
                        )
                    );
                }
            }
        );
    }

    @Override
    public List<FontStyle> enumValues() {
        return Lists.of(
            FontStyle.values()
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontStyleComponent> type() {
        return FontStyleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
