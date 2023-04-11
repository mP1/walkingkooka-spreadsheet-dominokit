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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.function.Function;

public final class SpreadsheetSelectHistoryToken extends SpreadsheetNameHistoryToken {

    static SpreadsheetSelectHistoryToken with(final SpreadsheetId id,
                                              final SpreadsheetName name) {
        return new SpreadsheetSelectHistoryToken(
                id,
                name
        );
    }

    private SpreadsheetSelectHistoryToken(final SpreadsheetId id,
                                          final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override
    UrlFragment spreadsheetUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        HistoryToken result = this;

        switch(component) {
            case "cell":
            case "column":
            case "row":
                result = parseCellColumnOrRow(
                        component,
                        cursor
                );
                break;
            case "label":
                result = this.parseLabel(cursor);
                break;
            case "metadata":
                result = this.parseMetadata(cursor);
                break;
            default:
                cursor.end();
                break;
        }

        return result;
    }

    private HistoryToken parseCellColumnOrRow(final String cellColumnOrLabel,
                                              final TextCursor cursor) {
        HistoryToken result = this;

        switch(cellColumnOrLabel) {
            case "cell":
                result = parseCellColumnOrRow0(
                        cursor,
                        SpreadsheetSelection::parseExpressionReference,
                        this::cell
                );
                break;
            case "column":
                result = parseCellColumnOrRow0(
                        cursor,
                        SpreadsheetSelection::parseColumnOrColumnRange,
                        this::column
                );
                break;
            case "row":
                result = parseCellColumnOrRow0(
                        cursor,
                        SpreadsheetSelection::parseRowOrRowRange,
                        this::row
                );
                break;
            default:
                // cant happen.
                cursor.end();
                break;
        }

        return result;
    }

    private HistoryToken parseCellColumnOrRow0(final TextCursor cursor,
                                               final Function<String, SpreadsheetSelection> parser,
                                               final Function<SpreadsheetViewportSelection, HistoryToken> HistoryTokenFactory) {
        HistoryToken result = this;

        final Optional<String> maybeSelection = parseComponent(cursor);
        if(maybeSelection.isPresent()) {
                final SpreadsheetSelection selection = parser.apply(maybeSelection.get());

                final SpreadsheetViewportSelectionAnchor defaultAnchor = selection.defaultAnchor();
                final SpreadsheetViewportSelectionAnchor anchor = tryParseAnchor(
                        cursor,
                        defaultAnchor
                );

                SpreadsheetViewportSelection viewportSelection = null;
                try {
                    viewportSelection = selection.setAnchor(anchor);
                } catch (final RuntimeException badAnchor) {
                    viewportSelection = selection.setAnchor(defaultAnchor); // ignore invalid anchor use default instead.
                }

                result = HistoryTokenFactory.apply(viewportSelection);
        }

        return result;
    }

    /**
     * Tries to parse the next component into a {@link SpreadsheetViewportSelectionAnchor}.
     */
    private static SpreadsheetViewportSelectionAnchor tryParseAnchor(final TextCursor cursor,
                                                                     final SpreadsheetViewportSelectionAnchor defaultAnchor) {
        SpreadsheetViewportSelectionAnchor anchor = null;

        final TextCursorSavePoint save = cursor.save();

        final Optional<String> maybePossibleAnchor = parseComponent(cursor);
        if (maybePossibleAnchor.isPresent()) {
            final String possibleAnchor = maybePossibleAnchor.get();

            for (final SpreadsheetViewportSelectionAnchor possible : SpreadsheetViewportSelectionAnchor.values()) {
                if (SpreadsheetViewportSelectionAnchor.NONE == possible) {
                    continue;
                }
                if (possible.kebabText().equals(possibleAnchor)) {
                    anchor = possible;
                    break;
                }
            }
        }

        if (null == anchor) {
            save.restore();
            anchor = defaultAnchor;
        }

        return anchor;
    }

    private HistoryToken parseLabel(final TextCursor cursor) {
        HistoryToken result = this;

        final Optional<String> label = parseComponent(cursor);
        if (label.isPresent()) {
            result = labelMapping(
                    SpreadsheetSelection.labelName(label.get())
            );
        }
        return result;
    }

    private HistoryToken parseMetadata(final TextCursor cursor) {
        HistoryToken result = null;

        final Optional<String> maybeNext = parseComponent(cursor);
        if (maybeNext.isPresent()) {
            final String next = maybeNext.get();

            try {
                switch (next) {
                    case "pattern":
                        result = this.parsePattern(cursor);
                        break;
                    case "style":
                        result = this.parseStyle(cursor);
                        break;
                    default:
                        result = metadataPropertySelect(
                                this.id(),
                                this.name(),
                                SpreadsheetMetadataPropertyName.with(next)
                        );
                        break;
                }
            } catch (final RuntimeException ignored) {
                // ignored
            }
        }

        if(null == result) {
            result = metadataSelect(
                    this.id(),
                    this.name()
            );
        }

        return result;
    }

    @Override
    HistoryToken idNameViewportSelection0(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final SpreadsheetViewportSelection viewportSelection) {
        return this.viewportSelectionHistoryToken(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    SpreadsheetNameHistoryToken clear() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken delete() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken freeze() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken menu() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryToken menu0(final SpreadsheetSelection selection) {
        return this.viewportSelectionHistoryToken(
                Optional.of(
                        selection.setDefaultAnchor()
                )
        ).menu();
    }

    // factory for /spreadsheet-id/spreadsheet-name/metadata/pattern/*
    @Override
    SpreadsheetNameHistoryToken pattern(final SpreadsheetPatternKind patternKind) {
        return metadataPropertySelect(
                this.id(),
                this.name(),
                patternKind.spreadsheetMetadataPropertyName()
        );
    }

    @Override
    SpreadsheetNameHistoryToken save(final String value) {
        return this;
    }

    // factory for /spreadsheet-id/spreadsheet-name/metadata/style/*
    @Override
    SpreadsheetNameHistoryToken style(final TextStylePropertyName<?> propertyName) {
        return metadataPropertyStyle(
                this.id(),
                this.name(),
                propertyName
        );
    }

    @Override
    SpreadsheetNameHistoryToken unfreeze() {
        return this;
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .loadSpreadsheetMetadata(
                        this.id()
                );
    }
}
