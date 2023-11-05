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

package walkingkooka.spreadsheet.dominokit.menu;

import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.direction.MouseBestFitDirection;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.Separator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

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

        return new SpreadsheetContextMenu(
                element,
                context
        );
    }

    public SpreadsheetContextMenu(final DominoElement<?> element,
                                  final AppContext context) {
        this.menu = Menu.<Void>create()
                .setContextMenu(true)
                .setDropDirection(new MouseBestFitDirection())
                .setTargetElement(element);
        this.context = context;

        element.setDropMenu(this.menu);
    }

    public SpreadsheetContextMenu item(final String text,
                                       final Optional<HistoryToken> historyToken) {
        this.menu.appendChild(
                this.context.menuItem(
                        text,
                        historyToken
                )
        );
        return this;
    }

    public SpreadsheetContextMenu separator() {
        this.menu.appendChild(new Separator());
        return this;
    }

    /**
     * Gives focus to the menu
     */
    public SpreadsheetContextMenu focus() {
        this.menu.open(true);
        return this;
    }

    private final Menu<Void> menu;

    private final AppContext context;
}
