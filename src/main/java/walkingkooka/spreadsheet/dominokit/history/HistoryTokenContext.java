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

package walkingkooka.spreadsheet.dominokit.history;

import org.dominokit.domino.ui.menu.AbstractMenuItem;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Context} that includes operations to interact with the history tokens.
 */
public interface HistoryTokenContext extends Context {

    /**
     * Adds a new {@link HistoryTokenWatcher}, the returned {@link Runnable} may be invoked to remove the watcher.
     */
    Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher);


    /**
     * Adds a new {@link HistoryTokenWatcher} the returned {@link Runnable} may be invoked to remove the watcher,
     * and will also be removed on the next {@link HistoryToken} event.
     */
    Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher);

    /**
     * Returns the current history token.
     */
    HistoryToken historyToken();

    /**
     * Pushes the given {@link HistoryToken} to the browser location.hash
     */
    void pushHistoryToken(final HistoryToken token);

    /**
     * Fires the current {@link HistoryToken} to all {@link HistoryTokenWatcher watchers}.
     */
    void fireCurrentHistoryToken();

    /**
     * Creates an ANCHOR with the given text and if a {@link HistoryToken} is passed, it will be pushed that if this
     * menu item clicked or selected with ENTER.
     */
    default AbstractMenuItem<Void> menuItem(final String id,
                                            final String text,
                                            final Optional<HistoryToken> historyToken) {
        Objects.requireNonNull(historyToken, "historyToken");

        final HistoryTokenAnchorComponent anchor = HistoryTokenAnchorComponent.empty()
                .setId(id)
                .setHistoryToken(historyToken)
                .setTextContent(text);

        final AbstractMenuItem<Void> menu = new AbstractMenuItem<>() {

        };
        menu.appendChild(anchor);

        if (historyToken.isPresent()) {
            menu.addSelectionHandler(
                    (ignored) -> this.pushHistoryToken(
                            historyToken.get()
                    )
            );
            // need to kill margin other menu items with links wont line up with text-only menu items.
            anchor.element()
                    .style
                    .setProperty("margin-left", "0");
        }
        return menu;
    }
}
