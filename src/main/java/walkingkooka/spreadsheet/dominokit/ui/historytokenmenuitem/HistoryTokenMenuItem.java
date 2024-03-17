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

package walkingkooka.spreadsheet.dominokit.ui.historytokenmenuitem;

import org.dominokit.domino.ui.menu.AbstractMenuItem;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

/**
 * A {@link AbstractMenuItem} that includes a {@link HistoryTokenAnchorComponent}.
 */
public final class HistoryTokenMenuItem extends AbstractMenuItem<Void> {

    public HistoryTokenMenuItem(final HistoryTokenAnchorComponent anchor) {
        super();
        this.anchor = anchor;

        this.appendChild(anchor);

        // need to kill margin other menu items with links wont line up with text-only menu items.
        anchor.element()
                .style
                .setProperty("margin-left", "0");
    }

    public HistoryTokenAnchorComponent anchor() {
        return this.anchor;
    }

    private final HistoryTokenAnchorComponent anchor;
}
