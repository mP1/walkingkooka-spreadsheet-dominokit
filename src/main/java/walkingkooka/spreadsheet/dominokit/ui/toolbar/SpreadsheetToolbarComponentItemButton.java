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
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetDominoKitColor;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

/**
 * A templated class that displays a button. Sub classes will have to add {link EventListeners} as required.
 */
abstract class SpreadsheetToolbarComponentItemButton extends SpreadsheetToolbarComponentItem {

    SpreadsheetToolbarComponentItemButton(final String id,
                                          final MdiIcon icon,
                                          final String tooltipText,
                                          final HistoryTokenContext context) {
        final Button button = Button.create(icon)
                .circle();

        button.style(BUTTON_STYLE.css());
        final HTMLElement element = button.element();
        element.id = id;
        element.tabIndex = 0;

        this.tooltip = Tooltip.create(
                button,
                tooltipText
        ).setPosition(DropDirection.BOTTOM_MIDDLE);

        this.button = button.addEventListener(
                EventType.click,
                this::onClick
        ).addEventListener(
                EventType.focus,
                this::onFocus
        ).setTabIndex(0);

        this.context = context;
    }

    abstract void onClick(final Event event);

    abstract void onFocus(final Event event);

    final void setButtonSelected(final boolean selected,
                                 final Color selectedBackgroundColor) {
        TextStyle style = BUTTON_STYLE;
        if (selected) {
            style = style.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    selectedBackgroundColor
            ).set(
                    TextStylePropertyName.COLOR,
                    SpreadsheetDominoKitColor.TOOLBAR_ICON_SELECTED_COLOR
            );
        }

        this.button.style(
                style.css()
        );
    }

    private final static TextStyle BUTTON_STYLE = TextStyle.EMPTY.setMargin(
            Length.pixel(5.0)
    );

    @Override
    public final HTMLElement element() {
        return this.button.element();
    }

    final Button button;

    /**
     * Replaces the current tooltip text with the new {@link String text}.
     */
    final void setTooltipText(final String text) {
        // setTextContent only updates the text and not the Node.
        this.tooltip.setContent(
                Doms.textNode(text)
        );
    }

    final Tooltip tooltip;

    final HistoryTokenContext context;

    @Override
    public final String toString() {
        return this.element().id;
    }
}
