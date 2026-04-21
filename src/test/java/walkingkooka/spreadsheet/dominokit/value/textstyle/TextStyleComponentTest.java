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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleComponentTest implements ValueTextBoxComponentLikeTesting<TextStyleComponent, TextStyle> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setValue(
                    Optional.of(
                        TextStyle.parse("color: #111; text-align: left;")
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: LEFT;] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetValueFontVariantSmallCaps() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setValue(
                    Optional.of(
                        TextStyle.parse("font-variant: SMALL_CAPS;")
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [font-variant: SMALL_CAPS;] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "color: #111; text-align: left;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: left;] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueFontVariantSmallCaps() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "font-variant: SMALL_CAPS;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [font-variant: SMALL_CAPS;] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueFontVariantSmallCapsMixedCase() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "font-variant: SMALL_caps;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [font-variant: SMALL_caps;] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithExtraWhitespaceEtc() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "  color:  #111;  text-align:  left;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [  color:  #111;  text-align:  left;] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidText() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "color: #111; text-align: XYZ;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: XYZ;] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Unknown value \"XYZ\"\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .clearValue(),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle REQUIRED\n"
        );
    }

    // pushHistoryTokenIfNecessary......................................................................................

    @Test
    public void testPushHistoryTokenIfNecessaryWithNullPropertyFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .pushHistoryTokenIfNecessary(
                    null,
                    HistoryContexts.fake()
                )
        );
    }

    private final static TextStyleProperty<TextAlign> PROPERTY = TextStylePropertyName.TEXT_ALIGN.setValue(TextAlign.LEFT);

    @Test
    public void testPushHistoryTokenIfNecessaryWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .pushHistoryTokenIfNecessary(
                    PROPERTY,
                    null
                )
        );
    }

    @Test
    public void testPushHistoryTokenIfNecessaryWithSame() {
        this.createComponent()
            .setValue(
                Optional.of(
                    TextStyle.EMPTY.set(
                        PROPERTY.name(),
                        PROPERTY.value()
                            .get()
                    )
                )
            ).pushHistoryTokenIfNecessary(
                PROPERTY,
                new FakeHistoryContext()
            );
    }

    @Test
    public void testPushHistoryTokenIfNecessaryWithDifferent() {
        final TextStyle textStyle = TextStyle.parse("color: #111;");

        final SpreadsheetId spreadsheetId = SpreadsheetId.with(1);
        final SpreadsheetName spreadsheetName = SpreadsheetName.with("SpreadsheetName111");

        final HistoryContext historyContext = new FakeHistoryContext() {
            @Override
            public HistoryToken historyToken() {
                return this.historyToken;
            }

            private HistoryToken historyToken = HistoryToken.spreadsheetSelect(
                spreadsheetId,
                spreadsheetName
            );

            @Override
            public void pushHistoryToken(final HistoryToken token) {
                this.historyToken = token;
            }
        };

        this.createComponent()
            .setValue(
                Optional.of(textStyle)
            ).pushHistoryTokenIfNecessary(
                PROPERTY,
                historyContext
            );

        this.checkEquals(
            HistoryToken.metadataPropertyStyleSave(
                spreadsheetId,
                spreadsheetName,
                PROPERTY.name(),
                PROPERTY.value()
            ),
            historyContext.historyToken()
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public TextStyleComponent createComponent() {
        return TextStyleComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleComponent> type() {
        return TextStyleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
