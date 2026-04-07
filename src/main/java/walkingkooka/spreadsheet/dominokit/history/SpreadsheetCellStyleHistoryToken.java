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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

abstract public class SpreadsheetCellStyleHistoryToken<T> extends SpreadsheetCellHistoryToken {

    SpreadsheetCellStyleHistoryToken(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                     final Optional<TextStylePropertyName<T>> stylePropertyName) {
        super(
            id,
            name,
            anchoredSelection
        );

        this.stylePropertyName = Objects.requireNonNull(stylePropertyName, "stylePropertyName");
    }

    final Optional<TextStylePropertyName<T>> stylePropertyName;

    // /1/SpreadsheetName/cell/A1/style/
    // /1/SpreadsheetName/cell/A1/style/color/
    // /1/SpreadsheetName/cell/A1/style/color/save/#123
    @Override //
    final UrlFragment cellUrlFragment() {
        UrlFragment urlFragment = STYLE;

        final TextStylePropertyName<T> stylePropertyNameOrNull = this.stylePropertyName.orElse(null);
        if (null != stylePropertyNameOrNull) {
            urlFragment = urlFragment.appendSlashThen(
                stylePropertyNameOrNull.urlFragment()
            ).appendSlashThen(this.styleUrlFragment());
        }

        return urlFragment;
    }

    abstract UrlFragment styleUrlFragment();

    @Override
    public final HistoryToken clearAction() {
        return this.setStylePropertyName(
            Cast.to(this.stylePropertyName)
        );
    }
}
