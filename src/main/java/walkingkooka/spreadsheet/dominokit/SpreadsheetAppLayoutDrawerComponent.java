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
import org.dominokit.domino.ui.layout.AppLayout;
import walkingkooka.text.printer.IndentingPrinter;

/**
 * A {@link SpreadsheetFormComponentLifecycle} for a {@link AppLayout} drawer.
 */
abstract class SpreadsheetAppLayoutDrawerComponent<E extends HTMLElement, T extends SpreadsheetAppLayoutDrawerComponent<E, T>>
    implements SpreadsheetFormComponentLifecycle<E, T>,
    HtmlComponentDelegator<E, T> {

    SpreadsheetAppLayoutDrawerComponent(final AppLayout appLayout,
                                        final SpreadsheetFormComponentLifecycle<E, ?> form) {
        this.appLayout = appLayout;
        this.form = form;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public final HtmlComponent<E, ?> htmlComponent() {
        return this.form;
    }

    // SpreadsheetFormComponentLifecycle...............................................................................

    @Override
    public final void open(final RefreshContext context) {
        this.showDrawer();
        this.form.open(context);
    }

    abstract void showDrawer();

    @Override
    public final void openGiveFocus(final RefreshContext context) {
        this.form.openGiveFocus(context);
    }

    @Override
    public final void refresh(final RefreshContext context) {
        this.form.refresh(context);
    }

    @Override
    public final void close(final RefreshContext context) {
        this.hideDrawer();
        this.form.close(context);
    }

    abstract void hideDrawer();

    // isEditing........................................................................................................

    @Override
    public final boolean isEditing() {
        return false; // too complex just say no
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.form.printTree(printer);
        }
        printer.outdent();
    }

    final AppLayout appLayout;

    private final SpreadsheetFormComponentLifecycle<E, ?> form;

    @Override
    public final String toString() {
        return this.appLayout.toString();
    }
}
