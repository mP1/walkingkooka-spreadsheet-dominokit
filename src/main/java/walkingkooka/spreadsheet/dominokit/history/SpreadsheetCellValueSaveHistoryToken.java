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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.cell.value.SpreadsheetCellValueDialogComponent;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.validation.ValueTypeName;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link HistoryToken} that contains a {@link SpreadsheetFormula#value()} encoded as JSON.
 * The {@link ValueTypeName} will contain the value type.
 */
public final class SpreadsheetCellValueSaveHistoryToken extends SpreadsheetCellValueHistoryToken implements Value<Optional<?>> {

    static SpreadsheetCellValueSaveHistoryToken with(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final ValueTypeName valueType,
                                                     final Optional<?> value) {
        return new SpreadsheetCellValueSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            valueType,
            value
        );
    }

    private SpreadsheetCellValueSaveHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final ValueTypeName valueType,
                                                 final Optional<?> value) {
        super(
            id,
            name,
            anchoredSelection,
            Optional.of(valueType)
        );

        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public Optional<?> value() {
        return this.value;
    }

    private final Optional<?> value;

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellValueSelect(
            this.id,
            this.name,
            this.anchoredSelection(),
            this.valueType.get()
        );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellValueSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            this.valueType.get(),
            this.value
        );
    }

    // cell/A1/value/save/Value
    @Override
    UrlFragment valueUrlFragment() {
        // TODO add support to remove JsonString open/close quotes, which is possible since #valueType is present.

        final ValueTypeName valueType = this.valueType.orElse(SpreadsheetValueType.TEXT);

        String json = "";

        Object valueOrNull = this.value.orElse(null);
        if (null != valueOrNull) {
            if (SpreadsheetCellValueDialogComponent.TODAY_TEXT.equals(valueOrNull) && ((SpreadsheetValueType.DATE.equals(valueType) || SpreadsheetValueType.LOCAL_DATE.equals(valueType)))) {
                json = SpreadsheetCellValueDialogComponent.TODAY_TEXT;
            } else {
                if (SpreadsheetCellValueDialogComponent.NOW_TEXT.equals(valueOrNull) && ((SpreadsheetValueType.DATE_TIME.equals(valueType) || SpreadsheetValueType.LOCAL_DATE_TIME.equals(valueType) || SpreadsheetValueType.TIME.equals(valueType) || SpreadsheetValueType.LOCAL_TIME.equals(valueType)))) {
                    json = SpreadsheetCellValueDialogComponent.NOW_TEXT;
                } else {
                    json = MARSHALL_UNMARSHALL_CONTEXT.marshall(valueOrNull).toString();
                }
            }
        }

        return valueType.urlFragment()
            .appendSlashThen(
                saveUrlFragment(json)
            );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchValue(
                this.id,
                this.anchoredSelection().selection(),
                this.valueType.orElse(SpreadsheetValueType.TEXT),
                this.value
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellValueSave(
            this.id,
            this.name,
            this.anchoredSelection,
            this.valueType.get(),
            this.value
        );
    }
}
