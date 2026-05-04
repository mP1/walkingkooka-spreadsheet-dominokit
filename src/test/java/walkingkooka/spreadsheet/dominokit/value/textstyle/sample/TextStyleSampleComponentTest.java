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

package walkingkooka.spreadsheet.dominokit.value.textstyle.sample;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public final class TextStyleSampleComponentTest implements ValueComponentTesting<HTMLDivElement, TextStyle, TextStyleSampleComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent(),
            "TextStyleSampleComponent\n" +
                "  DIV\n" +
                "    \"The quick brown fox jumps over the lazy dog\"\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextStyle.parse("background-color: #111; color: #222; margin-left: 1px; padding-left: 2px;")
                    )
                ),
            "TextStyleSampleComponent\n" +
                "  DIV\n" +
                "    style=\"background-color: #111111; color: #222222; margin-left: 1px; padding-left: 2px;\"\n" +
                "      \"The quick brown fox jumps over the lazy dog\"\n"
        );
    }


    @Override
    public TextStyleSampleComponent createComponent() {
        return TextStyleSampleComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleSampleComponent> type() {
        return TextStyleSampleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
