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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontfamily;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;

public final class FontFamilyComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, FontFamily, FontFamilyComponent> {

    private final static FontFamily COURIER = FontFamily.with("Courier");

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "FontFamilyComponent\n" +
                "  SelectComponent\n" +
                "    [] id=FontFamily123-Select\n" +
                "      \"Courier\" DISABLED id=FontFamily123-courier-Option\n" +
                "      \"Helvetica\" DISABLED id=FontFamily123-helvetica-Option\n" +
                "      \"Times New Roman\" DISABLED id=FontFamily123-timesNewRoman-Option\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(COURIER)
                ),
            "FontFamilyComponent\n" +
                "  SelectComponent\n" +
                "    [Courier] id=FontFamily123-Select\n" +
                "      \"Courier\" DISABLED id=FontFamily123-courier-Option\n" +
                "      \"Helvetica\" DISABLED id=FontFamily123-helvetica-Option\n" +
                "      \"Times New Roman\" DISABLED id=FontFamily123-timesNewRoman-Option\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public FontFamilyComponent createComponent() {
        return FontFamilyComponent.empty(
            "FontFamily123-",
            new FakeFontFamilyComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellStyle(
                        SpreadsheetId.with(1),
                        SpreadsheetName.with("SpreadsheetName111"),
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Optional.of(
                            TextStylePropertyName.FONT_FAMILY
                        )
                    );
                }

                @Override
                public List<FontFamily> fontFamilies() {
                    return Lists.of(
                        COURIER,
                        FontFamily.with("Helvetica"),
                        FontFamily.with("Times New Roman")
                    );
                }
            }
        );
    }

    // HasName..........................................................................................................

    @Test
    public void testName() {
        this.nameAndCheck(
            this.createComponent(),
            TextStylePropertyName.FONT_FAMILY
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontFamilyComponent> type() {
        return FontFamilyComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
