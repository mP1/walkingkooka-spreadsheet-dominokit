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
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.text.TextComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLikeTest.TestTextStylePropertyValueTextBoxComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public final class TextStylePropertyValueTextBoxComponentLikeTest implements TextStylePropertyValueTextBoxComponentLikeTesting<TestTextStylePropertyValueTextBoxComponentLike, Color> {

    @Test
    public void testSetLabelFromPropertyName() {
        this.treePrintAndCheck(
            new TestTextStylePropertyValueTextBoxComponentLike()
                .setLabelFromPropertyName(),
            "TestTextStylePropertyValueTextBoxComponentLike\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Color [] icons=mdi-close-circle id=TestIdPrefix123-color-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testSetInnerRight() {
        this.treePrintAndCheck(
            new TestTextStylePropertyValueTextBoxComponentLike()
                .setInnerRight(
                    TextComponent.with(
                            Optional.of("HelloInnerRight")
                    )
                ),
            "TestTextStylePropertyValueTextBoxComponentLike\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-color-TextBox REQUIRED\n" +
                "        innerRight\n" +
                "          TextComponent\n" +
                "            \"HelloInnerRight\"\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            new TestTextStylePropertyValueTextBoxComponentLike()
                .setValue(
                    Optional.of(
                        Color.BLACK
                    )
                ),
            "TestTextStylePropertyValueTextBoxComponentLike\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [black] icons=mdi-close-circle id=TestIdPrefix123-color-TextBox REQUIRED\n"
        );
    }

    @Override
    public TestTextStylePropertyValueTextBoxComponentLike createComponent() {
        return new TestTextStylePropertyValueTextBoxComponentLike();
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestTextStylePropertyValueTextBoxComponentLike> type() {
        return TestTextStylePropertyValueTextBoxComponentLike.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestTextStylePropertyValueTextBoxComponentLike implements TextStylePropertyValueTextBoxComponentLike<TestTextStylePropertyValueTextBoxComponentLike, Color> {

        TestTextStylePropertyValueTextBoxComponentLike() {
            this.setIdPrefix(
                "TestIdPrefix123-",
                SpreadsheetElementIds.TEXT_BOX
            );
        }

        @Override
        public ValueTextBoxComponent<Color> textStylePropertyValueTextBoxComponentLike() {
            return this.valueTextBoxComponent;
        }

        private final ValueTextBoxComponent<Color> valueTextBoxComponent = ValueTextBoxComponent.with(
            Color::parse,
            Color::text
        );

        @Override
        public TextStylePropertyName<Color> name() {
            return TextStylePropertyName.COLOR;
        }

        @Override
        public boolean filterTest(final TextStylePropertyFilter filter) {
            return filter.testComponent(this);
        }

        @Override
        public Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
            return EnumSet.of(
                TextStylePropertyFilterKind.COLOR
            );
        }
    }
}
