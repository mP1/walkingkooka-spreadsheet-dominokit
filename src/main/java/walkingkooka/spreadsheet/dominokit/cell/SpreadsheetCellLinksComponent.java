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

package walkingkooka.spreadsheet.dominokit.cell;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * A container that holds a few links CREATE LABELS, LABELS, REFERENCES and DELETE for a {@link SpreadsheetExpressionReference}.
 */
public final class SpreadsheetCellLinksComponent implements HtmlComponent<HTMLDivElement, SpreadsheetCellLinksComponent> {

    public static SpreadsheetCellLinksComponent empty(final String id,
                                                      final SpreadsheetCellLinksComponentContext context) {
        return new SpreadsheetCellLinksComponent(
            id,
            context
        );
    }

    private SpreadsheetCellLinksComponent(final String id,
                                          final SpreadsheetCellLinksComponentContext context) {
        this.value = SpreadsheetCellValueAnchorComponent.with(
            id + "value" + SpreadsheetElementIds.LINK,
            context
        );

        this.createLabel = SpreadsheetCellCreateLabelSelectAnchorComponent.with(
            id + "createLabel" + SpreadsheetElementIds.LINK,
            context
        );

        this.labels = SpreadsheetCellLabelsAnchorComponent.with(
            id + "label" + SpreadsheetElementIds.LINK,
            context
        );

        this.references = SpreadsheetCellReferencesAnchorComponent.with(
            id + "references" + SpreadsheetElementIds.LINK,
            context
        );

        this.delete = SpreadsheetCellDeleteAnchorComponent.with(
            id + "delete" + SpreadsheetElementIds.LINK,
            context
        );

        this.root = SpreadsheetLinkListComponent.empty()
            .appendChild(this.value)
            .appendChild(this.createLabel)
            .appendChild(this.labels)
            .appendChild(this.references)
            .appendChild(this.delete);

        this.clearValue();
    }

    public Optional<SpreadsheetExpressionReference> value() {
        return this.createLabel.value();
    }

    public SpreadsheetCellLinksComponent setValue(final Optional<SpreadsheetExpressionReference> value) {
        Objects.requireNonNull(value, "value");

        this.value.setValue(value)
            .setTextContent("Value");
        this.createLabel.setValue(value)
            .setTextContent("Create Label");
        this.labels.setValue(value)
            .setTextContent("Labels");
        this.references.setValue(value)
            .setTextContent("References");
        this.delete.setValue(value)
            .setTextContent("Delete");

        return this;
    }

    public SpreadsheetCellLinksComponent clearValue() {
        return this.setValue(Optional.empty());
    }

    private final SpreadsheetCellValueAnchorComponent value;

    private final SpreadsheetCellCreateLabelSelectAnchorComponent createLabel;

    private final SpreadsheetCellLabelsAnchorComponent labels;

    private final SpreadsheetCellReferencesAnchorComponent references;

    private final SpreadsheetCellDeleteAnchorComponent delete;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetCellLinksComponent setCssText(final String css) {
        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetCellLinksComponent setCssProperty(final String name,
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
