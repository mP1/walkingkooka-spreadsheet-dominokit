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
import elemental2.dom.Node;
import org.dominokit.domino.ui.layout.AppLayout;
import walkingkooka.spreadsheet.dominokit.form.SpreadsheetFormComponentLifecycle;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;

/**
 * A {@link OpenableComponent} for a {@link AppLayout} right drawer.
 */
final class SpreadsheetAppLayoutDrawerComponentRight<E extends HTMLElement> implements SpreadsheetFormComponentLifecycle<E, SpreadsheetAppLayoutDrawerComponentRight<E>> {

    static <E extends HTMLElement> SpreadsheetAppLayoutDrawerComponentRight<E> with(final AppLayout appLayout,
                                                                                    final SpreadsheetFormComponentLifecycle<E, ?> form) {
        return new SpreadsheetAppLayoutDrawerComponentRight(
                Objects.requireNonNull(appLayout, "appLayout"),
                Objects.requireNonNull(form, "form")
        );
    }

    private SpreadsheetAppLayoutDrawerComponentRight(final AppLayout appLayout,
                                                     final SpreadsheetFormComponentLifecycle<E, ?> form) {
        this.appLayout = appLayout;
        this.form = form;
    }

    @Override
    public SpreadsheetAppLayoutDrawerComponentRight<E> setCssText(final String css) {
        this.form.setCssText(css);
        return this;
    }

    @Override
    public Node node() {
        return this.form.node();
    }

    @Override
    public E element() {
        return this.form.element();
    }

    // SpreadsheetFormComponentLifecycle...............................................................................

    @Override
    public boolean isOpen() {
        return this.appLayout.isRightDrawerOpen();
    }

    @Override
    public void open(final AppContext context) {
        this.appLayout.showRightDrawer();
        this.form.open(context);
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        this.form.openGiveFocus(context);
    }

    @Override
    public void refresh(final AppContext context) {
        this.form.refresh(context);
    }

    @Override
    public void close(final AppContext context) {
        this.appLayout.hideRightDrawer();
        this.form.close(context);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.form.printTree(printer);
        }
        printer.outdent();
    }

    private final AppLayout appLayout;

    private final SpreadsheetFormComponentLifecycle<E, ?> form;

    @Override
    public String toString() {
        return this.appLayout.toString();
    }
}
