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

package walkingkooka.spreadsheet.dominokit.query;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.select.SelectComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link SpreadsheetCellRangeReferencePath}.
 */
public final class SpreadsheetCellRangeReferencePathComponent implements FormValueComponent<HTMLFieldSetElement, SpreadsheetCellRangeReferencePath, SpreadsheetCellRangeReferencePathComponent>,
    SelectComponentDelegator<SpreadsheetCellRangeReferencePath, SpreadsheetCellRangeReferencePathComponent> {

    public static SpreadsheetCellRangeReferencePathComponent empty(final String id,
                                                                   final SpreadsheetCellRangeReferencePathComponentContext context) {
        return new SpreadsheetCellRangeReferencePathComponent(
            CharSequences.failIfNullOrEmpty(id, "id"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetCellRangeReferencePathComponent(final String id,
                                                       final SpreadsheetCellRangeReferencePathComponentContext context) {
        final SelectComponent<SpreadsheetCellRangeReferencePath> select = SelectComponent.empty(
            (v) -> {
                final SpreadsheetCellRangeReferencePath p = v.orElseThrow(() -> new IllegalArgumentException("Missing SpreadsheetCellRangeReferencePath"));

                return context.selectOption(
                    id + p.name() + SpreadsheetElementIds.OPTION, // id
                    p.labelText(), // text
                    v, // value
                    Optional.empty() // HistoryToken
                );
            }
        );

        for (final SpreadsheetCellRangeReferencePath path : SpreadsheetCellRangeReferencePath.values()) {
            select.appendOption(
                Optional.of(path) // value
            );
        }

        this.select = select;
        this.setId(id);
    }

    // SelectComponentDelegator.........................................................................................

    @Override
    public SelectComponent<SpreadsheetCellRangeReferencePath> selectComponent() {
        return this.select;
    }

    private final SelectComponent<SpreadsheetCellRangeReferencePath> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.select.printTree(printer);
        }
        printer.outdent();
    }
}