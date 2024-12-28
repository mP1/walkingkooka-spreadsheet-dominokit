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

package walkingkooka.spreadsheet.dominokit.find;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.grid.GridLayout;
import org.dominokit.domino.ui.style.CompositeCssClass;
import org.dominokit.domino.ui.utils.Domino;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Collection;

final class SpreadsheetFindDialogComponentGridLayout extends SpreadsheetFindDialogComponentGridLayoutLike {

    private final static CompositeCssClass dui_grid_section = CompositeCssClass.of(
            Domino.dui_text_center
    );

    static SpreadsheetFindDialogComponentGridLayout empty() {
        return new SpreadsheetFindDialogComponentGridLayout();
    }

    private SpreadsheetFindDialogComponentGridLayout() {
        this.gridLayout = GridLayout.create()
                .setPadding("5px")
                .setHeight("100%");
    }

    @Override SpreadsheetFindDialogComponentGridLayout setLeft(final Collection<HtmlElementComponent<?, ?>> children) {
        this.gridLayout.withLeftPanel(
                (parent, content) ->
                {
                    content.addCss(dui_grid_section);
//                    parent.setLeftSpan(
//                        SectionSpan._5,
//                        true,
//                        true
//                    );
                    for (final HtmlElementComponent<?, ?> child : children) {
                        content.appendChild(child);
                    }
                }
        );
        return this;
    }

    @Override SpreadsheetFindDialogComponentGridLayout setContent(final Collection<HtmlElementComponent<?, ?>> children) {
        this.gridLayout.withContent(
                (parent, content) ->
                {
                    content.addCss(dui_grid_section);
                    for (final HtmlElementComponent<?, ?> child : children) {
                        content.appendChild(child);
                    }
                }
        );

        return this;
    }

    @Override SpreadsheetFindDialogComponentGridLayout setFooter(final Collection<HtmlElementComponent<?, ?>> children) {
        this.gridLayout.withFooter(
                (parent, content) ->
                {
                    content.addCss(dui_grid_section);
                    for (final HtmlElementComponent<?, ?> child : children) {
                        content.appendChild(child);
                    }
                }
        );

        return this;
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public SpreadsheetFindDialogComponentGridLayout setCssText(final String css) {
        this.gridLayout.cssText(css);
        return this;
    }

    @Override
    public HTMLDivElement element() {
        return this.gridLayout.element();
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        throw new UnsupportedOperationException();
    }

    private final GridLayout gridLayout;
}
