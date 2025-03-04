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
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;

/**
 * Holds a list of {@link AnchorComponent}, and is a dumb container and does not support refreshing all anchors. Refreshing
 * the anchors must be done externally.
 * This is useful for inserting links with commands such as SAVE, CLOSE at the bottom of a {@link walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent}.
 */
public final class SpreadsheetLinkListComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetLinkListComponent> {

    public static SpreadsheetLinkListComponent with(final String id) {
        return new SpreadsheetLinkListComponent(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private SpreadsheetLinkListComponent(final String id) {
        this.root = SpreadsheetFlexLayout.row()
            .setId(id);
    }

    public SpreadsheetLinkListComponent appendChild(final AnchorComponent<?, ?> anchor) {
        Objects.requireNonNull(anchor, "anchor");

        this.root.appendChild(anchor);
        return this;
    }

    /**
     * The parent holding the links.
     */
    private final SpreadsheetFlexLayout root;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetLinkListComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetLinkListComponent setCssProperty(final String name,
                                                       final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
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
