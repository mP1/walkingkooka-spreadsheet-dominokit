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

package walkingkooka.spreadsheet.dominokit.label;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingListHistoryToken;

final class AppContextSpreadsheetLabelMappingListDialogComponentContextLabel extends AppContextSpreadsheetLabelMappingListDialogComponentContext {

    static AppContextSpreadsheetLabelMappingListDialogComponentContextLabel with(final AppContext context) {
        return new AppContextSpreadsheetLabelMappingListDialogComponentContextLabel(context);
    }

    private AppContextSpreadsheetLabelMappingListDialogComponentContextLabel(final AppContext context) {
        super(context);
    }

    @Override
    public void loadLabelMappings(final SpreadsheetId id,
                                  final HistoryTokenOffsetAndCount offsetAndCount) {
        this.context.spreadsheetDeltaFetcher()
            .loadLabelMappings(
                id,
                offsetAndCount.offset()
                    .orElse(0),
                offsetAndCount.count()
                    .orElse(0)
            );
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetLabelMappingListHistoryToken;
    }
}
