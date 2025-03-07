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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;

/**
 * Interface that provides simple operations to update the visual state of the ui.
 * Dont forget to register this {@link HistoryTokenWatcher}.
 */
public interface HistoryTokenAwareComponentLifecycle extends HistoryTokenWatcher,
    ComponentLifecycleMatcher,
    ComponentLifecycle {

    /**
     * Conditionally calls {@link #refresh(RefreshContext)} if this ui is {@link #isOpen()}.
     */
    @Override
    default void refreshIfOpen(final RefreshContext context) {
        // extra isMatch, which should avoid ClassCastExceptions for Components that have delayed closes.
        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1468

        final boolean open = this.isOpen();
        final boolean match = this.isMatch(context.historyToken());

        if (this.shouldLogLifecycleChanges()) {
            context.debug(this.getClass().getSimpleName() + ".refreshIfOpen isOpen: " + open + " isMatch: " + match);
        }

        if (open & match) {
            this.refresh(context);
        }
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    default void onHistoryTokenChange(final HistoryToken previous,
                                      final AppContext context) {
        this.componentLifecycleHistoryTokenQuery(context);
    }

    /**
     * Looks at the current {@link HistoryToken} and fires the other lifecycle methods. This is sometimes useful
     * where other state also affects whether a component requires a refresh.
     */
    default void componentLifecycleHistoryTokenQuery(final AppContext context) {
        final HistoryToken token = context.historyToken();
        final String prefix = this.getClass().getSimpleName();

        if (this.shouldIgnore(token)) {
            if (this.shouldLogLifecycleChanges()) {
                context.debug(prefix + ".ignored");
            }
        } else {
            boolean canOpen = true;

            // some dialogs will NOT work if the SpreadsheetMetadata is not loaded.
            if (this instanceof LoadedSpreadsheetMetadataRequired) {
                if (context.isSpreadsheetMetadataLoaded()) {
                    canOpen = true;
                } else {
                    canOpen = false;
                    if (this.shouldLogLifecycleChanges()) {
                        context.debug(prefix + ".close " + token);
                    }
                    this.close(context);
                }
            }

            if (canOpen) {
                final boolean nextOpen = this.isMatch(token);

                if (this.isOpen()) {
                    if (nextOpen) {
                        // open -> open -> refresh

                        if (this.shouldLogLifecycleChanges()) {
                            context.debug(prefix + ".refresh");
                        }
                        this.refresh(context);
                    } else {
                        // open -> close -> close

                        if (this.shouldLogLifecycleChanges()) {
                            context.debug(prefix + ".close " + token);
                        }
                        this.close(context);
                    }
                } else {
                    if (nextOpen) {
                        // close -> open -> open
                        if (this.shouldLogLifecycleChanges()) {
                            context.debug(prefix + ".open " + token);
                        }
                        this.open(context);

                        if (this.shouldLogLifecycleChanges()) {
                            context.debug(prefix + ".refresh");
                        }
                        this.refresh(context);

                        if (this.shouldLogLifecycleChanges()) {
                            context.debug(prefix + ".openGiveFocus");
                        }
                        this.openGiveFocus(context);
                    }
                    // close -> close -> do nothing
                }
            }
        }
    }

    /**
     * When true the {@link #onHistoryTokenChange(HistoryToken, AppContext)} messages will be logged.
     */
    boolean shouldLogLifecycleChanges();
}
