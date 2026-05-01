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
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class HtmlComponentTest implements ClassTesting2<HtmlComponent<?, ?>> {

    // setStyleProperty.................................................................................................

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
            testHtmlComponent.css
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
            testHtmlComponent.css
        );
    }

    final static class TestHtmlComponent extends FakeHtmlComponent<HTMLDivElement, TestHtmlComponent> {
        @Override
        public TestHtmlComponent setCssProperty(final String name,
                                                final String value) {
            this.css = name + ": " + value;
            return this;
        }

        @Override
        public TestHtmlComponent removeCssProperty(final String name) {
            this.css = "";
            return this;
        }

        String css;
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
