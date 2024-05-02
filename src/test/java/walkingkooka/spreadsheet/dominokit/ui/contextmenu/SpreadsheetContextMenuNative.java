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

package walkingkooka.spreadsheet.dominokit.ui.contextmenu;

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.menu.Menu;
import org.gwtproject.core.shared.GwtIncompatible;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;

import java.util.Optional;

/**
 * This will implement TreePrintable allowing junit tests to validate menu builders.
 */
@GwtIncompatible
public class SpreadsheetContextMenuNative {

    static SpreadsheetContextMenu empty(final SpreadsheetContextMenuTarget<Element> target,
                                        final HistoryTokenContext context) {
        final SpreadsheetContextMenu contextMenu = SpreadsheetContextMenu.with(
                Menu.create(
                        "", // id
                        "", // text
                        Optional.empty(), // icon
                        Optional.empty() // badge
                ),
                context
        );
        target.setSpreadsheetContextMenu(contextMenu);
        return contextMenu;
    }

    static Menu<Void> addSubMenu(final String id,
                                 final String text,
                                 final Optional<Icon<?>> icon,
                                 final Optional<String> badge,
                                 final SpreadsheetContextMenu menu) {

        final Menu<Void> subMenu = Menu.create(id, text, icon, badge);
        menu.menu.appendChild(subMenu);
        return subMenu;
    }

    static void menuAppendChildSpreadsheetContextMenuItem(final SpreadsheetContextMenuItem item,
                                                          final SpreadsheetContextMenu menu) {
        menu.menu.appendChild(item);
    }

    static void menuAppendChildIsElement(final IsElement<?> isElement,
                                         final SpreadsheetContextMenu menu) {
        menu.menu.appendChild(isElement);
    }

    static void menuAppendChildSeparator(final SpreadsheetContextMenu menu) {
        menu.menu.appendChild("-----");
    }
}
