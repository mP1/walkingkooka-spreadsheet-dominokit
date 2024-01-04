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

package walkingkooka.spreadsheet.dominokit.ui;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

/**
 * Interface that provides simple operations to update the visual state of a ui.
 * Dont forget to register this {@link HistoryTokenWatcher}.
 */
public interface ComponentLifecycle extends HistoryTokenWatcher,
        ComponentLifecycleMatcher,
        ComponentRefreshable,
        OpenableComponent {

    /**
     * Conditionally calls {@link #refresh(AppContext)} if this ui is {@link #isOpen()}.
     */
    default void refreshIfOpen(final AppContext context) {
        // extra isMatch, which should avoid ClassCastExceptions for Components that have delayed closes.
        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1468
        context.debug(this.getClass().getSimpleName() + ".refreshIfOpen isOpen: " + this.isOpen() + " isMatch: " + this.isMatch(context.historyToken()));
        if (this.isOpen() && this.isMatch(context.historyToken())) {
            this.refresh(context);
        }
    }

    // HistoryTokenWatcher..............................................................................................

    /**
     * Watches {@link HistoryToken} changes and fires the other lifecycle methods.
     */
    @Override
    default void onHistoryTokenChange(final HistoryToken previous,
                                      final AppContext context) {
        final HistoryToken token = context.historyToken();
        final String prefix = this.getClass().getSimpleName();
        
        if (this.shouldIgnore(token)) {
            context.debug(prefix + ".ignored");
        } else {
            final boolean nextOpen = this.isMatch(token);

            if (this.isOpen()) {
                if (nextOpen) {
                    // open -> open -> refresh

                    context.debug(prefix + ".refresh");
                    this.refresh(context);
                } else {
                    // open -> close -> close

                    context.debug(prefix + ".close");
                    this.close(context);
                }
            } else {
                if (nextOpen) {
                    // close -> open -> open
                    context.debug(prefix + ".open");
                    this.open(context);
                }
                // close -> close -> do nothing
            }
        }
    }
}
