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
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextReadWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Optional;

/**
 * A {@link HistoryToken} that represents a PASTE to the clipboard of a cell or cell range.
 */
public final class SpreadsheetCellClipboardPasteHistoryToken extends SpreadsheetCellClipboardHistoryToken {

    static SpreadsheetCellClipboardPasteHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                                          final SpreadsheetCellClipboardKind kind) {
        return new SpreadsheetCellClipboardPasteHistoryToken(
                id,
                name,
                anchoredSelection,
                kind
        );
    }

    private SpreadsheetCellClipboardPasteHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final SpreadsheetCellClipboardKind kind) {
        super(
                id,
                name,
                anchoredSelection,
                kind
        );
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellClipboardPasteHistoryToken(
                id,
                name,
                anchoredSelection,
                this.kind()
        );
    }

    @Override //
    UrlFragment clipboardUrlFragment() {
        return PASTE;
    }

    @Override
    void onHistoryTokenChangeClipboard(final AppContext context) {
        final Optional<SpreadsheetSelection> maybeSelection = context.spreadsheetViewportCache()
                .nonLabelSelection(
                        SpreadsheetCellClipboardPasteHistoryToken.this.anchoredSelection()
                                .selection()
                );
        if (maybeSelection.isPresent()) {
            final SpreadsheetCellRangeReference rangeReference = maybeSelection.get()
                    .toCellRange();
            context.readClipboardItem(
                    Predicates.is(ClipboardTextItem.MEDIA_TYPE),
                    new ClipboardContextReadWatcher() {
                        @Override
                        public void onSuccess(final List<ClipboardTextItem> items) {
                            final SpreadsheetCellClipboardPasteHistoryToken that = SpreadsheetCellClipboardPasteHistoryToken.this;
                            final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();
                            final SpreadsheetId id = that.id();
                            final SpreadsheetCellClipboardKind kind = that.kind();

                            for (final ClipboardTextItem item : items) {
                                kind.saveOrUpdateCells(
                                        fetcher,
                                        id,
                                        item.toSpreadsheetCellRange(context)
                                                .move(rangeReference)
                                );
                            }
                        }

                        @Override
                        public void onFailure(final Object cause) {
                            context.error("Paste failed", cause);
                        }
                    }
            );
        }
    }
}
