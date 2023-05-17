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
import org.dominokit.domino.ui.button.ButtonSize;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.style.StyleType;
import org.jboss.elemento.EventType;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.tree.text.TextStylePropertyName;

final class SpreadsheetViewportToolbarComponentButton extends SpreadsheetViewportToolbarComponent {

    static <T> SpreadsheetViewportToolbarComponentWatcher watcher(final TextStylePropertyName<T> propertyName,
                                                                  final T propertyValue,
                                                                  final AppContext context) {
        return new SpreadsheetViewportToolbarComponentWatcher() {
            @Override
            public void onClick() {
                context.historyToken()
                        .viewportSelectionHistoryTokenOrEmpty()
                        .map(
                                t -> t.setStyle(propertyName)
                                        .setSave(save(propertyValue))
                        ).ifPresent(context::pushHistoryToken);
            }

            @Override
            public void onFocus() {
                context.historyToken()
                        .viewportSelectionHistoryTokenOrEmpty()
                        .map(
                                t -> t.setStyle(propertyName)
                        ).ifPresent(context::pushHistoryToken);
            }

            @Override
            public void onBlur() {
                // nop
            }
        };
    }

    private static String save(final Object value) {
        return null == value ?
                "" :
                value.toString();
    }

    static SpreadsheetViewportToolbarComponentButton with(final MdiIcon icon,
                                                          final String id,
                                                          final SpreadsheetViewportToolbarComponentWatcher watcher) {
        return new SpreadsheetViewportToolbarComponentButton(
                icon,
                id,
                watcher
        );
    }

    private SpreadsheetViewportToolbarComponentButton(final MdiIcon icon,
                                                      final String id,
                                                      final SpreadsheetViewportToolbarComponentWatcher watcher) {
        final Button button = Button.create(icon)
                .circle()
                .setSize(ButtonSize.MEDIUM)
                .setButtonType(StyleType.DEFAULT)
                .addEventListener(
                        EventType.click,
                        (event) -> this.onClick()
                ).addEventListener(
                        EventType.focus,
                        (event) -> this.onFocus()
                );

        button.style()
                .setMargin("5px");
        final HTMLElement element = button.element();
        element.id = id;
        element.tabIndex = 0;

        this.button = button;

        this.watcher = watcher;
    }

    private void onClick() {
        this.watcher.onClick();
    }

    private void onFocus() {
        this.watcher.onFocus();
    }

    private final SpreadsheetViewportToolbarComponentWatcher watcher;

    @Override
    public HTMLElement element() {
        return this.button.element();
    }

    private final Button button;

    @Override
    public String toString() {
        return this.element().id;
    }
}
