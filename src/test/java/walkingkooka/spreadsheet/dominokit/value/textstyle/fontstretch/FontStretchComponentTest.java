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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontstretch;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontStretch;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class FontStretchComponentTest implements TextStylePropertyEnumComponentTesting<FontStretch, FontStretchComponent> {

    @Test
    public void testSetValue() {
        final FontStretchComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                FontStretch.CONDENSED
            )
        );

        this.treePrintAndCheck(
            component,
            "FontStretchComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/] id=Test123-fontStretch-Link\n" +
                "          \"Ultra Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/ULTRA_CONDENSED] id=Test123-fontStretch-ULTRA_CONDENSED-Link\n" +
                "          \"Extra Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/EXTRA_CONDENSED] id=Test123-fontStretch-EXTRA_CONDENSED-Link\n" +
                "          \"Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/CONDENSED] CHECKED id=Test123-fontStretch-CONDENSED-Link\n" +
                "          \"Semi Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/SEMI_CONDENSED] id=Test123-fontStretch-SEMI_CONDENSED-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/NORMAL] id=Test123-fontStretch-NORMAL-Link\n" +
                "          \"Semi Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/SEMI_EXPANDED] id=Test123-fontStretch-SEMI_EXPANDED-Link\n" +
                "          \"Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/EXPANDED] id=Test123-fontStretch-EXPANDED-Link\n" +
                "          \"Extra Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/EXTRA_EXPANDED] id=Test123-fontStretch-EXTRA_EXPANDED-Link\n" +
                "          \"Ultra Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/ULTRA_EXPANDED] id=Test123-fontStretch-ULTRA_EXPANDED-Link\n"
        );
    }

    @Test
    public void testTextStyleValueWatcherOnValueChange() {
        final FontStretchComponent component = this.createComponent();

        component.textStyleValueWatcher()
            .onValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.FONT_STRETCH,
                        FontStretch.NORMAL
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.WHITE
                    )
                )
            );

        this.treePrintAndCheck(
            component,
            "FontStretchComponent\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/] id=Test123-fontStretch-Link\n" +
                "          \"Ultra Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/ULTRA_CONDENSED] id=Test123-fontStretch-ULTRA_CONDENSED-Link\n" +
                "          \"Extra Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/EXTRA_CONDENSED] id=Test123-fontStretch-EXTRA_CONDENSED-Link\n" +
                "          \"Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/CONDENSED] id=Test123-fontStretch-CONDENSED-Link\n" +
                "          \"Semi Condensed\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/SEMI_CONDENSED] id=Test123-fontStretch-SEMI_CONDENSED-Link\n" +
                "          \"Normal\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/NORMAL] CHECKED id=Test123-fontStretch-NORMAL-Link\n" +
                "          \"Semi Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/SEMI_EXPANDED] id=Test123-fontStretch-SEMI_EXPANDED-Link\n" +
                "          \"Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/EXPANDED] id=Test123-fontStretch-EXPANDED-Link\n" +
                "          \"Extra Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/EXTRA_EXPANDED] id=Test123-fontStretch-EXTRA_EXPANDED-Link\n" +
                "          \"Ultra Expanded\" [#/1/SpreadsheetName111/cell/A1/style/font-stretch/save/ULTRA_EXPANDED] id=Test123-fontStretch-ULTRA_EXPANDED-Link\n"
        );
    }

    @Override
    public FontStretchComponent createComponent() {
        return FontStretchComponent.with(
            "Test123-",
            new FakeFontStretchComponentContext() {
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
                            TextStylePropertyName.FONT_STRETCH
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontStretchComponent> type() {
        return FontStretchComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
