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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
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

    @Override
    final SpreadsheetHistoryToken setIdAndName0(final SpreadsheetId id,
                                                final SpreadsheetName name) {
        return this.id().equals(id) && this.name().equals(name) ?
                this :
                this.setDifferentIdOrName(
                        id,
                        name
                );
    }

    /**
     * Sub classes should recreate themselves with the new {@link SpreadsheetName}.
     */
    abstract SpreadsheetHistoryToken setDifferentIdOrName(final SpreadsheetId id,
                                                          final SpreadsheetName name);

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} assuming the default {@link walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor anchor}
     * if necessary.
     */
    public final SpreadsheetNameHistoryToken viewportSelection(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetNameHistoryTokenSelectionSpreadsheetSelectionVisitor.selectionToken(
                this,
                viewportSelection
        );
    }

    final SpreadsheetNameHistoryToken cell(final SpreadsheetViewportSelection viewportSelection) {
        return cell(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    final SpreadsheetNameHistoryToken column(final SpreadsheetViewportSelection viewportSelection) {
        return column(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    final SpreadsheetNameHistoryToken labelMapping(final SpreadsheetLabelName labelName) {
        return labelMapping(
                this.id(),
                this.name(),
                labelName
        );
    }

    final SpreadsheetNameHistoryToken row(final SpreadsheetViewportSelection viewportSelection) {
        return row(
                this.id(),
                this.name(),
                viewportSelection
        );
    }

    /**
     * Creates a clear {@link SpreadsheetNameHistoryToken}.
     */
    abstract SpreadsheetNameHistoryToken clear();

    /**
     * Creates a delete {@link SpreadsheetNameHistoryToken}.
     */
    abstract SpreadsheetNameHistoryToken delete();

    /**
     * Creates a formula {@link SpreadsheetNameHistoryToken}.
     */
    abstract SpreadsheetNameHistoryToken formulaHistoryToken();

    /**
     * Creates a freeze {@link SpreadsheetNameHistoryToken}.
     */
    abstract SpreadsheetNameHistoryToken freeze();

    /**
     * Creates a menu {@link SpreadsheetNameHistoryToken}.
     */
    abstract SpreadsheetNameHistoryToken menu();

    /**
     * Creates a menu {@link SpreadsheetNameHistoryToken} for the given {@link SpreadsheetSelection}.
     */
    final SpreadsheetNameHistoryToken menu(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        if(false == (selection.isCellReference() || selection.isColumnReference() || selection.isRowReference())) {
            throw new IllegalArgumentException("Expected cell, column or row but got " + selection);
        }

        return this.menu0(selection);
    }

    abstract SpreadsheetNameHistoryToken menu0(final SpreadsheetSelection selection);

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link SpreadsheetPatternKind}.
     */
    abstract SpreadsheetNameHistoryToken pattern(final SpreadsheetPatternKind patternKind);

    /**
     * Creates a save {@link HistoryToken} after attempting to parse the value.
     */
    abstract SpreadsheetNameHistoryToken save(final String value);

    /**
     * Factory that creates a {@link SpreadsheetNameHistoryToken} with the given {@link TextStylePropertyName} property name.
     */
    abstract SpreadsheetNameHistoryToken style(final TextStylePropertyName<?> propertyName);

    /**
     * Creates a unfreeze {@link SpreadsheetNameHistoryToken}.
     */
    abstract SpreadsheetNameHistoryToken unfreeze();

    final HistoryToken parsePattern(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> patternKind = parseComponent(cursor);
        if(patternKind.isPresent()) {
            result = this.pattern(
                    SpreadsheetPatternKind.fromTypeName("spreadsheet-" + patternKind.get() + "-pattern")
            );
        } else {
            cursor.end();
        }
        return result;
    }

    final SpreadsheetNameHistoryToken parseSave(final TextCursor cursor) {
        return this.save(
                parseAll(cursor)
        );
    }

    final HistoryToken parseStyle(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> style = parseComponent(cursor);
        if(style.isPresent()) {
            result = this.style(
                    TextStylePropertyName.with(
                            style.get()
                    )
            );
        }
        return result;
    }

    /**
     * Hash changed, restore the id and metadata if necessary.
     */
    @Override
    public final void onHashChange(final HistoryToken previous,
                                   final AppContext context) {
        final SpreadsheetId id = this.id();
        if (false == id.equals(
                context.spreadsheetMetadata()
                        .id()
                        .orElse(null))
        ) {
            context.spreadsheetMetadataFetcher()
                    .loadSpreadsheetMetadata(id);
        }

        this.pushHistoryTokenIdAndName(
                id,
                this.name(),
                context
        );

        this.onHashChange0(
                previous,
                context
        );
    }

    /**
     * Continues executing some action for this history token.
     */
    abstract void onHashChange0(final HistoryToken previous,
                                final AppContext context);

    /**
     * The new metadata might have a different id or name, update the history token.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.pushHistoryTokenIdAndName(
                metadata.id(),
                metadata.name(),
                context
        );
    }

    private void pushHistoryTokenIdAndName(final Optional<SpreadsheetId> id,
                                           final Optional<SpreadsheetName> name,
                                           final AppContext context) {
        if (id.isPresent() && name.isPresent()) {
            this.pushHistoryTokenIdAndName(
                    id.get(),
                    name.get(),
                    context
            );
        }
    }

    private void pushHistoryTokenIdAndName(final SpreadsheetId id,
                                           final SpreadsheetName name,
                                           final AppContext context) {
        context.pushHistoryToken(
                this.setIdAndName(
                        id,
                        name
                )
        );
    }
}
