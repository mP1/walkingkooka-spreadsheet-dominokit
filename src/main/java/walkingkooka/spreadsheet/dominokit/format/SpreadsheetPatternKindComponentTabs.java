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
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetTabsComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

/**
 * A tab component that displays tabs for each of the given {@link SpreadsheetPatternKind}.
 */
final class SpreadsheetPatternKindComponentTabs implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternKindComponentTabs>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetPatternKindComponentTabs}.
     */
    static SpreadsheetPatternKindComponentTabs empty(final String id,
                                                     final SpreadsheetPatternKind[] kinds,
                                                     final SpreadsheetPatternKindComponentTabsContext context) {
        return new SpreadsheetPatternKindComponentTabs(
                id,
                kinds,
                context
        );
    }

    private SpreadsheetPatternKindComponentTabs(final String id,
                                                final SpreadsheetPatternKind[] kinds,
                                                final SpreadsheetPatternKindComponentTabsContext context) {
        this.tabsComponent = this.tabsComponentCreate(
                id,
                kinds,
                context
        );

        this.kinds = kinds;
    }

    // SpreadsheetTabsComponent.........................................................................................

    private SpreadsheetTabsComponent tabsComponentCreate(final String id,
                                                         final SpreadsheetPatternKind[] kinds,
                                                         final SpreadsheetPatternKindComponentTabsContext context) {
        final SpreadsheetTabsComponent tabs = SpreadsheetTabsComponent.with(context);

        for (final SpreadsheetPatternKind kind : kinds) {
            tabs.appendTab(
                    id +
                            CaseKind.SNAKE.change(
                                    kind.name()
                                            .toLowerCase(),
                                    CaseKind.KEBAB
                            ).replace("-pattern", ""),
                    tabTitle(kind)
            );
        }

        return tabs;
    }

    /**
     * Returns the text that will appear on a tab that when clicked switches to the given {@link SpreadsheetPatternKind}.
     * <pre>
     * SpreadsheetPatternKind.TEXT_FORMAT -> Text Format
     * </pre>
     */
    private static String tabTitle(final SpreadsheetPatternKind kind) {
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
    void refresh(final SpreadsheetPatternKindComponentTabsContext context) {
        final SpreadsheetTabsComponent tabs = this.tabsComponent;
        final SpreadsheetPatternKind kind = context.historyToken().patternKind()
                .orElse(null);

        int i = 0;
        for (final SpreadsheetPatternKind possible : this.kinds) {
            final HistoryTokenAnchorComponent anchor = tabs.anchor(i);

            final boolean match = possible.equals(kind);
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

    private final SpreadsheetPatternKind[] kinds;

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
    public SpreadsheetPatternKindComponentTabs setCssText(final String css) {
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

    private final SpreadsheetTabsComponent tabsComponent;
}
