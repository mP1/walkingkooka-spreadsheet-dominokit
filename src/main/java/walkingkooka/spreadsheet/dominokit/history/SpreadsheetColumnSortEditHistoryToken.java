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
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;

/**
 * This {@link HistoryToken} represents a EDIT dialog to edit the sort parameters. The comparatorNames property may be
 * empty or invalid it is not verified for correctness by this token.
 * <pre>
 * /123/SpreadsheetName456/column/A/sort/edit
 * /123/SpreadsheetName456/column/B:C/bottom/sort/edit
 *
 * /spreadsheet-id/spreadsheet-name/column/column or column-range/sort/edit
 * </pre>
 */
public final class SpreadsheetColumnSortEditHistoryToken extends SpreadsheetColumnSortHistoryToken {

    static SpreadsheetColumnSortEditHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final String comparatorNames) {
        return new SpreadsheetColumnSortEditHistoryToken(
            id,
            name,
            anchoredSelection,
            comparatorNames
        );
    }

    private SpreadsheetColumnSortEditHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final String comparatorNames) {
        super(
            id,
            name,
            anchoredSelection
        );

        this.comparatorNames = Objects.requireNonNull(comparatorNames, "comparatorNames");
    }

    public String comparatorNames() {
        return this.comparatorNames;
    }

    final String comparatorNames;

    @Override
    UrlFragment sortUrlFragment() {
        final String comparatorNames = this.comparatorNames;

        return comparatorNames.isEmpty() ?
            EDIT :
            EDIT.append(
                UrlFragment.SLASH.append(
                    UrlFragment.with(comparatorNames)
                )
            );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetColumnSortEditHistoryToken(
            id,
            name,
            anchoredSelection,
            this.comparatorNames
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // EDIT dialog appears!
    }
}
