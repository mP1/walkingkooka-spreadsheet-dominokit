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
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * Used by the numerous toolbar style icons, such as BOLD to save ar PATCH a cell style or styles.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/style/color/save/#123456
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/style/{@link TextStylePropertyName}/save/value-as-text
 * </pre>
 */
final public class SpreadsheetCellStyleSaveHistoryToken<T> extends SpreadsheetCellStyleHistoryToken<T> {

    static <T> SpreadsheetCellStyleSaveHistoryToken<T> with(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final AnchoredSpreadsheetSelection anchoredSelection,
                                                            final TextStylePropertyName<T> propertyName,
                                                            final Optional<T> propertyValue) {
        return new SpreadsheetCellStyleSaveHistoryToken<>(
            id,
            name,
            anchoredSelection,
            propertyName,
            propertyValue
        );
    }

    private SpreadsheetCellStyleSaveHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final TextStylePropertyName<T> propertyName,
                                                 final Optional<T> propertyValue) {
        super(
            id,
            name,
            anchoredSelection,
            propertyName
        );
        this.propertyValue = Objects.requireNonNull(propertyValue, "propertyValue");
    }

    public Optional<T> propertyValue() {
        return this.propertyValue;
    }

    private final Optional<T> propertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return saveUrlFragment(this.propertyValue());
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
        ).save(
            this.propertyValue()
        );
    }

    @Override
    HistoryToken save0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .saveStyleProperty(
                this.id(),
                this.anchoredSelection()
                    .selection(),
                this.propertyName(),
                this.propertyValue()
            );
    }

    public TextStyleProperty<T> textStyleProperty() {
        return TextStyleProperty.with(
            this.propertyName(),
            this.propertyValue()
        );
    }
}
