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

package walkingkooka.spreadsheet.dominokit.valuetype;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.select.SelectComponentDelegator2;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.validation.ValueType;

import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link ValueType}.
 */
public final class ValueTypeEditComponent implements FormValueComponent<HTMLFieldSetElement, ValueType, ValueTypeEditComponent>,
    SelectComponentDelegator2<ValueType, ValueTypeEditComponent>,
    TreePrintable {

    public static ValueTypeEditComponent empty(final String id,
                                               final ValueTypeEditComponentContext context) {
        return new ValueTypeEditComponent(
            id,
            context
        );
    }

    private ValueTypeEditComponent(final String id,
                                   final ValueTypeEditComponentContext context) {
        final SelectComponent<ValueType> select = SelectComponent.empty(
            (v) -> {
                final ValueType n = v.orElseThrow(() -> new IllegalArgumentException("Missing ValueType"));
                final String nameText = n.text();

                return context.selectOption(
                    id + nameText + SpreadsheetElementIds.OPTION, // id
                    n.isAny() ?
                        "Any" :
                        CaseKind.KEBAB.change(
                            nameText,
                            CaseKind.TITLE
                        ), // text
                    v, // value
                    Optional.empty() // HistoryToken
                );
            }
        );

        select.appendOption(
            Optional.of(SpreadsheetValueType.ANY)
        );

        for (final ValueType typeName : SpreadsheetValueType.ALL_CELL_VALUE_TYPES) {
            select.appendOption(
                Optional.of(typeName)
            );
        }

        this.select = select;
        this.setId(id);
    }

    // SelectComponentDelegator.........................................................................................

    @Override
    public SelectComponent<ValueType> selectComponent() {
        return this.select;
    }

    private final SelectComponent<ValueType> select;

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