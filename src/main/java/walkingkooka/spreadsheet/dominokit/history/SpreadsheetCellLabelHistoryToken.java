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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

/**
 * Base class for various label functions for a cell selection.
 */
public abstract class SpreadsheetCellLabelHistoryToken extends SpreadsheetCellHistoryToken {

    SpreadsheetCellLabelHistoryToken(final SpreadsheetId id,
                                     final SpreadsheetName name,
                                     final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );
    }

    public abstract Optional<SpreadsheetLabelName> labelName();

    @Override
    UrlFragment cellUrlFragment() {
        return LABEL.appendSlashThen(this.cellLabelUrlFragment());
    }

    abstract UrlFragment cellLabelUrlFragment();

    @Override
    final HistoryToken save0(final String labelName) {
        return labelName.isEmpty() ?
            this.clearAction() :
            this.setLabelName(
                Optional.of(
                    SpreadsheetSelection.labelName(labelName)
                )
            );
    }
}
