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
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class UnknownHistoryTokenTest extends HistoryTokenTestCase<UnknownHistoryToken> {

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    @Test
    public void testIdName() {
        this.setIdAndNameAndCheck(
            ID,
            NAME,
            HistoryToken.spreadsheetSelect(ID, NAME)
        );
    }

    @Test
    public void testSetSelectionWithCell() {
        this.setSelectionAndCheck(
            this.createHistoryToken(),
            SpreadsheetSelection.A1
        );
    }

    @Test
    public void testViewportOrEmpty() {
        this.checkEquals(
            Optional.empty(),
            this.createHistoryToken().anchoredSelectionOrEmpty()
        );
    }

    @Override
    UnknownHistoryToken createHistoryToken() {
        return UnknownHistoryToken.with(UrlFragment.with("hello"));
    }

    @Override
    public Class<UnknownHistoryToken> type() {
        return UnknownHistoryToken.class;
    }
}
