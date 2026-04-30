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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.tree.text.FontSize;

import java.util.Optional;

public final class FontSizeComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, FontSize, FontSizeComponent>,
    FormValueComponentTesting<HTMLFieldSetElement, FontSize, FontSizeComponent> {

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "FontSizeComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [] REQUIRED\n" +
                "    Errors\n" +
                "      Required\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        FontSize.with(10)
                    )
                ),
            "FontSizeComponent\n" +
                "  SuggestBoxComponent\n" +
                "    [10] REQUIRED\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final FontSizeComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<FontSize> value) {
                    FontSizeComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        final FontSize value = FontSize.with(123);

        component.setValue(
            Optional.of(value)
        );

        this.checkEquals(
            value,
            this.fired,
            "fired value"
        );
    }

    private FontSize fired;

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    // ValueComponent...................................................................................................

    @Override
    public FontSizeComponent createComponent() {
        return FontSizeComponent.empty(
            new FakeFontSizeComponentContext() {

                @Override
                public Optional<FontSize> toValue(final FontSize fontSize) {
                    return Optional.of(fontSize);
                }

                @Override
                public void verifyOption(final FontSize value,
                                         final SuggestBoxComponent<FontSize> suggestBox) {
                    suggestBox.setVerifiedOption(value);
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<FontSizeComponent> type() {
        return Cast.to(FontSizeComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
