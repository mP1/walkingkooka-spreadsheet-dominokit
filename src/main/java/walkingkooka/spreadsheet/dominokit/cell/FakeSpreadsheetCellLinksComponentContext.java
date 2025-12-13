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

package walkingkooka.spreadsheet.dominokit.cell;

import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Optional;
import java.util.Set;

public class FakeSpreadsheetCellLinksComponentContext extends FakeSpreadsheetCellLabelsAnchorComponentContext implements SpreadsheetCellLinksComponentContext {

    public FakeSpreadsheetCellLinksComponentContext() {
        super();
    }

    @Override
    public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        throw new UnsupportedOperationException();
    }
}
