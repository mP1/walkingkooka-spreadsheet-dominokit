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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.Padding;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public final class HtmlComponentTest implements ClassTesting2<HtmlComponent<?, ?>> {

    // setStyleProperty.................................................................................................

    @Test
    public void testSetStylePropertyWithBorderDifferentValues() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER,
            Border.parse(
                "top-color: BLACK; top-style: SOLID; top-width: 1px;" +
                    "right-color: WHITE; right-style: DOTTED; right-width: 2px;" +
                    "bottom-color: RED; bottom-style: DASHED; bottom-width: 3px;" +
                    "left-color: BLUE; left-style: DOUBLE; left-width: 4px;"
                ),
            "border-top-color: black; border-top-style: SOLID; border-top-width: 1px; " +
                "border-right-color: white; border-right-style: DOTTED; border-right-width: 2px; " +
                "border-bottom-color: red; border-bottom-style: DASHED; border-bottom-width: 3px; " +
                "border-left-color: blue; border-left-style: DOUBLE; border-left-width: 4px"
        );
    }

    @Test
    public void testSetStylePropertyWithBorderEmpty() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER,
            Border.parse(
                ""
            ),
            ""
        );
    }

    @Test
    public void testSetStylePropertyWithBorderSame() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER,
            Border.parse(
                "BLACK SOLID 1px"
            ),
            "border-top-color: black; border-top-style: SOLID; border-top-width: 1px; " +
                "border-right-color: black; border-right-style: SOLID; border-right-width: 1px; " +
                "border-bottom-color: black; border-bottom-style: SOLID; border-bottom-width: 1px; " +
                "border-left-color: black; border-left-style: SOLID; border-left-width: 1px"
        );
    }

    @Test
    public void testSetStylePropertyWithBorderBottom() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER_BOTTOM,
            Border.parse(
                "BLACK SOLID 1px"
            ),
            "border-bottom-color: black; border-bottom-style: SOLID; border-bottom-width: 1px"
        );
    }

    @Test
    public void testSetStylePropertyWithBorderLeft() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER_LEFT,
            Border.parse(
                "BLACK SOLID 1px"
            ),
            "border-left-color: black; border-left-style: SOLID; border-left-width: 1px"
        );
    }

    @Test
    public void testSetStylePropertyWithBorderRight() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER_RIGHT,
            Border.parse(
                "BLACK SOLID 1px"
            ),
            "border-right-color: black; border-right-style: SOLID; border-right-width: 1px"
        );
    }

    @Test
    public void testSetStylePropertyWithBorderTop() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.BORDER_TOP,
            Border.parse(
                "BLACK SOLID 1px"
            ),
            "border-top-color: black; border-top-style: SOLID; border-top-width: 1px"
        );
    }

    @Test
    public void testSetStylePropertyWithColor() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.COLOR,
            Color.BLACK,
            "color: black"
        );
    }

    @Test
    public void testSetStylePropertyWithEnum() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT,
            "text-align: LEFT"
        );
    }

    @Test
    public void testSetStylePropertyWithLength() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(1.0),
            "margin-top: 1px"
        );
    }

    @Test
    public void testSetStylePropertyWithLength2() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(2.5),
            "margin-top: 2.5px"
        );
    }

    @Test
    public void testSetStylePropertyWithMargin() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.MARGIN,
            Margin.parse(
                "1px 2px 3px 4px"
            ),
            "margin-top: 1px; margin-right: 2px; margin-bottom: 3px; margin-left: 4px"
        );
    }

    @Test
    public void testSetStylePropertyWithPadding() {
        this.setStylePropertyAndCheck(
            TextStylePropertyName.PADDING,
            Padding.parse(
                "1px 2px 3px 4px"
            ),
            "padding-top: 1px; padding-right: 2px; padding-bottom: 3px; padding-left: 4px"
        );
    }

    private <T> void setStylePropertyAndCheck(final TextStylePropertyName<T> name,
                                              final T value,
                                              final String expected) {
        final TestHtmlComponent testHtmlComponent = new TestHtmlComponent();
        testHtmlComponent.setStyleProperty(
            name,
            value
        );

        this.checkEquals(
            expected,
            testHtmlComponent.text()
        );
    }
    
    // setOrRemoveStyleProperty.........................................................................................

    @Test
    public void testSetOrRemoveStylePropertyWithColorValue() {
        this.setOrRemoveStylePropertyAndCheck(
            TextStylePropertyName.COLOR,
            Color.BLACK,
            "color: black"
        );
    }

    @Test
    public void testSetOrRemoveStylePropertyWithColorMissing() {
        this.setOrRemoveStylePropertyAndCheck(
            TextStylePropertyName.COLOR,
            ""
        );
    }

    @Test
    public void testSetOrRemoveStylePropertyWithEnumValue() {
        this.setOrRemoveStylePropertyAndCheck(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT,
            "text-align: LEFT"
        );
    }

    @Test
    public void testSetOrRemoveStylePropertyWithLengthValue() {
        this.setOrRemoveStylePropertyAndCheck(
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(1.0),
            "margin-top: 1px"
        );
    }

    @Test
    public void testSetOrRemoveStylePropertyWithLengthValue2() {
        this.setOrRemoveStylePropertyAndCheck(
            TextStylePropertyName.MARGIN_TOP,
            Length.pixel(2.5),
            "margin-top: 2.5px"
        );
    }

    private <T> void setOrRemoveStylePropertyAndCheck(final TextStylePropertyName<T> name,
                                                      final String expected) {
        this.setOrRemoveStylePropertyAndCheck(
            name,
            Optional.empty(),
            expected
        );
    }

    private <T> void setOrRemoveStylePropertyAndCheck(final TextStylePropertyName<T> name,
                                                      final T value,
                                                      final String expected) {
        this.setOrRemoveStylePropertyAndCheck(
            name,
            Optional.of(value),
            expected
        );
    }

    private <T> void setOrRemoveStylePropertyAndCheck(final TextStylePropertyName<T> name,
                                                      final Optional<T> value,
                                                      final String expected) {
        final TestHtmlComponent testHtmlComponent = new TestHtmlComponent();
        testHtmlComponent.setOrRemoveStyleProperty(
            name,
            value
        );

        this.checkEquals(
            expected,
            testHtmlComponent.text()
        );
    }

    final static class TestHtmlComponent extends FakeHtmlComponent<HTMLDivElement, TestHtmlComponent> {
        @Override
        public TestHtmlComponent setCssProperty(final String name,
                                                final String value) {
            this.nameToValue.put(
                name,
                value
            );
            return this;
        }

        @Override
        public TestHtmlComponent removeCssProperty(final String name) {
            this.nameToValue.remove(name);
            return this;
        }

        String text() {
            return this.nameToValue.entrySet()
                .stream()
                .map((Entry<String, String> nameAndValue) -> nameAndValue.getKey() + ": " + nameAndValue.getValue())
                .collect(Collectors.joining("; "));
        }

        private final Map<String, String> nameToValue = Maps.ordered();
    }

    // class............................................................................................................

    @Override
    public Class<HtmlComponent<?, ?>> type() {
        return Cast.to(HtmlComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
