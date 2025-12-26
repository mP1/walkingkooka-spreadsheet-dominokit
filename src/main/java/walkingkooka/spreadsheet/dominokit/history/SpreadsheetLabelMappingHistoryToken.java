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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for various {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping} CRUD actions.
 * <pre>
 * #/SpreadsheetId/SpreadsheetName/label-create
 * #/SpreadsheetId/SpreadsheetName/label/SpreadsheetLabelName
 * #/SpreadsheetId/SpreadsheetName/label/SpreadsheetLabelName/save/XXX
 * </pre>
 */
public abstract class SpreadsheetLabelMappingHistoryToken extends SpreadsheetSelectionHistoryToken {

    SpreadsheetLabelMappingHistoryToken(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        super(
            id,
            name
        );
    }

    @Override
    public final HistoryToken setSaveValue(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        HistoryToken historyToken = null;

        if (this instanceof SpreadsheetLabelMappingSelectHistoryToken || this instanceof SpreadsheetLabelMappingSaveHistoryToken) {
            Object valueOrNull = value.orElse(null);

            if (null != valueOrNull && false == valueOrNull instanceof SpreadsheetExpressionReference) {
                this.reportInvalidSaveValue(
                    valueOrNull,
                    SpreadsheetExpressionReference.class
                );
            }

            if (null != valueOrNull) {
                historyToken = HistoryToken.labelMappingSave(
                    id,
                    name,
                    this.cast(SpreadsheetLabelMappingHistoryToken.class)
                        .labelName()
                        .get()
                        .setLabelMappingReference((SpreadsheetExpressionReference) valueOrNull)
                );
            }
        }

        return this.elseIfDifferent(historyToken);
    }
    
    /**
     * /label-create
     * /label/$label...
     */
    @Override //
    final UrlFragment selectionUrlFragment() {
        final UrlFragment labelUrlFragment = this.labelUrlFragment();

        return this instanceof SpreadsheetLabelMappingCreateHistoryToken ?
            labelUrlFragment :
            LABEL.appendSlashThen(labelUrlFragment);
    }

    abstract UrlFragment labelUrlFragment();
}
