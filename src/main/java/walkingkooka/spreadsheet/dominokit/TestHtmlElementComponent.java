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

import elemental2.dom.HTMLElement;

/**
 * Interface to be implemented by test {@link HtmlElementComponent}.
 */
public interface TestHtmlElementComponent<E extends HTMLElement, C extends HtmlElementComponent<E, C>> extends HtmlElementComponent<E, C> {

    // isEditing........................................................................................................

    @Override
    default boolean isEditing() {
        return false;
    }

    // element..........................................................................................................

    @Override
    default E element() {
        throw new UnsupportedOperationException();
    }

    // setCssText.......................................................................................................

    @Override
    default C setCssText(final String css) {
        // ignore
        return (C) this;
    }

    // setCssProperty...................................................................................................

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        // ignore
        return (C) this;
    }
}
