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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Objects;

/**
 * A modal dialog with a text box that allows user entry of a {@link SpreadsheetPattern pattern}.
 * Buttons are available along the bottom that support SAVE, UNDO and CLOSE.
 */
final class SpreadsheetPatternDialogComponentParse extends SpreadsheetPatternDialogComponent {

    /**
     * Creates a new {@link SpreadsheetPatternDialogComponentParse}.
     */
    static SpreadsheetPatternDialogComponentParse with(final SpreadsheetPatternDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetPatternDialogComponentParse(context);
    }

    private SpreadsheetPatternDialogComponentParse(final SpreadsheetPatternDialogComponentContext context) {
        super(context);
    }

    @Override
    SpreadsheetPatternKind[] spreadsheetPatternKinds() {
        return SpreadsheetPatternKind.parseValues();
    }
}
