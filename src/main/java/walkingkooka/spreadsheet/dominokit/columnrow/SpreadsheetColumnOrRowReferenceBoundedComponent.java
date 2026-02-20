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

package walkingkooka.spreadsheet.dominokit.columnrow;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SuggestBoxComponent} that supports ONLY selecting a {@link SpreadsheetColumnOrRowReference} from the given {@link SpreadsheetColumnOrRowReferenceOrRange}.
 */
public final class SpreadsheetColumnOrRowReferenceBoundedComponent implements SuggestBoxComponentDelegator<HTMLFieldSetElement, SpreadsheetColumnOrRowReference, SpreadsheetColumnOrRowReferenceBoundedComponent>,
    TreePrintable {

    /**
     * A {@link SpreadsheetColumnOrRowReferenceBoundedComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static SpreadsheetColumnOrRowReferenceBoundedComponent empty(final SpreadsheetColumnOrRowReferenceBoundedComponentContext context) {
        return new SpreadsheetColumnOrRowReferenceBoundedComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetColumnOrRowReferenceBoundedComponent(final SpreadsheetColumnOrRowReferenceBoundedComponentContext context) {
        this.suggestBox = SuggestBoxComponent.with(
            context,
            context::createMenuItem
        );
    }

    @Override
    public SpreadsheetColumnOrRowReferenceBoundedComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceBoundedComponent blur() {
        this.suggestBox.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.suggestBox.isEditing();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetColumnOrRowReferenceBoundedComponent setValue(final Optional<SpreadsheetColumnOrRowReference> spreadsheetColumnOrRowReference) {
        Objects.requireNonNull(spreadsheetColumnOrRowReference, "spreadsheetColumnOrRowReference");

        this.suggestBox.setValue(
            spreadsheetColumnOrRowReference
        );
        return this;
    }

    @Override //
    public Optional<SpreadsheetColumnOrRowReference> value() {
        return this.suggestBox.value();
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<SpreadsheetColumnOrRowReference> watcher) {
        return this.suggestBox.addValueWatcher(watcher);
    }

    // SuggestBoxComponentDelegator.....................................................................................

    @Override
    public SuggestBoxComponent<SpreadsheetColumnOrRowReference> suggestBoxComponent() {
        return this.suggestBox;
    }

    private final SuggestBoxComponent<SpreadsheetColumnOrRowReference> suggestBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.suggestBox.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.suggestBox.printTree(printer);
        }
        printer.outdent();
    }
}
