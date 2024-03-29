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
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatchers;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardValueKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;

/**
 * A {@link HistoryToken} that represents a COPY to the clipboard of a cell or cell range.
 */
public final class SpreadsheetCellClipboardCopyHistoryToken extends SpreadsheetCellClipboardHistoryToken {

    static SpreadsheetCellClipboardCopyHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final SpreadsheetCellClipboardValueKind kind) {
        return new SpreadsheetCellClipboardCopyHistoryToken(
                id,
                name,
                anchoredSelection,
                kind
        );
    }

    private SpreadsheetCellClipboardCopyHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetCellClipboardValueKind kind) {
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
        return new SpreadsheetCellClipboardCopyHistoryToken(
                id,
                name,
                anchoredSelection,
                this.kind()
        );
    }

    @Override //
    UrlFragment clipboardUrlFragment() {
        return COPY;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final SpreadsheetCellClipboardValueKind kind = this.kind();
        final SpreadsheetCellRange range = this.anchoredSelection()
                .selection()
                .toCellRange();

        final ClipboardTextItem clipboardTextItem = ClipboardTextItem.prepare(
                range,
                context.viewportCache()
                        .cells(
                                range
                        ),
                kind,
                context
        );
        context.debug("Copying " + clipboardTextItem);

        context.writeClipboardItem(
                clipboardTextItem,
                ClipboardContextWriteWatchers.logging(
                        clipboardTextItem,
                        context
                )
        );

        context.pushHistoryToken(
                previous
        );
    }
}
