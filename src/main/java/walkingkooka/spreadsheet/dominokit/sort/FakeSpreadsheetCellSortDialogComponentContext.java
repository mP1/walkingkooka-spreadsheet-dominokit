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

package walkingkooka.spreadsheet.dominokit.sort;

import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorSelector;
import walkingkooka.spreadsheet.dominokit.dialog.FakeDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;

import java.util.List;

public class FakeSpreadsheetCellSortDialogComponentContext extends FakeDialogComponentContext
    implements SpreadsheetCellSortDialogComponentContext {

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorSelector selector,
                                                          final ProviderContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                          final List<?> values,
                                                          final ProviderContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetComparatorInfoSet spreadsheetComparatorInfos() {
        throw new UnsupportedOperationException();
    }
}
