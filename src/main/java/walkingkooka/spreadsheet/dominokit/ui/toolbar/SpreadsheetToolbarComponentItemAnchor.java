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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

import java.util.Optional;

/**
 * A templated class that displays a link with text and icon.
 */
abstract class SpreadsheetToolbarComponentItemAnchor<C extends SpreadsheetToolbarComponentItemAnchor<C>> extends SpreadsheetToolbarComponentItem<C> {

    SpreadsheetToolbarComponentItemAnchor(final String id,
                                          final Optional<Icon<?>> icon,
                                          final String text,
                                          final String tooltipText,
                                          final HistoryTokenContext context) {
        final HistoryTokenAnchorComponent anchor = context.historyToken()
                .link(id)
                .setTextContent(text)
                .setIconBefore(icon)
                .addFocusListener(this::onFocus);

        this.anchor = anchor;

        this.tooltip = Tooltip.create(
                anchor,
                tooltipText
        ).setPosition(DropDirection.BOTTOM_MIDDLE);

        this.context = context;
    }

    abstract void onFocus(final Event event);

    /**
     * Replaces the current tooltip text with the new {@link String text}.
     */
    final void setTooltipText(final String text) {
        // setTextContent only updates the text and not the Node.
        this.tooltip.setContent(
                Doms.textNode(text)
        );
    }

    // IsElement........................................................................................................

    @Override
    public final HTMLElement element() {
        return this.anchor.element();
    }

    final HistoryTokenAnchorComponent anchor;

    final Tooltip tooltip;

    final HistoryTokenContext context;

    @Override
    public final String toString() {
        return this.element().id;
    }
}
