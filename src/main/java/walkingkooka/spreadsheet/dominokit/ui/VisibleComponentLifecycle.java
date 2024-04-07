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
import walkingkooka.spreadsheet.dominokit.AppContext;

/**
 * A {@link ComponentLifecycle} that is visible when open and hidden when closed.
 * Sub-classes must implement the remaining {@link ComponentLifecycle} methods that match {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} and {@link #refresh(AppContext)}.
 */
public interface VisibleComponentLifecycle<E extends HTMLElement, C extends VisibleComponentLifecycle<E, C>> extends ComponentLifecycle,
        HtmlElementComponent<E, C> {

    @Override
    default boolean isOpen() {
        return false == "hidden".equals(
                this.element()
                        .style
                        .visibility
        );
    }

    @Override
    default void open(final AppContext context) {
        this.setVisibility(true);
    }

    @Override
    default void close(final AppContext context) {
        this.setVisibility(false);
    }
}
