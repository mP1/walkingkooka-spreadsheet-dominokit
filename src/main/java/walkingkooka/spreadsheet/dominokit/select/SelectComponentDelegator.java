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

package walkingkooka.spreadsheet.dominokit.select;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

public interface SelectComponentDelegator<T, C extends FormValueComponent<HTMLFieldSetElement, T, C>> extends FormValueComponentDelegator<HTMLFieldSetElement, T, C> {

    default SelectComponent<T> appendOption(final Optional<T> value) {
        return this.selectComponent()
            .appendOption(value);
    }

    default SelectComponent<T> clearOptions() {
        return this.selectComponent()
            .clearOptions();
    }

    SelectComponent<T> selectComponent();

    // FormValueComponentDelegator......................................................................................

    @Override
    default FormValueComponent<HTMLFieldSetElement, T, ?> formValueComponent() {
        return this.selectComponent();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.selectComponent()
                .printTree(printer);
        }
        printer.outdent();
    }
}
