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

package walkingkooka.spreadsheet.dominokit.pattern;

import walkingkooka.spreadsheet.dominokit.dom.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetPatternEditorComponent} provided various inputs.
 */
public interface SpreadsheetPatternEditorComponentContext extends CanGiveFocus,
        HistoryTokenContext,
        LoggingContext {

    /**
     * The {@link SpreadsheetPatternKind} being edited.
     */
    SpreadsheetPatternKind patternKind();

    /**
     * Returns the text that will appear on a button that when clicked switches to the given {@link SpreadsheetPatternKind}.
     */
    String patternKindButtonText(final SpreadsheetPatternKind kind);

    /**
     * Switches the editor to the given {@link SpreadsheetPatternKind}.
     */
    void setPatternKind(final SpreadsheetPatternKind patternKind);

    /**
     * The title that will appear above the modal dialog box.
     */
    String title();

    /**
     * Provides the UNDO or loaded text.
     */
    String loaded();

    /**
     * Saves the given pattern text.
     */
    void save(final String patternText);

    /**
     * Call back when the editor is closed.
     */
    void close();

    /**
     * Removes the pattern text from its source cell etc.
     */
    void remove();

    /**
     * More specialised {@link HistoryToken} getter
     */
    @Override
    SpreadsheetCellPatternHistoryToken historyToken();

    /**
     * A {@link SpreadsheetFormatterContext} which will be used to format values for the samples table.
     */
    SpreadsheetFormatterContext spreadsheetFormatterContext();
}
