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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToTextStyleMap;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStyle;

import java.util.Map;

/**
 * This {@link HistoryToken} is used by to paste a styles for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1:A2/save/style/{"A1":{"color":"#111111"},"A2":{"color":"#222222"}}
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/style/style-patch as JSON.
 * </pre>
 */
public final class SpreadsheetCellSaveStyleHistoryToken extends SpreadsheetCellSaveMapHistoryToken<TextStyle> {

    static SpreadsheetCellSaveStyleHistoryToken with(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, TextStyle> value) {
        return new SpreadsheetCellSaveStyleHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToTextStyleMap.with(value)
        );
    }

    private SpreadsheetCellSaveStyleHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final SpreadsheetCellReferenceToTextStyleMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override
    SpreadsheetCellReferenceToTextStyleMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToTextStyleMap.class
        );
    }

    @Override
    SpreadsheetCellSaveStyleHistoryToken replace(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final Map<SpreadsheetCellReference, TextStyle> value) {
        return new SpreadsheetCellSaveStyleHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToTextStyleMap.with(value)
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return STYLE;
    }

    // HistoryToken.....................................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsStyle(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
