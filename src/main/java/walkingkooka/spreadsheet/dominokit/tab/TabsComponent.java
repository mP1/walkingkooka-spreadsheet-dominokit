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

import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.tabs.Tab;
import org.dominokit.domino.ui.tabs.TabsPanel;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;

/**
 * Wraps a DOMINO-KIT TabsPanel and tabs.
 */
public final class TabsComponent implements TabsComponentLike {

    public static TabsComponent with(final HistoryContext context) {
        Objects.requireNonNull(context, "context");

        return new TabsComponent(context);
    }

    private TabsComponent(final HistoryContext context) {
        this.tabsPanel = TabsPanel.create();
        this.tabs = Lists.array();
        this.anchors = Lists.array();
        this.context = context;
    }

    /**
     * Appends a new tab. The anchor holding the title will be disabled.
     */
    @Override
    public TabsComponent appendTab(final String id,
                                   final String title) {
        CharSequences.failIfNullOrEmpty(id, "id");
        CharSequences.failIfNullOrEmpty(title, "title");

        final Tab tab = Tab.create((String)null);
        this.tabs.add(tab);

        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.with(
                (HTMLAnchorElement)
                    tab.getTab()
                        .element()
                        .firstElementChild
            ).setId(id)
            .setTextContent(title)
            .setDisabled(true);

        this.tabsPanel.appendChild(tab);
        this.anchors.add(anchor);
        return this;
    }

    /**
     * This is necessary to create an {@link HistoryTokenAnchorComponent} when a new tab is appended.
     */
    private final HistoryContext context;

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
    public TabsComponent setTab(final int index) {
        int i = 0;

        for (final Tab tab : this.tabs) {
            if (index == i) {
                tab.activate(true);
            } else {
                tab.deActivate(true);
            }
        }
        return this;
    }

    @Override
    public int selectedTab() {
        int index = -1;

        int i = 0;
        for (final Tab tab : this.tabs) {
            if (tab.isActive()) {
                index = -1;
                break;
            }
        }

        return index;
    }

    @Override
    public int tabCount() {
        return this.tabs.size();
    }

    private final List<Tab> tabs;


    // id...............................................................................................................

    @Override
    public String id() {
        return this.element().id;
    }

    @Override
    public TabsComponent setId(final String id) {
        this.element()
            .id = id;
        return this;
    }

    // width............................................................................................................

    @Override
    public int width() {
        return this.element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.element()
            .offsetHeight;
    }

    // setCssText.......................................................................................................

    @Override
    public TabsComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.tabsPanel.cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public TabsComponent setCssProperty(final String name,
                                        final String value) {
        this.tabsPanel.setCssProperty(
            name,
            value
        );
        return this;
    }

    // removeCssProperty................................................................................................

    @Override
    public TabsComponent removeCssProperty(final String name) {
        this.tabsPanel.removeCssProperty(name);
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.tabsPanel.isExpanded();
    }

    // element..........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.tabsPanel.element();
    }

    private final TabsPanel tabsPanel;
}
