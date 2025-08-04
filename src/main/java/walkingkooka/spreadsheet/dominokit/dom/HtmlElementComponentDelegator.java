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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;

/**
 * A delegator that delegates a few {@link HtmlComponent} to a {@link HtmlElementComponent}.
 */
public interface HtmlElementComponentDelegator<E extends HTMLElement, C extends HtmlComponent<E, C>> extends HtmlComponent<E, C> {

    @Override
    default C setCssText(final String css) {
        this.htmlElementComponent()
            .setCssText(css);
        return (C) this;
    }

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        this.htmlElementComponent()
            .setCssProperty(
                name,
                value
            );
        return (C) this;
    }

    @Override
    default E element() {
        return this.htmlElementComponent()
            .element();
    }

    HtmlElementComponent<E, ?> htmlElementComponent();
}
