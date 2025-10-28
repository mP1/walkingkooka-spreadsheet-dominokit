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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;

/**
 * Wraps a DOMINO-KIT TabsPanel and tabs.
 */
public final class TabsComponent implements TabsComponentLike, TestHtmlElementComponent<HTMLDivElement, TabsComponent> {

    private final HistoryContext context;
    private final List<HistoryTokenAnchorComponent> anchors;
    private int activate = -1;

    private TabsComponent(final HistoryContext context) {
        this.anchors = Lists.array();
        this.context = context;
    }

    public static TabsComponent with(final HistoryContext context) {
        Objects.requireNonNull(context, "context");

        return new TabsComponent(context);
    }

    @Override
    public TabsComponent appendTab(final String id,
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

    /**
     * Returns the anchor for the given tab.
     */
    @Override
    public HistoryTokenAnchorComponent anchor(final int index) {
        return this.anchors.get(index);
    }

    /**
     * Activate the given tab and de-actives all other tabs.
     */
    @Override
    public TabsComponent setTab(final int index) {
        this.activate = index;
        return this;
    }

    @Override
    public int selectedTab() {
        return this.activate;
    }

    @Override
    public int tabCount() {
        return this.anchors.size();
    }
}
