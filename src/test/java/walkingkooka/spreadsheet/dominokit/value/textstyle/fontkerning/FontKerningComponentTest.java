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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontkerning;

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
import walkingkooka.tree.text.FontKerning;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class FontKerningComponentTest implements HtmlComponentTesting<FontKerningComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final FontKerningComponent component = FontKerningComponent.with(
            "Test123-",
            new FakeFontKerningComponentContext() {
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
                            TextStylePropertyName.FONT_KERNING
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                FontKerning.AUTO
            )
        );

        this.treePrintAndCheck(
            component,
            "FontKerningComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Font Kerning\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/] id=Test123-fontKerning-Link\n" +
                "            \"Auto\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/AUTO] CHECKED id=Test123-fontKerning-AUTO-Link\n" +
                "            \"None\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/NONE] id=Test123-fontKerning-NONE-Link\n" +
                "            \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/NORMAL] id=Test123-fontKerning-NORMAL-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final FontKerningComponent component = FontKerningComponent.with(
            "Test123-",
            new FakeFontKerningComponentContext() {
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
                            TextStylePropertyName.FONT_KERNING
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.FONT_KERNING,
                        FontKerning.NORMAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "FontKerningComponent\n" +
                "  TextStylePropertyNameEnumHistoryTokenAnchorListComponent\n" +
                "    Font Kerning\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/] id=Test123-fontKerning-Link\n" +
                "            \"Auto\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/AUTO] id=Test123-fontKerning-AUTO-Link\n" +
                "            \"None\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/NONE] id=Test123-fontKerning-NONE-Link\n" +
                "            \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-kerning/save/NORMAL] CHECKED id=Test123-fontKerning-NORMAL-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontKerningComponent> type() {
        return FontKerningComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
