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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

public abstract class SpreadsheetNameHistoryHashToken extends SpreadsheetHistoryHashToken {

    SpreadsheetNameHistoryHashToken(final SpreadsheetId id,
                                    final SpreadsheetName name) {
        super(id);

        this.name = Objects.requireNonNull(name, "name");
    }

    public final SpreadsheetName name() {
        return this.name;
    }

    private final SpreadsheetName name;

    @Override
    final UrlFragment spreadsheetIdUrlFragment() {
        return UrlFragment.SLASH.append(
                        this.name.urlFragment()
                )
                .append(
                        this.spreadsheetUrlFragment()
                );
    }

    abstract UrlFragment spreadsheetUrlFragment();

    final SpreadsheetNameHistoryHashToken cell(final SpreadsheetViewportSelection viewportSelection) {
        return cell(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    final SpreadsheetNameHistoryHashToken column(final SpreadsheetViewportSelection viewportSelection) {
        return column(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    final SpreadsheetNameHistoryHashToken labelMapping(final SpreadsheetLabelName labelName) {
        return labelMapping(
                this.id(),
                this.name(),
                labelName
        );
    }

    final SpreadsheetNameHistoryHashToken row(final SpreadsheetViewportSelection viewportSelection) {
        return row(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    /**
     * Creates a clear {@link SpreadsheetNameHistoryHashToken}.
     */
    abstract SpreadsheetNameHistoryHashToken clear();

    /**
     * Creates a delete {@link SpreadsheetNameHistoryHashToken}.
     */
    abstract SpreadsheetNameHistoryHashToken delete();

    /**
     * Creates a formula {@link SpreadsheetNameHistoryHashToken}.
     */
    abstract SpreadsheetNameHistoryHashToken formulaHistoryHashToken();

    /**
     * Creates a freeze {@link SpreadsheetNameHistoryHashToken}.
     */
    abstract SpreadsheetNameHistoryHashToken freeze();

    /**
     * Creates a menu {@link SpreadsheetNameHistoryHashToken}.
     */
    abstract SpreadsheetNameHistoryHashToken menu();

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryHashToken} with the given {@link SpreadsheetPatternKind}.
     */
    abstract SpreadsheetNameHistoryHashToken pattern(final SpreadsheetPatternKind patternKind);

    /**
     * Creates a save {@link HistoryHashToken} after attempting to parse the value.
     */
    abstract SpreadsheetNameHistoryHashToken save(final String value);

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryHashToken} with the given {@link TextStylePropertyName} property name.
     */
    abstract SpreadsheetNameHistoryHashToken style(final TextStylePropertyName<?> propertyName);

    /**
     * Creates a unfreeze {@link SpreadsheetNameHistoryHashToken}.
     */
    abstract SpreadsheetNameHistoryHashToken unfreeze();
}
