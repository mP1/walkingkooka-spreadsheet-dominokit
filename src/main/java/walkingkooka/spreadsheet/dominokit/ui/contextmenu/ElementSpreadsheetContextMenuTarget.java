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

package walkingkooka.spreadsheet.dominokit.ui.contextmenu;

import elemental2.dom.Element;

import java.util.Objects;

/**
 * A {@link SpreadsheetContextMenuTarget} that wraps an {@link Element}.
 */
public final class ElementSpreadsheetContextMenuTarget implements SpreadsheetContextMenuTarget<Element> {

    static ElementSpreadsheetContextMenuTarget with(final Element element) {
        Objects.requireNonNull(element, "element");

        return new ElementSpreadsheetContextMenuTarget(element);
    }

    private ElementSpreadsheetContextMenuTarget(final Element element) {
        this.element = element;
    }

    // SpreadsheetContextMenuTarget.....................................................................................

    @Override
    public void setSpreadsheetContextMenu(final SpreadsheetContextMenu menu) {
        // TODO
    }

    // IsElement........................................................................................................

    @Override
    public Element element() {
        return this.element;
    }

    private final Element element;

    // toString..........................................................................................................

    @Override
    public String toString() {
        return this.element.toString();
    }
}
