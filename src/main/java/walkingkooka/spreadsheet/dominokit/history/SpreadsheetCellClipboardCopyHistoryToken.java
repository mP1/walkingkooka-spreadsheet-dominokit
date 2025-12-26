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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatchers;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * A {@link HistoryToken} that represents a COPY to the clipboard of a cell or cell range in part of whole.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/copy/cell
 * /spreadsheet-id/spreadsheet-name/cell/selected-cell-or-cell-range-or-label/copy/SpreadsheetCellClipboardKind
 * </pre>
 */
public final class SpreadsheetCellClipboardCopyHistoryToken extends SpreadsheetCellClipboardHistoryToken {

    static SpreadsheetCellClipboardCopyHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final SpreadsheetCellClipboardKind kind) {
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
    void onHistoryTokenChangeClipboard(final AppContext context) {
        final SpreadsheetCellClipboardKind kind = this.kind();
        final SpreadsheetCellRangeReference range = context.spreadsheetViewportCache()
            .resolveIfLabelOrFail(
                this.anchoredSelection()
                    .selection()
            ).toCellRange();

        final ClipboardTextItem clipboardTextItem = ClipboardTextItem.toJson(
            context.spreadsheetViewportCache()
                .cellRange(range),
            kind,
            context
        );

        context.writeClipboardItem(
            clipboardTextItem,
            ClipboardContextWriteWatchers.logging(
                clipboardTextItem,
                context
            )
        );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellClipboardCopy(
            this.id,
            this.name,
            this.anchoredSelection,
            this.kind()
        );
    }
}
