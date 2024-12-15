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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * Selects one of the available style toolbar links.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/style/color
 * </pre>
 */
final public class SpreadsheetCellStyleSelectHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSelectHistoryToken<T> with(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final AnchoredSpreadsheetSelection anchoredSelection,
                                                              final TextStylePropertyName<T> propertyName) {
        return new SpreadsheetCellStyleSelectHistoryToken<>(
                id,
                name,
                anchoredSelection,
                propertyName
        );
    }

    private SpreadsheetCellStyleSelectHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final TextStylePropertyName<T> propertyName) {
        super(
                id,
                name,
                anchoredSelection,
                propertyName
        );
    }

    @Override
    UrlFragment styleUrlFragment() {
        return SELECT;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).style(
                this.propertyName()
        );
    }

    @Override
    HistoryToken save0(final String value) {
        final TextStylePropertyName<T> propertyName = this.propertyName();

        return cellStyleSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                propertyName,
                Optional.ofNullable(
                        value.isEmpty() ?
                                null :
                                propertyName.parseValue(value)
                )
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO select toolbar icon
    }
}
