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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

abstract public class SpreadsheetSelectionHistoryHashToken extends SpreadsheetNameHistoryHashToken {

    SpreadsheetSelectionHistoryHashToken(final SpreadsheetId id,
                                         final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override
    final UrlFragment spreadsheetUrlFragment() {
        return this.selectionUrlFragment();
    }

    abstract UrlFragment selectionUrlFragment();

    @Override
    HistoryHashToken parse0(final String component,
                            final TextCursor cursor) {
        HistoryHashToken result = this;

        switch(component) {
            case "clear":
                result = this.clear();
                break;
            case "delete":
                result = this.delete();
                break;
            case "formula":
                result = this.formula();
                break;
            case "freeze":
                result = this.freeze();
                break;
            case "menu":
                result = this.menu();
                break;
            case "pattern":
                final Optional<String> patternKind = parseComponent(cursor);
                if(patternKind.isPresent()) {
                    result = this.pattern(
                            SpreadsheetPatternKind.fromTypeName("spreadsheet-" + patternKind.get() + "-pattern")
                    );
                } else {
                    cursor.end();
                }
                break;
            case "save":
                result = this.save(
                        parseAll(cursor)
                );
                break;
            case "style":
                final Optional<String> style = parseComponent(cursor);
                if(style.isPresent()) {
                    result = this.style(
                            TextStylePropertyName.with(
                                    style.get()
                            )
                    );
                }
                break;
            case "unfreeze":
                result = unfreeze();
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    /**
     * Creates a clear {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken clear();

    /**
     * Creates a delete {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken delete();

    /**
     * Creates a formula {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken formula();

    /**
     * Creates a freeze {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken freeze();

    /**
     * Creates a menu {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken menu();

    /**
     * Factory that creates a {@link SpreadsheetSelectionHistoryHashToken} with the given {@link SpreadsheetPatternKind}.
     */
    abstract SpreadsheetSelectionHistoryHashToken pattern(final SpreadsheetPatternKind patternKind);

    /**
     * Creates a save {@link HistoryHashToken} after attempting to parse the value..
     */
    abstract SpreadsheetSelectionHistoryHashToken save(final String value);

    /**
     * Factory that creates a {@link HistoryHashToken} with the given {@link TextStylePropertyName} property name.
     */
    abstract SpreadsheetSelectionHistoryHashToken style(final TextStylePropertyName<?> propertyName);

    /**
     * Creates a unfreeze {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken unfreeze();
}
