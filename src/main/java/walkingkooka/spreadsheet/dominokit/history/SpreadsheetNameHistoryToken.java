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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

public abstract class SpreadsheetNameHistoryToken extends SpreadsheetIdHistoryToken {

    SpreadsheetNameHistoryToken(final SpreadsheetId id,
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

    /**
     * Creates a clear {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setClear0();

    /**
     * Creates a delete {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setDelete0();

    /**
     * Creates a freeze {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setFreeze0();

    /**
     * Creates a setMenu1 {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setMenu1();

    /**
     * Creates a setMenu1 {@link SpreadsheetNameHistoryToken} for the given {@link SpreadsheetSelection}.
     */
    final HistoryToken setMenu2(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        if (selection.isCellRange() || selection.isColumnReferenceRange() || selection.isRowReferenceRange()) {
            throw new IllegalArgumentException("Expected cell, column or row but got " + selection);
        }

        HistoryToken token;

        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final SpreadsheetViewportSelection menuViewportSelection = this.setMenu2ViewportSelection(selection);
        final SpreadsheetSelection menuSelection = menuViewportSelection.selection();

        if (menuSelection.isCellReference() || menuSelection.isCellRange() || menuSelection.isLabelName()) {
            token = cellMenu(
                    id,
                    name,
                    this.setMenu2ViewportSelection(selection)
            );
        } else {
            if (menuSelection.isColumnReference() || menuSelection.isColumnReferenceRange()) {
                token = columnMenu(
                        id,
                        name,
                        menuViewportSelection
                );
            } else {
                if (menuSelection.isRowReference() || menuSelection.isRowReferenceRange()) {
                    token = rowMenu(
                            id,
                            name,
                            menuViewportSelection
                    );
                } else {
                    throw new IllegalArgumentException("Expected cell, column or row but got " + menuSelection);
                }
            }
        }

        return token;
    }

    abstract SpreadsheetViewportSelection setMenu2ViewportSelection(final SpreadsheetSelection selection);

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link SpreadsheetPatternKind}.
     */
    abstract HistoryToken setPatternKind0(final SpreadsheetPatternKind patternKind);

    /**
     * Creates a save {@link HistoryToken} after attempting to parse the value.
     */
    abstract HistoryToken setSave0(final String value);

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link TextStylePropertyName} property name.
     */
    abstract HistoryToken setStyle0(final TextStylePropertyName<?> propertyName);

    /**
     * Creates a unfreeze {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setUnfreeze0();

    // parse............................................................................................................

    final HistoryToken parsePattern(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> patternKind = parseComponent(cursor);
        if (patternKind.isPresent()) {
            result = this.setPatternKind(
                    SpreadsheetPatternKind.fromTypeName("spreadsheet-" + patternKind.get() + "-pattern")
            );
        } else {
            cursor.end();
        }
        return result;
    }

    final HistoryToken parseSave(final TextCursor cursor) {
        return this.setSave(
                parseAll(cursor)
        );
    }

    final HistoryToken parseStyle(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> style = parseComponent(cursor);
        if(style.isPresent()) {
            result = this.setStyle(
                    TextStylePropertyName.with(
                            style.get()
                    )
            );
        }
        return result;
    }
}
