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

package walkingkooka.spreadsheet.dominokit.columnrow;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;

public class FakeSpreadsheetColumnOrRowReferenceBoundedComponentContext implements SpreadsheetColumnOrRowReferenceBoundedComponentContext {

    public FakeSpreadsheetColumnOrRowReferenceBoundedComponentContext() {
        super();
    }

    @Override
    public SpreadsheetColumnOrRowReferenceOrRange columnOrRowRange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MenuItem<SpreadsheetColumnOrRowReference> createMenuItem(final SpreadsheetColumnOrRowReference value) {
        throw new UnsupportedOperationException();
    }
}
