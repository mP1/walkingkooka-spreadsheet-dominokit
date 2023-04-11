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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public abstract class SpreadsheetMetadataHistoryTokenTestCase<T extends SpreadsheetMetadataHistoryToken> extends SpreadsheetNameHistoryTokenTestCase<T> {

    SpreadsheetMetadataHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testIdNameViewportSelectionWithViewportSelection() {
        final SpreadsheetViewportSelection viewportSelection = SpreadsheetSelection.parseCell("Z99")
                .setDefaultAnchor();

        this.idNameViewportSelectionAndCheck(
                ID,
                NAME,
                viewportSelection,
                SpreadsheetHistoryToken.cell(
                        ID,
                        NAME,
                        viewportSelection
                )
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public final void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    @Test
    public final void testMenuWithColumn() {
        this.menuWithColumnAndCheck();
    }

    @Test
    public final void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name);
}
