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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetCreateHistoryTokenTest extends SpreadsheetHistoryTokenTestCase<SpreadsheetCreateHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/");
    }

    @Test
    public void testIdNameViewportWithDifferentId() {
        final SpreadsheetId differentId = SpreadsheetId.with(9999);

        this.idNameViewportSelectionAndCheck(
                differentId,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                SpreadsheetHistoryToken.spreadsheetLoad(
                        differentId
                )
        );
    }

    @Override
    SpreadsheetCreateHistoryToken createHistoryToken() {
        return SpreadsheetCreateHistoryToken.with();
    }

    @Override
    public Class<SpreadsheetCreateHistoryToken> type() {
        return SpreadsheetCreateHistoryToken.class;
    }
}
