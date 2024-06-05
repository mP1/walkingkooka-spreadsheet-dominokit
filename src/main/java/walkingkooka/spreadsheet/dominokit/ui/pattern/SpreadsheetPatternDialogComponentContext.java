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

import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

import java.util.Optional;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetPatternDialogComponent} provided various inputs.
 */
public interface SpreadsheetPatternDialogComponentContext extends CanGiveFocus,
        ComponentLifecycleMatcher,
        SpreadsheetDialogComponentContext {

    /**
     * The {@link SpreadsheetPatternKind} being edited.
     */
    SpreadsheetPatternKind patternKind();

    /**
     * Prepare the save pattern text, this is necessary for the moment because of the different text for FORMAT v PARSE
     */
    default String savePatternText(final String patternText) {
        final String save;

        if (patternText.isEmpty()) {
            save = "";
        } else {
            switch (this.patternKind()) {
                case DATE_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.DATE_FORMAT + " " + patternText;
                    break;
                case DATE_TIME_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.DATE_TIME_FORMAT + " " + patternText;
                    break;
                case NUMBER_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.NUMBER_FORMAT + " " + patternText;
                    break;
                case TEXT_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.TEXT_FORMAT + " " + patternText;
                    break;
                case TIME_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.TIME_FORMAT + " " + patternText;
                    break;
                default:
                    save = patternText;
                    break;
            }
        }

        return save;
    }

    /**
     * Provides the UNDO {@link SpreadsheetPattern}
     */
    Optional<SpreadsheetPattern> undo();

    /**
     * A {@link SpreadsheetFormatterContext} which will be used to format values for the samples table.
     */
    SpreadsheetFormatterContext spreadsheetFormatterContext();

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
