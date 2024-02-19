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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.tree.text.TextStyle;

import java.util.Map;

/**
 * This {@link HistoryToken} is used by to paste a styles for many cells over another range.
 */
public final class SpreadsheetCellSaveStyleHistoryToken extends SpreadsheetCellSaveHistoryToken<TextStyle> {

    static SpreadsheetCellSaveStyleHistoryToken with(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final Map<SpreadsheetCellReference, TextStyle> value) {
        return new SpreadsheetCellSaveStyleHistoryToken(
                id,
                name,
                anchoredSelection,
                Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveStyleHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final Map<SpreadsheetCellReference, TextStyle> value) {
        super(
                id,
                name,
                anchoredSelection,
                value
        );
    }

    @Override
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellSaveStyleHistoryToken(
                this.id(),
                this.name(),
                anchoredSelection,
                this.value
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return new SpreadsheetCellSaveStyleHistoryToken(
                id,
                name,
                this.anchoredSelection(),
                this.value
        );
    }

    @Override
    UrlFragment cellSaveUrlFragment() {
        return STYLE;
    }

    private final static UrlFragment STYLE = UrlFragment.parse("style");

    @Override
    HistoryToken setSave0(final String value) {
        return new SpreadsheetCellSaveStyleHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                HistoryToken.parseJson(
                        TextCursors.charSequence(value),
                        TextStyle.class
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO PATCH cell to styles
    }
}
