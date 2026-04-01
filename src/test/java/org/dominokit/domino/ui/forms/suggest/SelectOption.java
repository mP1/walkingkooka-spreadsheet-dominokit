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

package org.dominokit.domino.ui.forms.suggest;

import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.menu.AbstractMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenMenuItem;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

public class SelectOption<V> extends Option<V, DivElement, SelectOption<V>> implements TreePrintable {

    public static <V> SelectOption<V> create(
        final String key,
        final V value,
        final OptionSupplier<DivElement, V> componentSupplier,
        final OptionSupplier<AbstractMenuItem<V>, V> menuItemSupplier) {
        return new SelectOption<>(
            key,
            value,
            componentSupplier,
            menuItemSupplier
        );
    }

    private SelectOption(final String key,
                         final V value,
                         final OptionSupplier<DivElement, V> componentSupplier,
                         final OptionSupplier<AbstractMenuItem<V>, V> menuItemSupplier) {
        this.value = value;
        this.menuItem = menuItemSupplier.get(key, value);
    }

    public String getTextContent() {
        final HistoryTokenMenuItem<V> historyTokenMenuItem = (HistoryTokenMenuItem<V>)this.menuItem;
        return historyTokenMenuItem.getText();
    }

    private final AbstractMenuItem<V> menuItem;

    public V getValue() {
        return this.value;
    }

    private final V value;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        TreePrintable.printTreeOrToString(
            this.menuItem,
            printer
        );
    }
}
