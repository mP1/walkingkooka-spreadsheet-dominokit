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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLDivElement;
import walkingkooka.naming.HasName;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public interface TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<V extends Enum<V>, C extends ValueComponentDelegator<HTMLDivElement, V, C>>
    extends ValueComponentDelegator<HTMLDivElement, V, C>,
    HasName<TextStylePropertyName<V>> {

    @Override
    default TextStylePropertyName<V> name() {
        return this.textStylePropertyNameEnumHistoryTokenAnchorListComponent()
            .name();
    }

    default ValueWatcher<TextStyle> textStyleValueWatcher() {
        return this.textStylePropertyNameEnumHistoryTokenAnchorListComponent()
            .textStyleValueWatcher();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.htmlComponent()
                .printTree(printer);
        }
        printer.outdent();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    default TextStylePropertyNameEnumHistoryTokenAnchorListComponent<V> valueComponent() {
        return this.textStylePropertyNameEnumHistoryTokenAnchorListComponent();
    }

    TextStylePropertyNameEnumHistoryTokenAnchorListComponent<V> textStylePropertyNameEnumHistoryTokenAnchorListComponent();
}
