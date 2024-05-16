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

package walkingkooka.spreadsheet.dominokit.ui.datatable;

import elemental2.dom.Node;
import org.dominokit.domino.ui.datatable.CellRenderer;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.text.SpreadsheetTextComponent;

import java.util.function.BiFunction;

/**
 * A {@link CellRenderer} that acts as a bridge to a function which returns a {@link HtmlElementComponent}.
 */
final class SpreadsheetDataTableComponentCellRenderer<T> implements CellRenderer<T> {

    static <T> SpreadsheetDataTableComponentCellRenderer<T> with(final int columnNumber,
                                                                 final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        return new SpreadsheetDataTableComponentCellRenderer(
                columnNumber,
                cellRenderer
        );
    }

    private SpreadsheetDataTableComponentCellRenderer(final int columnNumber,
                                                      final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        this.columnNumber = columnNumber;
        this.cellRenderer = cellRenderer;
    }

    @Override
    public Node asElement(final CellInfo<T> cellInfo) {
        final HtmlElementComponent<?, ?> component = this.cellRenderer.apply(
                this.columnNumber,
                cellInfo.getRecord()
        );

        Node node;

        // special case we dont want the element only the TextNode.
        if (component instanceof SpreadsheetTextComponent) {
            SpreadsheetTextComponent spreadsheetTextComponent = (SpreadsheetTextComponent) component;
            node = Doms.textNode(
                    spreadsheetTextComponent.value()
                            .orElse("")
            );
        } else {
            node = component.element();
        }

        return node;
    }

    private final Integer columnNumber;

    /**
     * Provides the source {@link HtmlElementComponent} which will appear in the requested column.
     */
    private final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return "column: " + this.columnNumber;
    }
}
