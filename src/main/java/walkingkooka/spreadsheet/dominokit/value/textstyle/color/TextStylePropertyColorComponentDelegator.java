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
import walkingkooka.color.Color;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyColorComponentLike;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStyleDialogComponentFilter;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public interface TextStylePropertyColorComponentDelegator<C extends TextStylePropertyColorComponentDelegator<C>>
    extends TextStylePropertyColorComponentLike<HTMLFieldSetElement, C>,
    FormValueComponentDelegator<HTMLFieldSetElement, Color, C>,
    HasName<TextStylePropertyName<Color>> {

    // TextStylePropertyComponent.......................................................................................

    @Override
    default boolean filterTest(final TextStyleDialogComponentFilter filter) {
        return this.textStylePropertyColorComponent()
            .filterTest(filter);
    }

    // FormValueComponentDelegator......................................................................................

    default C setIdPrefix(final String idPrefix) {
        return this.setIdPrefix(
            idPrefix,
            SpreadsheetElementIds.TEXT_BOX
        );
    }

    @Override
    default TextStylePropertyName<Color> name() {
        return this.textStylePropertyColorComponent()
            .name();
    }

    @Override
    default ValueWatcher<TextStyle> textStyleValueWatcher() {
        return this.textStylePropertyColorComponent()
            .textStyleValueWatcher();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.textStylePropertyColorComponent()
                .printTree(printer);
        }
        printer.outdent();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    default TextStylePropertyColorComponent formValueComponent() {
        return this.textStylePropertyColorComponent();
    }

    TextStylePropertyColorComponent textStylePropertyColorComponent();
}
