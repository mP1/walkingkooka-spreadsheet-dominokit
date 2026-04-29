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

package walkingkooka.spreadsheet.dominokit.value.textstyle.length;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.text.TextComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyLengthComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.TextStylePropertyLengthComponentLikeTest.TestTextStylePropertyLengthComponentLike;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextStylePropertyLengthComponentLikeTest implements FormValueComponentTesting<HTMLFieldSetElement, Length<?>, TestTextStylePropertyLengthComponentLike> {

    @Test
    public void testSetLabelFromPropertyName() {
        this.treePrintAndCheck(
            new TestTextStylePropertyLengthComponentLike()
                .setLabelFromPropertyName(),
            "TestTextStylePropertyLengthComponentLike\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        Text Decoration Thickness [] icons=mdi-close-circle id=TestIdPrefix123-textDecorationThickness-TextBox REQUIRED\n" +
                "        Errors\n" +
                "          Empty \"text\"\n"
        );
    }

    @Test
    public void testSetInnerRight() {
        this.treePrintAndCheck(
            new TestTextStylePropertyLengthComponentLike()
                .setInnerRight(
                    TextComponent.with(
                            Optional.of("HelloInnerRight")
                    )
                ),
            "TestTextStylePropertyLengthComponentLike\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle id=TestIdPrefix123-textDecorationThickness-TextBox REQUIRED\n" +
                "          innerRight\n" +
                "            TextComponent\n" +
                "              \"HelloInnerRight\"\n" +
                "        Errors\n" +
                "          Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            new TestTextStylePropertyLengthComponentLike()
                .setValue(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                ),
            "TestTextStylePropertyLengthComponentLike\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [12.5px] icons=mdi-close-circle id=TestIdPrefix123-textDecorationThickness-TextBox REQUIRED\n"
        );
    }

    @Override
    public TestTextStylePropertyLengthComponentLike createComponent() {
        return new TestTextStylePropertyLengthComponentLike();
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestTextStylePropertyLengthComponentLike> type() {
        return TestTextStylePropertyLengthComponentLike.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestTextStylePropertyLengthComponentLike implements TextStylePropertyLengthComponentLike<TestTextStylePropertyLengthComponentLike> {

        TestTextStylePropertyLengthComponentLike() {
            this.setIdPrefix(
                "TestIdPrefix123-"
            );
        }

        @Override
        public LengthComponent lengthComponent() {
            return this.lengthComponent;
        }

        private final LengthComponent lengthComponent = LengthComponent.with(
            TextStylePropertyName.TEXT_INDENT
        );

        @Override
        public TextStylePropertyName<Length<?>> name() {
            return TextStylePropertyName.TEXT_DECORATION_THICKNESS;
        }
    }
}
