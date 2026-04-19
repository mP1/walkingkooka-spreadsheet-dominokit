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
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleLengthPropertyComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.length.TextStyleLengthPropertyComponentLikeTest.TestTextStyleLengthPropertyComponentLike;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextStyleLengthPropertyComponentLikeTest implements FormValueComponentTesting<HTMLFieldSetElement, Length<?>, TestTextStyleLengthPropertyComponentLike> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            new TestTextStyleLengthPropertyComponentLike()
                .setValue(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                ),
            "TestTextStyleLengthPropertyComponentLike\n" +
                "  LengthComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [12.5px] icons=mdi-close-circle id=TestIdPrefix123-textDecorationThickness-TextBox REQUIRED\n"
        );
    }

    @Override
    public TestTextStyleLengthPropertyComponentLike createComponent() {
        return new TestTextStyleLengthPropertyComponentLike();
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestTextStyleLengthPropertyComponentLike> type() {
        return TestTextStyleLengthPropertyComponentLike.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestTextStyleLengthPropertyComponentLike implements TextStyleLengthPropertyComponentLike<TestTextStyleLengthPropertyComponentLike> {

        TestTextStyleLengthPropertyComponentLike() {
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
