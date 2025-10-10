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

    // centralised switches to turn on/off debugging in each of the components.

    boolean CONVERTER_SELECTOR_DIALOG_COMPONENT = false;

    boolean DATE_TIME_SYMBOLS_DIALOG_COMPONENT = false;

    boolean DECIMAL_NUMBER_SYMBOLS_DIALOG_COMPONENT = false;

    boolean FORM_HANDLER_SELECTOR_DIALOG_COMPONENT = false;

    boolean JAR_ENTRY_INFO_LIST_DIALOG_COMPONENT = false;

    boolean PLUGIN_ALIAS_SET_LIKE_SELECTOR_DIALOG_COMPONENT = false;

    boolean PLUGIN_FILE_VIEW_DIALOG_COMPONENT = false;

    boolean PLUGIN_NAME_SET_DIALOG_COMPONENT = false;

    boolean PLUGIN_SET_DIALOG_COMPONENT = false;

    boolean PLUGIN_UPLOAD_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_APP_LAYOUT = false;

    boolean SPREADSHEET_CELL_FIND_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_CELL_REFERENCES_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_CELL_SORT_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_CELL_VALUE_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_COLUMN_ROW_INSERT_COUNT_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_COMPARATOR_NAME_LIST_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_FORMATTER_SELECTOR_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_LABEL_MAPPING_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_LABEL_MAPPING_LIST_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_LIST_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_LOCALE_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_METADATA_HISTORY_TOKEN_AWARE_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_NAME_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_NAVIGATE_COMPONENT_LIFECYCLE = false;

    boolean SPREADSHEET_PARSER_SELECTOR_DIALOG_COMPONENT = false;

    boolean SPREADSHEET_TOOLBAR_COMPONENT = false;

    boolean SPREADSHEET_TOOLBAR_COMPONENT_ITEM = false;

    boolean SPREADSHEET_VIEWPORT_COMPONENT = false;

    boolean SPREADSHEET_VIEWPORT_FORMULA_COMPONENT = false;

    boolean SPREADSHEET_VIEWPORT_SCROLLBAR_COMPONENT = false;

    boolean VALIDATOR_SELECTOR_DIALOG_COMPONENT = false;

    /**
     * Conditionally calls {@link #refresh(RefreshContext)} if this ui is {@link #isOpen()}.
     */
    @Override
    default void refreshIfOpen(final RefreshContext context) {
        // extra isMatch, which should avoid ClassCastExceptions for Components that have delayed closes.
        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1468

        final boolean open = this.isOpen();

        final HistoryToken historyToken = context.historyToken();
        final boolean match = this.isMatch(historyToken);

        if (this.shouldLogLifecycleChanges()) {
            context.debug(
                this.getClass().getSimpleName() + ".refreshIfOpen " +
                    (open ? "OPEN" : "CLOSED") +
                    " isMatch: " +
                    (match ? "MATCH" : "NOT MATCHED") +
                    " " +
                    historyToken
            );
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
