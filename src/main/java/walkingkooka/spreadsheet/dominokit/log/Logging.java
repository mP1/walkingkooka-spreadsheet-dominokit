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

package walkingkooka.spreadsheet.dominokit.log;

/**
 * Global / centralised constants that turn on/off logging for components.
 */
public interface Logging {

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

    boolean SPREADSHEET_KEYBOARD_EVENT_LISTENER = true;

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

    boolean SPREADSHEET_VIEWPORT_COMPONENT_TABLE = false;

    boolean SPREADSHEET_VIEWPORT_FORMULA_COMPONENT = false;

    boolean SPREADSHEET_VIEWPORT_SCROLLBAR_COMPONENT = false;

    boolean VALIDATOR_SELECTOR_DIALOG_COMPONENT = false;
}
