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

import walkingkooka.naming.HasName;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

public abstract class SpreadsheetNameHistoryToken extends SpreadsheetIdHistoryToken implements HasName<SpreadsheetName> {

    SpreadsheetNameHistoryToken(final SpreadsheetId id,
                                final SpreadsheetName name) {
        super(id);

        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public final SpreadsheetName name() {
        return this.name;
    }

    final HistoryToken replaceName(final SpreadsheetName name) {
        return this.replaceIdAndName(
                this.id(),
                name
        );
    }

    private final SpreadsheetName name;

    /**
     * Creates a clear {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setClear0();

    /**
     * Creates a delete {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setDelete0();

    /**
     * Creates a format pattern from {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setFormatPattern();

    /**
     * Creates a freeze {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setFreeze0();

    /**
     * Creates a insert after {@link SpreadsheetNameHistoryToken}.
     * This only works for column and rows.
     */
    abstract HistoryToken setInsertAfter0(final OptionalInt count);

    /**
     * Creates a insert before {@link SpreadsheetNameHistoryToken}.
     * This only works for column and rows.
     */
    abstract HistoryToken setInsertBefore0(final OptionalInt count);

    /**
     * Creates a setMenu1 {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setMenu1();

    /**
     * Creates a setMenu1 {@link SpreadsheetNameHistoryToken} for the given {@link SpreadsheetSelection}.
     */
    final HistoryToken setMenu2(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        if (selection.isCellRangeReference() || selection.isColumnRangeReference() || selection.isRowRangeReference()) {
            throw new IllegalArgumentException("Expected cell, column or row but got " + selection);
        }

        final HistoryToken token;

        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection anchored = this.setMenuSelection(selection);
        final SpreadsheetSelection menuSelection = anchored.selection();

        if (menuSelection.isCellReference() || menuSelection.isCellRangeReference() || menuSelection.isLabelName()) {
            token = cellMenu(
                    id,
                    name,
                    this.setMenuSelection(selection)
            );
        } else {
            if (menuSelection.isColumnReference() || menuSelection.isColumnRangeReference()) {
                token = columnMenu(
                        id,
                        name,
                        anchored
                );
            } else {
                if (menuSelection.isRowReference() || menuSelection.isRowRangeReference()) {
                    token = rowMenu(
                            id,
                            name,
                            anchored
                    );
                } else {
                    throw new IllegalArgumentException("Expected cell, column or row but got " + menuSelection);
                }
            }
        }

        return token;
    }

    abstract AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection);

    /**
     * Creates a parse pattern {@link SpreadsheetNameHistoryToken}.
     */
    abstract HistoryToken setParsePattern();

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link SpreadsheetPatternKind}.
     */
    abstract HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind);

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

    final HistoryToken parseFormatPattern(final TextCursor cursor) {
        return this.setFormatPattern()
                .setPatternKind(
                        this.parsePatternKind(
                                cursor,
                                "/format-pattern"
                        )
                );
    }

    final HistoryToken parseParsePattern(final TextCursor cursor) {
        return this.setParsePattern()
                .setPatternKind(
                        this.parsePatternKind(
                                cursor,
                                "/parse-pattern"
                        )
                );
    }

    private Optional<SpreadsheetPatternKind> parsePatternKind(final TextCursor cursor,
                                                              final String prefix) {
        return parseComponent(cursor)
                .flatMap(
                        k -> {
                            SpreadsheetPatternKind kind = null;

                            for (final SpreadsheetPatternKind possible : SpreadsheetPatternKind.values()) {
                                if (possible.urlFragment().value().equals(prefix + "/" + k)) {
                                    kind = possible;
                                    break;
                                }
                            }

                            return Optional.ofNullable(kind);
                        }
                );
    }

    final HistoryToken parseSave(final TextCursor cursor) {
        return this instanceof SpreadsheetCellSelectHistoryToken ?
                this.cast(SpreadsheetCellSelectHistoryToken.class)
                        .parseCellSave(cursor) :
                this.setSave(
                        parseAll(cursor)
                );
    }

    final HistoryToken parseStyle(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> style = parseComponent(cursor);
        if (style.isPresent()) {
            result = this.setStyle(
                    TextStylePropertyName.with(
                            style.get()
                    )
            );
        }
        return result;
    }

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment spreadsheetIdUrlFragment() {
        return UrlFragment.SLASH.append(
                        this.name.urlFragment()
                )
                .append(
                        this.spreadsheetUrlFragment()
                );
    }

    abstract UrlFragment spreadsheetUrlFragment();

    // onHistoryTokenChange.............................................................................................

    /**
     * Fired whenever a new history token change happens.
     */
    @Override
    public final void onHistoryTokenChange(final HistoryToken previous,
                                           final AppContext context) {
        // if the metadata.spreadsheetId and current historyToken.spreadsheetId DONT match wait for the metadata to
        // be loaded then fire history token again.
        final SpreadsheetId id = this.id();

        // special case SpreadsheetDeleteHistoryToken otherwise load SpradsheetMetadata might happen.
        if (this instanceof SpreadsheetDeleteHistoryToken || context.isSpreadsheetMetadataLoaded()) {
            this.onHistoryTokenChange0(
                    previous,
                    context
            );
        } else {
            final SpreadsheetId previousId = context.spreadsheetMetadata()
                    .id()
                    .orElse(null);
            context.debug(
                    this.getClass().getSimpleName() +
                            ".onHistoryTokenChange token " +
                            id +
                            " and context metadata " +
                            previousId +
                            " have different ids, load SpreadsheetId and then fire current history token"
            );
            context.spreadsheetMetadataFetcher()
                    .loadSpreadsheetMetadata(id);
        }
    }

    /**
     * This method is only called if the {@link SpreadsheetMetadata} for the {@link #id()} has already been loaded.
     */
    abstract void onHistoryTokenChange0(final HistoryToken previous,
                                        final AppContext context);
}
