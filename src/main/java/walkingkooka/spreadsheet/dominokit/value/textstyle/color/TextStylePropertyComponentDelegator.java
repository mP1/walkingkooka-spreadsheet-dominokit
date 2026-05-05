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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Set;

public interface TextStylePropertyComponentDelegator<V, C extends TextStylePropertyComponent<HTMLFieldSetElement, V, C>>
    extends TextStylePropertyComponent<HTMLFieldSetElement, V, C>,
    FormValueComponentDelegator<HTMLFieldSetElement, V, C>,
    HasName<TextStylePropertyName<V>> {

    // TextStylePropertyComponent.......................................................................................

    @Override
    default boolean filterTest(final TextStylePropertyFilter filter) {
        return this.textStylePropertyComponent()
            .filterTest(filter);
    }

    @Override
    default Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return this.textStylePropertyComponent()
            .textStylePropertyFilterKinds();
    }

    // FormValueComponentDelegator......................................................................................

    @Override
    default TextStylePropertyName<V> name() {
        return this.textStylePropertyComponent()
            .name();
    }

    @Override
    default ValueWatcher<TextStyle> textStyleValueWatcher() {
        return this.textStylePropertyComponent()
            .textStyleValueWatcher();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.textStylePropertyComponent()
                .printTree(printer);
        }
        printer.outdent();
    }

    // FormValueComponentDelegator......................................................................................

    @Override
    default TextStylePropertyComponent<HTMLFieldSetElement, V, ?> formValueComponent() {
        return this.textStylePropertyComponent();
    }

    TextStylePropertyComponent<HTMLFieldSetElement, V, ?> textStylePropertyComponent();
}
