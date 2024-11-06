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

package walkingkooka.spreadsheet.dominokit.form;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.text.printer.TreePrintable;

/**
 * A form holds form components such as text boxes, links etc. Note it does not receive {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher}
 * events, it instead receives events to prepare or {@link #giveFocus(AppContext)} and {@link #refresh(AppContext)}.
 */
public interface SpreadsheetFormComponentLifecycle<E extends HTMLElement, C extends SpreadsheetFormComponentLifecycle<E, C>> extends HtmlElementComponent<E, C>, TreePrintable {

    void giveFocus(final AppContext context);

    void refresh(final AppContext context);
}
