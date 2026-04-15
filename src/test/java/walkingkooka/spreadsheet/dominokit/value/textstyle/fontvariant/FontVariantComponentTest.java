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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontvariant;

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
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class FontVariantComponentTest implements HtmlComponentTesting<FontVariantComponent, HTMLFieldSetElement> {

    @Test
    public void testSetValue() {
        final FontVariantComponent component = FontVariantComponent.with(
            "Test123-",
            new FakeFontVariantComponentContext() {
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
                            TextStylePropertyName.FONT_VARIANT
                        )
                    );
                }
            }
        );

        component.setValue(
            Optional.of(
                FontVariant.SMALL_CAPS
            )
        );

        this.treePrintAndCheck(
            component,
            "FontVariantComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Font Variant\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/] id=Test123-fontVariant-Link\n" +
                "            \"Initial\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/INITIAL] id=Test123-fontVariant-INITIAL-Link\n" +
                "            \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/NORMAL] id=Test123-fontVariant-NORMAL-Link\n" +
                "            \"Small Caps\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/SMALL_CAPS] CHECKED id=Test123-fontVariant-SMALL_CAPS-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final FontVariantComponent component = FontVariantComponent.with(
            "Test123-",
            new FakeFontVariantComponentContext() {
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
                            TextStylePropertyName.FONT_VARIANT
                        )
                    );
                }
            }
        );

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.FONT_VARIANT,
                        FontVariant.NORMAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "FontVariantComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    Font Variant\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/] id=Test123-fontVariant-Link\n" +
                "            \"Initial\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/INITIAL] id=Test123-fontVariant-INITIAL-Link\n" +
                "            \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/NORMAL] CHECKED id=Test123-fontVariant-NORMAL-Link\n" +
                "            \"Small Caps\" [#/1/SpreadsheetName111/cell/A1/style/font-variant/save/SMALL_CAPS] id=Test123-fontVariant-SMALL_CAPS-Link\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontVariantComponent> type() {
        return FontVariantComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
