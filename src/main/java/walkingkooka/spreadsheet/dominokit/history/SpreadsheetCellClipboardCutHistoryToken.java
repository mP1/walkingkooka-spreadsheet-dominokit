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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

/**
 * A {@link HistoryToken} that represents a CUT to the clipboard of a cell or cell range.
 */
public final class SpreadsheetCellClipboardCutHistoryToken extends SpreadsheetCellClipboardHistoryToken {

    static SpreadsheetCellClipboardCutHistoryToken with(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final SpreadsheetCellClipboardKind kind) {
        return new SpreadsheetCellClipboardCutHistoryToken(
                id,
                name,
                anchoredSelection,
                kind
        );
    }

    private SpreadsheetCellClipboardCutHistoryToken(final SpreadsheetId id,
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
        return new SpreadsheetCellClipboardCutHistoryToken(
                id,
                name,
                anchoredSelection,
                this.kind()
        );
    }

    @Override //
    UrlFragment clipboardUrlFragment() {
        return CUT;
    }

    @Override
    void onHistoryTokenChangeClipboard(final AppContext context) {
        final SpreadsheetCellClipboardKind kind = this.kind();
        final SpreadsheetCellRangeReference range = context.spreadsheetViewportCache()
                .resolveIfLabel(
                        this.anchoredSelection()
                                .selection()
                ).toCellRange();

        final ClipboardTextItem clipboardTextItem = ClipboardTextItem.toJson(
                context.spreadsheetViewportCache()
                        .cellRange(range),
                kind,
                context.marshallContext()
        );

        context.writeClipboardItem(
                clipboardTextItem,
                new ClipboardContextWriteWatcher() {
                    @Override
                    public void onSuccess() {
                        final SpreadsheetCellClipboardCutHistoryToken that = SpreadsheetCellClipboardCutHistoryToken.this;
                        final SpreadsheetDeltaFetcher fetcher = context.spreadsheetDeltaFetcher();
                        final SpreadsheetId id = that.id();
                        final AnchoredSpreadsheetSelection anchoredSelection = that.anchoredSelection();
                        final SpreadsheetSelection selection = anchoredSelection.selection();

                        switch (kind) {
                            case CELL:
                                fetcher.deleteCells(
                                        id,
                                        context.viewport(
                                                Optional.of(
                                                        anchoredSelection
                                                )
                                        )
                                );
                                break;
                            case FORMULA:
                                fetcher.patchFormula(
                                        id,
                                        selection,
                                        SpreadsheetFormula.EMPTY // delete formulas
                                );
                                break;
                            case FORMAT_PATTERN:
                                fetcher.patchFormatPattern(
                                        id,
                                        selection,
                                        SpreadsheetCell.NO_FORMAT_PATTERN
                                );
                                break;
                            case PARSE_PATTERN:
                                fetcher.patchParsePattern(
                                        id,
                                        selection,
                                        SpreadsheetCell.NO_PARSE_PATTERN
                                );
                                break;
                            case STYLE:
                                fetcher.patchStyle(
                                        id,
                                        selection,
                                        JsonNode.nullNode()
                                );
                                break;
                            case FORMATTED_VALUE:
                                throw new UnsupportedOperationException("Cut formatted-value is not supported");
                        }
                    }

                    @Override
                    public void onFailure(final Object cause) {

                    }
                }
        );
    }
}
