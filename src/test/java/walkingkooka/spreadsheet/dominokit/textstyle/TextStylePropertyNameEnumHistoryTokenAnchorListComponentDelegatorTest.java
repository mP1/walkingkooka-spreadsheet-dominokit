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

package walkingkooka.spreadsheet.dominokit.textstyle;

import elemental2.dom.HTMLDivElement;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegatorTest implements HtmlComponentTesting<TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign>, HTMLDivElement> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign>> type() {
        return Cast.to(TextStylePropertyNameEnumHistoryTokenAnchorListComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    final static class TestTextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator implements HtmlComponent<HTMLDivElement, TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign>>,
        TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<TextAlign>,
        HasName<TextStylePropertyName<TextAlign>> {

        @Override
        public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
            return this.component;
        }
        
        private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextAlign> component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            "Test-",
            TextStylePropertyName.TEXT_ALIGN,
            Lists.of(TextAlign.values()),
            (n) -> n.map(TextAlign::name).orElse("Clear"),
            (n) -> Optional.empty(),
            new FakeTextStylePropertyNameEnumHistoryTokenAnchorListComponentContext()
        );
    }
}
