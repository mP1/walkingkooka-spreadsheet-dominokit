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

package walkingkooka.spreadsheet.dominokit.ui.flexlayout;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.style.SpacingCss;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * A very basic attempt at re-creating the old DominoUI 1.x FlexLayout.
 */
public class SpreadsheetFlexLayout implements HtmlElementComponent<HTMLDivElement, SpreadsheetFlexLayout>,
        TreePrintable {

    public static SpreadsheetFlexLayout emptyColumn() {
        return new SpreadsheetFlexLayout()
                .column();
    }

    public static SpreadsheetFlexLayout emptyRow() {
        return new SpreadsheetFlexLayout()
                .row();
    }

    private SpreadsheetFlexLayout() {
    }

    private SpreadsheetFlexLayout column() {
        this.div.addCss(
                SpacingCss.dui_flex_col,
                //SpacingCss.dui_v_full,
                SpacingCss.dui_items_start,
                SpacingCss.dui_gap_4);
        return this;
    }

    private SpreadsheetFlexLayout row() {
        this.div.addCss(
                SpacingCss.dui_flex_row,
                SpacingCss.dui_h_full,
                SpacingCss.dui_items_start,
                SpacingCss.dui_gap_4);
        return this;
    }

    public SpreadsheetFlexLayout appendChild(final IsElement<?> child) {
        this.div.element()
                .appendChild(
                        child.element()
                );
        return this;
    }

    @Override
    public HTMLDivElement element() {
        return this.div.element();
    }

    private final DivElement div = ElementsFactory.elements.div();

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            TreePrintable.printTreeOrToString(
                    this.div,
                    printer
            );
        }
        printer.outdent();
    }
}
