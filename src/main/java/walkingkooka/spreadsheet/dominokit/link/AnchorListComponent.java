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

package walkingkooka.spreadsheet.dominokit.link;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.text.printer.IndentingPrinter;

/**
 * Holds a list of {@link AnchorComponent}, and is a dumb container and does not support refreshing all anchors. Refreshing
 * the anchors must be done externally.
 * This is useful for inserting links with commands such as SAVE, CLOSE at the bottom of a {@link DialogComponent}.
 */
public final class AnchorListComponent implements HtmlComponentDelegator<HTMLDivElement, AnchorListComponent> {

    public static AnchorListComponent empty() {
        return new AnchorListComponent();
    }

    private AnchorListComponent() {
        this.root = FlexLayoutComponent.row();
    }

    public AnchorListComponent appendChild(final AnchorComponent<?, ?> anchor) {
        this.root.appendChild(anchor);
        return this;
    }

    public AnchorListComponent removeAllChildren() {
        this.root.removeAllChildren();
        return this;
    }

    /**
     * The parent holding the links.
     */
    private final FlexLayoutComponent root;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.root.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.root;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.root.printTree(printer);
        }
        printer.outdent();
    }
}
