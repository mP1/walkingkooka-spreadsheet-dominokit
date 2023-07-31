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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;

/**
 * A templated class that displays a button. Sub classes will have to add {link EventListeners} as required.
 */
abstract class SpreadsheetViewportToolbarComponentButton extends SpreadsheetViewportToolbarComponent {

    SpreadsheetViewportToolbarComponentButton(final String id,
                                              final MdiIcon icon,
                                              final String tooltipText,
                                              final HistoryTokenContext context) {
        final Button button = Button.create(icon)
                .circle();

        button.style(BUTTON_STYLE.css());
        final HTMLElement element = button.element();
        element.id = id;
        element.tabIndex = 0;

        Tooltip.create(
                button,
                tooltipText
        ).setPosition(DropDirection.BOTTOM_MIDDLE);

        this.button = button;

        this.context = context;
    }

    final void setButtonSelected(final boolean selected,
                                 final AppContext context) {
        TextStyle style = BUTTON_STYLE;
        if (selected) {
            style = style.merge(
                    context.selectedIconStyle()
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

    final HistoryTokenContext context;

    @Override
    public final String toString() {
        return this.element().id;
    }
}
