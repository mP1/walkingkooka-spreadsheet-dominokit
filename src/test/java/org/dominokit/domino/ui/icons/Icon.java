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

package org.dominokit.domino.ui.icons;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.IsElement;

public class Icon<T extends Icon<T>> implements IsElement<HTMLElement> {

    public Icon(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private final String name;

    public IsElement<HTMLElement> cssText(final String css) {
        // ignored
        return (T) this;
    }

    @Override
    public HTMLElement element() {
        throw new UnsupportedOperationException();
    }
}
