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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.textstyle.color.TextStylePropertyColorComponentDelegatorTest.TestTextStylePropertyColorComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;

public final class TextStylePropertyColorComponentDelegatorTest implements HtmlComponentTesting<TestTextStylePropertyColorComponentDelegator, HTMLFieldSetElement> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestTextStylePropertyColorComponentDelegator> type() {
        return TestTextStylePropertyColorComponentDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestTextStylePropertyColorComponentDelegator implements TextStylePropertyColorComponentDelegator<TestTextStylePropertyColorComponentDelegator> {
        @Override
        public TextStylePropertyColorComponent textStylePropertyColorComponent() {
            return TextStylePropertyColorComponent.with(
                "TestIdPrefix-",
                TextStylePropertyName.COLOR,
                new FakeTextStylePropertyColorComponentContext()
            );
        }
    }
}
