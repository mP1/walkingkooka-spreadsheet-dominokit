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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.HTMLElement;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponentDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.function.Predicate;

/**
 * Base class for all TABLE CELL (TD) components within a {@link SpreadsheetViewportComponentTable}
 */
abstract class SpreadsheetViewportComponentTableCell<E extends HTMLElement, C extends SpreadsheetViewportComponentTableCell<E, C>> implements HtmlComponent<E, C>,
    HtmlElementComponentDelegator<E, C> {

    SpreadsheetViewportComponentTableCell() {
        super();
    }

    abstract void setIdAndName(final SpreadsheetId id,
                               final SpreadsheetName name);

    final void refresh(final SpreadsheetViewportComponentTableContext context) {
        this.refresh(
            context.historyToken()
                .anchoredSelectionOrEmpty()
                .map(s -> (Predicate<SpreadsheetSelection>) s.selection())
                .orElse(Predicates.never()),
            context
        );
    }

    abstract void refresh(final Predicate<SpreadsheetSelection> selected,
                          final SpreadsheetViewportComponentTableContext context);

    abstract TextStyle selectedTextStyle(final SpreadsheetViewportComponentTableContext context);

    abstract TextStyle unselectedTextStyle(final SpreadsheetViewportComponentTableContext context);

    /**
     * Creates some CSS with some minimal styling, mostly width/height control.
     * Colors, fonts are other text type styles are not included.
     */
    final String setWidthAndHeight(final TextStyle style,
                                   final SpreadsheetViewportComponentTableContext context) {
        final Length<?> width = this.width(context);
        final Length<?> height = this.height(context);

        return style.setValues(
            Maps.of(
                TextStylePropertyName.MIN_WIDTH,
                width,
                TextStylePropertyName.WIDTH,
                width,
                TextStylePropertyName.MIN_HEIGHT,
                height,
                TextStylePropertyName.HEIGHT,
                height
            )
        ).text() + "box-sizing: border-box;";
    }

    abstract Length<?> width(final SpreadsheetViewportComponentTableContext context);

    abstract Length<?> height(final SpreadsheetViewportComponentTableContext context);

    @Override
    public final boolean isEditing() {
        return false;
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.htmlElementComponent()
                .printTree(printer);
        }
        printer.outdent();
    }
}
