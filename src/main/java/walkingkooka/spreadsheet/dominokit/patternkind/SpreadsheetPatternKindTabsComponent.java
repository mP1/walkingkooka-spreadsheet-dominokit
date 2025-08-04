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

package walkingkooka.spreadsheet.dominokit.patternkind;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.tab.SpreadsheetTabsComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

/**
 * A tab component that displays tabs for each of the given {@link SpreadsheetPatternKind}.
 */
public final class SpreadsheetPatternKindTabsComponent implements HtmlComponent<HTMLDivElement, SpreadsheetPatternKindTabsComponent> {

    /**
     * Creates an empty {@link SpreadsheetPatternKindTabsComponent}.
     */
    public static SpreadsheetPatternKindTabsComponent empty(final String id,
                                                            final SpreadsheetPatternKind[] kinds,
                                                            final SpreadsheetPatternKindTabsComponentContext context) {
        return new SpreadsheetPatternKindTabsComponent(
            id,
            kinds,
            context
        );
    }

    private SpreadsheetPatternKindTabsComponent(final String id,
                                                final SpreadsheetPatternKind[] kinds,
                                                final SpreadsheetPatternKindTabsComponentContext context) {
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
                                                         final SpreadsheetPatternKindTabsComponentContext context) {
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
    public void refresh(final SpreadsheetPatternKindTabsComponentContext context) {
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

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetPatternKindTabsComponent setCssText(final String css) {
        this.tabsComponent.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetPatternKindTabsComponent setCssProperty(final String name,
                                                              final String value) {
        this.tabsComponent.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
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
