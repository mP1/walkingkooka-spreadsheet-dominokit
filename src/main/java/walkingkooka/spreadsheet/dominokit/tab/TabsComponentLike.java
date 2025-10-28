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

package walkingkooka.spreadsheet.dominokit.tab;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.printer.IndentingPrinter;

/**
 * Defines the public interface of a {@link TabsComponent}.
 */
public interface TabsComponentLike extends HtmlComponent<HTMLDivElement, TabsComponent> {

    /**
     * Appends a new tab. The anchor holding the title will be disabled.
     */
    TabsComponentLike appendTab(final String id,
                                final String title);

    /**
     * Fetches the anchor holding the tab text.
     */
    HistoryTokenAnchorComponent anchor(final int index);

    /**
     * Activate the given tab and de-actives all other tabs.
     */
    TabsComponentLike setTab(final int index);

    /**
     * Returns the selected tab index.
     */
    int selectedTab();

    /**
     * Returns the number of added tabs
     */
    int tabCount();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final int selected = this.selectedTab();

            final int count = this.tabCount();
            for (int i = 0; i < count; i++) {

                printer.println("TAB " + i + (selected == i ? " SELECTED" : ""));
                printer.indent();
                {
                    this.anchor(i)
                        .printTree(printer);
                    printer.lineStart();
                }
                printer.outdent();
            }
        }
        printer.outdent();
    }
}
