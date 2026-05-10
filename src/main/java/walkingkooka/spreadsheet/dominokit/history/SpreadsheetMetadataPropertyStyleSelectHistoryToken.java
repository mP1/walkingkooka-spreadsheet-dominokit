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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetMetadataPropertyStyleSelectHistoryToken<T> extends SpreadsheetMetadataPropertyStyleHistoryToken<T> {

    static <T> SpreadsheetMetadataPropertyStyleSelectHistoryToken<T> with(final SpreadsheetId spreadsheetId,
                                                                          final SpreadsheetName spreadsheetName,
                                                                          final Optional<TextStylePropertyName<T>> stylePropertyName,
                                                                          final Optional<String> filter) {
        return new SpreadsheetMetadataPropertyStyleSelectHistoryToken<>(
            spreadsheetId,
            spreadsheetName,
            stylePropertyName,
            filter
        );
    }

    private SpreadsheetMetadataPropertyStyleSelectHistoryToken(final SpreadsheetId spreadsheetId,
                                                               final SpreadsheetName spreadsheetName,
                                                               final Optional<TextStylePropertyName<T>> stylePropertyName,
                                                               final Optional<String> filter) {
        super(
            spreadsheetId,
            spreadsheetName,
            stylePropertyName
        );

        this.filter = Objects.requireNonNull(filter, "filter");
    }

    final Optional<String> filter;

    // style/
    // style/*
    // style/*/filter/FILTER
    // style/color
    // style/color/filter/FILTER
    @Override
    UrlFragment metadataPropertyUrlFragment() {
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

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // new id/name but still metadata+style+property select
    @Override //
    HistoryToken replaceSpreadsheetIdAndSpreadsheetName(final SpreadsheetId spreadsheetId,
                                                        final SpreadsheetName spreadsheetName) {
        return with(
            spreadsheetId,
            spreadsheetName,
            this.stylePropertyName,
            this.filter
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // show metadata style UI
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitMetadataStyleSelect(
            this.spreadsheetId,
            this.spreadsheetName,
            this.stylePropertyName
        );
    }
}
