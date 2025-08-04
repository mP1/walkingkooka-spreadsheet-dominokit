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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.HTMLDivElement;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellReferencesAnchorComponent;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * A container that holds a few links DELETE and CELL REFERENCES for a {@link SpreadsheetLabelName}.
 */
public final class SpreadsheetLabelLinksComponent implements HtmlComponent<HTMLDivElement, SpreadsheetLabelLinksComponent> {

    public static SpreadsheetLabelLinksComponent empty(final String id,
                                                       final SpreadsheetLabelLinksComponentContext context) {
        return new SpreadsheetLabelLinksComponent(
            id,
            context
        );
    }

    private SpreadsheetLabelLinksComponent(final String id,
                                           final SpreadsheetLabelLinksComponentContext context) {
        this.references = SpreadsheetCellReferencesAnchorComponent.with(
            id + "references" + SpreadsheetElementIds.LINK,
            context
        );

        this.delete = SpreadsheetLabelDeleteAnchorComponent.with(
            id + "delete" + SpreadsheetElementIds.LINK,
            context
        );

        this.root = SpreadsheetLinkListComponent.empty()
            .appendChild(this.references)
            .appendChild(this.delete);

        this.clearValue();
    }

    public Optional<SpreadsheetLabelName> value() {
        return this.delete.value();
    }

    public SpreadsheetLabelLinksComponent setValue(final Optional<SpreadsheetLabelName> value) {
        Objects.requireNonNull(value, "value");

        this.references.setValue(
            Cast.to(value)
        ).setTextContent("References");
        this.delete.setValue(value)
            .setTextContent("Delete");

        return this;
    }

    public SpreadsheetLabelLinksComponent clearValue() {
        return this.setValue(Optional.empty());
    }

    private final SpreadsheetCellReferencesAnchorComponent references;

    private final SpreadsheetLabelDeleteAnchorComponent delete;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetLabelLinksComponent setCssText(final String css) {
        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetLabelLinksComponent setCssProperty(final String name,
                                                         final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.root.isEditing();
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    private final SpreadsheetLinkListComponent root;

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

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.root.toString();
    }
}
