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

public final class SpreadsheetSelectHistoryTokenTest extends SpreadsheetNameHistoryTokenTestCase<SpreadsheetSelectHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456");
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    @Test
    public void testMenuWithColumn() {
        this.menuWithColumnAndCheck();
    }

    @Test
    public void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }

    @Override
    SpreadsheetSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name) {
        return SpreadsheetSelectHistoryToken.with(
                id,
                name
        );
    }

    @Override
    public Class<SpreadsheetSelectHistoryToken> type() {
        return SpreadsheetSelectHistoryToken.class;
    }
}
