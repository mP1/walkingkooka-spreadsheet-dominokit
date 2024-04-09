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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.CaseKind;

import java.util.Optional;

/**
 * Capture the tabs functionality within a {@link SpreadsheetPatternDialogComponent}.
 */
final class SpreadsheetPatternComponentTabs implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentTabs> {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentTabs}.
     */
    static SpreadsheetPatternComponentTabs empty(final SpreadsheetPatternKind[] kinds,
                                                 final SpreadsheetPatternDialogComponentContext context) {
        return new SpreadsheetPatternComponentTabs(
                kinds,
                context
        );
    }

    private SpreadsheetPatternComponentTabs(final SpreadsheetPatternKind[] kinds,
                                            final SpreadsheetPatternDialogComponentContext context) {
        this.tabs = this.patternKindTabs(
                kinds,
                context
        );
        this.tabsPanel = this.tabsPanel();
    }

    /**
     * Creates a tab for each {@link SpreadsheetPatternKind}.
     */
    private Tab[] patternKindTabs(final SpreadsheetPatternKind[] kinds,
                                  final SpreadsheetPatternDialogComponentContext context) {
        final Tab[] tabs = new Tab[kinds.length];

        int i = 0;
        for (final SpreadsheetPatternKind kind : kinds) {
            final Tab tab = Tab.create(
                    title(kind)
            );

            HistoryTokenAnchorComponent.with(
                            (HTMLAnchorElement)
                                    tab.getTab()
                                            .element()
                                            .firstElementChild
                    ).setId(SpreadsheetPatternDialogComponent.spreadsheetPatternKindId(kind))
                    .addPushHistoryToken(context)
                    .setDisabled(false);

            tabs[i++] = tab;
        }

        return tabs;
    }

    /**
     * Returns the text that will appear on a tab that when clicked switches to the given {@link SpreadsheetPatternKind}.
     * <pre>
     * SpreadsheetPatternKind.TEXT_FORMAT -> Text Format
     * </pre>
     */
    static String title(final SpreadsheetPatternKind kind) {
        return CaseKind.SNAKE.change(
                kind.name()
                        .replace("FORMAT_PATTERN", "")
                        .replace("PARSE_PATTERN", ""),
                CaseKind.TITLE
        ).trim();
    }

    private final Tab[] tabs;

    /**
     * Returns a {@link TabsPanel} with tabs for each of the possible {@link SpreadsheetPatternKind}, with each
     * tab holding a link which will switch to that pattern.
     */
    private TabsPanel tabsPanel() {
        final TabsPanel tabsPanel = TabsPanel.create();

        for (final Tab tab : this.tabs) {
            tabsPanel.appendChild(tab);
        }

        return tabsPanel;
    }

    private final TabsPanel tabsPanel;

    /**
     * Iterates over the links in each tab updating the link, disabling and activating as necessary.
     */
    void refresh(final SpreadsheetPatternKind[] kinds,
                 final SpreadsheetPatternDialogComponentContext context) {
        final SpreadsheetPatternKind kind = context.patternKind();

        int i = 0;
        final Tab[] tabs = this.tabs;
        for (final SpreadsheetPatternKind possible : kinds) {
            final Tab tab = tabs[i++];
            final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.with(
                    (HTMLAnchorElement)
                            tab.getTab()
                                    .element()
                                    .firstElementChild
            ).setId(SpreadsheetPatternDialogComponent.spreadsheetPatternKindId(possible));

            final boolean match = kind.equals(possible);
            anchor.setDisabled(match);

            if (match) {
                tab.activate(true); // true=silent
            } else {
                tab.deActivate(true); // true=silent

                final HistoryToken historyToken = context.historyToken();
                final HistoryToken historyTokenWithPatternKind = historyToken.setPatternKind(
                        Optional.of(possible)
                );
                anchor.setHistoryToken(
                        Optional.of(historyTokenWithPatternKind)
                );

                context.debug(this.getClass().getSimpleName() + ".patternKindTabsRefresh " + historyTokenWithPatternKind);
            }
        }
    }

    @Override
    public HTMLDivElement element() {
        return this.tabsPanel.element();
    }
}
