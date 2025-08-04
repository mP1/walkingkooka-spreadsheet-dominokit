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
 * A form holds form components such as text boxes, links etc. Note it does not receive {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher}
 * events, it instead receives events to prepare or {@link #openGiveFocus(RefreshContext)} and {@link #refresh(RefreshContext)}.
 */
public interface SpreadsheetFormComponentLifecycle<E extends HTMLElement, C extends SpreadsheetFormComponentLifecycle<E, C>> extends HtmlComponent<E, C>,
    ComponentLifecycle {

    @Override
    default void refreshIfOpen(final RefreshContext context) {
        if (this.isOpen()) {
            this.refresh(context);
        }
    }
}
