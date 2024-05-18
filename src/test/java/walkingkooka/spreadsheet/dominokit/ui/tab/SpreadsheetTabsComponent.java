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

package walkingkooka.spreadsheet.dominokit.ui.tab;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;

/**
 * Wraps a DOMINO-KIT TabsPanel and tabs.
 */
public final class SpreadsheetTabsComponent implements SpreadsheetTabsComponentLike, TestHtmlElementComponent<HTMLDivElement, SpreadsheetTabsComponent> {

    public static SpreadsheetTabsComponent with(final HistoryTokenContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetTabsComponent(context);
    }

    private SpreadsheetTabsComponent(final HistoryTokenContext context) {
        this.anchors = Lists.array();
        this.context = context;
    }

    @Override
    public SpreadsheetTabsComponent appendTab(final String id,
                                              final String title) {
        CharSequences.failIfNullOrEmpty(id, "id");
        CharSequences.failIfNullOrEmpty(title, "title");

        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
                .setId(id)
                .setTextContent(title)
                .setDisabled(true)
                .addPushHistoryToken(this.context);

        this.anchors.add(anchor);
        return this;
    }

    private final HistoryTokenContext context;

    /**
     * Returns the anchor for the given tab.
     */
    @Override
    public HistoryTokenAnchorComponent anchor(final int index) {
        return this.anchors.get(index);
    }

    private final List<HistoryTokenAnchorComponent> anchors;

    /**
     * Activate the given tab and de-actives all other tabs.
     */
    @Override
    public SpreadsheetTabsComponent setTab(final int index) {
        this.activate = index;
        return this;
    }

    @Override
    public int selectedTab() {
        return this.activate;
    }

    private int activate = -1;

    @Override
    public int tabCount() {
        return this.anchors.size();
    }
}
