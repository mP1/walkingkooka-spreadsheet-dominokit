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

package walkingkooka.spreadsheet.dominokit.format;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.tab.SpreadsheetTabsComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

/**
 * Capture the tabs functionality within a {@link SpreadsheetPatternDialogComponent}.
 */
final class SpreadsheetPatternComponentTabs implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentTabs>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentTabs}.
     */
    static SpreadsheetPatternComponentTabs empty(final SpreadsheetPatternDialogComponentContext context) {
        return new SpreadsheetPatternComponentTabs(
                context
        );
    }

    private SpreadsheetPatternComponentTabs(final SpreadsheetPatternDialogComponentContext context) {
        this.tabsComponent = this.tabsComponentCreate(
                context
        );
    }

    // SpreadsheetTabsComponent.........................................................................................

    private SpreadsheetTabsComponent tabsComponentCreate(final SpreadsheetPatternDialogComponentContext context) {
        final SpreadsheetTabsComponent tabs = SpreadsheetTabsComponent.with(context);

        for (final SpreadsheetPatternKind kind : context.filteredPatternKinds()) {
            tabs.appendTab(
                    SpreadsheetPatternDialogComponent.spreadsheetPatternKindId(kind),
                    tabTitle(kind)
            );
        }

        return tabs;
    }

    private final SpreadsheetTabsComponent tabsComponent;

    /**
     * Returns the text that will appear on a tab that when clicked switches to the given {@link SpreadsheetPatternKind}.
     * <pre>
     * SpreadsheetPatternKind.TEXT_FORMAT -> Text Format
     * </pre>
     */
    static String tabTitle(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                kind.name()
                        .replace("FORMAT_PATTERN", "")
                        .replace("PARSE_PATTERN", ""),
                CaseKind.TITLE
        ).trim();
    }

    // refresh..........................................................................................................

    /**
     * Iterates over the links in each tab updating the link, disabling and activating as necessary.
     */
    void refresh(final SpreadsheetPatternDialogComponentContext context) {
        final SpreadsheetTabsComponent tabs = this.tabsComponent;
        final SpreadsheetPatternKind kind = context.patternKind();

        int i = 0;
        for (final SpreadsheetPatternKind possible : context.filteredPatternKinds()) {
            final HistoryTokenAnchorComponent anchor = tabs.anchor(i);

            final boolean match = kind.equals(possible);
            anchor.setDisabled(match);

            if (match) {
                tabs.setTab(i);
            } else {
                final HistoryToken historyToken = context.historyToken();
                final HistoryToken historyTokenWithPatternKind = historyToken.setPatternKind(
                        Optional.of(possible)
                );
                anchor.setHistoryToken(
                        Optional.of(historyTokenWithPatternKind)
                );
            }

            i++;
        }
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.tabsComponent.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetPatternComponentTabs setCssText(final String css) {
        this.tabsComponent.setCssText(css);
        return this;
    }

    // TreePrinter......................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.tabsComponent.printTree(printer);
        }
        printer.outdent();
    }
}
