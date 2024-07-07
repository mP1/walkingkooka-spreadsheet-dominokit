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
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;

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
                    save = SpreadsheetFormatterName.DATE_FORMAT_PATTERN.setText(patternText)
                            .toString();
                    break;
                case DATE_PARSE_PATTERN:
                    save = SpreadsheetParserName.DATE_PARSER.setText(patternText)
                            .toString();
                    break;
                case DATE_TIME_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.DATE_TIME_FORMAT_PATTERN.setText(patternText)
                            .toString();
                    break;
                case DATE_TIME_PARSE_PATTERN:
                    save = SpreadsheetParserName.DATE_TIME_PARSER.setText(patternText)
                            .toString();
                    break;
                case NUMBER_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN.setText(patternText)
                            .toString();
                    break;
                case NUMBER_PARSE_PATTERN:
                    save = SpreadsheetParserName.NUMBER_PARSER.setText(patternText)
                            .toString();
                    break;
                case TEXT_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.TEXT_FORMAT_PATTERN.setText(patternText)
                            .toString();
                    break;
                case TIME_FORMAT_PATTERN:
                    save = SpreadsheetFormatterName.TIME_FORMAT_PATTERN.setText(patternText)
                            .toString();
                    break;
                case TIME_PARSE_PATTERN:
                    save = SpreadsheetParserName.TIME_PARSER.setText(patternText)
                            .toString();
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
