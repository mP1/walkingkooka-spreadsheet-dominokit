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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.function.Function;

/**
 * This token represents a loaded and open spreadsheet ready for viewing and editing.
 * <pre>
 * /1/SpreadsheetName123
 * /spreadsheet-id/spreadsheet-name-here
 * </pre>
 */
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
    UrlFragment spreadsheetNameUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        HistoryToken result = this;

        switch(component) {
            case CELL_STRING:
                result = this.parseCell(cursor);
                break;
            case COLUMN_STRING:
                result = this.parseColumn(cursor);
                break;
            case DELETE_STRING:
                result = this.parseDelete(cursor);
                break;
            case LABEL_STRING:
                result = this.parseLabel(cursor);
                break;
            case SPEADSHEET_STRING:
                result = this.parseMetadata(cursor);
                break;
            case RELOAD_STRING:
                result = this.parseReload(cursor);
                break;
            case RENAME_STRING:
                result = this.parseRename(cursor);
                break;
            case ROW_STRING:
                result = this.parseRow(cursor);
                break;
            default:
                cursor.end();
                break;
        }

        return result;
    }

    private HistoryToken parseCell(final TextCursor cursor) {
        return parseCellColumnOrRow(
                cursor,
                SpreadsheetSelection::parseExpressionReference
        );
    }

    private HistoryToken parseColumn(final TextCursor cursor) {
        return parseCellColumnOrRow(
                cursor,
                SpreadsheetSelection::parseColumnOrColumnRange
        );
    }

    private HistoryToken parseRow(final TextCursor cursor) {
        return parseCellColumnOrRow(
                cursor,
                SpreadsheetSelection::parseRowOrRowRange
        );
    }

    private HistoryToken parseCellColumnOrRow(final TextCursor cursor,
                                              final Function<String, SpreadsheetSelection> parser) {
        HistoryToken result = this;

        final Optional<String> maybeSelection = parseComponent(cursor);
        if (maybeSelection.isPresent()) {
            final SpreadsheetSelection selection = parser.apply(
                    maybeSelection.get()
            );

            result = this.setAnchoredSelection(
                    Optional.of(
                            selection.setDefaultAnchor()
                    )
            );

            final TextCursorSavePoint beforeAnchor = cursor.save();
            boolean restoreCursor = true;

            final Optional<String> maybePossibleAnchor = parseComponent(cursor);
            if (maybePossibleAnchor.isPresent()) {
                final String possibleAnchor = maybePossibleAnchor.get();

                for (final SpreadsheetViewportAnchor possible : SpreadsheetViewportAnchor.values()) {
                    if (SpreadsheetViewportAnchor.NONE == possible) {
                        continue;
                    }
                    if (possible.kebabText().equals(possibleAnchor)) {
                        try {
                            result = result.setAnchoredSelection(
                                    Optional.of(
                                            selection.setAnchor(possible)
                                    )
                            );
                            restoreCursor = false;
                        } catch (final IllegalArgumentException ignore) {
                            // nop
                        }
                        break;
                    }
                }
            }

            if (restoreCursor) {
                beforeAnchor.restore();
            }
        }

        return result;
    }

    private HistoryToken parseLabel(final TextCursor cursor) {
        final Optional<String> label = parseComponent(cursor);
        return this.setLabelName(
                label.map(SpreadsheetSelection::labelName)
        );
    }

    private HistoryToken parseDelete(final TextCursor cursor) {
        return this.setDelete();
    }

    private HistoryToken parseMetadata(final TextCursor cursor) {
        HistoryToken result = null;

        final Optional<String> maybeNext = parseComponent(cursor);
        if (maybeNext.isPresent()) {
            final String next = maybeNext.get();

            try {
                switch (next) {
                    case STYLE_STRING:
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

        if (null == result) {
            result = metadataSelect(
                    this.id(),
                    this.name()
            );
        }

        return result;
    }

    private HistoryToken parseReload(final TextCursor cursor) {
        return this.reload();
    }

    private HistoryToken parseRename(final TextCursor cursor) {
        return this.setRename();
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    HistoryToken setDelete0() {
        return this;
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    HistoryToken setMenu1() {
        return this;
    }

    @Override //
    AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        return selection.setDefaultAnchor();
    }

    // factory for /spreadsheet-id/spreadsheet-name/metadata/pattern/*
    @Override
    HistoryToken replacePatternKind(final Optional<SpreadsheetPatternKind> patternKind) {
        return metadataPropertySelect(
                this.id(),
                this.name(),
                patternKind.get()
                        .spreadsheetMetadataPropertyName()
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    // factory for /spreadsheet-id/spreadsheet-name/metadata/style/*
    @Override
    HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return metadataPropertyStyle(
                this.id(),
                this.name(),
                propertyName
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {

    }
}
