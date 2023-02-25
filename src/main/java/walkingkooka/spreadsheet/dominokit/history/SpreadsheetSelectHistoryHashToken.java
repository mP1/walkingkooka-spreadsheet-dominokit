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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.function.Function;

public final class SpreadsheetSelectHistoryHashToken extends SpreadsheetNameHistoryHashToken {

    static SpreadsheetSelectHistoryHashToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name) {
        return new SpreadsheetSelectHistoryHashToken(
                id,
                name
        );
    }

    private SpreadsheetSelectHistoryHashToken(final SpreadsheetId id,
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
    HistoryHashToken parse0(final String component,
                            final TextCursor cursor) {
        HistoryHashToken result = this;

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
                final Optional<String> label = parseComponent(cursor);
                if(label.isPresent()) {
                    result = labelMapping(
                            SpreadsheetSelection.labelName(label.get())
                    );
                }
               break;
            default:
                cursor.end();
                break;
        }

        return result;
    }

    private HistoryHashToken parseCellColumnOrRow(final String cellColumnOrLabel,
                                                  final TextCursor cursor) {
        HistoryHashToken result = this;

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

    private HistoryHashToken parseCellColumnOrRow0(final TextCursor cursor,
                                                   final Function<String, SpreadsheetSelection> parser,
                                                   final Function<SpreadsheetViewportSelection, HistoryHashToken> historyHashTokenFactory) {
        HistoryHashToken result = this;

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

                result = historyHashTokenFactory.apply(viewportSelection);
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

    @Override
    SpreadsheetNameHistoryHashToken clear() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken delete() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken formula() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken freeze() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken menu() {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken pattern(final SpreadsheetPatternKind patternKind) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken save(final String value) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken style(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    @Override
    SpreadsheetNameHistoryHashToken unfreeze() {
        return this;
    }
}
