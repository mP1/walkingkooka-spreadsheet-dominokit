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
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

import java.util.Optional;

public final class TabsComponentTest implements HtmlComponentTesting<TabsComponent, HTMLDivElement> {

    @Test
    public void testEmptyNoTabs() {
        this.treePrintAndCheck(
            TabsComponent.with(
                new FakeHistoryContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/Untitled/cell/B1/formatter");
                    }
                }
            ),
            "TabsComponent\n"
        );
    }

    @Test
    public void testSeveralTabsNonActive() {
        this.treePrintAndCheck(
            TabsComponent.with(
                    new FakeHistoryContext() {
                        @Override
                        public HistoryToken historyToken() {
                            return HistoryToken.parseString("/1/Untitled/cell/B1/formatter");
                        }
                    }
                ).appendTab("tab-1", "Tab-1A")
                .appendTab("tab-2", "Tab-2B")
                .appendTab("tab-3", "Tab-3C"),
            "TabsComponent\n" +
                "  TAB 0\n" +
                "    \"Tab-1A\" DISABLED id=tab-1\n" +
                "  TAB 1\n" +
                "    \"Tab-2B\" DISABLED id=tab-2\n" +
                "  TAB 2\n" +
                "    \"Tab-3C\" DISABLED id=tab-3\n"
        );
    }

    @Test
    public void testSeveralTabsIncludingActive() {
        this.treePrintAndCheck(
            TabsComponent.with(
                    new FakeHistoryContext() {
                        @Override
                        public HistoryToken historyToken() {
                            return HistoryToken.parseString("/1/Untitled/cell/B1/formatter");
                        }
                    }
                ).appendTab("tab-1", "Tab-1A")
                .appendTab("tab-2", "Tab-2B")
                .appendTab("tab-3", "Tab-3C")
                .setTab(1),
            "TabsComponent\n" +
                "  TAB 0\n" +
                "    \"Tab-1A\" DISABLED id=tab-1\n" +
                "  TAB 1 SELECTED\n" +
                "    \"Tab-2B\" DISABLED id=tab-2\n" +
                "  TAB 2\n" +
                "    \"Tab-3C\" DISABLED id=tab-3\n"
        );
    }

    @Test
    public void testSeveralTabsIncludingActiveLinksWithAnchor() {
        final TabsComponent tabs = TabsComponent.with(
                new FakeHistoryContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/Untitled/cell/B1/formatter");
                    }
                }
            ).appendTab("date-tab", "Date")
            .appendTab("number-tab", "Number")
            .appendTab("text-tab", "Text")
            .setTab(1);

        tabs.anchor(0)
            .setHistoryToken(
                Optional.of(
                    HistoryToken.parseString("/1/Untitled/cell/B1/formatter")
                )

            );
        tabs.anchor(1)
            .setHistoryToken(
                Optional.of(
                    HistoryToken.parseString("/1/Untitled/cell/B1/formatter")
                )
            );
        tabs.anchor(2)
            .setHistoryToken(
                Optional.of(
                    HistoryToken.parseString("/1/Untitled/cell/B1/formatter")
                )
            );

        this.treePrintAndCheck(
            tabs,
            "TabsComponent\n" +
                "  TAB 0\n" +
                "    \"Date\" [#/1/Untitled/cell/B1/formatter] id=date-tab\n" +
                "  TAB 1 SELECTED\n" +
                "    \"Number\" [#/1/Untitled/cell/B1/formatter] id=number-tab\n" +
                "  TAB 2\n" +
                "    \"Text\" [#/1/Untitled/cell/B1/formatter] id=text-tab\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TabsComponent> type() {
        return TabsComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
