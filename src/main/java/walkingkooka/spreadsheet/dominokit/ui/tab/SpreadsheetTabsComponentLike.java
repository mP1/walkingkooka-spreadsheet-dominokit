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
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

/**
 * Defines the public interface of a {@link SpreadsheetTabsComponent}.
 */
public interface SpreadsheetTabsComponentLike extends HtmlElementComponent<HTMLDivElement, SpreadsheetTabsComponent> {

    /**
     * Appends a new tab. The anchor holding the title will be disabled.
     */
    SpreadsheetTabsComponentLike appendTab(final String id,
                                           final String title);

    /**
     * Fetches the anchor holding the tab text.
     */
    HistoryTokenAnchorComponent anchor(final int index);

    /**
     * Activate the given tab and de-actives all other tabs.
     */
    SpreadsheetTabsComponentLike setTab(final int index);

    /**
     * Returns the selected tab index.
     */
    int selectedTab();

    /**
     * Returns the number of added tabs
     */
    int tabCount();
}
