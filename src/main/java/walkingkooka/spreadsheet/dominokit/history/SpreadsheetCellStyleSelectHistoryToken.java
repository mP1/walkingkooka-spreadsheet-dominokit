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

import walkingkooka.Cast;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * Selects a {@link walkingkooka.tree.text.TextStyleProperty} for editing within a {@link walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleDialogComponent}.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/style/
 * /123/SpreadsheetName456/cell/A1/style/STAR
 * /123/SpreadsheetName456/cell/A1/style/STAR/filter/FILTER
 * /123/SpreadsheetName456/cell/A1/style/color
 * /123/SpreadsheetName456/cell/A1/style/color/filter/FILTER
 * </pre>
 */
final public class SpreadsheetCellStyleSelectHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSelectHistoryToken<T> with(final SpreadsheetId spreadsheetId,
                                                              final SpreadsheetName spreadsheetName,
                                                              final AnchoredSpreadsheetSelection anchoredSelection,
                                                              final Optional<TextStylePropertyName<T>> propertyName,
                                                              final Optional<String> filter) {
        return new SpreadsheetCellStyleSelectHistoryToken<>(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            propertyName,
            filter
        );
    }

    private SpreadsheetCellStyleSelectHistoryToken(final SpreadsheetId spreadsheetId,
                                                   final SpreadsheetName spreadsheetName,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final Optional<TextStylePropertyName<T>> propertyName,
                                                   final Optional<String> filter) {
        super(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            propertyName
        );

        this.filter = Objects.requireNonNull(filter, "filter");
    }

    final Optional<String> filter;

    // /1/SpreadsheetName/cell/A1/style/
    // /1/SpreadsheetName/cell/A1/style/*/
    // /1/SpreadsheetName/cell/A1/style/*/filter/FILTER
    // /1/SpreadsheetName/cell/A1/style/color/
    // /1/SpreadsheetName/cell/A1/style/color/filter/FILTER
    @Override
    UrlFragment styleUrlFragment() {
        final UrlFragment urlFragment;

        final TextStylePropertyName<?> stylePropertyNameOrNull = this.stylePropertyName.orElse(null);

        final String filter = this.filter.orElse("")
            .trim();
        if (filter.isEmpty()) {
            urlFragment = null != stylePropertyNameOrNull ?
                stylePropertyNameOrNull.urlFragment() :
                UrlFragment.EMPTY;
        } else {
            urlFragment = (
                null != stylePropertyNameOrNull ?
                    stylePropertyNameOrNull.urlFragment() :
                    TextStylePropertyName.ALL.urlFragment()
            )
                .appendSlashThen(FILTER)
                .appendSlashThen(
                    UrlFragment.with(filter)
                );
        }

        return urlFragment;
    }


    @Override //
    HistoryToken replaceSpreadsheetIdSpreadsheetNameAnchoredSelection(final SpreadsheetId spreadsheetId,
                                                                      final SpreadsheetName spreadsheetName,
                                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection
        ).setStylePropertyName(
            Cast.to(this.stylePropertyName)
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO select toolbar icon
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellStyleSelect(
            this.spreadsheetId,
            this.spreadsheetName,
            this.anchoredSelection,
            Cast.to(this.stylePropertyName)
        );
    }
}
