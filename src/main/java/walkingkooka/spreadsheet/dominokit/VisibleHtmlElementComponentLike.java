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
 * A {@link Component} that adds a helpers to SET the visibility of this component.
 * It is assumed that a boolean field is also updated and used to track visibility. The copy under test/ will do nothing and
 * return this, allowing executing within a JVM.
 */
public interface VisibleHtmlElementComponentLike<E extends HTMLElement, C extends VisibleHtmlElementComponentLike<E, C>> extends HtmlElementComponent<E, C> {

    C setVisibility(final boolean visibility);
}
