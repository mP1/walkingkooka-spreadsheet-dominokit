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

package walkingkooka.spreadsheet.dominokit.history;

import org.dominokit.domino.ui.menu.AbstractMenuItem;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.HasSelectionListeners;
import walkingkooka.Cast;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * A {@link AbstractMenuItem} that includes a {@link HistoryTokenAnchorComponent},
 * a clickable link with text that will update the history token of the apps URL.
 */
public final class HistoryTokenMenuItem<T> extends MenuItem<T> implements TreePrintable {

    public HistoryTokenMenuItem(final HistoryTokenAnchorComponent anchor) {
        super(anchor.textContent()); // no text

        this.anchor = anchor;
    }

    public HistoryTokenAnchorComponent anchor() {
        return this.anchor;
    }

    private final HistoryTokenAnchorComponent anchor;

    public T addSelectionListener(final HasSelectionListeners.SelectionListener<?, T> selectionHandler) {
        return (T) this;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.anchor.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof HistoryTokenMenuItem &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final HistoryTokenMenuItem<?> other) {
        return this.anchor.equals(other.anchor);
    }

    @Override
    public String toString() {
        return this.anchor.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.anchor.printTree(printer);
    }
}
