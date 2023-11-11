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

package walkingkooka.spreadsheet.dominokit.component;

import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.menu.direction.MouseBestFitDirection;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.Separator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Abstraction for building a context menu.
 */
public class SpreadsheetContextMenu {

    public static SpreadsheetContextMenu empty(final DominoElement<?> element,
                                               final AppContext context) {
        Objects.requireNonNull(element, "element");
        Objects.requireNonNull(context, "context");

        final Menu<Void> menu = Menu.<Void>create()
                .setContextMenu(true)
                .setDropDirection(new MouseBestFitDirection())
                .setTargetElement(element);
        element.setDropMenu(menu);

        return new SpreadsheetContextMenu(
                menu,
                context
        );
    }

    public SpreadsheetContextMenu(final Menu<Void> menu,
                                  final AppContext context) {
        this.menu = menu;
        this.context = context;
    }

    /**
     * Creates an empty sub menu returning the {@link SpreadsheetContextMenu} which may be used to add items.
     */
    public SpreadsheetContextMenu subMenu(final String text) {
        checkText(text);

        this.addSeparatorIfNecessary();

        final Menu<Void> subMenu = Menu.create();

        this.menu.appendChild(
                MenuItem.<Void>create(text)
                        .setMenu(subMenu)
        );
        return new SpreadsheetContextMenu(
                subMenu,
                this.context
        );
    }

    public SpreadsheetContextMenu item(final String text,
                                       final Optional<HistoryToken> historyToken) {
        checkText(text);
        Objects.requireNonNull(historyToken, "historyToken");

        this.addSeparatorIfNecessary();

        this.menu.appendChild(
                this.context.menuItem(
                        text,
                        historyToken
                )
        );
        return this;
    }

    private String checkText(final String text) {
        return CharSequences.failIfNullOrEmpty(text, "text");
    }

    private void addSeparatorIfNecessary() {
        if (this.addSeparator) {
            this.menu.appendChild(new Separator());
            this.addSeparator = false;
        }
    }

    /**
     * Adds a separator before the next item is added.
     */
    public SpreadsheetContextMenu separator() {
        this.addSeparator = true;
        return this;
    }

    private boolean addSeparator;

    /**
     * Gives focus to the menu
     */
    public SpreadsheetContextMenu focus() {
        this.menu.open(true);
        return this;
    }

    private final Menu<Void> menu;

    private final AppContext context;

    @Override
    public String toString() {
        return this.menu.toString();
    }
}
