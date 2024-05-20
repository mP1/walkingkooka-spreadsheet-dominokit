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

package walkingkooka.spreadsheet.dominokit.ui;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.dom.Doms;

public interface VisibleHtmlElementComponent<E extends HTMLElement, C extends VisibleHtmlElementComponent<E, C>> extends VisibleHtmlElementComponentLike<E, C> {

    @Override
    default C setVisibility(final boolean visibility) {
        Doms.setVisibility(
                this.element(),
                visibility
        );
        return (C) this;
    }
}
