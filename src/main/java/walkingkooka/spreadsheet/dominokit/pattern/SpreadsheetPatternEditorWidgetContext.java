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

import walkingkooka.spreadsheet.dominokit.LoggingContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetPatternEditorWidget} provided various inputs.
 */
public interface SpreadsheetPatternEditorWidgetContext extends LoggingContext {

    /**
     * The {@link SpreadsheetPatternKind} being edited.
     */
    SpreadsheetPatternKind patternKind();

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
}
